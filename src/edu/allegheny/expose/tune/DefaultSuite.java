package edu.allegheny.expose.tune;

import java.util.*;

import edu.allegheny.expose.tune.josephus.JosephusExperiment;
import edu.allegheny.expose.tune.simplealgs.LinLogExperiment;
import edu.allegheny.expose.tune.sort.SortingExperiment;

public class DefaultSuite extends BenchMarkSuite {

    private ArrayList<BenchMark> benchmarks;

    public DefaultSuite(String[] args){
        super(args);
        benchmarks = new ArrayList<BenchMark>();
    }

    public Iterator<BenchMark> iterator(){
        initializeBenchmarks();
        return new Iterator<BenchMark>() {
            public BenchMark next(){
                return benchmarks.remove(0);
            }
            public boolean hasNext(){
                return !benchmarks.isEmpty();
            }
        };
    }

    private void initializeBenchmarks(){
        // get sorting algorithms
        for (String sortAlg : SortingExperiment.algs){
            benchmarks.add(new SortingExperiment(getArgs(),sortAlg));
        }
        // get simple algorithms
        for (int alg : LinLogExperiment.algs){
            benchmarks.add(new LinLogExperiment(getArgs(),alg));
        }
        benchmarks.add(new JosephusExperiment(getArgs()));
    }
}
