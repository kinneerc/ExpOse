package edu.allegheny.schemaexperiment.schemagen;

import org.schemaanalyst.sqlrepresentation.*;

import java.util.*;

import org.schemaanalyst.sqlwriter.SQLWriter;

public class Generator{

    private static final int MAX_DATATYPE_SIZE = 1000;

    public static void main(String[] args){

        if(args.length != 8){
            System.out.println("Usage - 8 args: int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, int uniques, int dataTypeSize, int compoundKeySize");
        }else{

        int[] iargs = new int[args.length];
        for (int count = 0; count < args.length; count++){
            iargs[count] = Integer.parseInt(args[count]);
        }

        int attempts = 0;

        int maxAttempts = 20;

        SchemaSpec sp = null;

        while (attempts++ < maxAttempts){

        try{
        sp = randomSchema(iargs[0],iargs[1],iargs[2],iargs[3],iargs[4],iargs[5],0,iargs[6],iargs[7]);
        attempts = maxAttempts;
        }catch(SchemaGenException e){
        }

        }


        if (sp != null){

        Schema s = sp.getSchema();
        
        SQLWriter writer = new SQLWriter();

        for (String x : writer.writeCreateTableStatements(s))
            System.out.println(x);

        }else{
            throw new SchemaGenException("Could not generate schema after "+attempts+" tries.");
        }

    }
    }

    public static SchemaSpec randomSchema(int[] sF){
        return randomSchema(sF[0],sF[1],sF[2],sF[3],sF[4],sF[5],sF[6],sF[7],sF[8]);
    }

    private static void validateSchema(int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, int uniques,int checks){
            if (columns < tables){
                /* System.out.println(columns+" < "+tables); */
                throw new SchemaGenException("More tables than columns, tables cannot be empty.");
            }
            if (notnulls > columns){
                /* System.out.println(notnulls+" > "+columns); */
                throw new SchemaGenException("More NOT NULLs than columns.");
            }
            if (primaryKeys > tables){
                /* System.out.println(primaryKeys+" > "+tables); */
                throw new SchemaGenException("More PRIMARY KEYs than tables.");
            }
            if(uniques > tables){
                /* System.out.println(uniques+" > "+tables); */
                throw new SchemaGenException("More UNIQUEs than tables.");

            }

            //TODO validation for foriegnKeys, and checks



    }

    // compound key size, random if < 1;
    // NOTICE: primaryKeys now means the actual number of primary keys 
    // must be <= num of tables
    public static SchemaSpec randomSchema(int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, int uniques,int checks,int dataTypeSize, int compoundKeySize){

        validateSchema(tables,columns,notnulls,primaryKeys,foriegnKeys,uniques,checks);
        
        Random rand = new Random();
        ForiegnKeyGen fkg = new ForiegnKeyGen();

        // split up columns randomly
        int[] colSplitPoints = Util.groupSplitPoints(tables,columns);

        Arrays.sort(colSplitPoints);

        int curTable = 0;

        // randomly assign datatypes to columns
        // TODO take into account datatypesize
        int[] colDataType = new int[columns];

        // simply set all datatypesizes to the desired size
        int[] dataTypeSizeTable = new int[columns];

        for (int count = 0; count<columns; count++){
            if (count>colSplitPoints[curTable]-1)
                curTable++;
            
            colDataType[count] = rand.nextInt(DataTypeFactory.numSupportedTypes);

            dataTypeSizeTable[count] = (dataTypeSize > 0) ? dataTypeSize : rand.nextInt(MAX_DATATYPE_SIZE);
            
            // build tables to support making FKeys
            fkg.put(curTable,colDataType[count]);
        }

        // pick primary keys
        boolean[] pkeys = new boolean[columns];

       // finally, the uniques
       // TODO use a list of tables instead of random choosing
       // TODO have the generator keep track of table eligibility for compound keys
        for(int count = 0; count < primaryKeys; count++){
            PotentialUnique pu;
            int tries = 0;
            do{
                if (tries++ > 1.5*tables)
                    throw new SchemaGenException("Could not provide requested PRIMARY KEYs.");
                pu = fkg.getPotUnique(rand.nextInt(tables));
            }while(pu.getMaxSize()==0||compoundKeySize>0&&pu.getMaxSize()<compoundKeySize);
            int size = (compoundKeySize>0) ? compoundKeySize : rand.nextInt(pu.getMaxSize())+1;
            for (int c : pu.getUniqueBySize(size).columns)
                pkeys[c] = true;
        }

        // now pick which columns are not null
        // NOTICE notNulls cannot overlap with a primary key
        boolean[] notNulls = new boolean[columns];
        for (int count = 0; count < notnulls; count++){
            int randCol;
            do{
                randCol = rand.nextInt(columns);
            }while(notNulls[randCol]==true||pkeys[randCol]==true);
                notNulls[randCol] = true;
        }
        // FKEYS!

       ArrayList<ForiegnKeySpec> fkeys = new ArrayList<ForiegnKeySpec>();

       Iterator<PotentialForeignKey> pfks = fkg.potentialForeignKeys();
       
       for (int count = 0; count < foriegnKeys; count++){

           
           if (!pfks.hasNext()){
            /* System.out.println("Choked on fkey: "+count); */
           throw new SchemaGenException("Cannot provide requested FKeys "+count);
           }

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
       // TODO have the generator keep track of table eligibility for compound keys
        for(int count = 0; count < uniques; count++){
            PotentialUnique pu;
            int tries = 0;
            do{
                if (tries++ > 1.5*tables)
                    throw new SchemaGenException("Could not provide requested UNIQUES.");
                pu = fkg.getPotUnique(rand.nextInt(tables));
            }while(pu.getMaxSize()==0||compoundKeySize>0&&pu.getMaxSize()<compoundKeySize);
            int size = (compoundKeySize>0) ? compoundKeySize : rand.nextInt(pu.getMaxSize())+1;
            uqs.add(pu.getUniqueBySize(size));
        }

        // TODO  checks

        return new SchemaSpec(tables,colSplitPoints,colDataType,pkeys,notNulls,fkeys,uqs,dataTypeSizeTable);
        
    }

}
