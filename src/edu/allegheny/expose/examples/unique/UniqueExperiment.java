package edu.allegheny.expose.examples.unique;

import java.util.*;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.DoublingExperiment;
import edu.allegheny.expose.ReverseEngineer;
import edu.allegheny.expose.examples.sort.*;

public class UniqueExperiment extends DoublingExperiment {

  protected int alg;
  protected String name;
  private int[] n;

  public static String[] algs = {"unique1", "unique2"};

  public static void main(String[] args) {

    String[] nargs = new String[args.length - 1];

    for (int count = 1; count < args.length; count++) {
      nargs[count - 1] = args[count];
    }

    UniqueExperiment exp = new UniqueExperiment(nargs);
    exp.name = args[0];

    switch (exp.name) {
      case "unique1":
        exp.alg = 1;
        break;
      case "unique2":
        exp.alg = 2;
        break;
    }

    exp.initN();
    System.out.println("Running experiment for: " + exp.name + " uniqueness detector.");
    exp.runExperiment();
    ReverseEngineer eng = new ReverseEngineer();
    eng.loadData(exp.getData());
    exp.getData().writeCSV();
    BigOh ans = eng.analyzeData();
    System.out.println(exp.name + "Uniqueness algorithm is " + ans);

  }

  public UniqueExperiment(String[] args) {
    super(args);
  }

  public UniqueExperiment() {
  }

  public static BigOh doubleExp(String name){
    UniqueExperiment exp = new UniqueExperiment();
    exp.name = name;

    switch (name){
      case "unique1":
        exp.alg = 1;
        break;
      case "unique2":
        exp.alg = 2;
        break;
    }

    exp.initN();
    System.out.println("Running experiment for: "+ exp.name + " uniqueness detector.");
    exp.runExperiment();
    ReverseEngineer eng = new ReverseEngineer();
    eng.loadData(exp.getData());
    /* exp.getData().writeCSV(); */
    BigOh ans = eng.analyzeData();
    return ans;

  }

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

    switch (alg) {
      case 1: Uniqueness.unique1(n);
              name = "unique1";
              break;
      case 2: Uniqueness.unique2(n);
              name = "unique2";
              break;
    }

    long endTime = System.nanoTime();

    /* System.out.println("Time = "+(endTime-startTime)); */

    return (double) endTime - startTime;

  }

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
