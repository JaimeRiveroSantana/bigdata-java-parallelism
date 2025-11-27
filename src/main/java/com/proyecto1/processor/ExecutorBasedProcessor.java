package com.proyecto1.processor;

import com.proyecto1.Transaction;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ExecutorBasedProcessor implements TransactionProcessor {
    @Override
    public ProcessingResult process(List<Transaction> transactions) throws RuntimeException {
        Map<String, Double> balanceMap = new HashMap<>();
        AtomicLong validCount = new AtomicLong(0);

        int nThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        long start = System.nanoTime();

        int chunkSize = transactions.size() / nThreads;
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < nThreads; i++) {
            final int startIdx = i * chunkSize;
            final int endIdx = (i == nThreads - 1) ? transactions.size() : (i + 1) * chunkSize;
            futures.add(executor.submit(() -> {
                for (int j = startIdx; j < endIdx; j++) {
                    Transaction t = transactions.get(j);
                    if (t.isValid()) {
                        validCount.incrementAndGet();
                        synchronized (balanceMap) {
                            balanceMap.merge(t.getClientId(), t.getAmount(), Double::sum);
                        }
                    }
                }
            }));
        }

        try {
            for (Future<?> f : futures) f.get();
            executor.shutdown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        long timeNs = System.nanoTime() - start;
        return new ProcessingResult(timeNs, validCount.get(), balanceMap.size());
    }
}