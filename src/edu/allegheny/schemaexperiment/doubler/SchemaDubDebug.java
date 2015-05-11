package edu.allegheny.schemaexperiment.doubler;

import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.mutation.analysis.util.SchemaMerger;
import com.beust.jcommander.JCommander;

/**
 * This class uses a schema doubler on a test schema to make sure it is working
 *
 * @author Cody Kinneer
 */
public class SchemaDubDebug{
    public static void main(String[] args){
        // Parse command line args, see Params class
        DubDebugParams params = new DubDebugParams();
        new JCommander(params, args);

        // create test schema
        Schema one = new parsedcasestudy.iTrust();
        Schema two = new parsedcasestudy.BioSQL();
        Schema test = SchemaMerger.merge(one, two);

        SchemaDoubler doubler;

        String name = params.pack + params.dubName;

        //TODO share this code

        // instantiate SchemaDoubler
        try{
            Class<?> c = Class.forName(name);
            doubler = (SchemaDoubler) c.newInstance(); 

            doubler.setSchema(test);
            for (int count = 0; count < params.doubles; count++){
                try {
                    doubler.doubleSchema();
                } catch (SchemaDoublerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            Schema after = doubler.getSchema();

            // get data before and after
            int[] bData = Metrics.allData(test);
            int[] aData = Metrics.allData(after);

            // show the results
            String[] labels = Metrics.DATALABELS;
            for(int count = 0; count < Metrics.DATALABELS.length; count++){
                System.out.println(labels[count]+" Before: "+bData[count]+" After: "+aData[count]);
            }


        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }

    }
}
