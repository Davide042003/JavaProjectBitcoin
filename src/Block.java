import java.util.ArrayList;
import java.util.Date;
import java.security.MessageDigest;

public class Block {
    private String previousHash;
    private String hash;
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private long timestamp;
    private int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
        this.nonce = 0;
    }

    public String calculateHash() {
        String data = previousHash + Long.toString(timestamp) + Integer.toString(nonce) + transactions.toString();
        return applySha256(data);
    }

    private String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) return false;
        if ((!previousHash.equals("0")) && (!transaction.verifyTransaction(transactions))) {
            System.out.println("Transaction failed to verify. Discarded.");
            return false;
        }
        transactions.add(transaction);
        hash = calculateHash();
        return true;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String Hash) {
        this.hash = Hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void incrementNonce() {
        nonce++;
    }
}
