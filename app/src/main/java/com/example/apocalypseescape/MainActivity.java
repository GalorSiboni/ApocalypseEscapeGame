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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import tyrantgit.explosionfield.ExplosionField;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Random rand;
    List<Integer> laneOptions = new ArrayList<>();

    Vibrator vibrator;

    //Image
    private ImageView Car, Zombie,Zombie2,Zombie3,Zombie4,Coin,life0,life1,life2;

    //Position
    private float zombieY, coinY;
    private int zombieX, zombie2X, zombie3X, zombie4X,  coinX, zombieAndCoinSpeed;
    private float carX;

    //Class
    private Handler handler = new Handler();
    private Timer timer;
    private SoundPlayer sound;

    //Score, Distance,Speed and Lifes
    private TextView scoreLable, disLable;
    private int lifeNum, score, distance = 0;
    private String speed;

    ExplosionField explosionField;

    //Status
    private boolean start_flg = false;
    private boolean action_left_flg = false;
    private boolean action_right_flg = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new SoundPlayer(this);

        Car = findViewById(R.id.Car);
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
        findViewById(R.id.Left).setOnClickListener(this);
        findViewById(R.id.Right).setOnClickListener(this);

        start_flg = true;
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
            zombieAndCoinSpeed = 15;
        else zombieAndCoinSpeed = 30;

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
//                                score++;
                                distance += zombieAndCoinSpeed;
                                disLable.setText("Distance : " + distance + " M");
//                                scoreLable.setText("Score : " + score);
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
        return carX == x && Car.getY() + 20 <= y;
    }

    public void changePos() {

        //Move Car
        if (action_right_flg) {
            carX += 290;
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
                life0.setVisibility(View.INVISIBLE);
                break;
            case 1:
                life1.setVisibility(View.INVISIBLE);
                break;
            case 0:
                life2.setVisibility(View.INVISIBLE);
                Zombie.setVisibility(View.INVISIBLE);
                Zombie2.setVisibility(View.INVISIBLE);
                Zombie3.setVisibility(View.INVISIBLE);
                Zombie4.setVisibility(View.INVISIBLE);
                Coin.setVisibility(View.INVISIBLE);
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
        startActivity(scoreIntent);
    }

    public void zombieDrops() {
        zombieY += zombieAndCoinSpeed;
        if (zombieY > 2100) {
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
            sound.playHitSound();
           // explosionField.explode(Car);
            Zombie.setY(-400);
            Zombie2.setY(-400);
            Zombie3.setY(-400);
            Zombie4.setY(-400);
            Coin.setY(-400);
            updateLife(lifeNum);
        }
        if(carX == (coinX - 30) && Car.getY() + 15 <= zombieY){
          //  explosionField.explode(Coin);
            if(zombieY % 30 == 0) {
                score += 5;
                sound.playCoinSound();
            }
            scoreLable.setText("Score : " + score);
            Zombie.setY(-200);
            Zombie2.setY(-200);
            Zombie3.setY(-200);
            Zombie4.setY(-200);
            Coin.setY(-200);
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
    }
