package com.example.prashant.messenger;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class ChatActivity extends AppCompatActivity {
    LinearLayout mLayout;
    EditText etMessage;
    Button btSend;
    byte[] data;
    TaskThread threadTask = new TaskThread();
    TaskThread threadTCP = new TaskThread();

    TaskThreadItem TCPConnect = new TaskThreadItem() {
        @Override
        public void doWork() {
            try { TCPConnect.setSocket(new Socket("192.168.1.2", 8081)); }
            catch (IOException e) { e.printStackTrace(); }
            handleConnection(this.s);
        }
    };

    TaskThreadItem sendItem = new TaskThreadItem() {
        @Override
        public void doWork() {
            try { sendMessage(TCPConnect.s, etMessage.getText().toString().trim().getBytes(StandardCharsets.UTF_8)); }
            catch (IOException e) { e.printStackTrace(); }
        }
    };

    public ChatActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextView textView = new TextView(this);
        textView.setText("New text");

        etMessage = (EditText) findViewById(R.id.etInput);
        btSend = (Button)findViewById(R.id.btSend);
        mLayout = (LinearLayout) findViewById(R.id.chatLayout);

        threadTCP.addWork(TCPConnect);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                threadTask.addWork(sendItem);
                etMessage.setText("");
            }
        });
    }

    public static void sendMessage(Socket s, byte[] data) throws IOException {
        short dataLength = (short)data.length;
        byte[] dataLengthRaw = new byte[2];

        dataLengthRaw[0] = (byte)(dataLength << 8 >> 8);
        dataLengthRaw[1] = (byte)(dataLength >> 8);

        s.getOutputStream().write(dataLengthRaw);
        s.getOutputStream().write(data);
    }

    private String handleConnection(Socket s) {
        byte[] sizeBytes;
        ByteBuffer b;
        short size = 0;

        while(true) {
            sizeBytes = awaitData(s, 2);
            b = ByteBuffer.wrap(sizeBytes);
            b.order(ByteOrder.LITTLE_ENDIAN);

            while(b.hasRemaining()) { size = b.getShort(); }
            data = awaitData(s, (int)size);
            runOnUiThread(new Runnable() {
                public void run()
                {
                    mLayout.addView(createNewTextView(new String(data)));
                }
            });
        }
    }

    private byte[] awaitData(Socket s, int totalSize) {
        byte[] buffer = new byte[totalSize];
        int readSize = 0;

        while (readSize < totalSize) {
            try { s.getInputStream().read(buffer, readSize, totalSize); }
            catch (IOException e) { e.printStackTrace(); }

            readSize += buffer.length;
        }

        return buffer;
    }

    private TextView createNewTextView(String text) {
        //final LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setPadding(16,0,0,0);
        //textView.setLayoutParams(lparams);
        textView.setText(text);
        return textView;
    }
}
