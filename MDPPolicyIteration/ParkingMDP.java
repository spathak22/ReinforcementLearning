import java.util.ArrayList;
import java.util.List;


public class ParkingMDP {

	List<String> states = new ArrayList<String>();
	ArrayList<ArrayList<Double>> drive = new ArrayList<ArrayList<Double>>(81);
	ArrayList<ArrayList<Integer>> exit = new ArrayList<ArrayList<Integer>>(81);
	ArrayList<ArrayList<Integer>> park = new ArrayList<ArrayList<Integer>>(81);
	ArrayList<Double> reward = new ArrayList<Double>(81);
	


	public static void main(String[] args) {
		ParkingMDP pmdp = new ParkingMDP();
		pmdp.instantiateMatrices();
		pmdp.populateZeros();
		int n = 10;

		pmdp.generateStates(n);
		pmdp.populateDriveMatrix();
		pmdp.populateParkMatrix();
		pmdp.populateExitMatrix();
		pmdp.calculateReward();
		pmdp.printDriveMatrix();
		pmdp.printExitMatrix();
		pmdp.printParkMatrix();
		pmdp.printReward();
	}

	private void printReward(){
		System.out.println("\n");
		for(Double val: reward){
			
			System.out.print(val+" ");
		}
		
		
	}
	
	
	private void instantiateMatrices(){
		for(int i = 0; i < 81; i++)  {
			drive.add(new ArrayList<Double>(81));
			exit.add(new ArrayList<Integer>(81));
			park.add(new ArrayList<Integer>(81));
		}	
	}

	private void calculateReward(){
		int i = 0;
		for (String state:states){
			String [] s = state.split(",");

			Integer no = Integer.parseInt(s[1]); 
			String ocup = s[2];
			String park = s[3];
			
			if (park.equals("F")){
				reward.add(-1.0);	
			}else if (park.equals("T")){
				
				if(ocup.equals("T")){
					reward.add(-100.0);
				} else if (no == 1){
					reward.add(50.0);
				} else{
					reward.add(1000.0/no);
				}
			}
			i++;
		}		
	}
	
	
	private void populateZeros(){
		for(int i = 0; i < 81; i++)  {
			for(int j = 0; j < 81; j++)  {
				drive.get(i).add(0.0);
				exit.get(i).add(0);
				park.get(i).add(0);
			}
		}
	}

	private  void populateExitMatrix() {
		int i = 0;
		for (String state:states){
			String [] s = state.split(",");

			String park = s[3];
			if (park.equals("T")){
				exit.get(i).add(80,1);	
			}
			i++;
		}

	}		


	private  void populateParkMatrix() {
		int i = 0;
		for (String state:states){
			String [] s = state.split(",");

			String pr = s[3];
			if (pr.equals("F")){
//				System.out.println(s[0]+","+s[1]+","+s[2]+","+"T");
				int a4 = states.indexOf(s[0]+","+s[1]+","+s[2]+","+"T");
				park.get(i).add(a4,1);	
			}
			i++;
		}
		
	}
	
	
	private  void populateDriveMatrix() {
		int i = 0;
		for (String state:states){
			String [] s = state.split(",");

			String park = s[3];
			if (park.equals("F")){

				int no = Integer.parseInt(s[1]);
				String loc = s[0]+s[1];
				if (loc.equals("A1")){
					int a2 = states.indexOf("B,1,T,F");
					int a4 = states.indexOf("B,1,F,F");

					drive.get(i).add(a2, 1.0);
					drive.get(i).add(a4, 0.0);
				}else if (loc.equals("B10")){
					int a2 = states.indexOf("A,10,T,F");
					int a4 = states.indexOf("A,10,F,F");

					drive.get(i).add(a2, 0.0);
					drive.get(i).add(a4, 1.0);				
				}else if (loc.startsWith("A")){
					int nextState = no - 1;
					double fprob = nextState*0.1;
					double tprob = 1- fprob;

					int a2 = states.indexOf("A,"+nextState+",T,F");
					int a4 = states.indexOf("A,"+nextState+",F,F");

					drive.get(i).add(a2, tprob);
					drive.get(i).add(a4, fprob);				

				}else if (loc.startsWith("B")){
					int nextState = no + 1;
					double fprob = nextState*0.1;
					double tprob = 1- fprob;

					int a2 = states.indexOf("B,"+nextState+",T,F");
					int a4 = states.indexOf("B,"+nextState+",F,F");

					drive.get(i).add(a2, tprob);
					drive.get(i).add(a4, fprob);	

				}
			}
			i++;

		}

	}




	public void generateStates(int n){
		int i = 1;
		while(i<=n){
			states.add("A,"+i+",T,T");
			states.add("A,"+i+",T,F");
			states.add("A,"+i+",F,T");
			states.add("A,"+i+",F,F");
			states.add("B,"+i+",T,T");
			states.add("B,"+i+",T,F");
			states.add("B,"+i+",F,T");
			states.add("B,"+i+",F,F");
			i++;
		}
	}


	public void printDriveMatrix(){

		System.out.println("Drive Matrix ");
		System.out.println("-------------------------------------------------------------------------- ");
		int i =0; 
		while(i<81){
			int j = 1;
//			System.out.print("[");
			while(j< 81){
				System.out.printf("%.5f",drive.get(i).get(j));
				System.out.print(" ");
				j++;
			}
//			System.out.print("]");
			System.out.print("\n");
			i++;
		}

	}
	
	public void printExitMatrix(){

		System.out.println("Exit Matrix ");
		System.out.println("-------------------------------------------------------------------------- ");
		int i =0; 
		while(i<81){
			int j = 1;
//			System.out.print("[");
			while(j< 81){
				System.out.print(exit.get(i).get(j));
				System.out.print(" ");
				j++;
			}
//			System.out.print("]");
			System.out.print("\n");
			i++;
		}

	}
	
	public void printParkMatrix(){

		System.out.println("Park Matrix ");
		System.out.println("-------------------------------------------------------------------------- ");
		int i =0; 
		while(i<81){
			int j = 1;
//			System.out.print("[");
			while(j< 81){
				System.out.print(park.get(i).get(j));
				System.out.print(" ");
				j++;
			}
//			System.out.print("]");
			System.out.print("\n");
			i++;
		}

	}
	
}
