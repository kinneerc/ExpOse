package edu.allegheny.expose;

import java.util.ArrayList;

import edu.allegheny.expose.tune.*;
import edu.allegheny.expose.tune.sort.SortingExperiment;

public class Tuner {

    private static final double initialTolerance = .9;
    private static final int trials = 1;
    /**
     * Desired success rate for tuning 
     */
    private static final double toleranceGoal = 0.9;
    private static final double trialsGoal = 0.10; 

    private static final int trialsGiveUp = 5;

    public static void main(String[] args){
        /* System.out.println(Tuner.tuneTolerance(new DefaultSuite())); */
        System.out.println(Tuner.tuneTrials(new SortingExperiment(new String[] {},"quick")));
    }


    public static int tuneTrials(DoublingExperiment de){
        double cv = Double.MAX_VALUE;
        int trials = 4;

        int run = -1;

        double min = Double.MAX_VALUE;

        int lastUpdate = 0;

        while (cv > trialsGoal){
            run++;
            trials++;
            ArrayList<Double> medianTime = new ArrayList<Double>();
            for (int batch = 0; batch < 10; batch++){
            ArrayList<Double> runtimeRecords = new ArrayList<Double>();
            for (int count = 0; count < trials; count++){
                runtimeRecords.add(de.timedTest());
            }
            medianTime.add(ExperimentResults.centralTendency(runtimeRecords));
            }
            cv = coefficientOfVariation(medianTime);
            System.out.printf("Run: %2d Trials: %2d Cv: %4.4f\n",run,trials,cv);

            if (cv < min){
                min = cv;
                lastUpdate = 0;
            }else{
                lastUpdate++;
                if (lastUpdate > trialsGiveUp){
                System.out.println("Cv not improved for "+trialsGiveUp+" runs.");
                break;
            }

            }
            
        }

        return trials;
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
                }

                // check if it is possible to pass the tolerance test
                // allows us to give up when we fail too many times
                if( (double) (bms.size()-runs+success) / (double) bms.size() < toleranceGoal){
                    System.out.println("Too many failures, tightening tolerance...");
                    break;
                }

        }
        successRate = (double) success /(double) runs;
        System.out.printf("Accuracy %4.2f - Tolerance %3.4f\n",successRate,tolerance);
        nexttolerance -= nexttolerance / 5;
        bms.setArg("-convergence",Double.toString(nexttolerance));
        }

        return tolerance;
        
    }

    private static double coefficientOfVariation(ArrayList<Double> times){
        double sum = 0;
        double dSquaredTotal = 0;
        for (double t : times){
            sum += t;
        }
        double mean = (double) sum / (double) times.size();
        for (double t : times){
            dSquaredTotal += Math.pow(Math.abs(mean-t),2);
        }
        return Math.sqrt(dSquaredTotal)/mean;
    }


}
