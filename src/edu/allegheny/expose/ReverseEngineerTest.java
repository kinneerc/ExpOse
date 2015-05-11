package edu.allegheny.expose;

import org.junit.*;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.ComplexityClass;
import edu.allegheny.expose.ReverseEngineer;

public class ReverseEngineerTest{


    @Test
    public void testWBubble(){
        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData("tests/bubblesort.csv");
        BigOh res = eng.analyzeData();
        ComplexityClass ans = res.getCompClass();
        Assert.assertEquals(ans, ComplexityClass.QUADRADIC);
    }


    @Test public void testWMerge(){
        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData("tests/mergesort.csv");
        BigOh res = eng.analyzeData();
        ComplexityClass ans = res.getCompClass();
        Assert.assertEquals(ans, ComplexityClass.LINEARITHMIC);
    }

    @Test
    public void testWLog(){
        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData("tests/log.csv");
        BigOh res = eng.analyzeData();
        ComplexityClass ans = res.getCompClass();
        Assert.assertEquals(ans, ComplexityClass.LOGARITHMIC);
    }

    @Test
    public void testWConstant(){
        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData("tests/constant.csv");
        BigOh res = eng.analyzeData();
        ComplexityClass ans = res.getCompClass();
        Assert.assertEquals(ans, ComplexityClass.CONSTANT);

    }

    @Test
    public void testWLinear(){
        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData("tests/linear.csv");
        BigOh res = eng.analyzeData();
        ComplexityClass ans = res.getCompClass();
        Assert.assertEquals(ans, ComplexityClass.LINEAR);
    }

    
}
