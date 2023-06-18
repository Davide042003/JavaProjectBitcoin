import java.util.ArrayList;

import java.util.ArrayList;

public class Blockchain {
    private ArrayList<Block> chain = new ArrayList<Block>(); // ArrayList to hold the blocks of the blockchain

    public Blockchain() {
        chain.add(createGenesisBlock()); // Create a new blockchain by adding a genesis block to the chain
    }

    // Create and return the genesis block with a hash value of "0"
    private Block createGenesisBlock() {
        return new Block("0");
    }

    // Add a new block to the blockchain
    public void addBlock(Block newBlock) {
        chain.add(newBlock);
    }

    // Check the integrity of the blockchain
    public boolean isValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            // Verify that the hash of the current block matches the calculated hash
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current hash does not match calculated hash for block " + i + ".");
                return false;
            }

            // Verify that the previous hash stored in the current block matches the hash of the previous block
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous hash does not match hash of previous block for block " + i + ".");
                return false;
            }
        }

        // If all blocks are valid, return true
        return true;
    }

    // Resolve conflicts between two chains by replacing the current chain with the longer chain
    public void resolveConflicts(Blockchain otherChain) {
        if (otherChain.chain.size() > chain.size()) {
            chain = otherChain.chain;
        }
    }

    // Get the last block in the chain
    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    // Get the entire chain as an ArrayList<Block>
    public ArrayList<Block> getChain() {
        return chain;
    }
}
