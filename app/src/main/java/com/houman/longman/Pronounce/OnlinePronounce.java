package com.houman.longman.Pronounce;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;

public class OnlinePronounce extends AsyncTask<String, Integer, String>
    {
    @SuppressLint("StaticFieldLeak")
    private
    Context mContext;

    public OnlinePronounce(Context mContext)
        {
        this.mContext = mContext;
        }

    @Override
    protected String doInBackground(String... strings)
        {
        String Text = strings[0];
        if (checkNetwork() && ! Text.equals(""))
            {
            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try
                {
                mediaPlayer.setDataSource(Text);
                mediaPlayer.prepare();
                }
            catch (IOException e)
                {
                e.printStackTrace();
                return null;
                }
            mediaPlayer.start();
            }
        return null;
        }

    private boolean checkNetwork()
        {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

    }
