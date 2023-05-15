import java.security.PublicKey;

public class Main {


    public static void main(String[] args) {

        Blockchain blockchain = new Blockchain();
        Blockchain blockchain2 = new Blockchain();
        Miner miner = new Miner(3 ,blockchain);
        Miner miner2 = new Miner(5 ,blockchain2);
        Agent agent = new Agent();
        Agent agent2 = new Agent();
        Agent agent3 = new Agent();

        miner.mine();
        miner2.mine();

        miner.sendMoney(agent, 5);
        agent.sendMoney(agent2, 2);
        miner2.sendMoney(agent, 1);
        agent2.sendMoney(agent3, 2);

        // Add the transactions to the blockchain
        System.out.println("Davide is dunky");
        System.out.println("miner balance : " + miner.calculateBalance());
        System.out.println("miner2 balance : " + miner2.calculateBalance());
        System.out.println("agent balance : " + agent.calculateBalance());
        System.out.println("agent2 balance : " + agent2.calculateBalance());
        System.out.println("agent3 balance : " + agent3.calculateBalance());
        System.out.println("blockchain is valid: " + blockchain.isValid());
        System.out.println("blockchain2 is valid: " + blockchain2.isValid());
        System.out.println(blockchain.toString());
        System.out.println(blockchain2.toString());

    }
}
