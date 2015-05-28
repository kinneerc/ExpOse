package edu.allegheny.expose.tune.simplealgs;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.tune.BenchMark;

public class LinLogExperiment extends BenchMark{

    protected int alg;
    protected String name;
    private BigOh correct;

    long value;

    public static final int[] algs = {1,2,3,4,5};

    public LinLogExperiment(String[] args, int alg){
        super(args);
        this.alg = alg;

         switch (alg){
            case 1: name = "linear";
                    correct = linear;
                    break;
            case 2: name = "log";
                    correct = logarithmic;
                    break;
            case 3: name = "constant";
                    correct = constant;
                    break;
            case 4: name = "linearithmic";
                    correct = linearithmic;
                     break;
            case 5: name = "cubic";
                    correct = cubic;
                    break;
            case 6: name = "weightedCubic";
                    correct = cubic;
                    break;
        }
         initN();

    }
    
    public String toString(){
    	return name;
    }

    public BigOh getCorrectBigOh(){        
        return correct;
    }

    protected void initN(){
        value = 1;
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
            case 5: testAlgs.cubic(value);
                    break;
            case 6 : testAlgs.weightedCubic(value);
                     break;
        }

        long endTime = System.nanoTime();

        //System.out.println("TIME: "+(endTime - startTime)+" N: "+value);

        return (double) endTime - startTime;

    }

}
