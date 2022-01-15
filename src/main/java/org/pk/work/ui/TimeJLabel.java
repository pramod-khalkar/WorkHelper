package org.pk.work.ui;

import java.time.LocalTime;
import javax.swing.JLabel;

/**
 * Date: 06/12/21
 * Time: 9:24 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public class TimeJLabel extends JLabel implements Runnable {
    private static long elapsedSeconds;
    private static Thread wThread;
    private LocalTime sTime, eTime;

    public TimeJLabel() {
    }

    public TimeJLabel(String text) {
        setText(text);
    }

    public void start() {
        if (wThread == null || !wThread.isAlive()) {
            wThread = new Thread(this);
            sTime = LocalTime.now();
            wThread.start();
        }
    }

    public void stop() {
        wThread.stop();
        eTime = LocalTime.now();
        elapsedSeconds = 0;
        setText("00:00");
    }

    @Override
    public void run() {
        while (true) {
            elapsedSeconds++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setText(readableTime(elapsedSeconds));
        }
    }

    private String readableTime(long value) {
        long min = value / 60;
        long hours = min / 60;
        return String.format("%d : %d : %d", hours, (min % 60), value % 60);
    }

    public LocalTime starTime() {
        return this.sTime;
    }

    public LocalTime endTime() {
        return this.eTime;
    }
}
