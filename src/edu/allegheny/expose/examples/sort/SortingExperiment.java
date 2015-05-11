package edu.allegheny.expose.examples.sort;

import java.util.*;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.DoublingExperiment;
import edu.allegheny.expose.ReverseEngineer;
import edu.allegheny.expose.examples.sort.*;

public class SortingExperiment extends DoublingExperiment{

    protected int alg;
    protected String name;
    private int[] n;

    public static String[] algs = {"quick", "insertion", "merge", "selection", "bubble"};

    public static void main(String[] args){
        SortingExperiment exp = new SortingExperiment();
        exp.alg = Integer.parseInt(args[0]);
        switch (exp.alg){
            case 1: exp.name = "quick";
                    break;
            case 2: exp.name = "insertion";
                    break;
            case 3: exp.name = "merge";
                    break;
            case 4: exp.name = "selection";
                    break;
            case 5: exp.name = "bubble";
                    break;
            default: System.out.println("Invalid choice, use: quick, insertion, merge, selection, or bubble");
                     return;
        }

        exp.initN();
        System.out.println("Running experiment for: "+exp.name+"sort.");
        exp.runExperiment();
        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData(exp.getData());
        exp.getData().writeCSV();
        BigOh ans = eng.analyzeData();
        System.out.println(exp.name+"sort is "+ans);

    }

    public static BigOh doubleExp(String name){
        SortingExperiment exp = new SortingExperiment();
        exp.name = name;

        switch (name){
            case "quick": 
                    exp.alg = 1;
                    break;
            case "insertion": 
                    exp.alg = 2;
                    break;
            case "merge": 
                    exp.alg = 3;
                    break;
            case "selection":
                    exp.alg = 4;
                    break;
            case "bubble": 
                    exp.alg = 5;
                    break;
        }

        exp.initN();
        System.out.println("Running experiment for: "+exp.name+"sort.");
        exp.runExperiment();
        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData(exp.getData());
        /* exp.getData().writeCSV(); */
        BigOh ans = eng.analyzeData();
        return ans;

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

        System.out.println("Time = "+(endTime-startTime));

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
