package com.example.prashant.messenger;

/**
 * Created by Prashant on 12/06/2017.
 */


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
