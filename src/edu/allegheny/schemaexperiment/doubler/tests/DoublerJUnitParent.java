package edu.allegheny.schemaexperiment.doubler.tests;

import org.junit.*;
import org.schemaanalyst.mutation.analysis.util.SchemaMerger;
import org.schemaanalyst.sqlrepresentation.Schema;

import edu.allegheny.schemaexperiment.doubler.Metrics;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;
import edu.allegheny.schemaexperiment.doubler.SchemaDoublerException;

public abstract class DoublerJUnitParent{

    static Schema test;
    SchemaDoubler doubler;
    String checkDoubles;

    int[] expectations;
    int[] initial;

    @BeforeClass
    public static void prepTests(){

        Schema one = new parsedcasestudy.iTrust();
        Schema two = new parsedcasestudy.BioSQL();
        test = SchemaMerger.merge(one, two);

    }

    @Before
    public void beforeTest(){
        doubler = schemaDoubler();
        checkDoubles = checkDoublesValue();
        doubler.setSchema(test);
    }

    @Test
    public void test1() throws SchemaDoublerException {
        testN(1);
    }

    @Test 
    public void test2() throws SchemaDoublerException {
        testN(2);
    }

    @Test
    public void test3() throws SchemaDoublerException {
        testN(3);
    }

    protected void doubleSchema(int N) throws SchemaDoublerException {
        for(int count = 0; count < N; count++){
                doubler.doubleSchema();
            }
        }

    protected void setExpectations(int N){
        expectations = new int[Metrics.DATALABELS.length];

        initial = Metrics.allData(test);

        if (checkDoubles.contains("t"))
            expectDub(0,N);
        else
            expectCons(0);

        if (checkDoubles.contains("f"))
            expectDub(1,N);
        else
            expectCons(1);

        if (checkDoubles.contains("p"))
            expectDub(2,N);
        else
            expectCons(2);

        if (checkDoubles.contains("u"))
            expectDub(3,N);
        else
            expectCons(3);

        if (checkDoubles.contains("n"))
            expectDub(4,N);
        else
            expectCons(4);
        
        if (checkDoubles.contains("c"))
            expectDub(5,N);
        else
            expectCons(5);

        expectations[6] = 0;
        for (int count = 1; count <= 5; count++){
            expectations[6] += expectations[count];
        }

        if (checkDoubles.contains("o"))
            expectDub(7,N);
        else
            expectCons(7);

    }

    private void expectDub(int i, int N){
        expectations[i] = initial[i] * (int) Math.pow(2,N);
    }

    private void expectCons(int i){
        expectations[i] = initial[i];
    }


    public void testN(int N) throws SchemaDoublerException {
        doubleSchema(N);
        setExpectations(N);
        Assert.assertArrayEquals(expectations,Metrics.allData(doubler.getSchema()));
    }

    abstract String checkDoublesValue();
    abstract SchemaDoubler schemaDoubler();

}
