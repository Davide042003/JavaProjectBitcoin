import java.security.PublicKey;

public class Main {
    public static void main(String[] args) {

        Blockchain blockchain = new Blockchain();
        Blockchain second_blockchain = new Blockchain();
        Miner miner = new Miner(3 ,blockchain);
        Miner second_miner = new Miner(5, second_blockchain);

        Agent agent = new Agent(blockchain);
        Agent second_agent = new Agent(blockchain);
        Agent third_agent = new Agent(second_blockchain);

        miner.mine();
        second_miner.mine();

        miner.sendMoney(agent, 5);
        second_miner.sendMoney(second_agent, 6);
        agent.sendMoney(third_agent, 3);
        third_agent.sendMoney(second_agent, 1);
        second_agent.sendMoney(agent,2);

        System.out.println("miner balance : " + miner.calculateBalance());
        System.out.println("second miner balance : " + second_miner.calculateBalance());
        System.out.println("agent balance : " + agent.calculateBalance());
        System.out.println("second agent balance : " + second_agent.calculateBalance());
        System.out.println("third agent balance : " + third_agent.calculateBalance());

        System.out.println("blockchain is valid: " + blockchain.isValid());
        System.out.println("second_blockchain is valid: " + second_blockchain.isValid());
        System.out.println(blockchain.toString());
        System.out.println(second_blockchain.toString());

    }
}
