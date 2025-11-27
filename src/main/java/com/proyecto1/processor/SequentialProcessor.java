package com.proyecto1.processor;

import com.proyecto1.Transaction;
import java.util.*;

public class SequentialProcessor implements TransactionProcessor {
    @Override
    public ProcessingResult process(List<Transaction> transactions) {
        Map<String, Double> balanceMap = new HashMap<>();
        long validCount = 0;

        long start = System.nanoTime();

        for (Transaction t : transactions) {
            if (t.isValid()) {
                validCount++;
                balanceMap.merge(t.getClientId(), t.getAmount(), Double::sum);
            }
        }

        long timeNs = System.nanoTime() - start;
        return new ProcessingResult(timeNs, validCount, balanceMap.size());
    }
}