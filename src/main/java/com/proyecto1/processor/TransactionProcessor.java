package com.proyecto1.processor;

import java.util.List;
import com.proyecto1.Transaction;

public interface TransactionProcessor {
    ProcessingResult process(List<Transaction> transactions);
}