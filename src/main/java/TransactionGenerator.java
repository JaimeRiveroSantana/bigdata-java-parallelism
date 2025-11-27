import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionGenerator {
    private static final int TOTAL_TRANSACTIONS = 1_000_000;
    private static final int NUM_CLIENTS = 1_000;

    public static List<Transaction> generateTransactions() {
        List<Transaction> transactions = new ArrayList<>(TOTAL_TRANSACTIONS);
        Random rand = ThreadLocalRandom.current();

        for (long i = 0; i < TOTAL_TRANSACTIONS; i++) {
            String clientId = "C" + (rand.nextInt(NUM_CLIENTS) + 1);
            double amount = -1000 + rand.nextDouble() * 6000; // [-1000, 5000]
            boolean valid = rand.nextDouble() > 0.2;

            transactions.add(new Transaction(i, clientId, amount, valid));
        }
        return transactions;
    }
}