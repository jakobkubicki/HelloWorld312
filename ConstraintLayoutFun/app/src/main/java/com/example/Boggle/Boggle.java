package com.example.Boggle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.constraintlayoutfun.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


public class Boggle extends AppCompatActivity {

    private static final String[] boggleDice =
            {"AAEEGN", "ABBJOO", "ACHOPS", "AFFKPS",
                    "AOOTTW", "CIMOTU", "DEILRX", "DELRVY",
                    "DISTTY", "EEGHNW", "EEINSU", "EHRTVW",
                    "EIOSST", "ELRTTY", "HIMNUQu", "HLNNRZ"};

    Handler handler = null; //timer is not running when null
    int seconds = 90;
    String currWord = "";
    Button startButton;
    Button pauseButton;
    Button submitButton;
    ArrayList correctWords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadWordsFromFile();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < boggleDice.length; i++){
            Button button;
            String Text_ID = "button";
            Text_ID += String.valueOf(i);
            button = findViewById(getResources().getIdentifier(Text_ID, "id", getPackageName()));
            button.setText("");
            button.setEnabled(false);
        }

        //runnable
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //tick
                updateSeconds(seconds - 1);
                handler.postDelayed(this, 1000);
            }
        };

        startButton = findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (handler == null) {
                    handler = new Handler();
                    handler.postDelayed(runnable, 1000);
                    startButton.setEnabled(false);
                    pauseButton.setEnabled(true);
                    startGame();
                }
            }
        });

        pauseButton = findViewById(R.id.pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearBoard();
            }
        });

        submitButton = findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });


    }

    private void startGame(){
        char[] die = new char[8];
        Random rnd = new Random();
        Button button;
        for (int i = 0; i < boggleDice.length; i++){
            String Text_ID = "button";
            Log.d("MyApp","I am here");
            int n = rnd.nextInt(4);
            char insert = boggleDice[i].charAt(n);
            Text_ID += String.valueOf(i);
            System.out.println(Text_ID);
            button = findViewById(getResources().getIdentifier(Text_ID, "id", getPackageName()));
            button.setEnabled(true);
            button.setText(String.valueOf(insert));
            Button finalButton = button;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currWord += insert;
                    finalButton.setEnabled(false);
                    TextView currentWord = findViewById(R.id.textView5);
                    currentWord.setText(currWord);
                }
            });
        }
    }

    private void clearBoard(){
        currWord = "";
        TextView currentWord = findViewById(R.id.textView5);
        currentWord.setText(currWord);
        for (int i = 0; i < boggleDice.length; i++){
            Button button;
            String Text_ID = "button";
            Text_ID += String.valueOf(i);
            System.out.println(Text_ID);
            button = findViewById(getResources().getIdentifier(Text_ID, "id", getPackageName()));
            button.setEnabled(true);
        }
    }

    private void submit(){
        System.out.println("Pushed");
        String temp = currWord.replaceAll("\\s+","").toLowerCase(Locale.ROOT);
        System.out.println(temp);
        System.out.println(correctWords.size());
        for (int i = 0; i < correctWords.size() - 1; i++){
            String s = correctWords.get(i).toString().replaceAll("\\s+","");
            System.out.println(s);
            if (s.equals(temp)){
                System.out.println("We're good");
                break;
            }
        }
    }

    void loadWordsFromFile() {
        correctWords = new ArrayList<>();
        try {
            ClassLoader context;
            InputStream in = this.getResources().openRawResource(R.raw.words_alpha);
            BufferedReader is = new BufferedReader(new InputStreamReader(in, "UTF8"));
            String line;
            do {
                line = is.readLine();
                correctWords.add(line);
            } while (line != null);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void stopTimer(Runnable runnable){
        handler.removeCallbacks(runnable);
        handler = null;
    }

    private void updateSeconds(int newSeconds){
        seconds = newSeconds;
        TextView textView = findViewById(R.id.textView3);
        textView.setText("" + seconds);
    }

    public static int getId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            throw new RuntimeException("No resource ID found for: "
                    + resourceName + " / " + c, e);
        }
    }
}

