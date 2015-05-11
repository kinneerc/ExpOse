package edu.allegheny.expose.examples;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.DoublingExperiment;
import edu.allegheny.expose.ReverseEngineer;

public class LinLogExperiment extends DoublingExperiment{

    protected int alg;
    protected String name;

    long value;

    public static void main(String[] args){
        LinLogExperiment exp = new LinLogExperiment();
        exp.alg = Integer.parseInt(args[0]);
        switch (exp.alg){
            case 1: exp.name = "linear";
                    break;
            case 2: exp.name = "log";
                    break;
            case 3: exp.name = "constant";
                    break;
            case 4: exp.name = "linearithmic";
                     break;
            case 5: exp.name = "factorial";
                    break;
            case 6: exp.name = "cubic";
                    break;
        }
        System.out.println("Running experiment for: "+exp.name+".");
        exp.setTrials(3);
        exp.initN();
        exp.runExperiment();
        exp.getData().writeCSV();
        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData(exp.getData());
        BigOh ans = eng.analyzeData();
        System.out.println(exp.name+" is "+ans);

    }

    protected void initN(){
        value = 1;
        System.out.println(value);
    }
    protected void doubleN(){
        value *= 2;
    }
    protected double timedTest(){

        long startTime = System.nanoTime();


        switch (alg){
            case 1: testAlgs.linear(value);
                    break;
            case 2: testAlgs.log(value);
                    break;
            case 3: testAlgs.constant(value);
                    break;
            case 4: testAlgs.linearithmic(value);
                    break;
            case 5: testAlgs.factorial(value);
                    break;
            case 6: testAlgs.cubic(value);
                    break;
        }

        long endTime = System.nanoTime();

        System.out.println("TIME: "+(endTime - startTime)+" N: "+value);

        return (double) endTime - startTime;

    }

}


