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
                validCount.incrementAndGet();
                synchronized (balanceMap) {
                    balanceMap.merge(t.getClientId(), t.getAmount(), Double::sum);
                }
            }
        });

        long timeNs = System.nanoTime() - start;
        return new ProcessingResult(timeNs, validCount.get(), balanceMap.size());
    }
}