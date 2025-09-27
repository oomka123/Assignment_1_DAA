package org.example.sortings;

import org.example.metrics.Metrics;
import org.example.util.TestHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

public class MergeSortTest {

    private static final int N = 10_000;

    @Test
    void testEmptyArray() {
        int[] arr = {};
        TestHelper.runWithMetrics("MergeSort", arr.length, metrics -> MergeSort.mergeSort(arr, metrics));
        assertArrayEquals(new int[]{}, arr);
    }

    @Test
    void testSingleElement() {
        int[] arr = {42};
        TestHelper.runWithMetrics("MergeSort", arr.length, metrics -> MergeSort.mergeSort(arr, metrics));
        assertArrayEquals(new int[]{42}, arr);
    }

    @Test
    void testAlreadySorted() {
        int[] arr = {1, 2, 3, 4, 5};
        TestHelper.runWithMetrics("MergeSort", arr.length, metrics -> MergeSort.mergeSort(arr, metrics));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testReverseSorted() {
        int[] arr = {5, 4, 3, 2, 1};
        TestHelper.runWithMetrics("MergeSort", arr.length, metrics -> MergeSort.mergeSort(arr, metrics));
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, arr);
    }

    @Test
    void testAllEqual() {
        int[] arr = {7, 7, 7, 7, 7};
        TestHelper.runWithMetrics("MergeSort", arr.length, metrics -> MergeSort.mergeSort(arr, metrics));
        assertArrayEquals(new int[]{7, 7, 7, 7, 7}, arr);
    }

    @Test
    void testRandomArraySortingCorrectness() {
        Random rnd = new Random(42);
        int[] arr = rnd.ints(N, -1000, 1000).toArray();
        int[] expected = Arrays.copyOf(arr, arr.length);

        TestHelper.runWithMetrics("MergeSort", arr.length, metrics -> MergeSort.mergeSort(arr, metrics));

        Arrays.sort(expected);
        assertArrayEquals(expected, arr);
    }

    @Test
    void testRecursionDepthBound() {
        Random rnd = new Random(123);
        int[] arr = rnd.ints(N, -1000, 1000).toArray();

        Metrics metrics = TestHelper.runWithMetrics("MergeSort", arr.length, m -> MergeSort.mergeSort(arr, m));

        int depth = metrics.getMaxDepth();
        int log2n = (int) (Math.log(N) / Math.log(2));

        assertTrue(depth <= log2n + 5,
                "Recursion depth too large: " + depth + " (bound = " + (log2n + 5) + ")");
    }

}
