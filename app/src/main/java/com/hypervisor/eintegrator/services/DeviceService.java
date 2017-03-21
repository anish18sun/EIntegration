package com.hypervisor.eintegrator.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hypervisor.eintegrator.R;
import com.hypervisor.eintegrator.utils.Constants;

import java.io.IOException;

/**
 * Created by dexter on 20/3/17.
 */
public class DeviceService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String ACTION_PLAY_FILE = "com.example.action.PLAYFILE";
    private static final String ACTION_PLAY_URL = "com.example.action.PLAYURL";
    MediaPlayer mp = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent == null){
            return -1 ;
        }
        String action = intent.getAction();
        if(action.equals(ACTION_PLAY_FILE)){
            int file = intent.getIntExtra(Constants.FILE_RAW,-1);
            if(file != -1){
                mp = MediaPlayer.create(this, file);
                mp.start();
            }
        }else{
            mp = new MediaPlayer();
            String url = intent.getStringExtra(Constants.URL_SOUND);
            try {
                mp.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mp.setOnPreparedListener(this);
            mp.prepareAsync();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Called when MediaPlayer is ready
     */
    public void onPrepared(MediaPlayer player) {
        //When Media prepare this method work and you start your player
        player.start();
    }
}
