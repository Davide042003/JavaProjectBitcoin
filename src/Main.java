public class Main {
    public static void main(String[] args) {
        // Create a blockchain with a genesis block
        Blockchain blockchain = new Blockchain();
        Blockchain blockchain2 = new Blockchain();

        // Create a miner and add some transactions to the blockchain
        Miner miner = new Miner(3 ,blockchain);
        Miner miner2 = new Miner(5 ,blockchain2);
        /*Transaction tx1 = new Transaction("Alice", "Bob", 10);
        Transaction tx2 = new Transaction("Bob", "Charlie", 5);
        Transaction tx3 = new Transaction("Charlie", "Alice", 3);*/

        // Add the transactions to the blockchain
        System.out.println("Davide is dunky");
        miner.mine();
        miner2.mine();

        // Print out the blockchain to verify that the transactions were added
        System.out.println(blockchain.toString());
        System.out.println(blockchain2.toString());

    }
}
