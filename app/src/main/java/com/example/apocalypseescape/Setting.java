package com.example.apocalypseescape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Button backToMenu = findViewById(R.id.backMain);
        Button slow = findViewById(R.id.Slow);
        Button fast = findViewById(R.id.Fast);
        Button sensor = findViewById(R.id.Sensor);
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainMenu.class);
                view.getContext().startActivity(intent);
                finish();
            }
        });
        slow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("speed", "slow");
                view.getContext().startActivity(intent);
                finish();
            }
        });
        fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("speed", "fast");
                view.getContext().startActivity(intent);
                finish();
            }
        });
        sensor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.putExtra("sensor", "on");
                    view.getContext().startActivity(intent);
                    finish();
                }
            });
        }
}
