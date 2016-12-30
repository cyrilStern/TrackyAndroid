package fr.tracky.cyrilstern.trackyandroid;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

import static android.R.attr.port;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private EditText urlrestapi;
    static final int frequency = 44100;
    static final int channelConfiguration = AudioFormat.CHANNEL_OUT_STEREO;
    static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    static final String ip = "31.12.64.211";
    static final int port = 8000;
    private Thread th;
    private JSONArray getRadio;
    private RelativeLayout rel;
    private JSONObject row;
    boolean isPlaying;
    int playBufSize;
    protected Socket socket;
    protected AudioTrack audioTrack;
    protected Handler handler;
    protected  CustomMediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        rel = (RelativeLayout) findViewById(R.id.content_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        final LinearLayout layout = (LinearLayout) findViewById(R.id.thelinear);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Socket socket = new Socket();
        mediaPlayer = new CustomMediaPlayer();

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String mesgget = (String) msg.getData().get("collection");
                try {
                    getRadio = new JSONArray(mesgget);// sb.toString());

                for (int i = 0; i < getRadio.length(); i++) {

                        row = getRadio.getJSONObject(i);

                    Button button  = new Button(getApplicationContext());
                    button.setId(10220 + i); button.setWidth(320);
                    button.setHeight(40);
                    button.getBackground().setAlpha(51);
                    layout.addView(button);


                    button.setText(row.getString("radioStationName"));


                    final String url3 = row.getString("radioUrl");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(th !=null || mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                                if(mediaPlayer.getDataSource().equals(url3)){
                                    mediaPlayer.reset();
                                    th.interrupt();
                                    th = null;

                                }else{
                                    try {
                                        mediaPlayer.setDataSource(url3);
                                        mediaPlayer.prepare();
                                        mediaPlayer.start();

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }else {
                                th = new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                        try {
                                            mediaPlayer.setDataSource(url3);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        //mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse("http://uk5.internet-radio.com:8278/live"));
                                        try {
                                            mediaPlayer.prepare();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        mediaPlayer.start();

                                    }
                                });
                                th.start();
                            }
                        }
                    });

                }
            } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

    };
        init(this);

    }
    private void init(final Context context){

        rel = (RelativeLayout) findViewById(R.id.content_main);
        playBufSize=AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency, channelConfiguration, audioEncoding, playBufSize, AudioTrack.MODE_STREAM);
//        Button button1 = new Button(this);
//        rel.addView(button1);
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(th !=null){
//                    mediaPlayer.stop();
//                    mediaPlayer.reset();
//                    th.interrupt();
//                    th = null;
//                }else {
//                    th = new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            String url = "http://uk5.internet-radio.com:8278/live";
//                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                            try {
//                                mediaPlayer.setDataSource(url);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            //mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse("http://uk5.internet-radio.com:8278/live"));
//                            try {
//                                mediaPlayer.prepare();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            mediaPlayer.start();
//
//                        }
//                    });
//                    th.start();
//                }

//                    socket = new Socket("uk5.internet-radio.com/live",8278);
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//                audioTrack.play();
//
//                isPlaying = true;
//                while (isPlaying) {
//                    int readSize = 0;
//                    try { readSize = socket.getInputStream().read(buffer); }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    short[] sbuffer = new short[1024];
//                    for(int i = 0; i < buffer.length; i++)
//                    {
//
//                        int asInt = 0;
//                        asInt = ((buffer[i] & 0xFF) << 0)
//                                | ((buffer[i+1] & 0xFF) << 8)
//                                | ((buffer[i+2] & 0xFF) << 16)
//                                | ((buffer[i+3] & 0xFF) << 24);
//                        float asFloat = 0;
//                        asFloat = Float.intBitsToFloat(asInt);
//                        int k=0;
//                        try{k = i/4;}catch(Exception e){}
//                        sbuffer[k] = (short)(asFloat * Short.MAX_VALUE);
//                        //Log.i("buffer: ", (String.valueOf(sbuffer[k])));
//                        i=i+3;
//                    }
//                    audioTrack.write(sbuffer, 0, sbuffer.length);
//                }
//                //audioTrack.stop();
//               // try { socket.close(); }
//                //catch (Exception e) { e.printStackTrace(); }
//               }else {
//                   audioTrack.stop();
//                   try {
//                       socket.close();
//                   } catch (IOException e) {
//                       e.printStackTrace();
//                   }




                new Thread(new Runnable() {
                    public void run() {
                        URL url;
                        Message myMessage;
                        myMessage = handler.obtainMessage();
                        HttpURLConnection urlConnection = null;
                        try {
                             url = new URL("http://192.168.1.10:3000/radio");

                             urlConnection = (HttpURLConnection) url.openConnection();
                            if(urlConnection.getResponseMessage().equals(HttpURLConnection.HTTP_OK)){
                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            InputStreamReader isw = new InputStreamReader(in);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null) // Read line by line
                                sb.append(line + "\n");
                            Bundle b = new Bundle();
                            b.putString("collection", sb.toString());
                            // envoyer le message au Hanlder
                            myMessage.setData(b);
                            handler.sendMessage(myMessage);
                                //Object obj = parser.parse(;

                            }else{
                                
                            }







                        } catch (Exception e) {
                            try {
                                url = new URL("http://89.92.177.105:3000/radio");
                            urlConnection = (HttpURLConnection) url.openConnection();

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            InputStreamReader isw = new InputStreamReader(in);
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "iso-8859-1"), 8);
                            StringBuilder sb = new StringBuilder();
                            String line = null;
                            while ((line = reader.readLine()) != null) // Read line by line
                                sb.append(line + "\n");
                            Bundle b = new Bundle();
                            b.putString("collection", sb.toString());
                            // envoyer le message au Hanlder
                            myMessage.setData(b);
                            handler.sendMessage(myMessage);
                            } catch (MalformedURLException e1) {
                                e1.printStackTrace();
                            } catch (UnsupportedEncodingException e1) {
                                e1.printStackTrace();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                        } finally {
                            if (urlConnection != null) {
                                urlConnection.disconnect();
                            }
                        }
                    }
                }).start();


            }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
