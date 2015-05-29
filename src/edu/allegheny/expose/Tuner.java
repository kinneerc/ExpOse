package edu.allegheny.expose;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import edu.allegheny.expose.tune.*;

public class Tuner {

    private static final double initialTolerance = 2;
    private static final int trials = 5;
    /**
     * Desired success rate for tuning.
     *
     * 1.0 = perfect score, all passing
     * 
     */
    private static final double toleranceGoal = 0.95;

    public static void main(String[] args){
        System.out.println(Tuner.tuneTolerance(new DefaultSuite()));
        /* System.out.println(Tuner.tuneTrials(new SortingExperiment(new String[] {},"quick"))); */
    }

    /**
     * Tune convergence based on the default collection of doubling experiments.
     *
     * Since this only needs to be done once per machine, first check to see
     * if this hostname has already been tuned, and then use the old settings.
     *
     * @param forceReturn will ignore existing tunning settings and overwrite them.
     */
    public static double tuneTolerance(boolean forceRetune){

        double ans=checkRecords();

        if (forceRetune || ans == -1) 
            ans = tuneTolerance(new DefaultSuite());

        writeRecord(ans);

        return ans;
    }

    private static void writeRecord(Double tolerance){
        String hostname = null;

        try {
            hostname = InetAddress.getLocalHost().getHostName();
            String filename = "tuning/"+hostname;

            try (PrintWriter writer = new PrintWriter(filename)) {

                writer.println(tolerance.toString());
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    private static double checkRecords(){

        (new File("tuning")).mkdirs(); 

        String hostname;
        try {
            hostname = InetAddress.getLocalHost().getHostName();
            String filename = "tuning/"+hostname;
            File target = new File(filename);

            if(target.exists())
                return readRecord(target);
            else
                return -1;

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }

    }

    private static double readRecord(File target){
        try {
            String rec = Files.readAllLines(target.toPath(), StandardCharsets.US_ASCII).get(0);
            return Double.parseDouble(rec);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }
    }

    public static double tuneTolerance(BenchMarkSuite bms){

        bms.setArg("--convergence",Double.toString(initialTolerance));
        bms.setArg("--noFile","");
        bms.setArg("--trials", "1");

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
                    double numerator = (double) (bms.size()*trials-runs+success);
                    double denominator = (double) bms.size()*trials;
                                        
                    if( numerator / denominator < toleranceGoal){
                        System.out.println("Too many failures, tightening tolerance...");
                        break trialloop;
                    }

                }

            }
            successRate = (double) success /(double) runs;
            System.out.printf("Accuracy %4.2f - Tolerance %3.4f\n",successRate,tolerance);
            nexttolerance -= nexttolerance / 5;
            bms.setArg("--convergence",Double.toString(nexttolerance));
        }

        return tolerance;

    }


}
