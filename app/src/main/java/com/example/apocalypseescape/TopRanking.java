package com.example.apocalypseescape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TopRanking extends AppCompatActivity {
    TextView FirstPlace,SecondPlace,ThirdPlace;
    int firstPlace,secondPlace,thirdPlace;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ranking);

        SharedPreferences preferences = getSharedPreferences("Prefs",0);
        score  = preferences.getInt("lastScore", 0);
        firstPlace  = preferences.getInt("firstPlace", 0);
        secondPlace  = preferences.getInt("secondPlace", 0);
        thirdPlace  = preferences.getInt("thirdPlace", 0);
        FirstPlace = findViewById(R.id.FirstPlace);
        SecondPlace = findViewById(R.id.SecondPlace);
        ThirdPlace = findViewById(R.id.ThirdPlace);
        updateRanking(score, preferences);


        Button backToMenu = findViewById(R.id.backMain);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainMenu.class);
                view.getContext().startActivity(intent);
                finish();
            }
        });


    }
    public void updateRanking(int newScore, SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        int temp2;
        if(firstPlace > newScore){
            if (secondPlace > newScore){
                if(thirdPlace > newScore);
                else{
                    thirdPlace = newScore;

                    editor.putInt("thirdPlace", thirdPlace);
                    editor.apply();
                }
            }
            else{
                thirdPlace = secondPlace;
                secondPlace = newScore;

                editor.putInt("secondPlace", secondPlace);
                editor.putInt("thirdPlace", thirdPlace);
                editor.apply();
            }
        }
        else{
            temp2 = firstPlace;
            firstPlace = newScore;
            thirdPlace = secondPlace;
            secondPlace = temp2;

            editor.putInt("firstPlace", firstPlace);
            editor.putInt("secondPlace", secondPlace);
            editor.putInt("thirdPlace", thirdPlace);
            editor.apply();
            }

        FirstPlace.setText("1st Place: " + firstPlace);
        SecondPlace.setText("2nd Place : " + secondPlace);
        ThirdPlace.setText("3rd Place : " + thirdPlace);
        score = 0;
    }
}
