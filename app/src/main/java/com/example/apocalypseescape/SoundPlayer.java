package com.example.apocalypseescape;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int hitSound;
    private static int gameOverSound;
    private static int coinSound;

    public SoundPlayer(Context context){

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        hitSound = soundPool.load(context, R.raw.hit, 1);
        gameOverSound = soundPool.load(context, R.raw.gameover, 1);
        coinSound = soundPool.load(context, R.raw.coin, 1);
    }

    public void playHitSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(hitSound,1.0f, 1.0f,1,0,1.0f);
    }
    public void playGameOverSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(gameOverSound,1.0f, 1.0f,1,0,1.0f);
    }
    public void playCoinSound(){
        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)
        soundPool.play(coinSound,1.0f, 1.0f,1,0,1.0f);
    }
}
