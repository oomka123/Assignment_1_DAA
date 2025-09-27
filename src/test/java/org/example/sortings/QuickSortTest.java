package org.example.sortings;

import org.example.metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

class QuickSortTest {

    private final Random rand = new Random();
    private static final int N = 1000;

    private void runAndCheck(String name, int[] arr) {
        Metrics metrics = TestHelper.runWithMetrics(name, arr.length, m -> {
            QuickSort.quickSort(arr, 0, arr.length - 1, m);
        });

        assertTrue(isSorted(arr), "The array must be sorted");

        if (arr.length > 1) {
            int bound = 2 * (int) (Math.log(arr.length) / Math.log(2)) + 5;
            assertTrue(metrics.getMaxDepth() <= bound,
                    "The recursion is too deep: " + metrics.getMaxDepth() + " (bound = " + bound + ")");
        }
    }

    private boolean isSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i - 1] > arr[i]) return false;
        }
        return true;
    }

    @Test
    void testRandomArray() {
        int[] arr = rand.ints(N, -10000, 10000).toArray();
        runAndCheck("QuickSort-Random", arr);
    }

    @Test
    void testSortedArray() {
        int[] arr = new int[N];
        for (int i = 0; i < arr.length; i++) arr[i] = i;
        runAndCheck("QuickSort-Sorted", arr);
    }

    @Test
    void testReverseSortedArray() {
        int[] arr = new int[N];
        for (int i = 0; i < arr.length; i++) arr[i] = arr.length - i;
        runAndCheck("QuickSort-Reverse", arr);
    }

    @Test
    void testSmallArrays() {
        runAndCheck("QuickSort-Empty", new int[]{});
        runAndCheck("QuickSort-Single", new int[]{1});
        runAndCheck("QuickSort-Two", new int[]{2, 1});
        runAndCheck("QuickSort-Three", new int[]{3, 1, 2});
    }

    @Test
    void testDuplicates() {
        int[] arr = new int[N];
        Arrays.fill(arr, 42);
        runAndCheck("QuickSort-Duplicates", arr);
    }

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
}
