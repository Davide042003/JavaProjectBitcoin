import java.security.*;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private String transactionId;
    private PublicKey sender;
    private PublicKey recipient;
    private float value;
    private byte[] signature;
    private static int sequence = 0;

    public Transaction(PublicKey sender, PublicKey recipient, float value, ArrayList<Transaction> inputTransactions, PrivateKey privateKey) {
        this.sender = sender;
        this.recipient = recipient;
        this.value = value;
        this.signature = signTransaction(privateKey, inputTransactions);
        this.transactionId = calculateTransactionHash();
    }

    private String calculateTransactionHash() {
        sequence++;
        String data = sender.toString() + recipient.toString() + Float.toString(value) + Integer.toString(sequence);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(data.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < hashBytes.length; i++) {
                String hex = Integer.toHexString(0xff & hashBytes[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] signTransaction(PrivateKey privateKey, ArrayList<Transaction> inputTransactions) {
        String data = getTransactionInputString(inputTransactions) + recipient.toString() + Float.toString(value);
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes("UTF-8"));
            return signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTransactionInputString(ArrayList<Transaction> inputTransactions) {
        StringBuffer inputString = new StringBuffer();
        for (Transaction tx : inputTransactions) {
            inputString.append(tx.transactionId);
        }
        return inputString.toString();
    }

    public boolean verifyTransaction(ArrayList<Transaction> inputTransactions) {
        String data = getTransactionInputString(inputTransactions) + recipient.toString() + Float.toString(value);
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(sender);
            signature.update(data.getBytes("UTF-8"));
            return signature.verify(this.signature);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean checkDoubleSpending(List<Transaction> inputs) {
        float totalInput = 0;
        for (Transaction input : inputs) {
            if (input.recipient.equals(sender)) {
                totalInput += input.value;
            } else {
                return false; // Invalid input
            }
        }
        return totalInput >= value; // Check if the transaction is valid
    }


    public float getValue(){
        return value;
    }

    public PublicKey getRecipient(){
        return recipient;
    }

    public PublicKey getSender(){
        return sender;
    }
}
