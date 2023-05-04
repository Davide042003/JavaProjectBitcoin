public class Miner {
    private int difficulty;
    private Blockchain blockchain;

    public Miner(int difficulty, Blockchain blockchain) {
        this.difficulty = difficulty;
        this.blockchain = blockchain;
    }

    public Block mine() {
        Block block = new Block(blockchain.getLatestBlock().getHash());

        while (!block.getHash().substring(0, difficulty).equals(Utils.zeros(difficulty))) {
            block.setHash(block.calculateHash());
        }

        System.out.println("Block mined with hash: " + block.getHash());
        blockchain.addBlock(block);

        return block;
    }
}

