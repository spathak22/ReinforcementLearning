package assignment5;

import java.util.ArrayList;
import java.util.List;

public class BanditProblems {
	List<Double> lA1;
	List<Double> lA2;
	List<List<Double>> lB1;
	
	BanditProblems(){
		lA1 = new ArrayList<Double>();
		lA1.add(0.05);
		lA1.add(1.0);
		
		lA2 = new ArrayList<Double>();
		lA2.add(1.0);
		lA2.add(1.0);
		
		lB1 = new ArrayList<List<Double>>();
		for(int i=1;i<=20;i++){
			List<Double> ls = new ArrayList<Double>();
			ls.add((double) (i/20));
			ls.add(0.1);
			lB1.add(ls);
		}
	}
	
	
	public int getNumArms(String bandit){
		if(bandit.equals("A")){
			return 10;
		} else if(bandit.equals("B")){
			return 20;
		} else 
			return 10;
	}
	
	
	public List<Double> pullArm(String bandit, int arm){
		if(bandit.equals("A")){
			return getBanditA(arm);
		} else if(bandit.equals("B")){
			return getBanditB(arm);
		} else 
			return getBanditC(arm);
	}
	
	
	//Bandit Problem1
	public List<Double> getBanditA(int arm){
		if (arm<9){
			return lA1;
		} else{
			return lA2;
		}
	}
	
	//Bandit Problem2
	public List<Double> getBanditB(int arm){
		return lB1.get(arm);
	}

	
	//Bandit Problem3
	public List<Double> getBanditC(int arm){
		return lA1;
	}
}
