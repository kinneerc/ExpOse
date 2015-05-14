package edu.allegheny.schemaexperiment.schemagen;

import org.schemaanalyst.sqlrepresentation.*;
import java.util.*;

import org.schemaanalyst.sqlwriter.SQLWriter;

public class Generator{

    public static void main(String[] args){

        if(args.length != 6){
            System.out.println("Usage - 6 args: int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, int uniques");
        }else{

        int[] iargs = new int[args.length];
        for (int count = 0; count < args.length; count++){
            iargs[count] = Integer.parseInt(args[count]);
        }

        SchemaSpec sp = randomSchema(iargs[0],iargs[1],iargs[2],iargs[3],iargs[4],iargs[5],0);

        Schema s = sp.getSchema();
        
        System.out.println("Generator done!");

        SQLWriter writer = new SQLWriter();

        for (String x : writer.writeCreateTableStatements(s))
            System.out.println(x);

        }

    }

    // FKEYS: will make compound keys according to a uniform distribution (just like PKeys) 
    public static SchemaSpec randomSchema(int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, int uniques,int checks){

    	if (columns < tables)
    		throw new SchemaGenException("More tables than columns, tables cannot be empty.");
    	
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

       Iterator<PotentialForeignKey> pfks = fkg.iterator();
       
       for (int count = 0; count < foriegnKeys; count++){
           if (!pfks.hasNext())
           throw new SchemaGenException("Cannot provide requested FKeys");

           PotentialForeignKey pfk = pfks.next();

           if(pfk.createsCycle()||pfk.getMaxSize()==0){
               // redo this selection
               count--;
           }else{

           int ncols = rand.nextInt(pfk.getMaxSize())+1;
           
           fkeys.add(pfk.getBySize(ncols));
           }

       }

       ArrayList<UniqueSpec> uqs = new ArrayList<UniqueSpec>();
    
       // finally, the uniques
        for(int count = 0; count < uniques; count++){
            PotentialUnique pu;
            int tries = 0;
            do{
                if (tries++ > 1.5*tables)
                    throw new SchemaGenException("Could not provide requested UNIQUES.");
                pu = fkg.getPotUnique(rand.nextInt(tables));
            }while(pu.getMaxSize()==0);
            uqs.add(pu.getUniqueBySize(rand.nextInt(pu.getMaxSize())+1));
        }

        // TODO  checks

        return new SchemaSpec(tables,colSplitPoints,colDataType,pkeys,notNulls,fkeys,uqs);
        
    }

}
