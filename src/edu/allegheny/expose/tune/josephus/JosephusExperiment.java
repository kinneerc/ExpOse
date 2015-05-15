package edu.allegheny.expose.tune.josephus;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.DoublingExperiment;
import edu.allegheny.expose.ReverseEngineer;
import edu.allegheny.expose.tune.BenchMark;
import edu.allegheny.expose.tune.josephus.*;
import net.datastructures.*;
public class JosephusExperiment extends BenchMark{

    protected int alg;
    protected String name;
    private int n;


    public JosephusExperiment(String[] args){
    super(args);
    initN();
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

        //System.out.println("N = "+n+"Time = "+time);

        return time;

    }

    public String toString(){
    	return "Josephus";
    }
    
	@Override
	protected BigOh getCorrectBigOh() {
		return linearithmic;
	}

}


