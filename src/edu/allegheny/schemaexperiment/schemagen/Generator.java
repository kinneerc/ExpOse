package edu.allegheny.schemaexperiment.schemagen;

import org.schemaanalyst.sqlrepresentation.*;
import java.util.*;

import org.schemaanalyst.sqlwriter.SQLWriter;

public class Generator{

    public static void main(String[] args){

        if(args.length != 5){
            System.out.println("Usage - 5 args: int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys");
        }else{

        int[] iargs = new int[args.length];
        for (int count = 0; count < args.length; count++){
            iargs[count] = Integer.parseInt(args[count]);
        }

        SchemaSpec sp = randomSchema(iargs[0],iargs[1],iargs[2],iargs[3],iargs[4],0,0);

        Schema s = sp.getSchema();

        SQLWriter writer = new SQLWriter();

        for (String x : writer.writeCreateTableStatements(s))
            System.out.println(x);

        }

    }

    // FKEYS: will make compound keys according to a uniform distribution (just like PKeys) 
    public static SchemaSpec randomSchema(int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, int checks, int uniques){

        Random rand = new Random();
        ForiegnKeyGen fkg = new ForiegnKeyGen();

        // split up columns randomly
        int[] colSplitPoints = Util.groupSplitPoints(tables,columns);

        Arrays.sort(colSplitPoints);

        int curTable = 0;

        // randomly assign datatypes to columns
        int[] colDataType = new int[columns];
        for (int count = 0; count<columns; count++){
            if (count>colSplitPoints[curTable]-1)
                curTable++;
            
            colDataType[count] = rand.nextInt(DataTypeFactory.numSupportedTypes);

            // build tables to support making FKeys
            fkg.put(curTable,colDataType[count]);
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
        // FKEYS!

        ArrayList<ForiegnKeySpec> fkeys = new ArrayList<ForiegnKeySpec>();

        // consider all the possible keys
       List<PotentialForeignKey> pfks = fkg.getAllPotFKeys();

       if (pfks.size() < foriegnKeys)
           throw new SchemaGenException("Cannot provide requested FKeys");

       // FIXME move this inside the for loop to allow for more than 1 key from s to d
       Collections.shuffle(pfks);

       for (int count = 0; count < foriegnKeys; count++){
           PotentialForeignKey pfk = pfks.get(count);

           int ncols = rand.nextInt(pfk.getMaxSize())+1;

           fkeys.add(pfk.getBySize(ncols));

       }
    
        // TODO  uniques, and checks



        return new SchemaSpec(tables,colSplitPoints,colDataType,pkeys,notNulls,fkeys,new ArrayList<UniqueSpec>());
        
    }

}
