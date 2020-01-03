package com.example.apocalypseescape;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {
    private String score;
    private String name_Text = "";


    int lastScore, lastPlace;
    double longitude, latitude;
    boolean mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);


        Intent startingIntent = getIntent();
        score = startingIntent.getStringExtra("key");
        latitude = startingIntent.getDoubleExtra("latitude", 0.0);
        longitude = startingIntent.getDoubleExtra("longitude", 0.0);
        mode = startingIntent.getBooleanExtra("mode",false);
        TextView scoreLable = (TextView) findViewById(R.id.scoreLabel);
        scoreLable.setText("Score : " + score);
        Button topRanking = findViewById(R.id.TopRankingFromGameOver);
        Button backMain = findViewById(R.id.backMainFromGameOver);
        Button playAgain = findViewById(R.id.playAgainFromGameOver);

        lastScore = Integer.valueOf(score);
        SharedPreferences preferences = getSharedPreferences("Prefs", 0);
        lastPlace = preferences.getInt("thirdPlace",0);
        if(lastPlace < lastScore){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("lastScore", lastScore);
            editor.putLong("lastLat", Double.doubleToLongBits(latitude));
            editor.putLong("lastLon", Double.doubleToLongBits(longitude));
            setName();
            editor.apply();
        }

        topRanking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), TopRank.class);
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
                    if (mode) intent.putExtra("sensor", "on");
                    else intent.putExtra("sensor", "off");
                    view.getContext().startActivity(intent);
                    finish();
                }
            });
        }
        private void setName(){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please enter your name:                   ");

            // Set up the input
            final EditText inputName = new EditText(this);
            // Specify the type of input expected;
            inputName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            builder.setView(inputName);

            // Set up the buttons
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    name_Text = inputName.getText().toString();
                    SharedPreferences preferences = getSharedPreferences("Prefs", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("nameLastScore",name_Text);
                    editor.apply();

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }

}

