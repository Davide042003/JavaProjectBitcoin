import java.util.ArrayList;

public class Blockchain{
    private ArrayList<Block> chain = new ArrayList<Block>();

    public Blockchain() {
        chain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        return new Block("0");
    }

    public void addBlock(Block newBlock) {
        chain.add(newBlock);
    }

    public boolean isValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current hash does not match calculated hash for block " + i + ".");
                return false;
            }

            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous hash does not match hash of previous block for block " + i + ".");
                return false;
            }

            if (!currentBlock.validateBlock()) {
                System.out.println("The bitcoin is already spent.");
                return false;
            }
        }

        return true;
    }

    public void resolveConflicts(Blockchain otherChain) {
        if (otherChain.chain.size() > chain.size()) {
            chain = otherChain.chain;
        }
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }
}
