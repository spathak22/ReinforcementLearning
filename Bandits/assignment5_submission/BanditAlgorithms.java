package assignment5;
import org.jfree.chart.JFreeChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BanditAlgorithms {

    BanditProblems bp;
    double epsilon = 0.5;

    BanditAlgorithms(){
        bp = new BanditProblems();
    }


    public     Map <Integer, Double> incrementalUniform(String bandit, String regretType, int trials){
        //Loop
        int totalArms = bp.getNumArms(bandit);
        //Key is arm, list stores AverageReward at 0 and number of Trials at 1
        Map <Integer, Double> armPullRewdAvg = new HashMap <Integer, Double>();
        Map <Integer, Integer> armPullTrials = new HashMap <Integer, Integer>();
        Map <Integer, Double> armExpectedReward = new HashMap <Integer, Double>();

        Map <Integer, Double> cumRegret = new HashMap <Integer, Double>();
        Map <Integer, Double> simpleRegret = new HashMap <Integer, Double>();

        //Calculate Rstar
        Double rStar = getRstar(totalArms,bandit);
        populateArms(bandit,totalArms,armPullRewdAvg,armPullTrials);
       
       
        double avgReward = 0.0;
        int arm = 1;
        Double sum = 0.0;
       
        for (int i=1; i<=trials; i++){
            //N Pulls
            for(int n=1;n<=500000;n++){           
                    List<Double> reward =  pullArm(arm,bandit,armPullRewdAvg,armPullTrials,armExpectedReward);

                    sum = sum + reward.get(0);
                    //Calculate Cumulative regret
                    if(regretType.equals("CR")){
                        Double valCR =  n*rStar - sum;
                        cumRegret.put(n, valCR);
                        System.out.println("Cumulative Regret :: "+n+" = "+valCR);
                    }else{
                        //Calculate Simple regret
                    	Map.Entry<Integer, Double> rewardArm = getArmWithMaxAvgReward(armPullRewdAvg);
                    	List<Double> probArm = bp.pullArm(bandit, rewardArm.getKey());
                        Double valSR = rStar - rewardArm.getValue() * probArm.get(1);
                        simpleRegret.put(n, valSR);                       
                    }
                    arm++;
                    if (arm > totalArms)
                        arm = 1;
                   
            }
        }
        if(regretType.equals("CR")){
            return cumRegret;
        }   
        return simpleRegret;
       
//        Map.Entry<Integer, Double> maxAvgArm = getArmWithMaxAvgReward(armPullRewdAvg);


    }

    public Map <Integer, Double> ucb(String bandit, String regretType, int trials){
        //Loop
        int totalArms = bp.getNumArms(bandit);
        List<Integer> arms = createArmsList(totalArms);

        //Key is arm, list stores AverageReward at 0 and number of Trials at 1
        Map <Integer, Double> armPullRewdAvg = new HashMap <Integer, Double>();
        Map <Integer, Integer> armPullTrials = new HashMap <Integer, Integer>();
        Map <Integer, Double> armExpectedReward = new HashMap <Integer, Double>();
       
       
        Map <Integer, Double> cumRegret = new HashMap <Integer, Double>();
        Map <Integer, Double> simpleRegret = new HashMap <Integer, Double>();

        populateArms(bandit,totalArms,armPullRewdAvg,armPullTrials);
        
        //Calculate Rstar
        Double rStar = getRstar(totalArms,bandit);
       
       
//        double avgReward = 0.0;
        Double sum = 0.0;
       
        for (int i=1; i<=trials; i++){
            //N Pulls
            for(int n=1;n<=500000;n++){
                   
                 int arm = getUCBArm(bandit,totalArms, n, armPullRewdAvg,  armPullTrials);
                   
                 List<Double> reward = pullArm(arm,bandit,armPullRewdAvg,armPullTrials,armExpectedReward);

                    sum = sum + reward.get(0);
                    
                    
                    //Calculate Cumulative regret
                    if(regretType.equals("CR")){
                        Double valCR =  n*rStar - sum;
                        cumRegret.put(n, valCR);
//                        System.out.println(n+","+,,);
                        System.out.println("Cumulative Regret :: "+n+" = "+valCR);
//                        for(Map.Entry<Integer, Integer> entr: armPullTrials.entrySet()){
////                        	System.out.print(entr.getValue()+",");
////                        	System.out.print(armPullRewdAvg.get(entr.getKey())+",");
//                        }
                    }else{
                        //Calculate Simple regret
                    	Map.Entry<Integer, Double> rewardArm = getArmWithMaxAvgReward(armPullRewdAvg);
                    	List<Double> probArm = bp.pullArm(bandit, rewardArm.getKey());
                        Double valSR = rStar - rewardArm.getValue() * probArm.get(1);
                        simpleRegret.put(n, valSR);                        
                    }
            }
           
           
//            arms.remove(getArmWithMinAvgReward(armPullRewdAvg));
        }   
       
        if(regretType.equals("CR")){
            return cumRegret;
        }   
        return simpleRegret;
    }



    public Map <Integer, Double> epsilonGreedy(String bandit, String regretType, int trials){
        //Loop
        int totalArms = bp.getNumArms(bandit);
        List<Integer> arms = createArmsList(totalArms);

        //Key is arm, list stores AverageReward at 0 and number of Trials at 1
        Map <Integer, Double> armPullRewdAvg = new HashMap <Integer, Double>();
        Map <Integer, Integer> armPullTrials = new HashMap <Integer, Integer>();
        Map <Integer, Double> armExpectedReward = new HashMap <Integer, Double>();
        Random rand = new Random();

       
        Map <Integer, Double> cumRegret = new HashMap <Integer, Double>();
        Map <Integer, Double> simpleRegret = new HashMap <Integer, Double>();

        populateArms(bandit,totalArms,armPullRewdAvg,armPullTrials);
       
        //Calculate Rstar
        Double rStar = getRstar(totalArms,bandit);
        Double sum = 0.0;
       
        for (int i=1; i<=trials; i++){
            //N Pulls
            for(int n=1;n<=500000;n++){
                double d = Math.random();;
                Integer arm;
                
                
                
                for(Map.Entry<Integer, Double> entr: armPullRewdAvg.entrySet()){
                	System.out.print(entr.getValue()+",");
//                	System.out.print(armPullRewdAvg.get(entr.getKey())+",");
                }
                
                
                //Explore
                if(d > epsilon){
                    //Pull random arm
                    arm = (rand.nextInt(totalArms)) + 1 ;
                    System.out.println(" Explore >> "+arm);
                    
                }else {   //Exploit
                    //Pull arm with the highest average reward
                    Map.Entry<Integer, Double> maxAvgArm = getArmWithMaxAvgReward(armPullRewdAvg);
                    arm = maxAvgArm.getKey();
                    System.out.println(" Exploit :: "+arm);
                }
                //pullArm(arm,bandit,armPullRewdAvg,armPullTrials,armExpectedReward);

                List<Double> reward =  pullArm(arm,bandit,armPullRewdAvg,armPullTrials,armExpectedReward);

                sum = sum + reward.get(0);
                //System.out.println("Reward :::: "+sum);
                
                //Calculate Cumulative regret
                if(regretType.equals("CR")){
                    Double valCR =  n*rStar - sum;
                    cumRegret.put(n, valCR);
                    System.out.println("Cumulative Regret :: "+n+" = "+valCR);
                    if(valCR<0)
                        System.out.println("Cumulative Regret :: "+n+" = "+valCR);
                }else{
                    //Calculate Simple regret
                	Map.Entry<Integer, Double> rewardArm = getArmWithMaxAvgReward(armPullRewdAvg);
                	List<Double> probArm = bp.pullArm(bandit, rewardArm.getKey());
                    Double valSR = rStar - rewardArm.getValue() * probArm.get(1);
                    simpleRegret.put(n, valSR);                       
                }
            }
        }
       
        if(regretType.equals("CR")){
            return cumRegret;
        }   
        return simpleRegret;
    }


    public List<Double> pullArm(int arm, String bandit, Map <Integer, Double> armPullRewdAvg, Map <Integer, Integer> armPullTrials, Map <Integer, Double> armExpectedReward){

        List<Double> reward = bp.pullArm(bandit, arm);
        Double riReward = getRi(reward);

            double avgReward = (armPullRewdAvg.get(arm) + riReward);
            armPullRewdAvg.put(arm, avgReward);
            armPullTrials.put(arm, armPullTrials.get(arm)+1);
//            armExpectedReward.put(arm, riReward*reward.get(1));
       
        reward.set(0, riReward);
        return reward;
    }

    private Integer getArmWithMinAvgReward(Map<Integer, Double> map) {
        Map.Entry<Integer, Double> minEntry = null;
        for (Map.Entry<Integer, Double> entry : map.entrySet()){
            if (minEntry == null || entry.getValue().compareTo(minEntry.getValue()) < 0){
                minEntry = entry;
            }
        }
        return minEntry.getKey();       
    }


    public Map.Entry<Integer, Double> getArmWithMaxAvgReward(Map <Integer, Double> map){
        Map.Entry<Integer, Double> maxEntry = null;
        for (Map.Entry<Integer, Double> entry : map.entrySet()){
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
                maxEntry = entry;
            }
        }
        return maxEntry;
    }


    public Double getRstar(int totArms, String bandit){
        Double maxReward = 0.0;
        for(int arm=1; arm<= totArms; arm++){
            List<Double> reward = bp.pullArm(bandit, arm);
            if(reward.get(0) * reward.get(1) > maxReward){
                maxReward = reward.get(0) * reward.get(1);   
            }
        }
        return maxReward;
    }
   
    public void populateArms(String bandit, int totArms, Map <Integer, Double> armPullRewdAvg, Map <Integer, Integer> armPullTrials){
        for(int arm=1; arm<= totArms; arm++){
            List<Double> reward = bp.pullArm(bandit, arm);
            armPullRewdAvg.put(arm, reward.get(0));
            armPullTrials.put(arm, 1);
        }
       
    }
   
    public List<Integer> createArmsList(int a){
        List<Integer> ls = new ArrayList<Integer>();
        for(int i=1; i<=a; i++)
            ls.add(i);
        return ls;
    }

    public Double greedyPolicyCalc(int trials){
        Double mid = (1-0.1)/10000;
        Double e = mid*trials;
        return (Double) (1-e);
    }
   
    public Double getRi(List<Double> reward){
        Double ranndomProb = Math.random();
        System.out.println("  Random  get(1)::" +reward.get(1));
        
        
        if (ranndomProb < reward.get(1)){
            return reward.get(0);
        }
        return 0.0;
    }

    public int getUCBArm(String bandit, int totArms, int n, Map <Integer, Double> armPullRewdAvg, Map <Integer, Integer> armPullTrials){
        //int retArm=0;
        Map <Integer, Double> temp = new HashMap <Integer, Double>();

            for (Map.Entry<Integer, Double> entry : armPullRewdAvg.entrySet()){
//            	Double second = 0.0;
            	Double second = Math.sqrt( (2 * Math.log(n)) /armPullTrials.get(entry.getKey()));
                Double first = ((entry.getValue()*1.0)/(1.0*armPullTrials.get(entry.getKey())));
//                
//                System.out.println("----------------------------------");
//                System.out.println(" Average reward :: "+ entry.getValue());
//                System.out.println(" Arm pulls  :: "+ armPullTrials.get(entry.getKey()));
//
//                
//                System.out.println(" First :: "+first+" Second :: "+second);
                Double calc = first + second;
                temp.put(entry.getKey(), calc);
            }
        
        return getArmWithMaxAvgReward(temp).getKey();
    }
}