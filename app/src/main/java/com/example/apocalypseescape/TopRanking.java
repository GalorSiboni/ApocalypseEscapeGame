package com.example.apocalypseescape;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TopRanking extends Fragment {
    TextView FirstPlace,SecondPlace,ThirdPlace;
    int firstPlace, secondPlace, thirdPlace, score;
    double latitude, longitude, firstLatitude, firstLongitude, secondLatitude, secondLongitude, thirdLatitude, thirdLongitude;
    String firstPlaceName, secondPlaceName, thirdPlaceName, name, password;
    private  View view = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view == null)
            view = inflater.inflate(R.layout.activity_top_ranking,container,false);
        SharedPreferences preferences = getActivity().getSharedPreferences("Prefs",0);
        getValues(preferences);
        FirstPlace = view.findViewById(R.id.FirstPlace);
        SecondPlace = view.findViewById(R.id.SecondPlace);
        ThirdPlace = view.findViewById(R.id.ThirdPlace);
        if(score != 0)
            updateRanking(score, name, preferences);
        else {
            FirstPlace.setText("1st: " + firstPlaceName + " - " + firstPlace);
            SecondPlace.setText("2nd: " + secondPlaceName + " - " + secondPlace);
            ThirdPlace.setText("3rd: " + thirdPlaceName + " - " + thirdPlace);
        }

        Button backToMenu = view.findViewById(R.id.backMain);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainMenu.class);
                view.getContext().startActivity(intent);

            }
        });

        Button resetTable = view.findViewById(R.id.resetTable);
        resetTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordForReset();
            }
        });


        return view;
    }
    public void updateRanking(int newScore,String newName, SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        String temp;
        int temp2;
        double temp3,temp4;
        if(firstPlace > newScore){
            if (secondPlace > newScore){
                    thirdPlace = newScore;
                    thirdPlaceName = newName;
                    thirdLatitude = latitude;
                    thirdLongitude = longitude;

                    editor.putLong("tLat",Double.doubleToLongBits(thirdLatitude));
                    editor.putLong("tLon",Double.doubleToLongBits(thirdLongitude));
                    editor.putString("thirdPlaceName",thirdPlaceName);
                    editor.putInt("thirdPlace", thirdPlace);

                }
            else{
                thirdPlace = secondPlace;
                thirdPlaceName = secondPlaceName;
                thirdLatitude = secondLatitude;
                thirdLongitude = secondLongitude;

                secondPlace = newScore;
                secondPlaceName = newName;
                secondLongitude = longitude;
                secondLatitude = latitude;


                editor.putLong("tLat",Double.doubleToLongBits(thirdLatitude));
                editor.putLong("tLon",Double.doubleToLongBits(thirdLongitude));
                editor.putLong("sLat",Double.doubleToLongBits(secondLatitude));
                editor.putLong("sLon",Double.doubleToLongBits(secondLongitude));

                editor.putInt("secondPlace", secondPlace);
                editor.putString("secondPlaceName", secondPlaceName);
                editor.putInt("thirdPlace", thirdPlace);
                editor.putString("thirdPlaceName",thirdPlaceName);

            }
        }
        else{
            temp = firstPlaceName;
            temp2 = firstPlace;
            temp3 = firstLatitude;
            temp4 = firstLongitude;

            firstPlace = newScore;
            firstPlaceName = newName;
            firstLongitude = longitude;
            firstLatitude = latitude;

            thirdPlace = secondPlace;
            thirdPlaceName = secondPlaceName;
            thirdLongitude = secondLongitude;
            thirdLatitude = secondLatitude;

            secondPlaceName = temp;
            secondPlace = temp2;
            secondLatitude = temp3;
            secondLongitude = temp4;

            editor.putLong("tLat",Double.doubleToLongBits(thirdLatitude));
            editor.putLong("tLon",Double.doubleToLongBits(thirdLongitude));
            editor.putLong("sLat",Double.doubleToLongBits(secondLatitude));
            editor.putLong("sLon",Double.doubleToLongBits(secondLongitude));
            editor.putLong("fLat",Double.doubleToLongBits(firstLatitude));
            editor.putLong("fLon",Double.doubleToLongBits(firstLongitude));

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
        editor.putLong("lastLat", Double.doubleToLongBits(0.0f));
        editor.putLong("lastLon", Double.doubleToLongBits(0.0f));
        editor.apply();

    }

    private void passwordForReset(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Password:                                             ");

        // Set up the input
        final EditText inputName = new EditText(getActivity());
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
                    SharedPreferences preferences = getActivity().getSharedPreferences("Prefs",0);
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

    private void getValues(SharedPreferences preferences){
        score  = preferences.getInt("lastScore", 0);
        name  = preferences.getString("nameLastScore", "");
        latitude = getDouble(preferences,"lastLat", 0.0f);
        longitude = getDouble(preferences,"lastLon", 0.0f);


        firstPlace  = preferences.getInt("firstPlace", 0);
        secondPlace  = preferences.getInt("secondPlace", 0);
        thirdPlace  = preferences.getInt("thirdPlace", 0);

        firstLatitude = getDouble(preferences,"fLat",0.0f);
        firstLongitude = getDouble(preferences,"fLon",0.0f);
        secondLatitude = getDouble(preferences,"sLat",0.0f);
        secondLongitude = getDouble(preferences,"sLon",0.0f);
        thirdLatitude = getDouble(preferences,"tLat",0.0f);
        thirdLongitude = getDouble(preferences,"tLon",0.0f);

        firstPlaceName  = preferences.getString("firstPlaceName", "");
        secondPlaceName  = preferences.getString("secondPlaceName", "");
        thirdPlaceName  = preferences.getString("thirdPlaceName","");
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue)
    { return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));}

}