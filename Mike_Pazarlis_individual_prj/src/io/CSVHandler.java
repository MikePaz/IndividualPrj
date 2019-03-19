package io;

import models.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CSVHandler implements FileUtils {

    @Override
    public List<Message> readData(String filename) {
        try {
            return Files.lines(Paths.get(filename))
                    .map(row -> {
                        String[] fields = row.split(",");
                        DateFormat formatter = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
                        try {
                            Date date = formatter.parse(fields[0]);
                            return new Message(date, Integer.parseInt(fields[1]),
                                    Integer.parseInt(fields[2]), fields[3]);
                        } catch (ParseException e) {
                            System.out.println(String.format("Failed to parse date %s.", fields[0]));
                            e.printStackTrace();
                        }
                        return new Message();
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void writeData(String filename, String data)  {
        BufferedWriter bufferedWriter= null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(
                    new File(filename), true));
            bufferedWriter.write(data + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
