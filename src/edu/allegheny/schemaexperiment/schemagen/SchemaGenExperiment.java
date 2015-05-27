package edu.allegheny.schemaexperiment.schemagen;

import org.schemaanalyst.data.ValueFactory;
import org.schemaanalyst.data.generation.DataGenerator;
import org.schemaanalyst.data.generation.DataGeneratorFactory;
import org.schemaanalyst.dbms.mysql.MySQLDBMS;
import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.testgeneration.TestSuiteGenerator;
import org.schemaanalyst.testgeneration.coveragecriterion.CoverageCriterion;
import org.schemaanalyst.testgeneration.coveragecriterion.CoverageCriterionFactory;

import com.beust.jcommander.JCommander;

import edu.allegheny.expose.BigOh;
import edu.allegheny.expose.DoublingExperiment;
import edu.allegheny.expose.ReverseEngineer;
import edu.allegheny.schemaexperiment.SchemaExpParams;

public class SchemaGenExperiment extends DoublingExperiment {

    // int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, 
    // int uniques,int checks
    // @see Generator
    int[] schemaSize;

    // corresponds to what array index we will be doubling
    int doubler;

    // for schema features that have size of their own
    // such as columns with VARCHAR which can have a variable number
    // of characters up to a max size, or compound keys that can
    // have some number of columns
    //
    // 0 - subsizes uniformly distributed 1 - max
    // 1 - subsizes = 1
    // 2 - subsizes = n
    int subDoublerType;

    // for when one schema feature depends on another
    // maximum anticipated size of the dependent feature
    int maxSize;

    Schema n;
    SchemaExpParams params;

    static final String[] CSVHEADER = {"Doubles","Time","Criterion",
        "DataGenerator","Doubler"}; 

    public static void main(String[] args){
        SchemaExpParams params = new SchemaExpParams();
        new JCommander(params,args);

        if (params.csv.equals("LastExperiment.csv.tmp")){
            params.csv = "data/DATA"+params.schema+"_"+params.criterion+"_"+params.datagenerator+"_"+params.doubler+".csv";
        }

        String[] settings = {params.criterion,params.datagenerator,params.doubler};

        SchemaGenExperiment exp = new SchemaGenExperiment(params, args, settings);

        boolean success = false;

        exp.initN(exp.maxSize);
        while(!success){
            try{
                exp.runExperiment();
                success = true;
            }catch(SchemaGenException e){
                exp.maxSize *= 2;
                exp.initN(exp.maxSize);
                System.out.println("Caught Exception, increasing maxsize to "+exp.maxSize);
            }
        }
ReverseEngineer eng = new ReverseEngineer();
        eng.loadData(exp.getData());
        BigOh ans = eng.analyzeData();
        exp.data.writeMetafile(exp.termCode, exp.runTime,"Generated", params.criterion, params.datagenerator, params.doubler,ans.toString());

        if (params.verbose){
            exp.printBigOh();
        }

    }

    public SchemaGenExperiment(SchemaExpParams params, String[] args, String[] settings){
        super(params,args,CSVHEADER,settings);
        setSubFeatureMode(params.subFeature);
        setDoubler(params.doubler);
        this.params = params;
        this.maxSize = 1000;
    }

    private void setDoubler(String doubler){
        switch(doubler){
            case "tables" : this.doubler = 0; break;
            case "columns" : this.doubler = 1; break;
            case "notNulls" : this.doubler = 2; break;
            case "primaryKeys" : this.doubler = 3; break;
            case "foriegnKeys" : this.doubler = 4; break;
            case "uniques" : this.doubler = 5; break;
            case "checks" : this.doubler = 6; break;
            case "all" : this.doubler = -1; this.subDoublerType = 2; break;
        }
    }

    private void setSubFeatureMode(String sub){
        switch(sub){
            case "min":this.subDoublerType = 1; break;
            case "max" : this.subDoublerType = 2; break;
            default: this.subDoublerType = 0; break;
        }
    }

    public void doubleN(){

        if(doubler==-1){
            for (int count = 0; count < schemaSize.length; count++){
                schemaSize[count] *= 2;
            }
        }else{

            schemaSize[doubler] = (schemaSize[doubler] == 0) ? 1 : schemaSize[doubler] * 2;

            // if we want the subsizes to be size n, then make it so
            switch(subDoublerType){
                case 0: schemaSize[7] = -1; schemaSize[8] = -1; break;
                case 1: schemaSize[7] = 10; schemaSize[8] = 1; break;
                case 2: schemaSize[7] = schemaSize[doubler]; 
                        schemaSize[8] = schemaSize[doubler]; 
                        break;
            }

        }

        for (int i : schemaSize){
            System.out.print(i+" ");
        }
        System.out.println();
    }

    // int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, 
    // int uniques,int checks

    public void initN(int maxSize){
        schemaSize = new int[] {1,1,0,0,0,0,0,0,0};
        switch(doubler){
            case 0: case 2: case 3: schemaSize[1] = maxSize; break;
            case 4: case 5: 
                                    double[] quad = quadForm(1,-1,-maxSize);
                                    double needed = Math.max(quad[0],quad[1]);
                                    needed = Math.ceil(needed*2.5); 
                                    schemaSize[0] = (int) needed; schemaSize[1] = (int) needed; 
                                    break;
            case -1: schemaSize = new int[] {2,40,1,1,1,1,1,1,1};
                     // TODO consider how uniques should be allowed
                     // i.e., one per table? overlapping columns allowed?
                     // for now, assume same as primary keys
        }
    }

    private double[] quadForm(double a, double b, double c){
        double sp = Math.sqrt(Math.pow(b,2)-4*a*c);
        double numP = -b + sp;
        double numN = -b - sp;
        return new double[] {numP/2*a,numN/2*a};
    }

    public double timedTest(){

        // instantiate schema if nessissary
        if(n==null){
            if(maxSize==0)
                maxSize = 10;
            initN(maxSize);
            for (int i : schemaSize)
                System.out.print(i+" ");
        }

        // instantiate the params if nessissary
        if (params==null)
            params = new SchemaExpParams();

        // generate the schema
        long sStart = System.currentTimeMillis();
        boolean caught = false;
        do{
            try{
                n = Generator.randomSchema(schemaSize).getSchema();
                caught = false;
            }catch(SchemaGenException e){
                if(doubler==-1){
                    caught=true;
                    System.out.println("Caught SchemaGenException, trying again...");

                    for (int i : schemaSize){
                        System.out.print(i+" ");
                    }
                    System.out.println();

                    e.printStackTrace();


                }else{
                    throw e;
                }
            }
        }while(caught);
        long sgen = System.currentTimeMillis() - sStart;

        if(params.verbose){
            System.out.println("Took "+sgen+"ms to generate schema.");
            for (int i : schemaSize){
                System.out.print(i+" ");
            }
            System.out.println();
        }


        // instantiate objects for parameters
        CoverageCriterion criterionObject = CoverageCriterionFactory.instantiateSchemaCriterion(params.criterion,n,new MySQLDBMS());
        DataGenerator dataGeneratorObject = DataGeneratorFactory.instantiate(params.datagenerator, 0L, 10000, n); 
        // generate the test suites
        TestSuiteGenerator testSuiteGenerator = new TestSuiteGenerator(
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
