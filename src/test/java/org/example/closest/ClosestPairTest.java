package org.example.closest;

import org.example.closest.ClosestPair;
import org.example.metrics.Metrics;
import org.example.util.TestHelper;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ClosestPairTest {

    private static final Random rand = new Random();

    private double bruteForce(ClosestPair.Point[] points, Metrics metrics) {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                double d = Math.hypot(points[i].x - points[j].x, points[i].y - points[j].y);
                min = Math.min(min, d);
            }
        }
        return min;
    }

    @Test
    void testSmallNMatchesBruteForce() {
        for (int trial = 0; trial < 20; trial++) {
            int n = rand.nextInt(500) + 2;
            ClosestPair.Point[] pts = new ClosestPair.Point[n];
            for (int i = 0; i < n; i++) {
                pts[i] = new ClosestPair.Point(rand.nextDouble() * 1000, rand.nextDouble() * 1000);
            }
            final double[] fastResult = new double[1];
            TestHelper.runWithMetrics("ClosestPair-Fast", n, metrics -> {
                fastResult[0] = ClosestPair.closestPair(pts, metrics);
            });

            double brute = bruteForce(pts, new Metrics());

            int finalTrial = trial;
            assertEquals(brute, fastResult[0], 1e-9,
                    () -> "Mismatch on trial " + finalTrial + " with n=" + n);
        }
    }

    @Test
    void testLargeNOnlyFastRuns() {
        int n = 100_000;
        ClosestPair.Point[] pts = new ClosestPair.Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new ClosestPair.Point(rand.nextDouble() * 1_000_000, rand.nextDouble() * 1_000_000);
        }

        final double[] result = new double[1];
        TestHelper.runWithMetrics("ClosestPair-Large", n, metrics -> {
            result[0] = ClosestPair.closestPair(pts, metrics);
        });

        assertTrue(result[0] >= 0, "Distance must be non-negative");
    }

}
