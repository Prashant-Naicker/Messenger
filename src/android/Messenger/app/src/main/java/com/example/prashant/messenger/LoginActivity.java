package com.example.prashant.messenger;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    ImageButton ibLogin;
    EditText etUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUserName = (EditText)findViewById(R.id.etUserName);
        ibLogin = (ImageButton)findViewById(R.id.imgButtonLogin);

        ibLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                request();
            }
        });
    }

    // API.
    private void request() {
        String username = etUserName.getText().toString().trim();

        if (username == "") {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please enter a username to continue");
            builder.setCancelable(true);

            AlertDialog alert11 = builder.create();
            alert11.show();
        }

        JSONObject reqObj = new JSONObject();
        try {
            reqObj.put("userName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        API req = new API(LoginActivity.this, "/login") {

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
        String userName = "";
        int statusCode = 0;

        if (ex != null) {
            alert(ex.getMessage());
            Log.e("ERROR",ex.getMessage());
            return;
        }

        try {
            statusCode = resObj.getJSONObject(0).getInt("statusCode");
            message = resObj.getJSONObject(1).getString("message");
            userName = resObj.getJSONObject(2).getString("userName");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (statusCode != 200) { alert(message); return; }
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("userName", userName);
        startActivity(i);
    }

    private void alert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);

        AlertDialog alert = builder.create();
        alert.show();
    }

}

