
public class PolicySimulator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "/Users/SherLock/Tomcat/Workspace/RLAssignment/src/mdp.txt";
		MDPSimulator mdp = new MDPSimulator();
		mdp.readInputMDP(path);
		Double p1Reward = mdp.simulateMDP(1000, "P1", null);
		Double p2Reward = mdp.simulateMDP(1000, "P2", null);
		Double p3Reward = mdp.simulateMDP(1000, "P3", null);
		Double p4Reward = mdp.simulateMDP(1000, "P4", null);
		
		System.out.println(" Policy1 Reward for 1000 trials::"+p1Reward);
		System.out.println(" Policy2 Reward for 1000 trials::"+p2Reward);
		System.out.println(" Policy3 Reward for 1000 trials::"+p3Reward);
		System.out.println(" Policy4 Reward for 1000 trials::"+p4Reward);
	}

}
