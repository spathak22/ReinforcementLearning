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


public class MDPPlanningAlgo {
	int noStates=0;
	int noAction=0;
	Double beta = 0.9;
	Double epsilon = 0.0001;
	Double delta = 1.0;
	
	Map<Integer,Double[][]> kMap = new HashMap<Integer,Double[][]>();
	Double [][] rewardMatrix = null;
	ArrayList<ArrayList<Double>> vf = null;
	ArrayList<ArrayList<Integer>> pf = null;
	Double [][] max = null;
	int horizon = 10;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MDPPlanningAlgo mdp = new MDPPlanningAlgo();
		
		String path = args[0];
		
		mdp.readInputMDP(path);
		mdp.calcValueFunction();
	}

	public void calcValueFunction(){
//		vf = new Double [horizon+1][noStates];
//		pf = new Integer [horizon+1][noStates];
		vf = new ArrayList<ArrayList<Double>>(noStates);
		instantiateVFList(vf,noStates);
		pf = new ArrayList<ArrayList<Integer>>(noStates);
		instantiatePFList(pf,noStates);
		
		max = new Double [noStates][noAction];
		populateValueFunction(vf, rewardMatrix);
		populatePolicyFunction(pf);

		int h = 1;
		List <Double> dList = new  ArrayList <Double>();
		while(dList.size() == 0 || calculateDelta(Collections.max(dList)) > epsilon){
			dList.clear();
			int i =0;
			while(i < noStates){
				int j = 0;
				while(j<noAction){
					max[i][j] =   addTransitionMatrix(i,j,h-1); 
					j++;
				}
				Double [] val = getMax(i);
				vf.get(i).add(rewardMatrix[0][i] + beta*val[0]);
				
				delta = vf.get(i).get(h) - vf.get(i).get(h-1);
				dList.add(delta);
				
				pf.get(i).add(val[1].intValue());
				i++;
			}
			h++;
			if(h == 3){
				truncateList();
				h--;
			}
		}
		printValueFunction(vf,noStates,h);
		printPolicyFunction(pf,noStates,h);
	}

	public void truncateList(){
	    for(int i = 0; i < noStates; i++)  {
	        pf.get(i).remove(0);
	        vf.get(i).remove(0);
	    }
	}
	
	public Double calculateDelta(Double d){
		Double res =  (beta*d)/(1-beta);
		return res;
	}
	
	public void instantiateVFList(ArrayList<ArrayList<Double>> l, int size){
	    for(int i = 0; i < size; i++)  {
	        l.add(new ArrayList<Double>());
	    }
	}
	
	public void instantiatePFList(ArrayList<ArrayList<Integer>> l, int size){
	    for(int i = 0; i < size; i++)  {
	        l.add(new ArrayList<Integer>());
	    }
	}
	
	
	public Double[] getMax(int state){
		int i = 0;
		Double [] maxVal = new Double[2];
		List <Double> ls = new <Double> ArrayList();
		while (i< noAction){
			ls.add(max[state][i]);
			i++;
		}
		maxVal[0] = Collections.max(ls);
		maxVal[1] = 1.0 * ls.indexOf(maxVal[0]);
		return maxVal;
	}
	
	public Double addTransitionMatrix(int ii, int jj, int h){
		Double [][] actionMatrix= kMap.get(jj);
		Double sum = 0.0;
		int k = 0;
		while(k<noStates){
//			sum += actionMatrix[ii][k] * vf[h][k];
			sum += actionMatrix[ii][k] * vf.get(k).get(h);
			k++;
		}
		return sum;
	}

	public void populateValueFunction(ArrayList<ArrayList<Double>> vf, Double [][]  rm){
		int i = 0;
		while(i<noStates){
			vf.get(i).add(rm[0][i]);
			i++;
		}
	}

	public void populatePolicyFunction(ArrayList<ArrayList<Integer>> pf){
		int i = 0;
		while(i<noStates){
			pf.get(i).add(0);
			i++;
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
					}else{
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
		int i =0;
		while(i<noStates){
			mat[rnum][i] = Double.parseDouble(sp[i]);
			i++;
		}
	}

	public void printValueFunction(ArrayList<ArrayList<Double>> mat, int ii, int jj){
		System.out.print("Epsilon         :: ");
		System.out.printf("%.5f", epsilon);
		System.out.println(" \nDiscount factor :: "+beta); 
		System.out.println("-------------------------------------------------------------------------- ");
		System.out.println("Value Function Matrix ");
		System.out.println("-------------------------------------------------------------------------- ");
		int i =0; 
		while(i<ii){
			int j = 1;
			System.out.print("[");
			while(j< jj){
				System.out.printf("%.5f",mat.get(i).get(j));
				System.out.print(" ");
				j++;
			}
			System.out.print("]");
			System.out.println("\n");
			i++;
		}
	}
	
	
	public void printPolicyFunction(ArrayList<ArrayList<Integer>> mat, int ii, int jj){
		System.out.println("Policy Matrix ");
		System.out.println("-------------------------------------------------------------------------- ");
		int i =0; 
		while(i<ii){
			int j = 1;
			System.out.print("[");
			while(j< jj){
				System.out.print(mat.get(i).get(j));
				System.out.print(" ");
				j++;
			}
			System.out.print("]");
			System.out.println("\n");
			i++;
		}
	}
}
