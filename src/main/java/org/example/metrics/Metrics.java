package org.example.metrics;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Metrics {
    private long comparisons = 0;
    private long swaps = 0;
    private long allocations = 0;

    private int currentDepth = 0;
    private int maxDepth = 0;

    private long startTime = 0;
    private long elapsedNs = 0;

    public static class Record {
        public final String algorithm;
        public final int n;
        public final double timeMs;
        public final int maxDepth;
        public final long comparisons;
        public final long swaps;
        public final long allocations;
        public final Instant timestamp;

        public Record(String algorithm, int n, double timeMs, int maxDepth, long comparisons, long swaps, long allocations) {
            this.algorithm = algorithm;
            this.n = n;
            this.timeMs = timeMs;
            this.maxDepth = maxDepth;
            this.comparisons = comparisons;
            this.swaps = swaps;
            this.allocations = allocations;
            this.timestamp = Instant.now();
        }

        public String toConsoleString() {
            return String.join(" / ",
                    algorithm,
                    String.format("%.6f", timeMs),
                    String.valueOf(comparisons),
                    String.valueOf(maxDepth),
                    String.valueOf(allocations)
            );
        }
    }

    private final List<Record> records = new ArrayList<>();

    public void incComparisons() { comparisons++; }
    public void incComparisons(long amount) { comparisons += amount; }

    public void incSwaps() { swaps++; }
    public void incSwaps(long amount) { swaps += amount; }

    public void incAllocations() { allocations++; }
    public void incAllocations(long amount) { allocations += amount; }

    public void enterRecursion() {
        currentDepth++;
        if (currentDepth > maxDepth) maxDepth = currentDepth;
    }

    public void exitRecursion() {
        if (currentDepth > 0) currentDepth--;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        elapsedNs = System.nanoTime() - startTime;
    }

    public double getTimeMs() {
        return elapsedNs / 1_000_000.0;
    }

    public int getMaxDepth() { return maxDepth; }
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getAllocations() { return allocations; }

    public void reset() {
        comparisons = swaps = allocations = 0;
        currentDepth = maxDepth = 0;
        startTime = elapsedNs = 0;
    }

    public Record addRecord(String algorithm, int n) {
        Record r = new Record(algorithm, n, getTimeMs(), getMaxDepth(), getComparisons(), getSwaps(), getAllocations());
        records.add(r);
        System.out.println(r.toConsoleString());
        return r;
    }

    public List<Record> getRecords() { return records; }
}
