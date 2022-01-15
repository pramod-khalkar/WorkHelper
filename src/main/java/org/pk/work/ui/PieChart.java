package org.pk.work.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

/**
 * Date: 22/12/21
 * Time: 8:42 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public class PieChart extends JPanel {

    private List<ArcData> dataList;
    private Random random = new Random();

    public PieChart(List<ArcData> dataList) {
        setVisible(true);
        /*this.dataList = List.of(new ArcData(30, "Idle", 100),
                new ArcData(70, "Work", 100));*/
        this.dataList = dataList;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        int startX = (getWidth() / 2) - 80;
        int startY = (getHeight() / 2) - 80;
        int redius = Math.min(getWidth(), getHeight()) - 50;

        //@Todo Need to improve
        int idle = dataList.stream().filter(v -> v.getDescription().equalsIgnoreCase("idle")).map(ArcData::getData).findAny().orElse(0);
        int work = dataList.stream().filter(v -> v.getDescription().equalsIgnoreCase("work")).map(ArcData::getData).findAny().orElse(0);
        g.drawString(String.format("Idle %dh:%dm", idle / 60, idle % 60), startX - 50, startY - 10);
        g.drawString(String.format("Work %dh:%dm", work / 60, work % 60), startX + 130, startY - 10);

        int startAngle = 0;
        for (ArcData value : dataList) {
            g.setColor(randomColor());
            g.fillArc(startX, startY, redius, redius, startAngle, value.angle);
            startAngle = value.angle;
        }
        revalidate();
    }

    public void repaintPie(List<ArcData> arcData) {
        this.dataList = arcData;
        paint(getGraphics());
    }

    private Color randomColor() {
        return new Color(random.nextInt(255),
                random.nextInt(155),
                random.nextInt(255));
    }

    public static class ArcData {
        private final Integer data;
        private Integer angle;
        private final String description;

        public ArcData(Integer data, String description, Integer total) {
            this.data = data;
            this.description = description;
            resolveAngle(total);
        }

        public Integer getAngle() {
            return this.angle;
        }

        public Integer getData() {
            return this.data;
        }

        public String getDescription() {
            return this.description;
        }

        public void resolveAngle(Integer total) {
            this.angle = (360 * this.data) / total;
        }
    }
}
