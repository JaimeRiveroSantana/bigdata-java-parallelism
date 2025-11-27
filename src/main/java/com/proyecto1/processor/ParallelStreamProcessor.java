package com.proyecto1.processor;

import com.proyecto1.Transaction;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ParallelStreamProcessor implements TransactionProcessor {

    @Override
    public ProcessingResult process(List<Transaction> transactions) {
        Map<String, Double> balanceMap = new HashMap<>();
        AtomicLong validCount = new AtomicLong(0);

        long start = System.nanoTime();

        transactions.parallelStream().forEach(t -> {
            if (t.isValid()) {
                double dummy = 0;
                double amount = t.getAmount();
                for (int i = 0; i < 100; i++) {
                    dummy += Math.sin(amount) * Math.cos(i);
                }
                dummy = Math.sqrt(Math.abs(dummy)); // abs to avoid NaN

                validCount.incrementAndGet();
                synchronized (balanceMap) {
                    balanceMap.merge(t.getClientId(), amount, Double::sum);
                }

                if (dummy < -1e9 || dummy > 1e9) System.out.println("unlikely");
            }
        });

        long timeNs = System.nanoTime() - start;
        return new ProcessingResult(timeNs, validCount.get(), balanceMap.size());
    }
}