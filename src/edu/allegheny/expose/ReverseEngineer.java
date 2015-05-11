package edu.allegheny.expose;

import java.util.List;

/**
 * This class will calculate big-oh given a doubling experiment
 *
 * @author Cody Kinneer
 */
public class ReverseEngineer{

    /**
     * Data to be analyzed stored here.
     * It will have already been sorted by times doubled
     */
    private ExperimentResults data;
    
    /**
     * Data aggregated by times doubled
     */
    private List<Double[]> avg;

    /**
     * Percentage that min / max of a dataset must be within to be considered 
     * consistant
     */
    private static double spreadTolerance = .10;

    // main is primarily a test method
    public static void main(String[] args){
        /* analyzeData(args[0]); */
        /* ReverseEngineer.checkIncrement(ReverseEngineer.readCSV(args[0])); */
    }

    // empty constructor
    public ReverseEngineer(){}

    /**
     * Finds the closest power of 2 to the given ratio.
     * @param input value to find closest power of 2 for
     * @return closest power of 2
     */
    protected static int findClosest(double input){

        int guess = 0; // our guess for the exponent
        double diff = Math.abs(Math.pow(2,guess) - input); // difference from the actual ratio
        double bestDiff = diff*2;
        while (diff <= bestDiff){ // as long as our guess improves, keep guessing
            bestDiff = diff;
            guess++;
            diff = Math.abs(Math.pow(2,guess) - input);
        }

        return (int) Math.pow(2,--guess);
    }

    private void aggregate(){
        avg = data.aggregate();
    }

    /**
     * Loads in data from a doubling experiment
     * @param input data to load in
     */
    public void loadData(ExperimentResults input){
        data = input;
    }

    /**
     * Allows reading in CSV
     * @param fileName path to CSV file
     */
    public void loadData(String fileName){
        data = new ExperimentResults();
        data.readCSV(fileName);
    }

   
    /**
     * Analyze the results of a doubling experiment.
     * @return BigOh determined from data
     */
    public BigOh analyzeData(){

        if (this.data.isEmpty()){
            System.out.println("No data to analyze.");
            return null;
        }

        aggregate();

        // TODO Verify consistency

        // Step one, attempt to model O as a power function.
        // since we sort the data by times doubled when it is read in, we know the last data
        // sets are the ones we are nterested in

        // cacluate the ratio
        double ratio = avg.get(avg.size()-1)[1] / avg.get(avg.size()-2)[1];

        int c = findClosest(ratio);

        // since c is a power of 2, log2(c) is an int
        int exp = (int)(Math.log(c) / Math.log(2));

        BigOh result = new BigOh();

        // now in the case of N^0 or N^1, get more specific 
        if ( exp == 0 ){
            // check for constant time
            if (checkConstant()){
                result.setCompClass(ComplexityClass.CONSTANT);
            }else{
                result.setCompClass(ComplexityClass.LOGARITHMIC);
            }
        }else if (exp == 1){
            if (checkLinear()){
                result.setCompClass(ComplexityClass.LINEAR);
            }else{
                result.setCompClass(ComplexityClass.LINEARITHMIC);
            }
        }else if (exp == 2){
                result.setCompClass(ComplexityClass.QUADRADIC);
        }else if (exp == 3){
            result.setCompClass(ComplexityClass.CUBIC);
        }else{
            result.setPower(exp);
        }

        return result;

    }

    /**
     * This method verifies that the % differance of the min and max elements of an array
     * is less than the tolerance
     * @param input the data to check for closeness
     * @param tolerance % differance that data must be within to pass
     * @return true if that data was within range, false otherwise
     */
    protected boolean checkWithinRange(double[] input, double tolerance){
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;

        // find min and max
        for (double x : input){
            if (x < min)
                min = x;
            if (x > max)
                max = x;
        }

        // Calculate percent differance
        double diff = (Math.abs(min-max) / ((min + max)/2));

        // Check condition
        if (diff > tolerance){
            return false;
        }else{
            return true;
        }

    }

    /**
     * Check if the data reflects a constant time algorithm
     * @return true if the data is deemed O(n)
     */
    protected boolean checkConstant(){
    
        aggregate();

        // fetch last 3 elements
        double[] last = {avg.get(avg.size()-1)[1], avg.get(avg.size()-2)[1], avg.get(avg.size()-3)[1]};

        // check to see that they are within 5 % of each other
        return checkWithinRange(last, spreadTolerance);
    }

    /**
     * Checks if the data reflects a linearithmic algoritm
     * @return true if the data is deemed O(nlog(n))
     */
    protected boolean checkLinear(){
    
        aggregate();

        // fetch last 3 elements
        double[] last = {avg.get(avg.size()-1)[1], avg.get(avg.size()-2)[1], avg.get(avg.size()-3)[1]};

        // divide by n
        double[] div = {last[0] / 4, last[1] / 2, last[2] };

        // check to see that these values are within tolerance of each other 
        return checkWithinRange(div, spreadTolerance);

    }
    
    
    

    
}
