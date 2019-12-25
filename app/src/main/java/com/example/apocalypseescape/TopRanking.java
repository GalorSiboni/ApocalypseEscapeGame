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

public class TopRanking extends AppCompatActivity {
    TextView FirstPlace,SecondPlace,ThirdPlace;
    int firstPlace, secondPlace, thirdPlace, score;
    String firstPlaceName, secondPlaceName, thirdPlaceName, name, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ranking);

        SharedPreferences preferences = getSharedPreferences("Prefs",0);
        score  = preferences.getInt("lastScore", 0);
        firstPlace  = preferences.getInt("firstPlace", 0);
        secondPlace  = preferences.getInt("secondPlace", 0);
        thirdPlace  = preferences.getInt("thirdPlace", 0);
        name  = preferences.getString("nameLastScore", "");
        firstPlaceName  = preferences.getString("firstPlaceName", "");
        secondPlaceName  = preferences.getString("secondPlaceName", "");
        thirdPlaceName  = preferences.getString("thirdPlaceName","");
        FirstPlace = findViewById(R.id.FirstPlace);
        SecondPlace = findViewById(R.id.SecondPlace);
        ThirdPlace = findViewById(R.id.ThirdPlace);
        if(score != 0)
            updateRanking(score, name, preferences);
        else {
            FirstPlace.setText("1st: " + firstPlaceName + " - " + firstPlace);
            SecondPlace.setText("2nd: " + secondPlaceName + " - " + secondPlace);
            ThirdPlace.setText("3rd: " + thirdPlaceName + " - " + thirdPlace);
        }

        Button backToMenu = findViewById(R.id.backMain);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainMenu.class);
                view.getContext().startActivity(intent);
                finish();
            }
        });

        Button resetTable = findViewById(R.id.resetTable);
        resetTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordForReset();
            }
        });


    }
    public void updateRanking(int newScore,String newName, SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        String temp;
        int temp2;
        if(firstPlace > newScore){
            if (secondPlace > newScore){
                    thirdPlace = newScore;
                    thirdPlaceName = newName;

                    editor.putString("thirdPlaceName",thirdPlaceName);
                    editor.putInt("thirdPlace", thirdPlace);

                }
            else{
                thirdPlace = secondPlace;
                thirdPlaceName = secondPlaceName;

                secondPlace = newScore;
                secondPlaceName = newName;


                editor.putInt("secondPlace", secondPlace);
                editor.putString("secondPlaceName", secondPlaceName);
                editor.putInt("thirdPlace", thirdPlace);
                editor.putString("thirdPlaceName",thirdPlaceName);

            }
        }
        else{
            temp = firstPlaceName;
            temp2 = firstPlace;

            firstPlace = newScore;
            firstPlaceName = newName;

            thirdPlace = secondPlace;
            thirdPlaceName = secondPlaceName;

            secondPlaceName = temp;
            secondPlace = temp2;

            editor.putString("firstPlaceName",firstPlaceName);
            editor.putString("secondPlaceName",secondPlaceName);
            editor.putString("thirdPlaceName",thirdPlaceName);
            editor.putInt("firstPlace", firstPlace);
            editor.putInt("secondPlace", secondPlace);
            editor.putInt("thirdPlace", thirdPlace);

            }

        FirstPlace.setText("1st: " + firstPlaceName + " - " + firstPlace);
        SecondPlace.setText("2nd: " + secondPlaceName + " - " + secondPlace);
        ThirdPlace.setText("3rd: " + thirdPlaceName + " - " + thirdPlace);
        editor.putInt("lastScore", 0);
        editor.putString("lastScoreName","");
        editor.apply();

    }

    private void passwordForReset(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Password:                                             ");

        // Set up the input
        final EditText inputName = new EditText(this);
        // Specify the type of input expected;
        inputName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(inputName);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                password = inputName.getText().toString();
                String pass = "reset1993";
                if(password.equals(pass) ){
                    SharedPreferences preferences = getSharedPreferences("Prefs",0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    firstPlace = 0;
                    secondPlace = 0;
                    thirdPlace = 0;
                    FirstPlace.setText("1st Place: " + 0);
                    SecondPlace.setText("2nd Place : " + 0);
                    ThirdPlace.setText("3rd Place : " + 0);
                }

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