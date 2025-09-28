package org.example.bench;

import org.example.metrics.Metrics;
import org.example.select.DeterministicSelect;
import org.example.sortings.MergeSort;
import org.example.sortings.QuickSort;
import org.example.closest.ClosestPair;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class SelectVsSortBench {

    @Param({"1000", "10000", "100000"})
    int n;

    int[] data;
    Random random = new Random();
    Metrics metrics = new Metrics();

    @Setup(Level.Iteration)
    public void setup() {
        data = random.ints(n, 0, 1_000_000).toArray();
    }

    // ---- SELECT ----
    @Benchmark
    public int benchDeterministicSelect() {
        int[] copy = Arrays.copyOf(data, data.length);
        return DeterministicSelect.select(copy, 0, copy.length - 1, n / 2, metrics);
    }

    // ---- SORTS ----
    @Benchmark
    public int benchArraysSortAndPick() {
        int k = n / 2;
        int[] copy = Arrays.copyOf(data, data.length);
        Arrays.sort(copy);
        return copy[k];
    }

    @Benchmark
    public int benchMergeSortAndPick() {
        int k = n / 2;
        int[] copy = Arrays.copyOf(data, data.length);
        MergeSort mergeSort = new MergeSort();
        mergeSort.mergeSort(copy, metrics);
        return copy[k];
    }

    @Benchmark
    public int benchQuickSortAndPick() {
        int k = n / 2;
        int[] copy = Arrays.copyOf(data, data.length);
        QuickSort quickSort = new QuickSort();
        quickSort.quickSort(copy, 0, copy.length - 1, metrics);
        return copy[k];
    }

    // ---- CLOSEST PAIR ----
    @Benchmark
    public double benchClosestPair() {
        ClosestPair.Point[] points = new ClosestPair.Point[n];
        for (int i = 0; i < n; i++) {
            double x = random.nextDouble() * 1_000_000;
            double y = random.nextDouble() * 1_000_000;
            points[i] = new ClosestPair.Point(x, y);
        }
        return ClosestPair.closestPair(points, metrics);
    }
}
