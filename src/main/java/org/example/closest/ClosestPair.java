package org.example.closest;

import org.example.metrics.Metrics;

import java.util.Arrays;
import java.util.Comparator;

public class ClosestPair {

    public static class Point {
        double x, y;
        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static double dist(Point p1, Point p2, Metrics metrics) {
        metrics.incComparisons();
        return Math.hypot(p1.x - p2.x, p1.y - p2.y);
    }

    public static double closestPair(Point[] points, Metrics metrics) {
        if (points.length < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        metrics.incAllocations();
        Point[] sortedByX = points.clone();
        Arrays.sort(sortedByX, Comparator.comparingDouble(p -> p.x));
        return closestUtil(sortedByX, metrics);
    }

    private static double closestUtil(Point[] points, Metrics metrics) {
        metrics.enterRecursion();

        int n = points.length;
        if (n <= 3) {
            double min = Double.MAX_VALUE;
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    min = Math.min(min, dist(points[i], points[j], metrics));
                }
            }
            metrics.exitRecursion();
            return min;
        }

        int mid = n / 2;
        Point midPoint = points[mid];

        metrics.incAllocations();
        double dl = closestUtil(Arrays.copyOfRange(points, 0, mid), metrics);
        metrics.incAllocations();
        double dr = closestUtil(Arrays.copyOfRange(points, mid, n), metrics);
        double d = Math.min(dl, dr);

        metrics.incAllocations();
        Point[] strip = Arrays.stream(points)
                .filter(p -> Math.abs(p.x - midPoint.x) < d)
                .toArray(Point[]::new);

        Arrays.sort(strip, Comparator.comparingDouble(p -> p.y));

        double minStrip = d;
        for (int i = 0; i < strip.length; i++) {
            for (int j = i + 1; j < strip.length && (strip[j].y - strip[i].y) < minStrip; j++) {
                minStrip = Math.min(minStrip, dist(strip[i], strip[j], metrics));
            }
        }

        metrics.exitRecursion();
        return Math.min(d, minStrip);
    }
}
