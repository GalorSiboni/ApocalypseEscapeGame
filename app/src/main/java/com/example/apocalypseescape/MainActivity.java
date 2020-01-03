package com.example.apocalypseescape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import tyrantgit.explosionfield.ExplosionField;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SensorEventListener {

    private Random rand;
    List<Integer> laneOptions = new ArrayList<>();

    Vibrator vibrator;
    private BroadcastReceiver broadcastReceiver;


    //Image
    private ImageView Car, Car2, Car3, Zombie,Zombie2,Zombie3,Zombie4,Coin,life0,life1,life2;
    private ImageButton Left,Right;

    //Position
    private float zombieY;
    private int zombieX, zombie2X, zombie3X, zombie4X,  coinX, zombieAndCoinSpeed;
    private float carX;
    private boolean visibilityFlag;

    //sensor
    private SensorManager sensorManager;
    private String sensorModeStr;
    Sensor accelo;


    //Class
    private Handler handler = new Handler();
    private Timer timer;
    private SoundPlayer sound;

    //Score, Distance,Speed and Lifes
    private TextView scoreLable, disLable;
    private int lifeNum, score, distance = 0, hit_resize = 25;
    private double latitude, longitude;
    private String speed;


    ExplosionField explosionField;

    //flags
    private boolean start_flg = false;
    private boolean action_left_flg = false;
    private boolean action_right_flg = false;
    private boolean sensorMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new SoundPlayer(this);

        Car = findViewById(R.id.Car);
        Car2 = findViewById(R.id.Car2);
        Car3 = findViewById(R.id.Car3);
        Coin = findViewById(R.id.Coin);
        Zombie = findViewById(R.id.Zombie);
        Zombie2 = findViewById(R.id.Zombie2);
        Zombie3 = findViewById(R.id.Zombie3);
        Zombie4 = findViewById(R.id.Zombie4);
        scoreLable = findViewById(R.id.scoreLabel);
        disLable = findViewById(R.id.disLabel);
        life0 = findViewById(R.id.life0);
        life1 = findViewById(R.id.life1);
        life2 = findViewById(R.id.life2);
        explosionField = ExplosionField.attach2Window(this);
        Left = findViewById(R.id.Left);
        Left.setOnClickListener(this);
        Right = findViewById(R.id.Right);
        Right.setOnClickListener(this);


        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        accelo = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this,accelo,SensorManager.SENSOR_DELAY_NORMAL);

        Car2.setVisibility(View.INVISIBLE);
        Car3.setVisibility(View.INVISIBLE);
        start_flg = true;
        startGpsService();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    latitude = (double) intent.getExtras().get("latitude");
                    longitude = (double) intent.getExtras().get("longitude");

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
        laneOptions.add(0);
        laneOptions.add(290);
        laneOptions.add(580);
        laneOptions.add(870);
        laneOptions.add(1160);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        carX =  laneOptions.get(2);
        zombieY = Zombie.getY();
        Car.setX(carX);



        String slowSpeed = "slow";
        Intent speedIntent = getIntent();
        speed = speedIntent.getStringExtra("speed");
        if(speed != null && speed.equals(slowSpeed))
            zombieAndCoinSpeed = 10;
        else zombieAndCoinSpeed = 20;

        String senMode = "on";
        Intent sensorIntent = getIntent();
        sensorModeStr = sensorIntent.getStringExtra("sensor");
        if(sensorModeStr != null && sensorModeStr.equals(senMode)) {
            sensorMode = true;
            Left.setVisibility(View.GONE);
            Right.setVisibility(View.GONE);
        }
        else sensorMode = false;


        lifeNum = 3;
        score = 0;
        Thread odometer = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                            Thread.sleep(1000); // 1000 ms == 1 sec = 1 point

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                distance += zombieAndCoinSpeed;
                                disLable.setText("Distance : " + distance + " M");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        odometer.start();
        rand = new Random();
        timer = new Timer();
        swapVisibility();



        //Car movement
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
            }
        }, 0, 20);

        //Zombie movement
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            zombieDrops();
                        }
                    });

            }
        }, 1, 20);
    }

    public boolean hitCheck(float x, float y){
        return carX == x && Car.getY() + hit_resize <= y;
    }

    public void changePos() {

        //Move Car
        if (action_right_flg) {
            if(carX < 1160) {
                carX += 290;
            }
            action_right_flg = false;
        }
        if (action_left_flg){
            carX -= 290;
            action_left_flg = false;
        }

        // Check car position
        if(carX < 0){
            carX = 0;
        }

        Car.setX(carX);
        if (carX > 1160) Car.setX(1160);
    }

    private void updateLife(int lifeNum) {
        switch (lifeNum){
            case 3:
            case 2:
                sound.playHitSound();
                life0.setVisibility(View.INVISIBLE);
                break;
            case 1:
                sound.playHitSound();
                life1.setVisibility(View.INVISIBLE);
                break;
            case 0:
                life2.setVisibility(View.INVISIBLE);
                swapVisibility();
                sound.playGameOverSound();
                gameOver();
                timer.cancel();
                finish();
                break;
        }
    }

    private void gameOver() {
        Intent scoreIntent = new Intent(MainActivity.this, GameOver.class);
        String scoreStr = String.valueOf(score);
        scoreIntent.putExtra("key", scoreStr);
        scoreIntent.putExtra("mode",sensorMode);
        scoreIntent.putExtra("latitude", latitude);
        scoreIntent.putExtra("longitude", longitude);
        startActivity(scoreIntent);
    }

    public void zombieDrops() {
        zombieY += zombieAndCoinSpeed;
        if (zombieY > 2100) {
            if (!visibilityFlag){
                swapVisibility();
            }
            zombieY = 0;
            zombieX = laneOptions.remove(rand.nextInt(laneOptions.size() - 1));
            zombie2X = laneOptions.remove(rand.nextInt(laneOptions.size() - 1));
            zombie3X = laneOptions.remove(rand.nextInt(laneOptions.size() - 1));
            zombie4X = laneOptions.remove(rand.nextInt(laneOptions.size() - 1));
            coinX = 30 + laneOptions.get(0);
            laneOptions.add(zombieX);
            laneOptions.add(zombie2X);
            laneOptions.add(zombie3X);
            laneOptions.add(zombie4X);
        }
            Zombie.setX(zombieX);
            Zombie.setY(zombieY);
            Zombie2.setX(zombie2X);
            Zombie2.setY(zombieY);
            Zombie3.setX(zombie3X);
            Zombie3.setY(zombieY);
            Zombie4.setX(zombie4X);
            Zombie4.setY(zombieY);
            Coin.setX(coinX);
            Coin.setY(100 + zombieY);

        if (hitCheck(zombieX, zombieY) || hitCheck(zombie2X, zombieY) || hitCheck(zombie3X, zombieY)|| hitCheck(zombie4X, zombieY)){
            lifeNum--;
            vibe();
            if (lifeNum == 2){
                Car2.setX(Car.getX());
                Car2.setVisibility(View.VISIBLE);
                Car.setVisibility(View.INVISIBLE);
                explosionField.explode(Car2);
                Car2.setImageBitmap(null);
            }
            else if (lifeNum == 1){
                Car3.setX(Car.getX());
                Car3.setVisibility(View.VISIBLE);
                Car.setVisibility(View.INVISIBLE);
                explosionField.explode(Car3);
                Car3.setImageBitmap(null);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Car.setVisibility(View.VISIBLE);
                }
            }, 400);

            Zombie.setY(-400);
            Zombie2.setY(-400);
            Zombie3.setY(-400);
            Zombie4.setY(-400);
            Coin.setY(-400);
            updateLife(lifeNum);

        }
        if (score == 100) hit_resize = 15;
        if(carX == (coinX - 30) && Car.getY() + hit_resize <= zombieY){
            if(zombieY % 20 == 0) {
                score += 5;
                if(score%100 == 0)zombieAndCoinSpeed += 10;
                sound.playCoinSound();
            }
            scoreLable.setText("Score : " + score);
            Zombie.setY(-400);
            Zombie2.setY(-400);
            Zombie3.setY(-400);
            Zombie4.setY(-400);
            Coin.setY(-400);
            }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Left:
                action_left_flg = true;
                break;
            case R.id.Right:
                action_right_flg = true;
                break;
            default:
                action_right_flg = false;
                action_left_flg = false;
                break;
        }
    }

    public void vibe(){
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            if (lifeNum == 0){
                vibrator.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        }else{
            vibrator.vibrate(200);
        }
    }

    public void swapVisibility(){
        if(Zombie.getVisibility() == View.VISIBLE) {
            Zombie.setVisibility(View.INVISIBLE);
            Zombie2.setVisibility(View.INVISIBLE);
            Zombie3.setVisibility(View.INVISIBLE);
            Zombie4.setVisibility(View.INVISIBLE);
            Coin.setVisibility(View.INVISIBLE);
            visibilityFlag = false;
        }
        else {
            Zombie.setVisibility(View.VISIBLE);
            Zombie2.setVisibility(View.VISIBLE);
            Zombie3.setVisibility(View.VISIBLE);
            Zombie4.setVisibility(View.VISIBLE);
            Coin.setVisibility(View.VISIBLE);
            visibilityFlag = true;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensorMode) {
            if (event.values[0] > 3)
                action_left_flg = true;
            else if (event.values[0] < -3)
                action_right_flg = true;
            else {
                action_left_flg = false;
                action_right_flg = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onResume() {
        start_flg = true;
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    public void onPause() {
        start_flg = false;
        super.onPause();
    }

    private void startGpsService() {
        if (!runtime_permissions()) {
            Intent i = new Intent(getApplicationContext(), GPS_service.class);
            startService(i);
        }

    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startGpsService();
            } else {
                runtime_permissions();
            }
        }
    }
}

