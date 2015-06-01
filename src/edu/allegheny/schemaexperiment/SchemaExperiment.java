package edu.allegheny.schemaexperiment;

import org.schemaanalyst.data.ValueFactory;
import org.schemaanalyst.data.generation.DataGenerator;
import org.schemaanalyst.data.generation.DataGeneratorFactory;
import org.schemaanalyst.mutation.analysis.util.SchemaMerger;
import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.testgeneration.TestSuiteGenerator;
import org.schemaanalyst.testgeneration.coveragecriterion.CoverageCriterion;
import org.schemaanalyst.testgeneration.coveragecriterion.CoverageCriterionFactory;
import org.schemaanalyst.dbms.mysql.MySQLDBMS;

import com.beust.jcommander.JCommander;

import edu.allegheny.expose.*;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;
import edu.allegheny.schemaexperiment.doubler.SchemaDoublerException;
import edu.allegheny.schemaexperiment.doubler.Metrics;

public class SchemaExperiment extends DoublingExperiment{

    SchemaDoubler doubler;
    CoverageCriterion criterionObject;
    DataGenerator dataGeneratorObject; 
    TestSuiteGenerator testSuiteGenerator;
    SchemaExpParams params;
    Schema n;

    static final String[] CSVHEADER = {"Doubles","Time","tables","fKeys","pKeys","uniques","notNulls",
        "checks", "constraints","columns","Schema","Criterion","DataGenerator","Doubler"}; 

    public static class writeMeta extends Thread {
        SchemaExperiment exp;
        public writeMeta(SchemaExperiment exp){
            this.exp = exp;
        }
        public void run() {
             exp.data.writeMetafile(exp.termCode, exp.runTime, exp.params.schema, exp.params.criterion, exp.params.datagenerator, exp.params.doubler, "KILLED");
        }
    }

    public static void main(String[] args){

        SchemaExpParams params = new SchemaExpParams();
        new JCommander(params,args);

        String schema;
        if (params.schema != null){
            schema = params.schema;
        }else{
            schema = "iTrustBioSQLMerged";
        }

        if (params.csv.equals("LastExperiment.csv.tmp")){
            params.csv = "data/DATA"+params.schema+"_"+params.criterion+"_"+params.datagenerator+"_"+params.doubler+".csv";
        }

        String[] settings = {schema,params.criterion,params.datagenerator,params.doubler};

        SchemaExperiment exp = new SchemaExperiment(params, args, settings);

        try {
            exp.doubler = instantiateSchemaDoubler(params.doublerPackage+params.doubler);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
                }

        if (params.schema == null){
            Schema one = new parsedcasestudy.iTrust();
            Schema two = new parsedcasestudy.BioSQL();
            exp.n = SchemaMerger.merge(one, two);
        }else{

            try {
                exp.n = instantiateSchema(params.schemaPackage+params.schema);
            } catch (InstantiationException | IllegalAccessException
                    | ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                    }

        }

        exp.doubler.setSchema(exp.n);

        // almost ready to run experiment, first, create a shutdown hook in case our job is killed
        Thread printIfKilled = new writeMeta(exp);

        Runtime.getRuntime().addShutdownHook(printIfKilled);

        exp.runExperiment();

        // if finished, then we do not require the shutdown hook, remove it
        Runtime.getRuntime().removeShutdownHook(printIfKilled);

        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData(exp.getData());
        BigOh ans = eng.analyzeData();


        exp.data.writeMetafile(exp.termCode, exp.runTime, schema, params.criterion, params.datagenerator, params.doubler,ans.toString());

        if (params.verbose){
            exp.printBigOh();
        }

    }

    // override default to save schema size statistics
    @Override
    protected Double[] getResult(int doubles){
        int[] stats = Metrics.allData(n);
        Double[] ans = new Double[2+stats.length];
        ans[0] = (double) doubles;
        ans[1] = timedTest();
        int count = 2;
        for (int x : stats)
            ans[count++] = (double) x;

        return ans;
    }

    public SchemaExperiment(SchemaExpParams params, String[] args, String[] settings){
        super(params,args,CSVHEADER,settings);
        this.params = params;
    }

    protected static SchemaDoubler instantiateSchemaDoubler(String schemaDoubler)
        throws ClassNotFoundException, InstantiationException,
                          IllegalAccessException {
                   Class<?> c = Class.forName(schemaDoubler);
                   SchemaDoubler readIn = (SchemaDoubler) c.newInstance();

                   return readIn;

    }

    protected static Schema instantiateSchema(String schema)
        throws InstantiationException, IllegalAccessException,ClassNotFoundException {

        Class<?> c = Class.forName(schema);
        Schema readIn = (Schema) c.newInstance();

        return readIn;


    }

    protected void initN(){
        //initialize params if needed
        if(params==null){
            params = new SchemaExpParams();
        }

        if (params.schema == null){
            Schema one = new parsedcasestudy.iTrust();
            Schema two = new parsedcasestudy.BioSQL();
            n = SchemaMerger.merge(one, two);
        }else{

            try {
                n = instantiateSchema(params.schemaPackage+params.schema);
            } catch (InstantiationException | IllegalAccessException
                    | ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                    }
        }

        try {
            doubler = instantiateSchemaDoubler(params.doublerPackage+params.doubler);
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
                }


        doubler.setSchema(n);
    }
    public void doubleN(){
        try {
            doubler.doubleSchema();
        } catch (SchemaDoublerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        n = doubler.getSchema();
    }
    public double timedTest(){

        // initialize N if needed
        if(n==null)
            initN();
        // instantiate objects for parameters
        criterionObject = CoverageCriterionFactory.instantiateSchemaCriterion(params.criterion,n,new MySQLDBMS());
        dataGeneratorObject = DataGeneratorFactory.instantiate(params.datagenerator, 0L, 10000, n);

        // generate the test suites
        testSuiteGenerator = new TestSuiteGenerator(
                n,
                criterionObject.generateRequirements(),
                new ValueFactory(),
                dataGeneratorObject);

        long startTime = System.nanoTime();


        testSuiteGenerator.generate();


        long endTime = System.nanoTime();

        if (params.verbose)
            System.out.println("Time = "+(endTime-startTime));

        return (double) endTime - startTime;

    }


}
