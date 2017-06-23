package com.example.prashant.messenger;

/**
 * Created by Prashant on 12/06/2017.
 */


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.LinkedBlockingQueue;

import static android.content.ContentValues.TAG;
import static java.security.AccessController.getContext;

public class TaskThread {

    private LinkedBlockingQueue<TaskThreadItem> _workQueue;
    private boolean _isCancelled;
    private Thread _thread;

    public TaskThread() {
        _workQueue = new LinkedBlockingQueue<TaskThreadItem>();
        _isCancelled = false;

        _thread = new Thread(new Runnable() {
            public void run() {
                processQueueTasks();
            }
        });
        _thread.start();
    }

    private void processQueueTasks() {
        while (!_isCancelled) {
            TaskThreadItem workItem;
            try { workItem = _workQueue.take(); }
            catch (Exception ex) { break; }

            workItem.doWork();
        }
    }

    public void addWork(TaskThreadItem workItem) {
        _workQueue.add(workItem);
    }

    public void cancelThread() {
        _isCancelled = true;
        _workQueue = null;
        _thread.interrupt();
    }

}
