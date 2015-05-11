package edu.allegheny.schemaexperiment.doubler;

import org.schemaanalyst.sqlrepresentation.*;

/**
 * This class gives information about the properties of a schema.
 * It is used for testing SchemaDoublers
 *
 * @author Cody Kinneer
 */
public class Metrics{

    public static final String[] DATALABELS = {"tables","fKeys","pKeys","uniques","notNulls",
        "checks", "constraints","columns"};

    /**
     * Shows information on desired schema
     * @param args the name of a schema class, package is assumed parsedcasestudy
     */
    public static void main(String args[]){
        String pack = "parsedcasestudy."; 
        try{
            Class<?> c = Class.forName(pack+args[0]);
            Schema test = (Schema) c.newInstance();
            printMetrics(test);
            
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }

    }

    // The following methods count the number of elements
    // found in a schema

    public static int tables(Schema schema){
        return schema.getTables().size();
    }

    public static int fKeys(Schema schema){
        return schema.getForeignKeyConstraints().size();
    }

    public static int pKeys(Schema schema){
        return schema.getPrimaryKeyConstraints().size();
    }

    public static int uniques(Schema schema){
        return schema.getUniqueConstraints().size();
    }

    public static int notNulls(Schema schema){
        return schema.getNotNullConstraints().size();
    }

    public static int checks(Schema schema){
        return schema.getCheckConstraints().size();
    }

    public static int constraints(Schema schema){
        return schema.getConstraints().size();
    }

    public static int columns(Schema schema){
        int columns = 0;
        for (Table x : schema.getTables()){
            columns += x.getColumns().size();
        }

        return columns;
    }


    /**
     * Returns an arraylist with all data.
     * See DATALABLES array for the meaning of each index
     * @param schema schema to analyze
     * @return array filled with all the data this class can collect
     */
    public static int[] allData(Schema schema){
        int[] ans = {tables(schema), fKeys(schema), pKeys(schema), uniques(schema), 
            notNulls(schema), checks(schema), constraints(schema), columns(schema)};
        return ans;
    }

    /**
     * This method outputs data from a schema to the terminal
     * @param schema schema to get information on
     */
    public static void printMetrics(Schema schema){
        int[] data = allData(schema);

        for (int count = 0; count < DATALABELS.length; count++){
            System.out.println(DATALABELS[count]+": "+data[count]);
        }
    }

}
