package com.example.prashant.messenger;

/**
 * Created by Prashant on 8/05/2017.
 */

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;


public abstract class API {
    private static String API_URL_STRING;

    private JSONObject _reqObj;
    private Activity _activity;

    // Constructor.
    public API(Activity activity, String url) {
        _activity = activity;
        API_URL_STRING = "https://messenger.jobjot.co.nz:8080" + url;
    }

    // Request/Response.
    public void setRequestObject(JSONObject reqObj) {
        _reqObj = reqObj;
    }
    private void raiseResponse(final Exception ex, final JSONArray resObj) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onResponseCallback(ex, resObj);
            }
        });
    }
    public abstract void onResponseCallback(Exception ex, JSONArray resObj);

    // Send.
    public void send() {
        AsyncTask.execute(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                commenceRequestSend();
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void commenceRequestSend() {
        try {
            URL url = new URL(API_URL_STRING);
            HttpsURLConnection httpsConnection = (HttpsURLConnection)url.openConnection();

            httpsConnection.setReadTimeout(10000);
            httpsConnection.setConnectTimeout(10000);

            httpsConnection.setRequestMethod("POST");
            httpsConnection.setRequestProperty("Content-Type", "application/json");

            httpsConnection.setDoInput(true);
            httpsConnection.setDoOutput(true);

            byte[] rawJSON = _reqObj.toString().getBytes(StandardCharsets.UTF_8);
            httpsConnection.getOutputStream().write(rawJSON, 0, rawJSON.length);

            String line;
            String resObjStr = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                resObjStr += line;
            }

            raiseResponse(null, new JSONArray(resObjStr));
        } catch (Exception ex) {
            raiseResponse(ex, null);
        }
    }
}
