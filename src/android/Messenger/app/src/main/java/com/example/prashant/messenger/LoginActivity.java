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

        if (ex != null) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(ex.getMessage());
            builder1.setCancelable(true);

            AlertDialog alert11 = builder1.create();
            alert11.show();

            Log.e("ERROR",ex.getMessage());
            return;
        }

        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
    }

}

