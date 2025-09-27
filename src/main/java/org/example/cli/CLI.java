package org.example.cli;

import org.example.closest.ClosestPair;
import org.example.metrics.CsvWriter;
import org.example.metrics.Metrics;
import org.example.select.DeterministicSelect;
import org.example.sortings.MergeSort;
import org.example.sortings.QuickSort;

import java.io.IOException;

public class CLI {
    private final String[] args;

    public CLI(String[] args) {
        this.args = args;
    }

    public void run() {
        if (args.length < 3) {
            System.out.println("Usage: java -jar app.jar <algorithm> <n> <output.csv>");
            System.out.println("Algorithms: closest, quicksort, mergesort, dselect");
            return;
        }

        String algorithm = args[0].toLowerCase();
        int n = Integer.parseInt(args[1]);
        String outputFile = args[2];

        Metrics metrics = new Metrics();

        switch (algorithm) {
            case "closest": {
                ClosestPair.Point[] points = generatePoints(n);
                metrics.reset();
                metrics.startTimer();
                double result = ClosestPair.closestPair(points, metrics);
                metrics.stopTimer();
                metrics.addRecord("ClosestPair", n);
                break;
            }

            case "quicksort": {
                int[] arr = generateArray(n);
                metrics.reset();
                metrics.startTimer();
                QuickSort.quickSort(arr, 0, arr.length - 1, metrics);
                metrics.stopTimer();
                metrics.addRecord("QuickSort", n);
                break;
            }

            case "mergesort": {
                int[] arr = generateArray(n);
                metrics.reset();
                metrics.startTimer();
                MergeSort.mergeSort(arr, metrics);
                metrics.stopTimer();
                metrics.addRecord("MergeSort", n);
                break;
            }

            case "dselect": {
                int[] arr = generateArray(n);
                int k = n / 2; // медиана
                metrics.reset();
                metrics.startTimer();
                int median = DeterministicSelect.select(arr, 0, arr.length - 1, k, metrics);
                System.out.println(median);
                metrics.stopTimer();
                metrics.addRecord("DeterministicSelect", n);
                break;
            }

            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithm);
        }

        try {
            CsvWriter.write(outputFile, metrics.getRecords(), true);
            System.out.println("Metrics results written to: " + outputFile);
            System.out.println("Done, results saved.");
        } catch (IOException e) {
            System.err.println("Ошибка при записи CSV: " + e.getMessage());
        }

    }

    private int[] generateArray(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = (int) (Math.random() * 100000);
        }
        return arr;
    }

    private ClosestPair.Point[] generatePoints(int n) {
        ClosestPair.Point[] arr = new ClosestPair.Point[n];
        for (int i = 0; i < n; i++) {
            arr[i] = new ClosestPair.Point(Math.random() * 1000, Math.random() * 1000);
        }
        return arr;
    }
}