public class Transaction {
    private final long id;
    private final String clientId;
    private final double amount;
    private final boolean valid;

    public Transaction(long id, String clientId, double amount, boolean valid) {
        this.id = id;
        this.clientId = clientId;
        this.amount = amount;
        this.valid = valid;
    }

    public String getClientId() { return clientId; }
    public double getAmount() { return amount; }
    public boolean isValid() { return valid; }
}