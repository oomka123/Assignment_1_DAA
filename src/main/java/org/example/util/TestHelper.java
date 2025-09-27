package org.example.util;

import org.example.metrics.Metrics;

import java.util.function.Consumer;

public class TestHelper {

    public static Metrics runWithMetrics(String algoName, int size, Consumer<Metrics> algo) {
        Metrics metrics = new Metrics();
        metrics.reset();
        metrics.startTimer();

        algo.accept(metrics);

        metrics.stopTimer();
        metrics.addRecord(algoName, size);
        System.out.println(metrics.getRecords());

        return metrics;
    }

}
