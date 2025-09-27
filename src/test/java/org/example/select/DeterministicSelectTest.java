package org.example.select;

import org.example.metrics.Metrics;
import org.example.util.TestHelper;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class DeterministicSelectTest {

    private static final Random rand = new Random();

    @RepeatedTest(100)
    void testDeterministicSelectAgainstSort() {
        int n = rand.nextInt(200) + 50;
        int[] arr = rand.ints(n, -1000, 1000).toArray();
        int[] arrCopy = Arrays.copyOf(arr, arr.length);

        int k = rand.nextInt(n) + 1;

        Metrics metrics = TestHelper.runWithMetrics("DeterministicSelect", arr.length, m -> {
            int selected = DeterministicSelect.select(arr, 0, arr.length - 1, k, m);

            Arrays.sort(arrCopy);
            int expected = arrCopy[k - 1];

            assertEquals(expected, selected,
                    () -> String.format("Mismatch for k=%d, expected=%d, got=%d",
                            k, expected, selected));
        });

        int bound = (int) (Math.log(arr.length) / Math.log(2)) * 5 + 10;
        assertTrue(metrics.getMaxDepth() <= bound,
                "Recursion depth too large: " + metrics.getMaxDepth() + " (bound = " + bound + ")");
    }

}
