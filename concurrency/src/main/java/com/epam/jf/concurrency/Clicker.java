package com.epam.jf.concurrency;

public class Clicker extends Thread {
    int click;
    private volatile boolean running = true;

    public void run() {
        while (running) {
            click++;
        }
    }

    public void stopClick() {
        running = false;
    }
}
