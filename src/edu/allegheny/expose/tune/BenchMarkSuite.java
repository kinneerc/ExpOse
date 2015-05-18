package edu.allegheny.expose.tune;

public abstract class BenchMarkSuite implements Iterable<BenchMark> {

    private String[] args;

    public BenchMarkSuite(String[] args){
        this.args = args;
    }

    /**
     * Returns arguments for initializing benchmarks
     */
    public String[] getArgs(){
        return args;
    }

    abstract public int size();

    public void setArg(String arg,String value){

        String[] nargs = new String[args.length+2];

        boolean found = false;
        for (int count = 0; count < args.length; count++){
            nargs[count] = args[count];
            if (args[count].equals(arg)){
                args[count+1] = value;
                found = true;
                break;
            }
        }
        if (!found){
            nargs[args.length] = arg;
            nargs[args.length+1] = value;
            args = nargs;
        }
    }

}
