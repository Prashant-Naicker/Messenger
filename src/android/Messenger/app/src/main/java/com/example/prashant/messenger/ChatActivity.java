package com.example.prashant.messenger;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    ListView mLayout;
    EditText etMessage;
    String Message;
    Button btSend;
    String userName;
    ListView list;
    ArrayAdapter<String> adapter;
    byte[] data;
    ArrayList<String> items;
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
            try { sendMessage(TCPConnect.s, Message.getBytes(StandardCharsets.UTF_8)); }
            catch (IOException e) { e.printStackTrace(); }
        }
    };

    public ChatActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etMessage = (EditText) findViewById(R.id.etInput);
        btSend = (Button)findViewById(R.id.btSend);
        mLayout = (ListView) findViewById(R.id.chatLayout);
        userName = getIntent().getStringExtra("userName");

        items = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this, R.layout.layout_item, items);

        list = (ListView) findViewById(R.id.chatLayout);
        list.setAdapter(adapter);

        threadTCP.addWork(TCPConnect);

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etMessage.getText().toString().trim().matches("")) {
                    Message = userName + ": " + etMessage.getText().toString().trim();
                    threadTask.addWork(sendItem);
                    etMessage.setText("");
                }
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
                    items.add(new String(data));
                    list.setAdapter(adapter);
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


    private void populateListView() {
        String[] items = {"Blue", "Green"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layout_item, items);

        ListView list = (ListView) findViewById(R.id.chatLayout);
        list.setAdapter(adapter);
    }
}
