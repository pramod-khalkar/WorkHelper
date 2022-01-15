package org.pk.work.log;

import static org.pk.work.utils.Helper.readableDate;
import static org.pk.work.utils.Helper.readableTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Date: 21/12/21
 * Time: 6:56 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public class Record {
    private final LocalDate date;
    private final LocalTime sTime;
    private final LocalTime eTime;
    private final Long totalMinutes;

    public Record(LocalDate date, LocalTime sTime, LocalTime eTime, Long totalMinutes) {
        this.date = date;
        this.sTime = sTime;
        this.eTime = eTime;
        this.totalMinutes = totalMinutes;
    }

    public Record(LocalDate date, LocalTime sTime, LocalTime eTime) {
        this.date = date;
        this.sTime = sTime;
        this.eTime = eTime;
        this.totalMinutes = ChronoUnit.MINUTES.between(sTime, eTime);
    }

    public Record(LocalTime sTime, LocalTime eTime) {
        this.date = LocalDate.now();
        this.sTime = sTime;
        this.eTime = eTime;
        this.totalMinutes = ChronoUnit.MINUTES.between(sTime, eTime);
    }

    public LocalTime getEndTime() {
        return this.eTime;
    }

    public LocalTime getStartTime() {
        return this.sTime;
    }

    public Long getTotalMin() {
        return this.totalMinutes;
    }

    public LocalDate getDate() {
        return this.date;
    }

    @Override
    public String toString() {
        return this.date.toString() + "|" +
                this.sTime.toString() + "|" +
                this.eTime.toString() + "|" +
                this.totalMinutes;
    }

    public Object[] toArray() {
        return new Object[] {readableDate(this.date),
                readableTime(this.sTime),
                readableTime(this.eTime),
                this.totalMinutes};
    }
}
