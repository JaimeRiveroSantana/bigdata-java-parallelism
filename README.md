# Big Data Performance Optimization in Java

[![Java](https://img.shields.io/badge/Java-21+-007396?logo=java&logoColor=white)](https://adoptium.net/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A modular, benchmark-driven implementation demonstrating performance optimization in Big Data processing using Java concurrency primitives — with rigorous correctness validation and real-world trade-off analysis.

---

## Objective

Demonstrate the effective use of Java concurrency mechanisms for Big Data workloads, as required by academic and industrial standards:

- **Parallelism**: `ExecutorService`, `parallelStream()`
- **Synchronization**: `AtomicLong`, `synchronized` blocks, `Semaphore`
- **Correctness**: All variants produce identical results (799,847 valid transactions, 1,000 clients)
- **Performance analysis**: Execution time, speedup, and scalability trade-offs

> 🔍 *Key insight*: Concurrency is not about speed—it’s about correctness under parallel execution. Performance gains emerge only when computation granularity and data structures are co-designed to minimize contention.

---

## Benchmark Results (M3 Pro, 18 GB RAM, Temurin JDK 21)

| Strategy             | Time (ms) | Valid Count | Speedup vs Sequential |
|----------------------|-----------|-------------|------------------------|
| **Sequential**       | 40.0      | 799,847     | 1.00x                  |
| ParallelStream       | 321.0     | 799,847     | 0.12x                  |
| ExecutorBased        | 98.0      | 799,847     | 0.41x                  |
| SemaphoreThrottled   | 100.0     | 799,847     | 0.40x                  |

**All strategies are consistent** — proving thread safety.  
**Sequential outperforms concurrent variants** due to low per-item computation and global lock contention (`synchronized` on shared `HashMap`).

![Execution Time Comparison](src/main/resources/plots/execution_time.png)

*(Plot generated with `XChart` — see `PlotGenerator.java`)*

---

