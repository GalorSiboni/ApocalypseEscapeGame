package com.example.apocalypseescape;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Random rand;
    int laneOptions[] = {0, 550, 1100};



    //Image
    private ImageView Car, Zombie,Zombie2,life0,life1,life2;
    private ImageButton Left, Right;

    //Position
    private float zombieX, zombieY, zombie2X, zombie2Y;
    private float carX;

    //Class
    private Handler handler = new Handler();
    private Timer timer;

    //Score and lifes
    private TextView scoreLable;
    private int score,timeCount,speed,lifeNum;

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
        carX = Car.getX() + 550;
        zombieY = Zombie.getY();
        zombie2Y = Zombie2.getY();
        Car.setX(carX);


        lifeNum = 3;
        timeCount = 0;
        score = 0;
        scoreLable.setText("Score : " + score);

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
        if (carX == x && Car.getY() == y)
            return true;
        return false;
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
        if(carX > 1000){
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
            case  0:
                life2.setVisibility(View.INVISIBLE);
                gameOver();
                break;
        }
    }

    private void gameOver() {

    }

    public void zombieDrops() {
        zombieY += 30;
        //Check zombie position
        if (zombieY > 2000) {
            zombieY = 0;
            zombieX = laneOptions[rand.nextInt(laneOptions.length)];
        }
        Zombie.setY(zombieY);
        Zombie.setX(zombieX);

        if (hitCheck(zombieX, zombieY) && zombieX != zombie2X){
            lifeNum--;
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