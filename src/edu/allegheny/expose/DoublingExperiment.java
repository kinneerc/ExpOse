package edu.allegheny.expose;

import java.util.ArrayList;

import com.beust.jcommander.JCommander;

import edu.allegheny.expose.tune.DefaultSuite;

/**
 * This abstract class provides the needed methods
 * to conduct a meaningfull doubling experiment.
 *
 * The type of DoublingExperiment should be the type of N
 *
 * @author Cody Kinneer
 */
public abstract class DoublingExperiment{
    /** The maximum change in ratio that signals convergence */
    private double convergenceTolerance;
    /** Stores results */
    protected ExperimentResults data;
    /** records termination reason 0 = convergent 1 = timeout 2 = out of RAM */
    protected int termCode;
    /** Time experiment ran for */
    protected long runTime;
    /** Minimum number of tests to run */
    private int minRuns;
    /** Number of times to repeat each test */
    private int trials;
    /** Allows for disabiling automatic paramter tuning */
    private boolean noTune;
    /** Should we attempt to find a good default minRuns */
    private boolean noMinimum;
    /** Number of times to double N before giving up in a tuning experiment */
    private int minimum;
    /** Number of ratios to consider when checking for convergence */
    private int lookBack;
    /** display debug output */
    private boolean verbose;
    /** Maximum time in hours allowed for a single double */
    private double giveUpTime;

    /** Parameters object, default settings handled in this class */
    protected DoublingExperimentParams params; 

    // for automatic tuning
    // paramters for tuning the number of trials
    private static final double trialsGoal = 0.10; 
    private static final int trialsGiveUp = 5;

    /**
     * Constructor initializes settings
     * @param params JCommander parameters object
     * @param args array of command line arguments
     * @param CSVheader first line of CSV output file
     * @param expParams any additional information to write to CSV file
     * @see #DoublingExperimentParams
     */
    public DoublingExperiment(DoublingExperimentParams params, String[] args, String[] CSVheader, 
            String[] expParams){
        this.params = params; 
        JCommander commander = new JCommander(params,args);

        if (params.help){
            commander.usage();
            System.exit(0);
        }

        noTune = params.tuning;

        // if tuning is enabled, then set tolerance and trials by automatic tuning
        if(!noTune){
            trials = tuneTrials();
            convergenceTolerance = Tuner.tuneTolerance(new DefaultSuite());
        }else{
            trials = params.trials;
            convergenceTolerance = params.convergence;
        }

        minimum = params.minimum;
        lookBack = params.lookBack;

        // getting a ratio requires at least 2 runs, so x ratios needs x + 1 runs
        // make sure we have enough runs for desired lookBack
        if (params.minRuns > lookBack + 1){
            minRuns = params.minRuns;
        }else{
            // override user input if nessissary
            minRuns = lookBack + 1;
        }

        noMinimum = params.noMinimum;
        verbose = params.verbose;
        giveUpTime = params.giveUpTime;

        termCode = -1;
        runTime = -1;

        String csvFileName = null;

        if (!params.noFile)
            csvFileName = params.csv;

        data = new ExperimentResults(csvFileName,CSVheader,expParams,!params.overwrite);
    }

    /**
     * {@inheritDoc}
     * @see Object#DoublingExperiment()
     */
    public DoublingExperiment(){
        this(new String[] {});
    }

    /**
     * Sets values from command line args
     * @param args array of command line arguments
     */
    public DoublingExperiment(String[] args){
        this(new DoublingExperimentParams(), args, new String[] {"Doubles","Time"},null);
    }


