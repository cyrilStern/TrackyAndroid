package fr.tracky.cyrilstern.trackyandroid;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

/**
 * Created by cyrilstern1 on 30/12/2016.
 */

public class CustomMediaPlayer extends MediaPlayer {
    private String dataSource;

    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        super.setDataSource(path);
        dataSource = path;
        Log.i("insideclass",dataSource);
    }

    public String getDataSource(){
       return dataSource;
    }
}
