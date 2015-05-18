package edu.allegheny.expose;

import edu.allegheny.expose.tune.*;

public class Tuner {

    private static final double initialTolerance = 100;
    private static final int trials = 2;
    /**
     * Desired success rate for tuning.
     *
     * 1.0 = perfect score, all passing
     * 
     */
    private static final double toleranceGoal = 0.90;

    public static void main(String[] args){
        System.out.println(Tuner.tuneTolerance(new DefaultSuite()));
        /* System.out.println(Tuner.tuneTrials(new SortingExperiment(new String[] {},"quick"))); */
    }

    public static double tuneTolerance(BenchMarkSuite bms){

        bms.setArg("-convergence",Double.toString(initialTolerance));
        bms.setArg("-noFile","");
        bms.setArg("-trials", "1");

        double tolerance = initialTolerance;
        double nexttolerance = tolerance;
        
        double successRate = 0;
        while(successRate < toleranceGoal){
             int success = 0;
             int runs = 0;
             tolerance = nexttolerance;

        trialloop:
        for(int count = 0; count < trials; count++){
        
            for (BenchMark b : bms){
                runs++;
                
                String pass;
                if (b.verifyCorrectness()){
                    success++;
                    pass = "PASS";
                }else{
                    pass = "FAIL";
                }
                
                System.out.printf("Alg: %14s Trial: %2d Result: %4s \n",b.toString(),count,pass);

                // check if it is possible to pass the tolerance test
                // allows us to give up when we fail too many times
                if( (double) (bms.size()-runs+success) / (double) bms.size() < toleranceGoal){
                    System.out.println("Too many failures, tightening tolerance...");
                    break trialloop;
                }

                }

              

        }
        successRate = (double) success /(double) runs;
        System.out.printf("Accuracy %4.2f - Tolerance %3.4f\n",successRate,tolerance);
        nexttolerance -= nexttolerance / 5;
        bms.setArg("-convergence",Double.toString(nexttolerance));
        }

        return tolerance;
        
    }


}
