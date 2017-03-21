package com.hypervisor.eintegrator.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hypervisor.eintegrator.R;

import java.io.IOException;

/**
 * Created by dexter on 19/3/17.
 */
public class MyService extends Service implements MediaPlayer.OnPreparedListener {
    private static final String ACTION_PLAY = "com.example.action.PLAY";
    MediaPlayer mMediaPlayer = null;
    MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();
        mp = MediaPlayer.create(this,R.raw.kripya_apni_bhasha_chunein);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        mp.start();
        return 1;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) {
        //When Media prepare this method work and you start your player
        player.start();
    }
    @Override
    public void onDestroy() {
        if (mMediaPlayer != null) mMediaPlayer.release();
        if(mp != null) mp.release();
    }
}
