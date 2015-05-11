package edu.allegheny.schemaexperiment;

import static edu.allegheny.schemaexperiment.Setup.*;

/**
 * Runs SchemaExperiment on every combination for criterions, datagenerators, schemas, and doublers found in
 * the setup class
 *
 * @author Cody Kinneer
 */
public class RunExperiment{

    public static void main(String[] args) throws InstantiationException {

        long start = 0;
        long end = 0;

        // Check setup file is valid before attempting to run experiments
        try {
            validate();
        } catch (InstantiationException | IllegalAccessException
                | ClassNotFoundException e) {
            e.printStackTrace();

            throw new InstantiationException("Setup.java invalid");

                }



        // now run every combination
        // all data will be appended to the LastExperiment.csv file
        // this file must be manually deleated if desired
        for (String doubler : DOUBLERS){
            for (String schema : SCHEMAS){
                for (String criteria : CRITERIA){
                    for(String dataGenerator : DATAGENERATORS){
                        args = buildArgs(schema, criteria, dataGenerator,doubler,"2");

                        System.out.print("Running");

                        for (int count = 0; count < args.length; count++){
                            System.out.print(" "+args[count]);
                        }
                        try{
                            start = System.currentTimeMillis();
                            SchemaExperiment.main(args);
                            end = System.currentTimeMillis();

                            System.out.println(" result: finished after: "+(end-start)+" ms.");

                        } catch (OutOfMemoryError e){
                            end = System.currentTimeMillis();

                            System.out.println(" result: Out of Memory after: "+(end-start)+" ms.");
                        }

                    }
                }
            }
        }
    }

    private static String[] buildArgs(String schema, String criteria, String dataGenerator, String doubler, String maxTimeRun){
        return new String[] {"-schema",schema,"-criterion",criteria,"-datagenerator",
            dataGenerator,"-doubler",doubler,"-maxTime",maxTimeRun,"-trials","1"};
    }


}
