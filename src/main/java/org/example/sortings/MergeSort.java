package org.example.sortings;

import org.example.metrics.Metrics;

public class MergeSort {

    private static final int CUTOFF = 16;

    public static void mergeSort(int[] arr, Metrics metrics) {
        metrics.incAllocations();
        int[] buffer = new int[arr.length]; // общий буфер
        mergeSort(arr, buffer, 0, arr.length - 1, metrics);
    }

    private static void mergeSort(int[] arr, int[] buffer, int left, int right, Metrics metrics) {
        metrics.enterRecursion();
        try {
            if (right - left <= CUTOFF) {
                insertionSort(arr, left, right, metrics);
                return;
            }

            int mid = left + (right - left) / 2;
            mergeSort(arr, buffer, left, mid, metrics);
            mergeSort(arr, buffer, mid + 1, right, metrics);

            metrics.incComparisons();
            if (arr[mid] <= arr[mid + 1]) {
                return;
            }

            merge(arr, buffer, left, mid, right, metrics);

        } finally {
            metrics.exitRecursion();
        }
    }

    private static void merge(int[] arr, int[] buffer, int left, int mid, int right, Metrics metrics) {
        metrics.incAllocations(right - left + 1);

        System.arraycopy(arr, left, buffer, left, right - left + 1);

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            metrics.incComparisons();
            if (buffer[i] <= buffer[j]) {
                arr[k++] = buffer[i++];
                metrics.incSwaps();
            } else {
                arr[k++] = buffer[j++];
                metrics.incSwaps();
            }
        }

        while (i <= mid) {
            arr[k++] = buffer[i++];
            metrics.incSwaps();
        }
    }

    private static void insertionSort(int[] arr, int left, int right, Metrics metrics) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            metrics.incSwaps();
            int j = i - 1;
            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];
                metrics.incSwaps();
                j--;
            }
            arr[j + 1] = key;
            metrics.incSwaps();
        }
    }
}
