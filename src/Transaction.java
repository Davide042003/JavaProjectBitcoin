import java.security.*;
import java.util.ArrayList;
import java.util.List;
public class Transaction {
    // Private fields
    private String transactionId; // Unique identifier for the transaction
    private PublicKey sender; // Public key of the transaction sender
    private PublicKey recipient; // Public key of the transaction recipient
    private float value; // Amount/value of the transaction
    private byte[] signature; // Cryptographic signature of the transaction
    private static int sequence = 0; // Static sequence number for generating unique transaction hashes

    // Constructor
    public Transaction(PublicKey sender, PublicKey recipient, float value, ArrayList<Transaction> inputTransactions, PrivateKey privateKey) {
        this.sender = sender; // Set the sender's public key
        this.recipient = recipient; // Set the recipient's public key
        this.value = value; // Set the transaction amount/value
        this.signature = signTransaction(privateKey, inputTransactions); // Generate the transaction signature
        this.transactionId = calculateTransactionHash(); // Generate the transaction ID
    }

    // Calculate the transaction hash by combining relevant data and hashing it
    private String calculateTransactionHash() {
        sequence++; // Increment the transaction sequence number
        String data = sender.toString() + recipient.toString() + Float.toString(value) + Integer.toString(sequence); // Concatenate sender, recipient, value, and sequence

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // Create a SHA-256 message digest
            byte[] hashBytes = md.digest(data.getBytes("UTF-8")); // Generate the hash of the data
            StringBuffer hexString = new StringBuffer();

            // Convert the hash bytes to a hexadecimal string representation
            for (int i = 0; i < hashBytes.length; i++) {
                String hex = Integer.toHexString(0xff & hashBytes[i]); // Convert each byte to a two-digit hexadecimal representation

                if (hex.length() == 1) {
                    hexString.append('0'); // Ensure a leading zero for single digit hex values
                }

                hexString.append(hex); // Append the hex value to the string buffer
            }

            return hexString.toString(); // Return the final transaction hash
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null if an exception occurs
    }

    // Generate a digital signature for the transaction using the private key
    public byte[] signTransaction(PrivateKey privateKey, ArrayList<Transaction> inputTransactions) {
        String data = getTransactionInputString(inputTransactions) + recipient.toString() + Float.toString(value); // Concatenate input transaction IDs, recipient, and value

        try {
            Signature signature = Signature.getInstance("SHA256withRSA"); // Create a SHA256withRSA signature instance
            signature.initSign(privateKey); // Initialize the signature with the private key
            signature.update(data.getBytes("UTF-8")); // Update the signature with the data
            return signature.sign(); // Generate the digital signature
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Return null if an exception occurs
    }

    // Create a concatenated string of transaction IDs from input transactions
    private String getTransactionInputString(List<Transaction> inputTransactions) {
        StringBuffer inputString = new StringBuffer();

        for (Transaction tx : inputTransactions) {
            inputString.append(tx.transactionId); // Append each transaction ID to the input string
        }

        return inputString.toString(); // Return the final input string
    }

    // Verify the transaction by checking the digital signature against the sender's public key
    public boolean verifyTransaction(List<Transaction> inputTransactions) {
        String data = getTransactionInputString(inputTransactions) + recipient.toString() + Float.toString(value); // Concatenate input transaction IDs, recipient, and value

        try {
            Signature signature = Signature.getInstance("SHA256withRSA"); // Create a SHA256withRSA signature instance
            signature.initVerify(sender); // Initialize the signature verification with the sender's public key
            signature.update(data.getBytes("UTF-8")); // Update the signature with the data
            return signature.verify(this.signature); // Verify the digital signature against the stored signature
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Return false if an exception occurs
    }

    // Getter method to access the transaction amount/value
    public float getValue() {
        return value;
    }

    // Getter method to access the recipient's public key
    public PublicKey getRecipient() {
        return recipient;
    }

    // Getter method to access the sender's public key
    public PublicKey getSender() {
        return sender;
    }
}
