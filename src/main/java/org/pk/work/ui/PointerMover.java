package org.pk.work.ui;

import static org.pk.work.ui.MainWindow.showMsgOnGUI;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.stream.IntStream;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.pk.work.log.Record;
import org.pk.work.log.RecordLogger;

/**
 * Date: 06/12/21
 * Time: 8:15 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public class PointerMover extends JPanel implements ActionListener, Runnable {

    private static JButton startBtn, stopBtn, search;
    private static JLabel coordinateLable;
    private static TimeJLabel timeJLabel;
    private static JPanel coordinatePanel;
    private static JPanel options;
    private static JComboBox<Integer> year, month, day;

    private static Robot robot;
    private static Random random;
    private static Thread mThread;

    private final RecordLogger recordLogger;
    private final Communicator communicator;


    public PointerMover(int width, int height, RecordLogger recordLogger, Communicator communicator) throws AWTException {
        setSize(width, height);
        setLayout(new BorderLayout());
        setVisible(true);
        setComponents();

        robot = new Robot();
        random = new Random();
        this.recordLogger = recordLogger;
        this.communicator = communicator;
    }

    private void setComponents() {
        startBtn = new JButton("Idle");
        add(startBtn, BorderLayout.WEST);
        startBtn.addActionListener(this);

        stopBtn = new JButton("Work");
        add(stopBtn, BorderLayout.EAST);
        stopBtn.addActionListener(this);

        coordinatePanel = new JPanel(new GridLayout(2, 1));
        coordinatePanel.setVisible(true);

        timeJLabel = new TimeJLabel("TIME");
        timeJLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeJLabel.setFont(new Font(Font.SERIF, Font.BOLD, 50));
        coordinatePanel.add(timeJLabel);

        coordinateLable = new JLabel("Pointer location");
        coordinateLable.setHorizontalAlignment(SwingConstants.CENTER);
        coordinatePanel.add(coordinateLable);
        add(coordinatePanel, BorderLayout.CENTER);

        options = new JPanel(new FlowLayout());

        LocalDate today = LocalDate.now();
        day = new JComboBox<>(IntStream.range(1, 32).boxed().toArray(Integer[]::new));
        day.setSelectedItem(today.getDayOfMonth());
        options.add(day);

        month = new JComboBox<>(IntStream.range(1, 13).boxed().toArray(Integer[]::new));
        month.setSelectedItem(today.getMonthValue());
        options.add(month);

        year = new JComboBox<>(IntStream.range(2021, 2031).boxed().toArray(Integer[]::new));
        year.setSelectedItem(today.getYear());
        options.add(year);

        search = new JButton("Search");
        search.addActionListener(this);
        options.add(search);

        add(options, BorderLayout.SOUTH);
        options.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startBtn) {
            if (mThread == null || !mThread.isAlive()) {
                mThread = new Thread(this);
                mThread.start();
            }
            timeJLabel.start();
        } else if (e.getSource() == stopBtn) {
            if (mThread != null && mThread.isAlive()) {
                mThread.stop();
                timeJLabel.stop();
                coordinateLable.setText("Pointer stopped");
                writeRecordToFile(timeJLabel.starTime(), timeJLabel.endTime());
            }
        } else if (e.getSource() == search) {
            LocalDate selectedDate = LocalDate.of((Integer) year.getSelectedItem(),
                    (Integer) month.getSelectedItem(),
                    (Integer) day.getSelectedItem());
            this.communicator.loadOldData(selectedDate);
        }
    }

    @Override
    public void run() {
        while (true) {
            int x = random.nextInt(400);
            int y = random.nextInt(400);
            robot.mouseMove(x, y);
            setCoordinateLable(x, y);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setCoordinateLable(int x, int y) {
        coordinateLable.setText(String.format("X=%d, Y=%d", x, y));
    }

    private void writeRecordToFile(LocalTime sTime, LocalTime eTime) {
        try {
            Record newRecord = new Record(sTime, eTime);
            recordLogger.insert(newRecord);
            this.communicator.newRecord(newRecord);
            this.communicator.loadOldData(LocalDate.now());
        } catch (IOException e) {
            showMsgOnGUI(String.format("Error: %s", e.getMessage()));
        }
    }
}
