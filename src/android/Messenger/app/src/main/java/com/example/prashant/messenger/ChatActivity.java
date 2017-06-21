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

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ChatActivity extends AppCompatActivity {
    EditText etMessage;
    Button btSend;
    TaskThread thread = new TaskThread();
    TaskThreadItem item = new TaskThreadItem() {
        @Override
        public void doWork() {

            try { sendMessage(item, etMessage.getText().toString().trim().getBytes(StandardCharsets.UTF_8)); }
            catch (IOException e) { e.printStackTrace(); }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etMessage = (EditText) findViewById(R.id.etInput);
        btSend = (Button)findViewById(R.id.btSend);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thread.addWork(item);
                etMessage.setText("");
            }
        });
    }

    public static void sendMessage(TaskThreadItem item, byte[] data) throws IOException {
        short dataLength = (short)data.length;
        byte[] dataLengthRaw = new byte[2];

        dataLengthRaw[0] = (byte)(dataLength << 8 >> 8);
        dataLengthRaw[1] = (byte)(dataLength >> 8);

        item.s.getOutputStream().write(dataLengthRaw);
        item.s.getOutputStream().write(data);
    }
}
