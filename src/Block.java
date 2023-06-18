import java.util.ArrayList;
import java.util.Date;
import java.security.MessageDigest;

public class Block {
    private String previousHash; // Hash of the previous block in the blockchain
    private String hash; // Hash of the current block
    private ArrayList<Transaction> transactions = new ArrayList<Transaction>(); // List of transactions in the block
    private long timestamp; // Time at which the block was created
    private int nonce; // Number used in the mining process

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timestamp = new Date().getTime(); // Set the timestamp to the current time in milliseconds
        this.hash = calculateHash(); // Calculate the hash of the block
        this.nonce = 0; // Set the nonce to 0 initially
    }

    // Calculate the hash of the block by combining various block components
    public String calculateHash() {
        String data = previousHash + Long.toString(timestamp) + Integer.toString(nonce) + transactions.toString();
        return applySha256(data); // Apply the SHA-256 algorithm to the combined data and return the resulting hash
    }

    // Apply the SHA-256 algorithm to a given input string
    private String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));

            // Convert the byte array hash to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Add a transaction to the block if it's valid
    public boolean addTransaction(Transaction transaction) {
        if (transaction == null)
            return false; // Reject null transactions

        // Verify the transaction against existing transactions in the block
        if ((!previousHash.equals("0")) && (!transaction.verifyTransaction(transactions))) {
            System.out.println("Transaction failed to verify. Discarded.");
            return false; // Reject invalid transactions
        }

        transactions.add(transaction); // Add the transaction to the block's list of transactions
        hash = calculateHash(); // Recalculate the hash of the block
        return true; // Return true to indicate successful addition of the transaction
    }

    // Getter method to retrieve the hash of the block
    public String getHash() {
        return hash;
    }

    // Setter method to set the hash of the block
    public void setHash(String hash) {
        this.hash = hash;
    }

    // Getter method to retrieve the hash of the previous block
    public String getPreviousHash() {
        return previousHash;
    }

    // Getter method to retrieve the list of transactions in the block
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    // Increment the nonce value
    public void incrementNonce() {
        nonce++;
    }
}
