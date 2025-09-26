package com.yogesh.stopwatch_timer;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView min,sec,mili;
    Button start, pause, reset,timer;

    int ms, m, s;
    long now,elapsed,startTime,pauseOffset = 0;
    boolean isRunning = false;
    Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent inte = new Intent(this, Timer.class);
        min = findViewById(R.id.minute);
        sec = findViewById(R.id.second);
        mili = findViewById(R.id.milisecond);
        start = findViewById(R.id.Start);
        pause = findViewById(R.id.Pause);
        reset = findViewById(R.id.Reset);
        timer = findViewById(R.id.Timer);

        // Restore state if available
        if (savedInstanceState != null) {
            ms = savedInstanceState.getInt("Milisecond");
            m = savedInstanceState.getInt("Minute");
            s = savedInstanceState.getInt("Second");
            updateText();
        }

        runnable = new Runnable() {
            @Override
            public void run() {
                if (isRunning) {
                    now = System.currentTimeMillis();
                    elapsed = now - startTime;

                    m = (int) (elapsed / 60000);
                    s = (int) ((elapsed / 1000) % 60);
                    ms = (int) ((elapsed % 1000) / 10);
                    updateText();
                    handler.postDelayed(this, 10);
                }
            }
        };

        // Start button
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isRunning) {
                    startTime = System.currentTimeMillis()-pauseOffset;
                    isRunning = true;
                    handler.post(runnable);
                }
            }
        });

        // Pause button
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { isRunning = false;
                pauseOffset = elapsed;
            }
        });

        // Reset button
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRunning = false;
                ms = m = s = 0;
                updateText();
            }
        });

        //switch to timer button
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(inte,null);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Milisecond", ms);
        outState.putInt("Minute", m);
        outState.putInt("Second", s);
    }

    private void updateText() {
        min.setText(String.format(Locale.US, "%02d",  m));
        sec.setText(String.format(Locale.US, "%02d",  s));
        mili.setText(String.format(Locale.US, "%02d",  ms));
    }
}
