package org.example.util;

import org.example.metrics.Metrics;

import java.util.Random;

public class Util {
    private static final Random random = new Random();

    public static int partition(int[] arr, int start, int end, Metrics metrics) {

        int pivot = arr[end];
        int i = start - 1;

        for (int j = start; j <= end - 1; j++) {
            metrics.incComparisons();
            if (arr[j] <= pivot) {
                i++;
                swap(arr, i, j, metrics);
            }
        }
        i++;
        swap(arr, i, end, metrics);
        return i;
    }

    public static void swap(int[] arr, int i, int j, Metrics metrics) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        metrics.incSwaps();
    }

    public static void shuffle(int[] arr, Metrics metrics) {
        for (int i = arr.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            swap(arr, i, j, metrics);
        }
    }

    public static void guardNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
}