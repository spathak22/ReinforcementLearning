package assignment5;

import java.util.Map;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

public class EvaluateBanditAlgorithms{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EvaluateBanditAlgorithms eval = new EvaluateBanditAlgorithms();
		BanditAlgorithms bandit = new BanditAlgorithms();
		
		//SimpleRegret
		Map <Integer, Double> regretInc = bandit.incrementalUniform("A","CR",1);
		eval.generateGraph(regretInc);
		
		
		Map <Integer, Double> regretUdb = bandit.ucb("A","CR",1);
		eval.generateGraph(regretUdb);
		
		Map <Integer, Double> regretEpsilon = bandit.epsilonGreedy("A","CR",1);
		eval.generateGraph(regretEpsilon);
		
//		//CumulativeRegret
//		bandit.incrementalUniform("A","CR",4);
//		bandit.ucb("A","CR",4);
//		bandit.epsilonGreedy("A","CR",4);
		
		
		
	}
	
	
	public void generateGraph(Map <Integer, Double> regret){
		System.out.println("Cumulative regret size is :: "+regret.size());
		DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		for (Map.Entry<Integer, Double> entry : regret.entrySet()){
			if(entry.getKey()%100 == 0 ){
//				System.out.println("Entry :: "+entry.getValue()+" Value :: "+entry.getKey());
				dataset.addValue(entry.getValue(),"Regret",entry.getKey().toString());
//			System.out.println("Adding dataset value :: "+entry.getKey());
			}
		}
		
	      LineChart_AWT chart = new LineChart_AWT(
	    	         "Bandits" ,
	    	         "Regret vs No of Pulls", dataset);

	    	      chart.pack( );
	    	      RefineryUtilities.centerFrameOnScreen( chart );
	    	      chart.setVisible( true );		
	}


}
