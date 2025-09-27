package org.example.bench;

import org.example.metrics.Metrics;
import org.example.select.DeterministicSelect;
import org.example.sortings.MergeSort;
import org.openjdk.jmh.annotations.*;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 2, time = 1)
@Fork(1)
public class SelectVsSortBench {

    @Param({"1000", "10000", "100000"})
    int n;

    int[] data;
    Metrics metrics = new Metrics();
    Random random = new Random();

    @Setup(Level.Invocation)
    public void setup() {
        data = random.ints(n, 0, 1_000_000).toArray();
    }

    @Benchmark
    public int benchDeterministicSelect() {
        int k = n / 2;
        int[] copy = Arrays.copyOf(data, data.length);
        return DeterministicSelect.select(copy, 0, copy.length - 1, k, metrics);
    }

    @Benchmark
    public int benchSortAndPick() {
        int k = n / 2;
        int[] copy = Arrays.copyOf(data, data.length);
        Arrays.sort(copy);
        return copy[k];
    }

    @Benchmark
    public int benchMergeSortAndPick() {
        int k = n / 2;
        int[] copy = Arrays.copyOf(data, data.length);
        MergeSort.mergeSort(copy, metrics);
        return copy[k];
    }
}