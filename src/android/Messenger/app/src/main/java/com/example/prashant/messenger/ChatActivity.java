package com.example.prashant.messenger;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChatActivity extends AppCompatActivity {
    EditText etMessage;
    Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        etMessage = (EditText) findViewById(R.id.etInput);
        btSend = (Button)findViewById(R.id.btSend);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSend();
                etMessage.setText("");
            }
        });
    }

    private void performSend() {
        String message = etMessage.getText().toString().trim();
        String userName = getIntent().getStringExtra("userName");

        JSONObject reqObj = new JSONObject();

        try {
            reqObj.put("message", message);
            reqObj.put("userName", userName);
        }
        catch(Exception e) { e.printStackTrace(); }

        API req = new API(ChatActivity.this, "/message") {
            @Override
            public void onResponseCallback(Exception ex, JSONArray resObj) {
                responseHandler(ex, resObj);
            }
        };
        req.setRequestObject(reqObj);
        req.send();
    }

    private void responseHandler(Exception ex, JSONArray resObj) {
        String message = "";
        int statusCode = 0;

        if (ex != null) {
            alert(ex.getMessage());
            Log.e("ERROR",ex.getMessage());
            return;
        }

        try {
            statusCode = resObj.getJSONObject(0).getInt("statusCode");
            message = resObj.getJSONObject(1).getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (statusCode != 200) { alert(message); return; }
    }

    private void alert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);

        AlertDialog alert = builder.create();
        alert.show();
    }
}