    /**
     * Automatically decide upon a good setting for a doubling experiment's number of trials
     * that is, the number of runs that we take a median of.
     * 
     * For a number of trials, we run the timedTest that many times, and record
     * the median for BATCH_COUNT number of times.  We then calculate the variance of
     * that, and increase the number of trials until the variance is acceptable,
     * or until the variance stops improving for trialsGiveUp number of batches
     * */
    private int tuneTrials(){
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
                    runtimeRecords.add(timedTest());
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


    /**
     * Checks to see if the ratio of 2N/N has converged
     * @return true if converged, false if not
     */
    private boolean checkConvergence(){

        // first check to see that we've had at least the min number of runs
        if (data.aggregate().size() < minRuns)
            return false;        

        // find ratio of last lookBack runs
        double[] ratio = new double[lookBack];

        for (int count = 0; count < lookBack; count++){
            ratio[count] = data.getRatio(count);
        }


        // now calculate sum of change in last lookBack runs
        double change = 0;

        for (int count = 0; count < lookBack - 1; count++){
            change += ratio[count] - ratio[count+1];
        }

        // now find the differance from zero
        change = Math.abs(change);

        if(verbose)
            System.out.println("Convergence Check: Change = "+change+" Ratio = "+data.getRatio(0));

        // check tolerance
        if (change <= convergenceTolerance){
            return true;
        }else{
            return false;
        }
    }


    /**
     * This method can be called after an experiment in completed to analyze the results and
     * print the bigOh time complexity to StdOut
     */
    public void printBigOh(){
        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData(getData());
        BigOh ans = eng.analyzeData();
        System.out.println(ans);
    }

    /**
     * Runs doubling experiment until convergence is reached
     */
    public void runExperiment(){
        long startTime, endTime;
        startTime = -1;
        try{
            startTime = System.currentTimeMillis();
            // tune minRuns if desired
            if (!noMinimum){
                tuneInitN();
            }else{
                // data is initialized in tuneInitN
                // if we do not do this, we need to init data
                data = new ExperimentResults();
            }

            int doubles = data.aggregate().size();

            while(!checkConvergence()){

                if(doubles > 1){
                    // check if we think the next run will take too long 
                    if (checkGiveUp()){
                        break;
                    }
                }

                if(doubles > 0){
                    // the first run we do not double N before test
                    doubleN();
                }

                // run each test multiple times
                for (int count = 0; count < trials; count++){
                    Double[] result = {(double) doubles, timedTest()};
                    data.add(result);
                }
                doubles++;
                if (verbose)
                    System.out.println("N doubled.");
            }

            endTime = System.currentTimeMillis();
            runTime = endTime - startTime;
            if (checkConvergence()){
                termCode = 0;
            }else{
                termCode = 1;
            }

        }catch(OutOfMemoryError e){
            termCode = 2;
            endTime = System.currentTimeMillis();
            if (startTime != -1)
                runTime = endTime - startTime;
            else
                runTime = startTime;

        }

    }

    /**
     * Check to see if the next double will exceed the give up time
     * @return true if give up condition met, false otherwise
     */
    private boolean checkGiveUp(){
        double lastTime = data.get(data.size()-1)[1];
        double lastRatio = data.getRatio(0);

        double predictedTime = lastTime * lastRatio;

        // convert nanoseconds to hours
        predictedTime *= 2.77778e-13;

        if (verbose)
            System.out.println("Predicted Time for next dub: "+predictedTime);

        if (predictedTime > giveUpTime)
            return true;
        return false;
    }

    /**
     * Run a tuning experiment to set intial N.
     * If starting N is too large, it might appear to converge on
     * O(1) or O(log(N))
     * Tuning will attempt to find the point where this does not occur
     * if it fails, then the algorithm is actualy constant or logarithmic
     * @returns The new minimum number of doubles for each test
     */
    public int tuneInitN(){
        int doubles = 0;

        if (verbose)
            System.out.println("Finding min doubles...");
        while(!checkInitN() && doubles < minimum){
            // repeat each test multiple times
            for (int count = 0; count < trials; count++){
                Double[] result = {(double) doubles, timedTest()};
                data.add(result);
            }
            doubleN();
            if (verbose)
                System.out.println("N doubled.");
            doubles++;
        }

        // change minruns to be the lookBack relative to current runs
        // first, make sure we cannot lower minRuns 
        if (minRuns < doubles + lookBack - 1)
            minRuns = doubles + lookBack - 1;

        if (verbose)
            System.out.println("Min doubles set to "+minRuns);

        return doubles;

    }

    /**
     * Check terminating conditions for tuning experiment
     */
    private boolean checkInitN(){

        // first check to see that we've had enough runs to calclulate a ratio
        if (data.aggregate().size() < 2)
            return false;        

        // find ratio of last run
        double ratio = data.getRatio(0);
        if (verbose)
            System.out.println("Tuning Run: Ratio = "+ratio);

        // return false if the ratio indiates constant or logarithmic time
        if (ReverseEngineer.findClosest(ratio) <= 1)
            return false;
        return true;

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

    /**
     * Return this objects data
     * @return data from last experiment
     */
    public ExperimentResults getData(){
        return data;
    }

    /**
     * Sets the average change in ratio needed to be met
     * for the experiment to be considered convergent
     */
    public void setTolerance(double tolerance){
        convergenceTolerance = tolerance;
    }

    /**
     * Sets the number of trials run for each time n is doubled
     * @param trials The number of trials for each double
     */
    public void setTrials(int trials){
        this.trials = trials;
    }

    // This method should return the runtime of f(n)
    protected abstract double timedTest();
    // This method should double n
    protected abstract void doubleN();

}

