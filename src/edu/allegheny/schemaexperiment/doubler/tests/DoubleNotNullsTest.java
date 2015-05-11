package edu.allegheny.schemaexperiment.doubler.tests;

import org.junit.Test;

import edu.allegheny.schemaexperiment.doubler.DoubleNotNulls;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;
import edu.allegheny.schemaexperiment.doubler.SchemaDoublerException;

public class DoubleNotNullsTest extends DoublerJUnitParent{

    SchemaDoubler schemaDoubler(){
        return new DoubleNotNulls();
    }

    String checkDoublesValue(){
        return "n";
    }

    @Override
    @Test(expected = SchemaDoublerException.class) 
    public void test2() throws SchemaDoublerException {
        testN(2);
    }

    @Override
    @Test(expected = SchemaDoublerException.class)
    public void test3() throws SchemaDoublerException {
        testN(3);
    }

}
