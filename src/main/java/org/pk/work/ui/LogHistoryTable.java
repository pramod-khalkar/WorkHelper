package org.pk.work.ui;

import java.awt.GridLayout;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.pk.work.log.Record;
import org.pk.work.log.RecordLogger;

/**
 * Date: 21/12/21
 * Time: 10:11 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public class LogHistoryTable extends JPanel implements Runnable {

    private final String[] colNames = new String[] {"DATE", "START", "END", "DURATION (MIN)"};
    private JTable table;
    private DefaultTableModel dtm;
    private JScrollPane jScrollPane;
    private PieChart pieChart;
    private JPanel contentPanel;

    private final RecordLogger recordLogger;

    public LogHistoryTable(int width, int height, RecordLogger recordLogger) {
        this.recordLogger = recordLogger;
        setSize(width, height);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setVisible(true);
        setComponents();
        loadData();
    }

    private void setComponents() {
        contentPanel = new JPanel(new GridLayout(1, 2));
        this.dtm = new DefaultTableModel(colNames, 0);
        this.table = new JTable(dtm);

        TableColumnModel columnModel = this.table.getColumnModel();
        int colWidth = columnModel.getColumn(0).getPreferredWidth();
        columnModel.getColumn(0).setPreferredWidth(colWidth + 10);
        colWidth = columnModel.getColumn(1).getPreferredWidth();
        columnModel.getColumn(1).setPreferredWidth(colWidth - 20);
        colWidth = columnModel.getColumn(2).getPreferredWidth();
        columnModel.getColumn(2).setPreferredWidth(colWidth - 20);
        colWidth = columnModel.getColumn(3).getPreferredWidth();
        columnModel.getColumn(3).setPreferredWidth(colWidth + 30);
        this.table.setColumnModel(columnModel);

        jScrollPane = new JScrollPane(this.table);
        contentPanel.add(jScrollPane);

        pieChart = new PieChart(Collections.emptyList());
        contentPanel.add(pieChart);

        add(contentPanel);
    }

    @Override
    public void run() {
        try {
            List<Record> todayRecords = this.recordLogger.todayRecords();
            todayRecords.forEach(record -> addNewRowInTable(record, false));
            setPieChartData(todayRecords);
            revalidate();
        } catch (IOException e) {
            //e.printStackTrace();@ToDo need to improve
        }
    }

    private void loadData() {
        Thread dThread = new Thread(this);
        dThread.start();
    }

    public void addNewRowInTable(Record record, boolean refresh) {
        this.dtm.addRow(record.toArray());
        if (refresh) {
            revalidate();
        }
    }

    public void loadOldRecords(LocalDate localDate) {
        try {
            clearTable();
            revalidate();
            List<Record> records = this.recordLogger.previousRecords(localDate);
            records.forEach(record -> addNewRowInTable(record, false));
            setPieChartData(records);
            revalidate();
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private void clearTable() {
        if (dtm != null) {
            int rowCount = dtm.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                dtm.removeRow(i);
            }
        }
    }

    public void setPieChartData(List<Record> records) {
        int totalWorkingHours = 9 * 60;
        long totalIdleMinutes = records.stream().mapToLong(Record::getTotalMin).sum();
        long workMinutes = totalWorkingHours - totalIdleMinutes;
        List<PieChart.ArcData> arcData = new ArrayList<>();
        arcData.add(new PieChart.ArcData((int) totalIdleMinutes, "Idle", totalWorkingHours));
        arcData.add(new PieChart.ArcData((int) workMinutes, "Work", totalWorkingHours));
        this.pieChart.repaintPie(arcData);
    }
}
