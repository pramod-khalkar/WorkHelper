package org.pk.work.log;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.pk.work.utils.Helper.getFileStorageDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date: 21/12/21
 * Time: 6:52 pm
 * This file is project specific to Work helper
 * Author: Pramod Khalkar
 */
public class FileDatabase implements RecordLogger {

    private final static String EXT = "log";
    private final static String DOT = ".";
    private final String storageDirectory;

    public FileDatabase() throws Exception {
        this.storageDirectory = getFileStorageDir();
    }

    @Override
    public void insert(Record... records) throws IOException {
        if (records.length > 0) {
            for (Record record : records) {
                writeRecord(record);
            }
        }
    }

    @Override
    public List<Record> todayRecords() throws IOException {
        return oldRecords(LocalDate.now());
    }

    @Override
    public List<Record> previousRecords(LocalDate whichDay) throws IOException {
        return oldRecords(whichDay);
    }

    @Override
    public boolean isRecordExist(LocalDate searchDate) {
        String path = absolutePathOfFile(searchDate);
        File file = new File(path);
        //return Files.exists(Path.of(path));
        return file.exists();
    }

    private void writeRecord(final Record record) throws IOException {
        makeDirIfNotExist(this.storageDirectory);
        Files.write(Paths.get(absolutePathOfFile(record.getDate())),
                (record + System.lineSeparator()).getBytes(),
                CREATE, APPEND);
        /*Files.writeString(Path.of(absolutePathOfFile(record.getDate())),
                record + System.lineSeparator(),
                CREATE,
                APPEND
        );*/
    }

    private List<Record> oldRecords(LocalDate date) throws IOException {
        if (isRecordExist(date)) {
            String path = absolutePathOfFile(date);
            //List<String> data = Files.readAllLines(Path.of(path), StandardCharsets.UTF_8);
            List<String> data = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            return data.stream().map(this::toRecord).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void makeDirIfNotExist(String parentDirPath) throws IOException {
        Path path = Paths.get(parentDirPath);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
    }

    private String absolutePathOfFile(LocalDate date) {
        return this.storageDirectory + date.toString() + DOT + EXT;
    }

    private Record toRecord(String fileRow) {
        String[] splitStr = fileRow.split("\\|");
        return new Record(LocalDate.parse(splitStr[0]),
                LocalTime.parse(splitStr[1]),
                LocalTime.parse(splitStr[2]),
                Long.parseLong(splitStr[3]));
    }
}
