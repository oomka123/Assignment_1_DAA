package org.example.metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

public class CsvWriter {
    public static void write(String path, List<Metrics.Record> records, boolean append) throws IOException {
        File file = new File(path);
        boolean fileExists = file.exists();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, append))) {
            if (!fileExists) {
                pw.println("timestamp,algorithm,n,time_ms,max_depth,comparisons,swaps,allocations");
            }

            for (Metrics.Record r : records) {
                pw.printf(Locale.US,"%s,%s,%d,%.6f,%d,%d,%d,%d%n",
                        r.timestamp.toString(),
                        r.algorithm,
                        r.n,
                        r.timeMs,
                        r.maxDepth,
                        r.comparisons,
                        r.swaps,
                        r.allocations
                );
            }
        }
    }
}
