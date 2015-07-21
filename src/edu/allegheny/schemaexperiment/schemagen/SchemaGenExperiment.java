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
import edu.allegheny.schemaexperiment.doubler.Metrics;

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
    // have some number of columns; 
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

    static final String[] CSVHEADER = {"Doubles","Time","tables","fKeys","pKeys","uniques","notNulls",
        "checks", "constraints","columns","Schema","Criterion","DataGenerator","Doubler","SubDoubler"}; 

    public static class writeMeta extends Thread {
        SchemaGenExperiment exp;
        public writeMeta(SchemaGenExperiment exp){
            this.exp = exp;
        }
        public void run() {
            exp.data.writeMetafile(exp.termCode, exp.runTime, exp.params.schema, exp.params.criterion, exp.params.datagenerator, exp.params.doubler, exp.params.subFeature,"KILLED");
        }
    }

    public static void main(String[] args){
        SchemaExpParams params = new SchemaExpParams();
        new JCommander(params,args);

        if (params.csv.equals("LastExperiment.csv.tmp")){
            params.csv = "data/DATA"+params.schema+"_"+params.criterion+"_"+params.datagenerator+"_"+params.doubler+"_"+params.subFeature+".csv";
        }

        String[] settings = {"Generated",params.criterion,params.datagenerator,params.doubler,params.subFeature};

        SchemaGenExperiment exp = new SchemaGenExperiment(params, args, settings);

        boolean success = false;

        exp.initN(exp.maxSize);

        // almost ready to run experiment, first, create a shutdown hook in case our job is killed
        Thread printIfKilled = new writeMeta(exp);

        Runtime.getRuntime().addShutdownHook(printIfKilled);

        while(!success){
            try{
                exp.runExperiment();
                success = true;
            }catch(SchemaGenException e){
                exp.maxSize *= 2;
                exp.initN(exp.maxSize);
                if(params.verbose)
                System.out.println("Caught Exception, increasing maxsize to "+exp.maxSize);
            }
        }

        // if finished, then we do not require the shutdown hook, remove it
        Runtime.getRuntime().removeShutdownHook(printIfKilled);

        ReverseEngineer eng = new ReverseEngineer();
        eng.loadData(exp.getData());
        BigOh ans = eng.analyzeData();
        exp.data.writeMetafile(exp.termCode, exp.runTime,"Generated", params.criterion, params.datagenerator, params.doubler, params.subFeature, ans.toString());

        if (params.verbose){
            exp.printBigOh();
        }

    }

    // override default to save schema size statistics
    @Override
    protected Double[] getResult(int doubles){
        if(n==null){
            if(maxSize==0)
                maxSize = 10;
            initN(maxSize);
        }

        int[] stats = Metrics.allData(n);
        Double[] ans = new Double[2+stats.length];
        ans[0] = (double) doubles;   // TODO consider how uniques should be allowed
        // i.e., one per table? overlapping columns allowed?
        // for now, assume same as primary keys
        ans[1] = timedTest();
        int count = 2;
        for (int x : stats)
            ans[count++] = (double) x;

        return ans;
    }

    public SchemaGenExperiment(SchemaExpParams params, String[] args, String[] settings){
        super(params,args,CSVHEADER,settings);
        setSubFeatureMode(params.subFeature);
        setDoubler(params.doubler);
        this.params = params;
        this.maxSize = 100;
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
            case "number":this.subDoublerType = 1; break;
            case "size" : this.subDoublerType = 2; break;
            default: this.subDoublerType = 0; break;
        }
    }

    public void doubleN(){

        if(doubler==-1){
            for (int count = 0; count < schemaSize.length; count++){
                schemaSize[count] *= 2;
            }
        }else{

            if (!params.subFeature.equals("size"))
            schemaSize[doubler] = (schemaSize[doubler] == 0) ? 1 : schemaSize[doubler] * 2;

            // if we want the subsizes to be size n, then make it so
            switch(subDoublerType){
                case 1: schemaSize[7] = 10; schemaSize[8] = 1; break;
                case 0: case 2: if (params.doubler.equals("columns")) 
                            schemaSize[7] *= 2; 
                        else
                            schemaSize[8] *= 2; 
                        break;
            }

        }

        if(params.verbose){
        for (int i : schemaSize){
            System.out.print(i+" ");
        }
        System.out.println();
        }
    }

    // int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, 
    // int uniques,int checks

    public void initN(int maxSize){
        schemaSize = new int[] {1,1,0,0,0,0,0,10,1};

        if (params == null)
            params = new SchemaExpParams();

        switch(params.subFeature){
            case "number":
                switch(doubler){
                    case 0: case 2: schemaSize[1] = maxSize; break;
                    case 4:         schemaSize[4] = 1;
                                    double[] quad = quadForm(1,-1,-maxSize);
                                    double needed = Math.max(quad[0],quad[1]);
                                    needed = Math.ceil(needed*2.5); 
                                    schemaSize[0] = (int) needed; schemaSize[1] = (int) needed; 
                                    break;
                    case 5: schemaSize[5] = 1; schemaSize[1] = maxSize; break;
                    case 3: schemaSize[3] = 1; schemaSize[0] = maxSize; schemaSize[1] = maxSize; break;
                    case -1: schemaSize = new int[] {2,40,1,1,1,1,1,10,1}; break;
                }
                break;
            case "size":
                switch(doubler){
                    case 0: case 2: schemaSize[1] = maxSize; break;
                    case 4: schemaSize[4] = 1; schemaSize[0] = 2;schemaSize[1] = maxSize; break;
                    case 5: schemaSize[5] = 1; schemaSize[1] = maxSize; break;
                    case 3: schemaSize[3] = 1; schemaSize[1] = maxSize; break;
                    case 1:  schemaSize = new int[] {1,40,0,0,0,0,0,10,1}; break;
                    case -1: schemaSize = new int[] {2,40,1,1,1,1,1,10,1}; break;
                }
                break;
            /*both*/ default:
                switch(doubler){
                    case 0: case 2: schemaSize[1] = maxSize; break;
                    case 4:  
                                    double[] quad = quadForm(1,-1,-maxSize);
                                    double needed = Math.max(quad[0],quad[1]);
                                    needed = Math.ceil(needed*2.5); 
                                    schemaSize[0] = (int) needed; schemaSize[1] = (int) (needed*needed);
                                    schemaSize[4] = 1;
                                    break;
                    case 5: schemaSize[5] = 1; schemaSize[1] = maxSize; break;
                    case 3: schemaSize[3] = 1; schemaSize[0] = maxSize; schemaSize[1] = maxSize*maxSize; break;
                    case -1: schemaSize = new int[] {2,40,1,1,1,1,1,10,1}; break;
                }
                break;
        }
        
        n = Generator.randomSchema(schemaSize).getSchema();

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
            if(params.verbose){
            for (int i : schemaSize)
                System.out.print(i+" ");
            }
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
                    if(params.verbose){
                    System.out.println("Caught SchemaGenException, trying again...");

                    for (int i : schemaSize){
                        System.out.print(i+" ");
                    }
                    System.out.println();
                    }

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
