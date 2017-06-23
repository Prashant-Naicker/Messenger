package com.example.prashant.messenger;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by Prashant on 12/06/2017.
 */

public abstract class TaskThreadItem {
    Socket s;

    protected TaskThreadItem() throws IOException {
    }

    public abstract void doWork();

    public void setSocket(Socket socket) {
        s = socket;
    }
}
