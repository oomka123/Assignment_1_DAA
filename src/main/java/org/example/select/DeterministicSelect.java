package org.example.select;

import org.example.metrics.Metrics;
import org.example.util.Util;

import java.util.Arrays;

public class DeterministicSelect {

    public static int select(int[] arr, int left, int right, int k, Metrics metrics) {
        while (true) {
            if (left == right) {
                return arr[left];
            }

            int pivot = medianOfMedians(arr, left, right, metrics);
            int pivotIndex = partition(arr, left, right, pivot, metrics);

            int rank = pivotIndex - left + 1;

            if (k == rank) {
                return arr[pivotIndex];
            } else if (k < rank) {
                right = pivotIndex - 1;
            } else {
                k = k - rank;
                left = pivotIndex + 1;
            }
        }
    }

    private static int medianOfMedians(int[] arr, int left, int right, Metrics metrics) {
        metrics.enterRecursion();

        int n = right - left + 1;
        if (n < 5) {
            Arrays.sort(arr, left, right + 1);
            metrics.exitRecursion();
            return arr[left + n / 2];
        }

        int numMedians = (int) Math.ceil((double) n / 5);
        metrics.incAllocations();
        int[] medians = new int[numMedians];

        for (int i = 0; i < numMedians; i++) {
            int subLeft = left + i * 5;
            int subRight = Math.min(subLeft + 4, right);
            Arrays.sort(arr, subLeft, subRight + 1);
            medians[i] = arr[subLeft + (subRight - subLeft) / 2];
        }

        metrics.exitRecursion();
        return medianOfMedians(medians, 0, numMedians - 1, metrics);
    }

    private static int partition(int[] arr, int left, int right, int pivotValue, Metrics metrics) {
        int pivotIndex = left;
        for (int i = left; i <= right; i++) {
            metrics.incComparisons();
            if (arr[i] == pivotValue) {
                pivotIndex = i;
                break;
            }
        }
        Util.swap(arr, pivotIndex, right, metrics);

        int storeIndex = left;
        for (int i = left; i < right; i++) {
            metrics.incComparisons();
            if (arr[i] < pivotValue) {
                Util.swap(arr, storeIndex, i, metrics);
                storeIndex++;
            }
        }
        Util.swap(arr, storeIndex, right, metrics);
        return storeIndex;
    }

}
