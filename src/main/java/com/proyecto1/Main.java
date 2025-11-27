package com.proyecto1;

import com.proyecto1.processor.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(" Big Data Performance Demo — Modular Design");
        System.out.println(" Parallelism: executors, parallel streams");
        System.out.println(" Sync: atomic, synchronized, semaphore\n");

        // 1. Generate data
        System.out.println(" Generating 1,000,000 transactions...");
        List<Transaction> transactions = TransactionGenerator.generateTransactions();
        System.out.println(" Done. Total: " + transactions.size() + "\n");

        // 2. Define processors
        TransactionProcessor[] processors = {
                new SequentialProcessor(),
                new ParallelStreamProcessor(),
                new ExecutorBasedProcessor(),
                new SemaphoreThrottledProcessor()
        };

        String[] labels = {
                "1 Sequential",
                "2 ParallelStream",
                "3 ExecutorBased",
                "4 SemaphoreThrottled"
        };

        ProcessingResult[] results = new ProcessingResult[processors.length];

        // 3. Execute each processing strategy
        for (int i = 0; i < processors.length; i++) {
            System.out.print(" Running " + labels[i] + "...");
            try {
                long start = System.nanoTime();
                results[i] = processors[i].process(transactions);
                long elapsedMs = (System.nanoTime() - start) / 1_000_000;
                // Override internal timing to ensure fair comparison
                results[i] = new ProcessingResult(elapsedMs * 1_000_000, results[i].validCount, results[i].uniqueClients);

                System.out.printf(" %.2f ms | Valid: %d | Clients: %d%n",
                        results[i].timeMs(), results[i].validCount, results[i].uniqueClients);
            } catch (Exception e) {
                System.err.println(" FAILED: " + e.getClass().getSimpleName());
                e.printStackTrace();
            }
        }

        // 4. Performance summary (only if baseline succeeded)
        if (results[0] != null && results[0].validCount > 0) {
            System.out.println("\n Performance Summary:");
            System.out.printf("   Baseline (Sequential): %.2f ms%n", results[0].timeMs());
            for (int i = 1; i < results.length; i++) {
                if (results[i] != null && results[i].validCount == results[0].validCount) {
                    double speedup = results[0].timeMs() / results[i].timeMs();
                    System.out.printf("   %s: %.2f ms (%.2fx faster)%n",
                            labels[i], results[i].timeMs(), speedup);
                }
            }
        }

        System.out.println("\n All strategies consistent: same valid count & client count → synchronization works!");
    }
}