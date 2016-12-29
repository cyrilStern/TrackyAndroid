package fr.tracky.cyrilstern.trackyandroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

/**
 * Created by cyrilstern1 on 18/12/2016.
 */

public class RestAPiConnection  {
    public RestAPiConnection(){

    }

    protected String getStringFromRestApi(URL url, String action){
        String stringreturn;
        HttpsURLConnection urlconnectionapirest = new HttpsURLConnection(url) {
            @Override
            public String getCipherSuite() {
                return null;
            }

            @Override
            public Certificate[] getLocalCertificates() {
                return new Certificate[0];
            }

            @Override
            public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
                return new Certificate[0];
            }

            @Override
            public void disconnect() {

            }

            @Override
            public boolean usingProxy() {
                return false;
            }

            @Override
            public void connect() throws IOException {

            }
        };

        return null;
    }
}
