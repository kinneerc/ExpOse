package edu.allegheny.schemaexperiment.schemagen;

import org.schemaanalyst.sqlrepresentation.*;
import org.schemaanalyst.sqlrepresentation.datatype.*;
import java.util.*;
import java.lang.reflect.*;

import org.schemaanalyst.sqlwriter.SQLWriter;

public class Generator{

    public static void main(String[] args){

        if(args.length != 4){
            System.out.println("Usage - 4 args: int tables, int columns, int notnulls, int primaryKeys");
        }else{

        int[] iargs = new int[args.length];
        for (int count = 0; count < args.length; count++){
            iargs[count] = Integer.parseInt(args[count]);
        }

        SchemaSpec sp = randomSchema(iargs[0],iargs[1],iargs[2],iargs[3],0,0,0);

        Schema s = sp.getSchema();

        SQLWriter writer = new SQLWriter();

        for (String x : writer.writeCreateTableStatements(s))
            System.out.println(x);

        }

    }

    public static SchemaSpec randomSchema(int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, int checks, int uniques){

        Random rand = new Random();

        // split up columns randomly
        int[] colSplitPoints = Util.groupSplitPoints(tables,columns);

        // randomly assign datatypes to columns
        int[] colDataType = new int[columns];
        for (int count = 0; count<columns; count++){
            colDataType[count] = rand.nextInt(DataTypeFactory.numSupportedTypes);
        }

        // NOTICE::funny interpretation, primarykeys = the number of columns in a primary key
        // so then pick which columns are in a primary key
        boolean[] pkeys = new boolean[columns];
        for (int count = 0; count < primaryKeys; count++){
            int randCol;
            do{
                randCol = rand.nextInt(columns);
            }while(pkeys[randCol]==true);
                pkeys[randCol] = true;
        }

        // now pick which columns are not null
        boolean[] notNulls = new boolean[columns];
        for (int count = 0; count < notnulls; count++){
            int randCol;
            do{
                randCol = rand.nextInt(columns);
            }while(notNulls[randCol]==true);
                notNulls[randCol] = true;
        }
        // TODO fkeys, uniques, and checks

        return new SchemaSpec(tables,colSplitPoints,colDataType,pkeys,notNulls,new ArrayList<ForiegnKeySpec>(),new ArrayList<UniqueSpec>());
        
    }

}
