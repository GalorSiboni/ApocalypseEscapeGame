package com.example.apocalypseescape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    private String score;
    int lastScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        Intent startingIntent = getIntent();
        score = startingIntent.getStringExtra("key");
        TextView scoreLable = (TextView) findViewById(R.id.scoreLabel);
        scoreLable.setText("Score : " + score);
        Button topRanking = findViewById(R.id.TopRankingFromGameOver);
        Button backMain = findViewById(R.id.backMainFromGameOver);
        Button playAgain = findViewById(R.id.playAgainFromGameOver);
        topRanking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), TopRanking.class);
                    Intent storeScore = new Intent(GameOver.this, TopRanking.class);

                    lastScore = Integer.valueOf(score);
                    SharedPreferences preferences = getSharedPreferences("Prefs", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("lastScore", lastScore);
                    editor.apply();
                    startActivity(storeScore);
                    view.getContext().startActivity(intent);

                    finish();
                }
            });

        backMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MainMenu.class);
                    view.getContext().startActivity(intent);
                    finish();
                }
            });
        playAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    view.getContext().startActivity(intent);
                    finish();
                }
            });
        }
}

