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


public class MDPValueIteration {
	int noStates=0;
	int noAction=0;
	Map<Integer,Double[][]> kMap = new HashMap<Integer,Double[][]>();
	Double [][] rewardMatrix = null;
	Double [][] vf = null;
	Integer [][] pf = null;
	Double [][] max = null;
	int horizon = 10;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MDPValueIteration mdp = new MDPValueIteration();
		
		String path = args[0];
		mdp.horizon = Integer.parseInt(args[1]);
		
		mdp.readInputMDP(path);
		mdp.calcValueFunction();
	}

	public void calcValueFunction(){
		vf = new Double [horizon][noStates];
		pf = new Integer [horizon][noStates];
		max = new Double [noStates][noAction];
		populateValueFunction(vf, rewardMatrix);
		populatePolicyFunction(pf);

		int h = 1;
		while(h< horizon){
			int i =0;
			while(i < noStates){
				int j = 0;
				while(j<noAction){
					max[i][j] =   addTransitionMatrix(i,j,h-1); 
					j++;
				}
				Double [] val = getMax(i);
				vf[h][i] = rewardMatrix[0][i] + val[0];
				pf[h][i] = val[1].intValue();
				i++;
			}
			h++;
		}
		printValueFunction(vf,horizon,noStates);
		printPolicyFunction(pf,horizon,noStates);
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
			sum += actionMatrix[ii][k] * vf[h][k];
			k++;
		}
		return sum;
	}

	public void populateValueFunction(Double [][] vf, Double [][]  rm){
		int i = 0;
		while(i<noStates){
			vf[0][i] = rm[0][i];
			i++;
		}
	}

	public void populatePolicyFunction(Integer [][] vf){
		int i = 0;
		while(i<noStates){
			vf[0][i] = 0;
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
		int i =0;
		while(i<noStates){
			mat[rnum][i] = Double.parseDouble(sp[i]);
			i++;
		}
	}

	public void printValueFunction(Double[][] mat, int ii, int jj){
		System.out.println("Value Function Matrix ");
		System.out.println("-------------------------------------------------------------------------- ");
		int j =0; 
		while(j<jj){
			int i = 0;
			System.out.print("[");
			while(i< ii){
				System.out.printf("%.5f",mat[i][j]);
				System.out.print(" ");
				i++;
			}
			System.out.print("]");
			System.out.println("\n");
			j++;
		}
	}
	
	
	public void printPolicyFunction(Integer[][] mat, int ii, int jj){
		System.out.println("Policy Matrix ");
		System.out.println("-------------------------------------------------------------------------- ");
		int j =0; 
		while(j<jj){
			int i = 0;
			System.out.print("[");
			while(i< ii){
				System.out.print(mat[i][j]);
				System.out.print(" ");
				i++;
			}
			System.out.print("]");
			System.out.println("\n");
			j++;
		}
	}
}
