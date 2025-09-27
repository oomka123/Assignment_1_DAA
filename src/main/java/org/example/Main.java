package org.example;

import org.example.metrics.Metrics;
import org.example.sortings.MergeSort;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        System.out.println("...");

        Metrics metrics = new Metrics();

        int[] arr = {3,4,5,2,3,9,7,8};
        MergeSort.mergeSort(arr, metrics);
        System.out.println(Arrays.toString(arr));
    }
}