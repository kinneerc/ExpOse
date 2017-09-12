package edu.allegheny.expose.examples.prefix;

import java.util.*;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.DoublingExperiment;
import edu.allegheny.expose.ReverseEngineer;
import edu.allegheny.expose.examples.sort.*;


/**
* This is a class that specifies the Big-Oh notation for the prefix1 and prefix2 algorithms given the existing data stored in the CSV file and write the running results into the csv file
*
*/
public class prefixExperiment extends DoublingExperiment {

  protected int alg;   // create a global variable integer alg
  protected String name;	// create a global variable integer String name
  private int[] n;	// create a global variable integer array n


  public static String[] algs = {"prefix1", "prefix2"}; // create a global String array for storing the two algorithms to run

    /**
     * This is the main method
     *
     * @param args supplies command line-argument as an array of objects of type String
     *
     */
  public static void main(String[] args) {

    String[] nargs = new String[args.length - 1];

    for (int count = 1; count < args.length; count++) {
      nargs[count - 1] = args[count];
    }

    prefixExperiment exp = new prefixExperiment(nargs);
    exp.name = args[0];

    switch (exp.name) {
      case "prefix1":
        exp.alg = 1;
        break;
      case "prefix2":
        exp.alg = 2;
        break;
    }
    // uses the method initN, which uses the method createInput with specific value
    exp.initN();	
    System.out.println("Running experiment for: " + exp.name + " prefixAverage detector.");
    // uses the method runExperiment, which runs doubling experiment until convergence is reached in DoublingExperiment
    exp.runExperiment();
    // creat a new reverseEngineer instance
    ReverseEngineer eng = new ReverseEngineer();
    // uses the method loadData and store the data in getData which is a method in DoublingExperiment
    eng.loadData(exp.getData());
    // uses the method getData and write the data in CSV file
    exp.getData().writeCSV();
    // creates an instance of BigOh, which is used to analyse data
    BigOh ans = eng.analyzeData();
    System.out.println(exp.name + "prefixAverage algorithm is " + ans);

  }
    /**
     * Constructor of prefixExperiment 
     *
     * @param args supplies command line-argument as an array of objects of type String
     *
     */
  public prefixExperiment(String[] args) {
    super(args);  // super method shows the inheritance relationship
  }


  // constructor 
  public prefixExperiment() {
  }

    /**
     * This method specifies the Big-Oh notation for the prefix1 and prefix2 algorithms
     *
     * @param String name calls to the Strings of both prefix1 and prefix2
     * @return ans which states what the worst case time complexity is of either prefix1 or prefix2
     *
     */
  public static BigOh doubleExp(String name){
    prefixExperiment exp = new prefixExperiment();
    exp.name = name;

    switch (name){
      case "prefix1":
        exp.alg = 1;
        break;
      case "prefix2":
        exp.alg = 2;
        break;
    }

    exp.initN();			// call method initN
    System.out.println("Running experiment for: "+ exp.name + " prefixAverage detector.");
    exp.runExperiment();		// call method runExperiment
    ReverseEngineer eng = new ReverseEngineer();
    eng.loadData(exp.getData());	//call method loadData
    /* exp.getData().writeCSV(); */
    BigOh ans = eng.analyzeData();	// creates an instance of BigOh
    return ans;

  }
    /**
     * This method creates initN using the method createInput with specific value
     *
     */
  protected void initN() {
    n = createInput(10);
  }
    /**
     * This method creates doubleN using the method createInput with doubling the length
     *
     */
  protected void doubleN() {
    n = createInput(n.length * 2);
  }
    /**
     * This method calculates the elasping time of running method of prefix1 or prefix2
     * @ return endTime - startTime to calculate the elapsed time
     */
  protected double timedTest() {

    // shuffle list first
    n = createInput(n.length);

    long startTime = System.nanoTime();

    switch (alg) {
      case 1: prefixAverage.prefix1(n);
              name = "prefix1";
              break;
      case 2: prefixAverage.prefix2(n);
              name = "prefix2";
              break;
    }

    long endTime = System.nanoTime();

    /* System.out.println("Time = "+(endTime-startTime)); */

    return (double) endTime - startTime;

  }
    /**
     * This method creats an integer array with random numbers 
     *
     * @ param integer size inputs the size as a parameter
     * @ return values that are generated from the random generator
     *
     */
  public static int[] createInput(int size) {

    int[] values = new int[size];

    //Random generator = new Random((long)1.0);
    Random generator = new Random();

    for (int i = 0; i < size; i++) {
      int next_value = generator.nextInt();
      values[i] = next_value;
    }

    return values;

  }

}
