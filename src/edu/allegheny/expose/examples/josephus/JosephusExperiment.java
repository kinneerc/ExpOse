package edu.allegheny.expose.examples.josephus;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.DoublingExperiment;
import edu.allegheny.expose.ReverseEngineer;
import edu.allegheny.expose.examples.josephus.*;
import net.datastructures.*;
public class JosephusExperiment extends DoublingExperiment{

    protected int alg;
    protected String name;
    private int n;


    public static void main(String[] args){
        JosephusExperiment exp = new JosephusExperiment();
        System.out.println("Running experiment for: Joshephus.");
        exp.initN();
        exp.runExperiment();
        exp.getData().writeCSV();
        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData(exp.getData());
        BigOh ans = eng.analyzeData();
        System.out.println(ans);

    }

    protected void initN(){
        n = 100;
    }
    protected void doubleN(){
        n *= 2;
    }
    protected double timedTest(){

        String[] victims = JosephusSolver.getChildren(n);

        Queue<String> q = JosephusSolver.buildQueue(victims);

        long startTime = System.nanoTime();

        JosephusSolver.Josephus(q, 4);

        long endTime = System.nanoTime();

        double time = (double) endTime - startTime;

        System.out.println("N = "+n+"Time = "+time);

        return time;

    }

}


