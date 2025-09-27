package org.example.sortings;

import org.example.metrics.Metrics;
import java.util.Random;

public class QuickSort {

    static Random rand = new Random();

    public static void quickSort(int[] arr, int start, int end, Metrics metrics) {
        while(start < end) {

            metrics.enterRecursion();
            int pivotIndex = randomizedPartition(arr, start, end, metrics);
            metrics.exitRecursion();

            if(pivotIndex - start > end - pivotIndex) {
                quickSort(arr, start, pivotIndex - 1, metrics);
                start = pivotIndex + 1;
            }else{
                quickSort(arr, pivotIndex + 1, end, metrics);
                end = pivotIndex - 1;
            }
        }
    }

    public static int randomizedPartition(int[] arr, int start, int end, Metrics metrics) {
        metrics.incAllocations();
        int randomIndex = start + rand.nextInt(end - start + 1);
        swap(arr, randomIndex, end, metrics);
        return partition(arr, start, end, metrics);
    }

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

}
