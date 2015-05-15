package edu.allegheny.expose.tune;

public abstract class BenchMarkSuite implements Iterable<BenchMark> {

    private String[] args;

    public BenchMarkSuite(String[] args){
        this.args = args;
    }

    /**
     * Returns arguments for initializing benchmarks
     */
    protected String[] getArgs(){
        return args;
    }

}
