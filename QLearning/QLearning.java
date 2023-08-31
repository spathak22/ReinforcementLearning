
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QLearning {

	int noStates=0;
	int noAction=0;
	Map<Integer,Double[][]> kMap = new HashMap<Integer,Double[][]>();
	Double [][] rewardMatrix = null;
	Double [][] policyMatrix = null;
	Double reward = 0.0;
	Double [][] qFunction = new Double[81][3];
	double alpha = 0.1;
	double beta = 0.9;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String path = "/Users/SherLock/Tomcat/Workspace/RLAssignment/src/mdp.txt";
		QLearning ql = new QLearning();
		MDPSimulator mdp = new MDPSimulator();
		mdp.readInputMDP(path);

		ql.populateQFunction();
		ql.readInputMDP(path);
		ql.generateQFunction(1001, mdp);
	}

	public void generateQFunction(int trials, MDPSimulator mdp ){
		int i = 0;
		int action = 0;
		Random rand = new Random();
		while(i<trials){
			//			System.out.println(" Trial :: "+i);
			//Get start state randomly A,10,T,F or A,10,F,F
			int state = getRandomStartState();
			//80 is Exit or terminal state
			while(state != 80){

				ParkingMDP pmdp = new ParkingMDP();
				pmdp.generateStates(10);
				String stateName = pmdp.getStateName(state);
				String [] sp = stateName.split(",");
				String park = sp[3];

				if(park.equals("T"))
					action = 2;
				else{
					double d = greedyPolicyCalc(i);

					//Explore
					if(Math.random() <= d){
						action = rand.nextInt(noAction-1);
					}else {   //Exploit
						action = maxQAction(state);
					}
				}

				Double ranndomProb = Math.random();
				int nextState = getNextState(ranndomProb, kMap.get(action), state);

				qFunction[state][action] = qFunction[state][action] + alpha*(rewardMatrix[0][state] + beta*maxQValue(nextState) - qFunction[state][action]);
				state = nextState;
			}
			
//			if (i%1000 == 0){
				Double p1Reward = mdp.simulateMDP(5000, "QFunction", qFunction);
				System.out.println("Qlearning reward after "+i+ " = "+p1Reward);
//			}
			i++;
		}

		printQFunction();
	}

	public void printQFunction(){
		System.out.println(" --------------QFunction matrix---------------- ");
		for (int i =0;i<81;i++){
			System.out.print("[");
			for (int j=0;j<3;j++){
				System.out.printf("%.5f",qFunction[i][j]);
				System.out.print(" ");
			}
			System.out.print("]");
			System.out.print("\n");
		}
	}

	public Double maxQValue(int state){
		List <Double> ls = new ArrayList <Double>();
		int i = 0;
		while(i<=2){
			ls.add(qFunction[state][i]);
			i++;
		}

		return Collections.max(ls);
	} 



	public int maxQAction(int state){
		List <Double> ls = new ArrayList <Double>();
		int i = 0;
		while(i<=2){
			ls.add(qFunction[state][i]);
			i++;
		}

		Double d = Collections.max(ls);
		return ls.indexOf(d);
	} 


	public Double greedyPolicyCalc(int trials){
		Double mid = (1-0.1)/10000;
		Double e = mid*trials;
		return (Double) (1-e);
	}

	public int getNextState(Double ranndomProb, Double [][] actionMatrix, int row){
		int i = 0;
		List <Integer> list = new ArrayList<Integer>();
		while(i<noStates){
			if (actionMatrix[row][i] >= ranndomProb){
				list.add(i);
			}
			i++;
		}
		int max = list.size()-1;
		int min = 0;
		if(max-min > 0){
			Random randomizer = new Random();
			return list.get(randomizer.nextInt(list.size()));
		} else if (max == 0)
			return list.get(0);
		else 
			return 80;
	}




	public int getRandomStartState(){
		Random randomizer = new Random();
		List <Integer> list = new ArrayList <Integer> ();
		list.add(73);
		list.add(75);
		return list.get(randomizer.nextInt(list.size()));
	}

	public void populateQFunction(){
		for (int i =0;i<81;i++){
			for (int j=0;j<3;j++){
				qFunction[i][j] = 0.0;
			}
		}

	}

	public void readInputMDP(String fpath){
		List <String> list = new ArrayList <String>();
		int count = 0;
		int rowNum = 0;
		Double [][] curMtrx = null;

		try {
			//			String fpath = System.getProperty("user.dir").toString()+"/src/mdp.txt";
			File f = new File(fpath);

			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;

			while((line = br.readLine()) != null) {
				if (!line.isEmpty()){ 
					String [] sp = line.split("\\s+");
					if (count == 0){
						noStates = Integer.parseInt(sp[0]);
						noAction = Integer.parseInt(sp[1]);
						for(int k = 0; k < noAction; k++){
							kMap.put(k, new Double[noStates][noStates]);
						}
					} else{
						populateMatrix(curMtrx,rowNum,sp);
						rowNum++;
					}

				} else{
					if (count == noAction){
						rewardMatrix = new Double[1][noStates];
						curMtrx = rewardMatrix;
					} else if (count == noAction+1){
						policyMatrix = new Double[1][noStates];
						curMtrx = policyMatrix;
					} else{
						curMtrx = kMap.get(count);
					}
					rowNum = 0;
					count++;
				}
			}

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
	}




	public void populateMatrix(Double [][] mat, int rnum, String [] sp){
		//		System.out.println(" Populating matrix  1 ::");
		//		System.out.println("Mat size :: "+mat);
		int i =0;
		while(i<noStates){
			//			System.out.println(i);
			//			System.out.println(sp[i]);
			mat[rnum][i] = Double.parseDouble(sp[i]);
			i++;
		}
	}


}
