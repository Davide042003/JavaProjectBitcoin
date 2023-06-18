import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private PublicKey publicKey;                   // The public key of the wallet
    private PrivateKey privateKey;                 // The private key of the wallet
    private ArrayList<Transaction> inputTransactions;   // List of input transactions
    private ArrayList<Transaction> outputTransactions;  // List of output transactions

    private Blockchain chain;                     // The blockchain associated with the wallet

    public Wallet(Blockchain chain) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");  // Create a key pair generator for RSA algorithm
            keyGen.initialize(2048);                                        // Set the key size to 2048 bits
            KeyPair pair = keyGen.generateKeyPair();                         // Generate a key pair
            this.publicKey = pair.getPublic();                               // Get the public key from the key pair
            this.privateKey = pair.getPrivate();                             // Get the private key from the key pair
            this.inputTransactions = new ArrayList<Transaction>();          // Initialize input transaction list
            this.outputTransactions = new ArrayList<Transaction>();         // Initialize output transaction list
            this.chain = chain;                                              // Set the blockchain associated with the wallet
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float calculateBalance() {
        float totalInputValue = 0;           // Variable to store the total input value
        float totalOutputValue = 0;          // Variable to store the total output value

        // Calculate total input value
        for (Transaction tx : this.inputTransactions) {
            if (tx.getRecipient().equals(this.publicKey)) {    // Check if the recipient of the transaction is the wallet's public key
                totalInputValue += tx.getValue();              // Add the value of the transaction to the total input value
            }
        }

        // Calculate total output value
        for (Transaction tx : this.outputTransactions) {
            if (tx.getSender().equals(this.publicKey)) {       // Check if the sender of the transaction is the wallet's public key
                totalOutputValue += tx.getValue();             // Add the value of the transaction to the total output value
            }
        }

        return totalInputValue - totalOutputValue;   // Return the calculated balance
    }

    public boolean hasEnoughMoney(float amount) {
        return calculateBalance() >= amount;   // Check if the wallet has enough money by comparing the balance with the given amount
    }

    public void sendMoney(Wallet recipient, float amount) {
        if (hasEnoughMoney(amount) & checkDoubleSpending(recipient.getPublicKey(), amount)) {
            // Create a new transaction with the sender's public key, recipient's public key, amount, input transactions, and private key
            Transaction transaction = new Transaction(this.publicKey, recipient.getPublicKey(), amount, this.inputTransactions, privateKey);

            if (transaction.verifyTransaction(this.inputTransactions)) {  // Verify the transaction using the input transactions
                this.outputTransactions.add(transaction);               // Add the transaction to the sender's output transactions
                recipient.inputTransactions.add(transaction);           // Add the transaction to the recipient's input transactions
            } else {
                System.out.println("Transaction failed to verify.");    // Print an error message if the transaction fails to verify
            }
        } else {
            System.out.println("Insufficient funds.");                  // Print an error message if the wallet has insufficient funds
        }
    }

    public boolean checkDoubleSpending(PublicKey recipient, float value) {
        // Iterate through the blockchain to check if the recipient has already spent the cryptocurrency
        // Iterate through the blockchain to check if the recipient has already spent the cryptocurrency
        for (Block block : chain.getChain()) {
            List<Transaction> transactions = block.getTransactions();
            for (Transaction transaction : transactions) {
                if (transaction.getRecipient().equals(recipient) && transaction.getValue() >= value) {
                    return false; // Recipient has already spent the cryptocurrency
                }
            }
        }
        return true; // Recipient has not spent the cryptocurrency
    }

    public void increaseBalance(Transaction tx){
        inputTransactions.add(tx);  // Add a transaction to the input transactions list (increase balance)
    }

    public ArrayList<Transaction> getInputTransactions(){
        return inputTransactions;   // Return the input transactions list
    }

    public PublicKey getPublicKey() {
        return this.publicKey;      // Return the public key of the wallet
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;     // Return the private key of the wallet
    }
}

