package org.pk.work.ui;

import java.time.LocalDate;
import org.pk.work.log.Record;

/**
 * Date: 21/12/21
 * Time: 11:54 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public interface Communicator {
    void newRecord(Record record);

    void loadOldData(LocalDate localDate);
}
