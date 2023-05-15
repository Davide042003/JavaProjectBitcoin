import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private ArrayList<Transaction> inputTransactions;
    private ArrayList<Transaction> outputTransactions;

    private Blockchain chain;

    public Wallet(Blockchain chain) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            this.publicKey = pair.getPublic();
            this.privateKey = pair.getPrivate();
            this.inputTransactions = new ArrayList<Transaction>();
            this.outputTransactions = new ArrayList<Transaction>();
            this.chain = chain;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public float calculateBalance() {
        float totalInputValue = 0;
        float totalOutputValue = 0;

        // Calculate total input value
        for (Transaction tx : this.inputTransactions) {
            if (tx.getRecipient().equals(this.publicKey)) {
                totalInputValue += tx.getValue();
            }
        }

        // Calculate total output value
        for (Transaction tx : this.outputTransactions) {
            if (tx.getSender().equals(this.publicKey)) {
                totalOutputValue += tx.getValue();
            }
        }

        return totalInputValue - totalOutputValue;
    }

    public boolean hasEnoughMoney(float amount) {
        return calculateBalance() >= amount;
    }

    public void sendMoney(Wallet recipient, float amount) {
        if (hasEnoughMoney(amount) & checkDoubleSpending(recipient.getPublicKey(), amount)) {
            Transaction transaction = new Transaction(this.publicKey, recipient.getPublicKey(), amount, this.inputTransactions, privateKey);
            if (transaction.verifyTransaction(this.inputTransactions)) {
                this.outputTransactions.add(transaction);
                recipient.inputTransactions.add(transaction);
            } else {
                System.out.println("Transaction failed to verify.");
            }
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public boolean checkDoubleSpending(PublicKey recipient, float value) {
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
        inputTransactions.add(tx);
    }

    public ArrayList<Transaction> getInputTransactions(){
        return inputTransactions;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

}
