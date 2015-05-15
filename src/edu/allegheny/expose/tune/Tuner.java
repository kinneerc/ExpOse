package edu.allegheny.expose.tune;

public class Tuner {

    private static final double initialTolerance = .5;
    private static final int trials = 1;
    /**
     * Desired success rate for tuning 
     */
    private static final double goal = 0.9; 

    protected BenchMarkSuite bms;

    public static void main(String[] args){
        Tuner t = new Tuner(new DefaultSuite(new String[] {}));
        System.out.println(t.tune());
    }

    public Tuner(BenchMarkSuite bms){
        this.bms = bms;
        bms.setArg("-convergence",Double.toString(initialTolerance));
        bms.setArg("-noFile","");
        bms.setArg("-trials", "1");
    }

    public double tune(){

        double tolerance = initialTolerance;
        double nexttolerance = tolerance;
        
        double successRate = 0;
        while(successRate < goal){
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
                
                System.out.printf("Alg: %10s Trial: %2d Result: %4s \n",b.toString(),count,pass);
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
