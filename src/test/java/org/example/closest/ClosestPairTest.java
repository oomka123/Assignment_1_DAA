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
    void testEmptyArray() {
        ClosestPair.Point[] pts = new ClosestPair.Point[0];
        Metrics metrics = new Metrics();
        assertThrows(IllegalArgumentException.class,
                () -> ClosestPair.closestPair(pts, metrics));
    }

    @Test
    void testSinglePoint() {
        ClosestPair.Point[] pts = { new ClosestPair.Point(0, 0) };
        Metrics metrics = new Metrics();
        assertThrows(IllegalArgumentException.class,
                () -> ClosestPair.closestPair(pts, metrics));
    }

    @Test
    void testDuplicatePoints() {
        ClosestPair.Point[] pts = {
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(1, 1),
                new ClosestPair.Point(2, 2)
        };
        Metrics metrics = new Metrics();
        double result = ClosestPair.closestPair(pts, metrics);
        assertEquals(0.0, result, 1e-9, "Duplicate points should give distance 0");
    }

    @Test
    void testTwoPoints() {
        ClosestPair.Point[] pts = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(3, 4)
        };
        Metrics metrics = new Metrics();
        double result = ClosestPair.closestPair(pts, metrics);
        assertEquals(5.0, result, 1e-9);
    }

    @Test
    void testThreePoints() {
        ClosestPair.Point[] pts = {
                new ClosestPair.Point(0, 0),
                new ClosestPair.Point(3, 4),
                new ClosestPair.Point(6, 8)
        };
        Metrics metrics = new Metrics();
        double result = ClosestPair.closestPair(pts, metrics);
        assertEquals(5.0, result, 1e-9);
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
