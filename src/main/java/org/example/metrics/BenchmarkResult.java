package org.example.metrics;

public class BenchmarkResult {
    private final String algorithm;
    private final int size;
    private final long time;
    private final long comparisons;
    private final long allocations;
    private final int depth;

    public BenchmarkResult(String algorithm, int size, long time, long comparisons, long allocations, int depth) {
        this.algorithm = algorithm;
        this.size = size;
        this.time = time;
        this.comparisons = comparisons;
        this.allocations = allocations;
        this.depth = depth;
    }

    public String toCsv(){
        return algorithm + "," + size + "," + time + "," + comparisons + "," + allocations + "," + depth;
    }
}
