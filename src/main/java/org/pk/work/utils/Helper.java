package org.pk.work.utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Date: 21/12/21
 * Time: 7:04 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public final class Helper {

    private static String storagePath;

    private Helper() {
    }

    public static String getFileStorageDir() throws Exception {
        if (storagePath == null) {
            String userHome = System.getProperty("user.home");
            switch (OS.detected) {
                case LINUX:
                case MAC:
                    storagePath = userHome + "/officehelper/";
                    break;
                case WINDOWS:
                    storagePath = userHome + "\\officehelper\\";
                    break;
                default:
                    throw new Exception("Not able to detect the OS");
            }
        }
        return storagePath;
    }

    public enum OS {
        MAC, WINDOWS, LINUX;

        public static final OS detected;

        static {
            String _os = System.getProperties()
                    .getProperty("os.name", "unknown")
                    .toLowerCase(Locale.ENGLISH);
            if (_os.contains("win")) {
                detected = WINDOWS;
            } else if (_os.contains("mac")) {
                detected = MAC;
            } else if (_os.contains("nux")) {
                detected = LINUX;
            } else {
                detected = null;
            }
        }
    }

    public static String readableDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public static String readableTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
