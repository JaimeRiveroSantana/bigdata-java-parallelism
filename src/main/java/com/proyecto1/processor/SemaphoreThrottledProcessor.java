package com.proyecto1.processor;

import com.proyecto1.Transaction;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class SemaphoreThrottledProcessor implements TransactionProcessor {
    private static final int MAX_WRITERS = 3;

    @Override
    public ProcessingResult process(List<Transaction> transactions) {
        Map<String, Double> balanceMap = new HashMap<>();
        AtomicLong validCount = new AtomicLong(0);
        Semaphore diskSemaphore = new Semaphore(MAX_WRITERS);

        // Step 1:
        int nThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        long start = System.nanoTime();

        int chunkSize = transactions.size() / nThreads;
        for (int i = 0; i < nThreads; i++) {
            final int startIdx = i * chunkSize;
            final int endIdx = (i == nThreads - 1) ? transactions.size() : (i + 1) * chunkSize;
            executor.submit(() -> {
                for (int j = startIdx; j < endIdx; j++) {
                    Transaction t = transactions.get(j);
                    if (t.isValid()) {
                        validCount.incrementAndGet();
                        synchronized (balanceMap) {
                            balanceMap.merge(t.getClientId(), t.getAmount(), Double::sum);
                        }
                    }
                }
            });
        }

        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Step 2:
        ExecutorService writer = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>();
        for (String client : balanceMap.keySet()) {
            futures.add(writer.submit(() -> {
                try {
                    diskSemaphore.acquire();
                    Thread.sleep(0); // simular I/O
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    diskSemaphore.release();
                }
            }));
        }

        try {
            for (Future<?> f : futures) f.get();
            writer.shutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long timeNs = System.nanoTime() - start;
        return new ProcessingResult(timeNs, validCount.get(), balanceMap.size());
    }
}