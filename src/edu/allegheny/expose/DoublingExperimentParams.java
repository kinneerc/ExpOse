package edu.allegheny.expose;

import com.beust.jcommander.Parameter;

public class DoublingExperimentParams{

    @Parameter(names = {"--trials"}, description = "Number of trials to run for each double")
        public int trials = 1000;

    @Parameter(names = {"--convergence"}, description = "Experiment convergent if diff < this")
        public double convergence = .4;

    @Parameter(names = {"-o","--out","--csv"}, description = "Name of the csv file to save data to")
        public String csv = "LastExperiment.csv.tmp";

    @Parameter(names = {"--noTune"}, description = "Disable trying to break out of O(1)")
        public boolean tuning = false;

    @Parameter(names = {"--tuningTries"}, description = "Number of times to double n to try to break out of O(1)")
        public int tuningTries = 20;

    @Parameter(names = {"--minDoubles"}, description = "Minimum number of doubles to try")
        public int minRuns = 20;

    @Parameter(names = {"--lookBack"}, description = "Number of ratios to compair for convergence")
        public int lookBack = 4;

    @Parameter(names = {"--verbose","--debug"}, description = "Display verbose output")
        public boolean verbose = false;

    @Parameter(names = {"--giveUp","--maxTime"}, description = "Max time for a single trial in hours")
        public int giveUpTime = 0.25;

    @Parameter(names = {"--overwrite"}, description = "Overwrite existing CSV rather than append")
        public boolean overwrite = false;

    @Parameter(names = {"--help","--usage"}, description = "Display command line options")
        public boolean help = false;
}
