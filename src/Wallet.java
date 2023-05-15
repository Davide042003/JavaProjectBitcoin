import java.security.*;
import java.util.ArrayList;

public class Wallet {
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private ArrayList<Transaction> inputTransactions;
    private ArrayList<Transaction> outputTransactions;

    public Wallet() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            this.publicKey = pair.getPublic();
            this.privateKey = pair.getPrivate();
            this.inputTransactions = new ArrayList<Transaction>();
            this.outputTransactions = new ArrayList<Transaction>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
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
        if (hasEnoughMoney(amount)) {
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

    public ArrayList<Transaction> getInputTransactions(){
        return inputTransactions;
    }

    public void increaseBalance(Transaction tx){
        inputTransactions.add(tx);
    }
}
