
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

public class MDPSimulator {
	int noStates=0;
	int noAction=0;
	Map<Integer,Double[][]> kMap = new HashMap<Integer,Double[][]>();
	Double [][] rewardMatrix = null;
	Double [][] policyMatrix = null;
	double epsilon = 0.6;
	
	
	public Double simulateMDP(int trials, String policy, Double [][] qf){
		Double reward = 0.0;
		int i = 0;
		while(i<trials){
			//Get start state randomly A,10,T,F or A,10,F,F
			int j = getRandomStartState();
			//81 is Exit or terminal state
			while(j != 80){
				//get Desired Action for State Using Policy Function
				Integer action = getPolicy(j,policy,qf);;
				//flip the coin and get random probability between 0 to 1 
				Double ranndomProb = Math.random();
				int nextState = getNextState(ranndomProb, kMap.get(action), j);
				reward = reward + rewardMatrix[0][nextState];
				j = nextState;
			}
			i++;
		}
		
		Double avgReward = reward/trials;
		return avgReward;
	}

	
	public int getPolicy(int state, String policy, Double [][] qf){
		ParkingMDP pmdp = new ParkingMDP();
		pmdp.generateStates(10);
		String stateName = pmdp.getStateName(state);
		String [] sp = stateName.split(",");
		String ocup = sp[2];
		String loc = sp[0];
		String no = sp[1];
		String park = sp[3];
		
		if (policy.equals("P1")){
			
//			System.out.println(" Executing Policy1 ");
			Double ranndomProb = Math.random();
			if (ranndomProb <= epsilon){
				return 0; // 
			} else {
				return 1; 
			}
		}
		else if (policy.equals("P2")){
//			System.out.println(" Executing Policy2 ");


			if(ocup.equals("T")){
				return 0;
			} else{

				Double ranndomProb = Math.random();
				if (ranndomProb <= epsilon){
					return 0; // Park
				} else {
					return 1; 
				}
			}
		} else if(policy.equals("QFunction")){
			List <Double> ls = new ArrayList <Double>();
			int i = 0;
			while(i<=2){
				ls.add(qf[state][i]);
				i++;
			}
			Double d = Collections.max(ls);
			return ls.indexOf(d);
		} else if(policy.equals("P3")){
			
			if (ocup.equals("T")){
				return 0;
			} return 1;
		} else if (policy.equals("P4")){
			if (ocup.equals("T") & (park.equals("F"))){
				return 0;
			} return 1;
			
		}
		
		return 80;
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
