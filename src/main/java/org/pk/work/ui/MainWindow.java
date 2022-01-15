package org.pk.work.ui;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.IOException;
import java.time.LocalDate;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.pk.work.log.FileDatabase;
import org.pk.work.log.Record;
import org.pk.work.log.RecordLogger;

/**
 * Date: 06/12/21
 * Time: 8:04 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public class MainWindow extends JFrame implements Communicator {

    private RecordLogger recordLogger;
    private PointerMover pointerMover;
    private LogHistoryTable logHistoryTable;

    public MainWindow() throws AWTException {
        try {
            this.recordLogger = new FileDatabase();
            this.pointerMover = new PointerMover(0, 0, this.recordLogger, this);
            this.logHistoryTable = new LogHistoryTable(0, 0, this.recordLogger);
            setMainFrame();
            setComponents(pointerMover, logHistoryTable);
        } catch (Exception e) {
            showMsgOnGUI(String.format("Error: %s", e.getMessage()));
        }
    }

    private void setMainFrame() throws IOException {
        setTitle("Work Helper");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 1, 20, 20));
    }

    private void setComponents(Component... component) {
        add(component[0]);
        add(component[1]);
    }

    public static void showMsgOnGUI(String msg) {
        JOptionPane.showMessageDialog(null,
                msg,
                "Message",
                ERROR_MESSAGE);
        System.exit(0);
    }

    @Override
    public void newRecord(Record record) {
        this.logHistoryTable.addNewRowInTable(record, true);
    }

    @Override
    public void loadOldData(LocalDate localDate) {
        logHistoryTable.loadOldRecords(localDate);
    }
}
