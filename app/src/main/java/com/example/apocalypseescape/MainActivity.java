package com.example.apocalypseescape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Random rand;
    int laneOptions[] = {0, 550, 1150};
    Vibrator vibrator;

    //Image
    private ImageView Car, Zombie,Zombie2,life0,life1,life2;

    //Position
    private float zombieX, zombieY, zombie2X, zombie2Y;
    private float carX;

    //Class
    private Handler handler = new Handler();
    private Timer timer;

    //Score and lifes
    private TextView scoreLable;
    private int timeCount,speed,lifeNum,score;


    //Status
    private boolean start_flg = false;
    private boolean action_left_flg = false;
    private boolean action_right_flg = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Car = findViewById(R.id.Car);
        Zombie = findViewById(R.id.Zombie);
        Zombie2 = findViewById(R.id.Zombie2);
        scoreLable = findViewById(R.id.scoreLabel);
        life0 = findViewById(R.id.life0);
        life1 = findViewById(R.id.life1);
        life2 = findViewById(R.id.life2);
        findViewById(R.id.Left).setOnClickListener(this);
        findViewById(R.id.Right).setOnClickListener(this);

        start_flg = true;
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        carX = Car.getX() + 550;
        zombieY = Zombie.getY();
        zombie2Y = Zombie2.getY();
        Car.setX(carX);


        lifeNum = 3;
        timeCount = 0;
        score = 0;
        Thread scoreCounter = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                            Thread.sleep(1000); // 1000 ms == 1 sec = 1 point

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                score++;
                                scoreLable.setText("Score : " + score);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        scoreCounter.start();
        rand = new Random();
        timer = new Timer();


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

        //Zombie2 movement
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg)
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            zombie2Drops();
                        }
                    });
            }
        }, 1, 20);
    }

    public boolean hitCheck(float x, float y){
        return carX == x && Car.getY() + 45 <= y;
    }

    public void changePos() {

        //Move Car
        if (action_right_flg) {
            carX += 550;
            action_right_flg = false;
        }
        if (action_left_flg){
            carX -= 600;
            action_left_flg = false;
        }

        // Check car position
        if(carX < 0){
            carX = 0;
        }
        if(carX > 600){
            carX = 1150;
        }
        Car.setX(carX);
    }

    private void updateLife(int lifeNum) {
        switch (lifeNum){
            case 3:
            case 2:
                life0.setVisibility(View.INVISIBLE);
                break;
            case 1:
                life1.setVisibility(View.INVISIBLE);
                break;
            case 0:
                life2.setVisibility(View.INVISIBLE);
                Zombie.setVisibility(View.INVISIBLE);
                Zombie2.setVisibility(View.INVISIBLE);
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
        startActivity(scoreIntent);
    }

    public void zombieDrops() {
        zombieY += 30;
        //Check zombie position
        if (zombieY > 2000) {
            zombieY = 0;
            zombieX = laneOptions[rand.nextInt(laneOptions.length)];
        }
        Zombie.setX(zombieX);
        Zombie.setY(zombieY);

        if (hitCheck(zombieX, zombieY) && zombieX != zombie2X){
            lifeNum--;

            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                if (lifeNum == 0){
                    vibrator.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            }else{
                vibrator.vibrate(200);
            }
            Zombie.setY(0);
            updateLife(lifeNum);
        }
    }

    public void zombie2Drops() {
        zombie2Y += 30;
         //Check zombie position
        if(zombie2Y > 2000){
            zombie2Y = 0;
            zombie2X = laneOptions[rand.nextInt(laneOptions.length)];
        }
        Zombie2.setY(zombie2Y);
        Zombie2.setX(zombie2X);

        if(hitCheck(zombie2X, zombie2Y)) {
            lifeNum--;
            if (Build.VERSION.SDK_INT >= 26) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                if (lifeNum == 0){
                    vibrator.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE));
                }
            }else{
                vibrator.vibrate(200);
            }
            Zombie.setY(0);
            updateLife(lifeNum);
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

    }