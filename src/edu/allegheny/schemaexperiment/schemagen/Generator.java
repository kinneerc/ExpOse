package edu.allegheny.schemaexperiment.schemagen;

import org.schemaanalyst.sqlrepresentation.*;
import org.schemaanalyst.sqlrepresentation.datatype.*;
import java.util.*;
import java.lang.reflect.*;

public class Generator{
    public Schema generate(int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, int checks, int uniques){

        // first get a basic schema of tables and columns
        Schema ans = genTabCol(tables,columns);
        // now we'll start to add constraints
        // first, primary keys
        addPKeys(ans,primaryKeys);

        return ans;
    }

    /**
     * Randomly add a set number of primary keys to the schema
     */
    private void addPKeys(Schema s,int pkeys){
    List<Table> tbs = s.getTables();

    if (tbs.size() < pkeys)
        throw new SchemaGenException("Cannot generate schema with more PRIMARY KEY constraints than tables");

    int npkeys = s.getPrimaryKeyConstraints().size();

    if (npkeys + pkeys > tbs.size())
        throw new SchemaGenException("Cannot generate schema with more PRIMARY KEY constraints than tables");
    
    Collections.shuffle(tbs);

    int tbsi = 0;
    for (int i = 0; i < pkeys; i++){
        Table cur;
        do{
        cur = tbs.get(tbsi++);
        }while(s.getPrimaryKeyConstraint(cur) != null);
        
        List<Column> cols = cur.getColumns();

        Collections.shuffle(cols);

        s.createPrimaryKeyConstraint(cur,cols.get(0));

    }
}


    private Schema genTabCol(int tables, int columns){

        Schema ans = new Schema("rs");

        // determine how columns will be assigned to tables
        // index = table, value = num of columns
        int[] map = Util.group(tables,columns);

        // add a table to the schema for each table
        for (int t = 0; t < tables; t++){
            Table cur = ans.createTable("rt");
            // for each table, create the decided number of columns
            // add a column to the schema for each col
            // for now, a random datatype will suffice
            for (int c = 0; c < map[t]; c++){
                cur.createColumn("rc",randDataType());
            }

        }

        return ans;

    }

    private DataType randDataType(){
        Class f = (new DataTypeFactory()).getClass();
        Method methods[] = f.getMethods();

        Random rand = new Random();

        try{
            Object dt = methods[rand.nextInt(methods.length)].invoke(null);

            return (DataType) dt;
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(InvocationTargetException e){
            e.printStackTrace();
        }
        return null;
    }

}
