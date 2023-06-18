public class Miner extends Wallet {
    private int difficulty; // Represents the level of difficulty for mining a block
    private Blockchain blockchain; // Represents the blockchain on which the miner operates

    public Miner(int difficulty, Blockchain blockchain) {
        super(blockchain); // Calls the constructor of the Wallet class
        this.difficulty = difficulty; // Initializes the difficulty variable
        this.blockchain = blockchain; // Initializes the blockchain variable
    }

    public Block mine() {
        Block block = new Block(blockchain.getLatestBlock().getHash()); // Creates a new block with the previous block's hash

        // Loop until a valid block hash is found based on the difficulty
        while (!block.getHash().substring(0, difficulty).equals(Utils.zeros(difficulty))) {
            block.incrementNonce(); // Increments the nonce value of the block
            block.setHash(block.calculateHash()); // Calculates the hash of the block
        }

        // Create a reward transaction for the miner
        Transaction rewardTransaction = new Transaction(this.getPublicKey(), this.getPublicKey(), 10.0f, this.getInputTransactions(), getPrivateKey());

        // Increase the miner's balance by the reward transaction amount
        this.increaseBalance(rewardTransaction);

        // Add the reward transaction to the mined block
        block.addTransaction(rewardTransaction);

        // Print the mined block's hash and the miner's actual balance
        System.out.println("Block mined with hash: " + block.getHash() + " actual Balance of miner is : " + this.calculateBalance());

        // Add the mined block to the blockchain
        blockchain.addBlock(block);

        // Return the mined block
        return block;
    }
}


