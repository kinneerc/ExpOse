package edu.allegheny.expose.examples.prefixAverage;

import java.util.*;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.DoublingExperiment;
import edu.allegheny.expose.ReverseEngineer;
import edu.allegheny.expose.examples.sort.*;


/**
 *Characterize the running time of an algorithm.
 */
public class prefixAverageExperiment extends DoublingExperiment {

  protected int alg;
  protected String name;
  private double[] n;

  public static String[] algs = {"prefixAverage1", "prefixAverage2"};

  public static void main(String[] args) {

    String[] nargs = new String[args.length - 1];

    for (int count = 1; count < args.length; count++) {
      nargs[count - 1] = args[count];
    }
/**read data from prefixAverageExperiment
 *@param String
 *@param int
 *@return ans
 */
    prefixAverageExperiment exp = new prefixAverageExperiment(nargs);
    exp.name = args[0];

    switch (exp.name) {
      case "prefixAverage1":
        exp.alg = 1;
        break;
      case "prefixAverage2":
        exp.alg = 2;
        break;
    }

    exp.initN();
    System.out.println("Running experiment for: " + exp.name + " prefixAverage detector.");
    exp.runExperiment();
    ReverseEngineer eng = new ReverseEngineer();
    eng.loadData(exp.getData());
    exp.getData().writeCSV();
    BigOh ans = eng.analyzeData();
    System.out.println(exp.name + "prefixAverage algorithm is " + ans);

  }

  public prefixAverageExperiment(String[] args) {
    super(args);
  }

  public prefixAverageExperiment() {
  }

  public static BigOh doubleExp(String name){
    prefixAverageExperiment exp = new prefixAverageExperiment();
    exp.name = name;

     switch (name){
      case "prefixAverage1":
        exp.alg = 1;
        break;
      case "prefixAverage2":
        exp.alg = 2;
        break;
    }
/**out put*/
    exp.initN();
    System.out.println("Running experiment for: "+ exp.name + " prefixAverage detector.");
    exp.runExperiment();
    ReverseEngineer eng = new ReverseEngineer();
    eng.loadData(exp.getData());
    /* exp.getData().writeCSV(); */
    BigOh ans = eng.analyzeData();
    return ans;

  }
/**find the prefixAverage 1,2 from prefixAverage
 *@param double
 *@param int
 *@return double
 */

  protected void initN() {
    n = createInput(10);
  }

  protected void doubleN() {
    n = createInput(n.length * 2);
  }

  protected double timedTest() {

    // shuffle list first
    n = createInput(n.length);

    long startTime = System.nanoTime();
/**find the prefixAverage1 from prefixAverage.*/
    switch (alg) {
      case 1: prefixAverage.prefixAverage1(n);
              name = "prefixAverage1";
              break;
/**fins the prefixAverage2 from prefixAverage.*/
      case 2: prefixAverage.prefixAverage2(n);
              name = "prefixAverage2";
              break;
    }

    long endTime = System.nanoTime();

    /* System.out.println("Time = "+(endTime-startTime)); */

    return (double) endTime - startTime;

  }
/**input size int, double prefixAverage1 and prefixAverage2.
 * @param double
 * @return values
 */
  public static double[] createInput(int size) {

    double[] values = new double[size];

    //Random generator = new Random((long)1.0);
    Random generator = new Random();

    for (int i = 0; i < size; i++) {
      double next_value = generator.nextDouble();
      values[i] = next_value;
    }

    return values;

  }

}
