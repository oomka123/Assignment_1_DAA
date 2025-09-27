package org.example.sortings;

import org.example.metrics.Metrics;
import org.example.util.Util;

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
        Util.swap(arr, randomIndex, end, metrics);
        return Util.partition(arr, start, end, metrics);
    }

}
