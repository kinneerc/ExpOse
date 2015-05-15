package edu.allegheny.expose.tune.sort;

import java.util.*;

import edu.allegheny.expose.*;
import edu.allegheny.expose.tune.BenchMark;

public class SortingExperiment extends BenchMark{

    protected int alg;
    protected String name;
    private int[] n;

    public static String[] algs = {"quick", "insertion", "merge", "selection", "bubble"};

    private static final BigOh quadratic = new BigOh(ComplexityClass.QUADRADIC);
    private static final BigOh linearithmic = new BigOh(ComplexityClass.LINEARITHMIC);

    private BigOh correct;

    public SortingExperiment(String[] args, String algName){
        super(args);
        name = algName;
        switch (algName){
            case "quick": 
                    alg = 1;
                    correct = linearithmic;
                    break;
            case "insertion": 
                    alg = 2;
                    correct = quadratic;
                    break;
            case "merge": 
                    alg = 3;
                    correct = linearithmic;
                    break;
            case "selection":
                    alg = 4;
                    correct = quadratic;
                    break;
            case "bubble": 
                    alg = 5;
                    correct = quadratic;
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
        n = createInput(10);
    }
    protected void doubleN(){
        n = createInput(n.length*2);
    }
    protected double timedTest(){

        // shuffle list first
        n = createInput(n.length);

        long startTime = System.nanoTime();


        switch (alg){
            case 1: QuickSort.quickSort(n, n.length);
                    name = "quick";
                    break;
            case 2: InsertionSort.insertionSort(n, n.length);
                    name = "insertion";
                    break;
            case 3: MergeSort.mergeSort(n, n.length);
                    name = "merge";
                    break;
            case 4: SelectionSort.selectionSort(n, n.length);
                    name = "selection";
                    break;
            case 5: BubbleSort.bubbleSort(n, n.length);
                    name = "bubble";
                    break;

        }

        long endTime = System.nanoTime();

        //System.out.println("Time = "+(endTime-startTime));

        return (double) endTime - startTime;

    }

    public static int[] createInput(int size)
    {

        int[] values = new int[size];

        //Random generator = new Random((long)1.0);
        Random generator = new Random();

        for(int i = 0; i < size; i++)
        {

            int next_value = generator.nextInt();
            values[i] = next_value;

        }

        return values;

    }


}
