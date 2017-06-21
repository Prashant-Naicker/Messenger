package com.example.prashant.messenger;

/**
 * Created by Prashant on 12/06/2017.
 */


import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskThread {

    private LinkedBlockingQueue<TaskThreadItem> _workQueue;
    private boolean _isCancelled;
    private Thread _thread;

    public TaskThread() {
        _workQueue = new LinkedBlockingQueue<TaskThreadItem>();
        _isCancelled = false;

        _thread = new Thread(new Runnable() {
            public void run() {
                try {
                    Socket s = new Socket("192.168.1.2", 8081);
                    processQueueTasks(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        _thread.start();
    }

    private void processQueueTasks(Socket s) {
        while (!_isCancelled) {
            TaskThreadItem workItem;
            try { workItem = _workQueue.take(); workItem.setSocket(s); }
            catch (Exception ex) { break; }

            workItem.doWork();
        }
        System.out.println("-- Underlying thread exited!");
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
