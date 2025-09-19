package org.example.metrics;

import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
    private final String filename;

    public CsvWriter(String filename) {
        this.filename = filename;
    }

    public void writeLine(String line) {
        try(FileWriter writer = new FileWriter(filename, true)) {
            writer.write(line + "\n");
        }catch(IOException e){
            throw new RuntimeException("Error writing CSV",e);
        }
    }
}
