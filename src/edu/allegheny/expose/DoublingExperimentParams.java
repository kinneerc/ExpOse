package edu.allegheny.expose; 

import com.beust.jcommander.Parameter;

public class DoublingExperimentParams{

    @Parameter(names = {"-trials"}, description = "Number of trials to run for each double")
        public int trials = 10;

    @Parameter(names = {"-convergence"}, description = "Experiment convergent if diff < this")
        public double convergence = 20;

    @Parameter(names = {"-o","-out","-csv"}, description = "Name of the csv file to save data to")
        public String csv = "LastExperiment.csv.tmp";

    @Parameter(names = {"-noTune"}, description = "Disable automatic tuning")
        public boolean tuning = false;

    @Parameter(names = {"-noMinimum"},description = "Disable requiring a minimum number of O(1) runs")
        public boolean noMinimum = false;

    @Parameter(names = {"-minimum"}, description = "Number of times to double n to try to break out of O(1)")
        public int minimum = 20;

    @Parameter(names = {"-minDoubles"}, description = "Minimum number of doubles to try")
        public int minRuns = 5;

    @Parameter(names = {"-lookBack"}, description = "Number of ratios to compair for convergence")
        public int lookBack = 4;

    @Parameter(names = {"-verbose","-debug"}, description = "Display verbose output")
        public boolean verbose = false;

    @Parameter(names = {"-giveUp","-maxTime"}, description = "Max time for a single trial in hours")
        public int giveUpTime = 8;

    @Parameter(names = {"-overwrite"}, description = "Overwrite existing CSV rather than append")
        public boolean overwrite = false;

    @Parameter(names = {"-noFile"}, description = "Do not save data to CSV")
        public boolean noFile = false;

    @Parameter(names = {"-help","-usage"}, description = "Display command line options")
        public boolean help = false;
}
