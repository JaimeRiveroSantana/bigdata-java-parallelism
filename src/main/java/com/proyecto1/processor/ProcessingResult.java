package com.proyecto1.processor;

public class ProcessingResult {
    public final long timeNs;
    public final long validCount;
    public final int uniqueClients;

    public ProcessingResult(long timeNs, long validCount, int uniqueClients) {
        this.timeNs = timeNs;
        this.validCount = validCount;
        this.uniqueClients = uniqueClients;
    }

    public double timeMs() { return timeNs / 1_000_000.0; }
}