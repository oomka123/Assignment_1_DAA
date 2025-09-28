#Design and Analysis of Algorithms — Assignment 1

##How to Use

Build:

```bash
mvn clean package
```

Run tests:

```bash
mvn test
```

Run CLI:

```bash
java -jar target/Assignment1_Daa-1.0-SNAPSHOT.jar <algorithm> <n> <output.csv>

Algorithms: closest, quicksort, mergesort, dselect
```

Execute benchmarks:

```bash
java -jar target/benchmarks.jar
```

---

##Architecture Overview

This project implements four classic divide-and-conquer algorithms, with a focus on safe recursion patterns and performance measurement.

###Metrics Collected
- **Time (JMH)**: nanosecond precision, average per operation (ms/op).<br>
- **Recursion Depth**: tracked to ensure bounded stack growth.
- **Allocations: MergeSort** reuses a buffer; QuickSort and Select work in-place.
- **Comparisons**: counted in merge and partition operations.

###Recursion Safety
- **MergeSort**: switches to insertion sort for small n to reduce overhead.
- **QuickSort**: always recurses on the smaller partition, iterates on the larger one → stack depth O(log n) typical.
- **Deterministic Select**: only recurses into the relevant side of the partition.
- **Closest Pair**: recursive split by x-coordinate, strip check uses y-order with ≤8 neighbor comparisons.

---

##Recurrence Analysis

- **MergeSort**
  - Recurrence: `T(n) = 2T(n/2) + Θ(n)`

    - According to Master Theorem (Case 2): a = 2, b = 2, f(n) = Θ(n), which means T(n) = Θ(n log n). The algorithm divides the array in half and performs a linear merge at each level.
  
  - Method: Master Theorem (Case 2)
  - Result: Θ(n log n)
  - Intuition: balanced division + linear merge step.
  
- **QuickSort (randomized)**
  - Recurrence: `T(n) = T(k) + T(n−k−1) + Θ(n)`

    - When a reference element is randomly selected, the average partition is balanced, which gives Θ(n log n). In the worst case, the time may be Θ(n2), but randomization makes this unlikely.
    
  - Method: Akra–Bazzi intuition (expected balanced split)
  - Result: Expected Θ(n log n), worst-case Θ(n²) avoided by pivot randomization
  - Intuition: smaller partition recursion ensures safe depth.
  
- **Deterministic Select (Median of Medians)**
  - Recurrence: `T(n) ≤ T(n/5) + T(7n/10) + Θ(n)`

    - The Akra–Bazzi method shows linear time Θ(n). The reference element is selected so that the partition is always balanced within 30-70%.
   
  - Method: Akra–Bazzi
  - Result: Θ(n) deterministic
  - Intuition: pivot guarantees constant-factor balance.
  
- **Closest Pair of Points (2D)**
  - Recurrence: `T(n) = 2T(n/2) + Θ(n)`

    - By Master Theorem (Case 2), we obtain Θ(n log n). Checking in a band is effective because each point is compared only with a limited number of neighbors.
        
  - Method: Master Theorem (Case 2)
  - Result: Θ(n log n)
  - Intuition: strip check bounded by constant neighbors.

---

##Experimental Results

###JMH Benchmarks (ms/op)###

|Algorithm	            |n=1,000	|n=10,000	|n=100,000|
|-----------------------|---------|---------|---------|
|Arrays.sort + Pick     |	0.009	  |0.128	  |3.497    |
|MergeSort + Pick	      |0.011	  |0.280	  |5.322    |
|QuickSort + Pick	      |0.033	  |0.418	  |5.091    |
|Deterministic Select	  |0.006	  |0.063	  |1.248    |
|Closest Pair of Points	|0.176	  |2.376	  |30.428   |

###Observations

- Scaling:
  - MergeSort and QuickSort follow Θ(n log n) with nearly identical growth.
  - Deterministic Select clearly scales linearly (Θ(n)).
  - Closest Pair matches Θ(n log n) but with large constants due to 2D geometry.
  
- Depth vs n:
  - MergeSort and Closest Pair: ~log n.
  - QuickSort: ~log n, bounded by safe recursion strategy.
  - Select: ≤log n, but typically very shallow.
  
- Constant-factor effects:
  - Cache locality: Merge buffer reuse helps; QuickSort less cache-friendly.
  - Garbage Collection: minimal due to in-place operations.
  - Pivot selection: random pivot in QuickSort adds slight overhead but prevents degeneration.

---

##Validation & Testing
- Sorting correctness: verified against Arrays.sort() on random, sorted, and reverse-sorted inputs.
- Select correctness: validated by comparing with sort+pick; deterministic pivot ensures reproducibility.
- Closest Pair correctness: cross-checked with brute force for small n.
- Depth tracking: verified logarithmic growth consistent with theoretical bounds.

---

##Theoretical vs Experimental Alignment
- Strong alignment: asymptotic trends (n log n for sorts, n for Select) are confirmed.
- Mismatches:
  - For small n, constants dominate (QuickSort faster than MergeSort until ~100k).
  - Closest Pair shows higher overhead due to geometric checks.
  - Insight: Real-world cache and GC effects are not captured by pure theory but explain constant-factor differences.

---

##Conclusion

This project demonstrates the classical divide-and-conquer algorithms with both theoretical analysis and experimental validation.
- MergeSort & QuickSort: asymptotically similar; QuickSort often faster in practice.
- Deterministic Select: optimal linear scaling, superior for large datasets.
- Closest Pair: matches theoretical Θ(n log n) but limited by constant factors.

The alignment between recurrence analysis and empirical data confirms the predictive power of algorithm analysis while highlighting the importance of constant factors in real implementations.
