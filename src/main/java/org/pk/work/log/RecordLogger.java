package org.pk.work.log;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Date: 21/12/21
 * Time: 5:44 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public interface RecordLogger {
    void insert(Record... records) throws IOException;

    List<Record> todayRecords() throws IOException;

    List<Record> previousRecords(LocalDate whichDay) throws IOException;

    boolean isRecordExist(LocalDate searchDate);
}
