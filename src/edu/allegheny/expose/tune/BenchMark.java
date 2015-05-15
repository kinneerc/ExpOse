package edu.allegheny.expose.tune;

import edu.allegheny.expose.*;

/**
 * The benchmark class provides a doubling experiment with a known worst-case
 * time complexity to assist in setting the parameters for convergence testing
 */
public abstract class BenchMark extends DoublingExperiment {

    // provide easy access to common bigOhs
    protected static final BigOh cubic = new BigOh(ComplexityClass.CUBIC);
    protected static final BigOh quadratic = new BigOh(ComplexityClass.QUADRADIC);
    protected static final BigOh linearithmic = new BigOh(ComplexityClass.LINEARITHMIC);
    protected static final BigOh linear = new BigOh(ComplexityClass.LINEAR);
    protected static final BigOh constant = new BigOh(ComplexityClass.CONSTANT);
    protected static final BigOh logarithmic = new BigOh(ComplexityClass.LOGARITHMIC);

/**
 * A benchmark should be provided with args to evaluate
 */
public BenchMark(String[] args){
    super(args);
}

/**
 * Runs the experiment and reports correct/incorrect
 */
public boolean verifyCorrectness(){
    runExperiment();
    ReverseEngineer eng = new ReverseEngineer();
    eng.loadData(getData());
    BigOh ans = eng.analyzeData();
    return ans.equals(getCorrectBigOh());
}

protected abstract BigOh getCorrectBigOh();

public abstract String toString();

}
