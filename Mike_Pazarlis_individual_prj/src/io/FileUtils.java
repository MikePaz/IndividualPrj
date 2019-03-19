package io;

import models.Message;

import java.io.IOException;
import java.util.List;

public interface FileUtils {

    List<Message> readData(String filename);
    void writeData(String filename, String data) throws IOException;
}
