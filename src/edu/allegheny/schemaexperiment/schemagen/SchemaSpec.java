package edu.allegheny.schemaexperiment.schemagen;

import org.schemaanalyst.sqlrepresentation.*;
import org.schemaanalyst.sqlrepresentation.datatype.*;

import java.util.*;
import java.lang.reflect.*;

public class SchemaSpec{
    private int tables;
    private int[] columns;
    private int[] datatypes;
    private int[] dataTypeSizeTable;
    private boolean[] primaryKey;
    private boolean[] notNull;

    private List<ForiegnKeySpec> foriegnKey;

    private List<UniqueSpec> unique;

    public SchemaSpec(int tables,int[] colSplitPoints,int[] datatypes, boolean[] primaryKey, boolean[] notNull, List<ForiegnKeySpec> fkeys, List<UniqueSpec> unique, int[] dataTypeSizeTable){
        this.tables = tables;
        this.columns = colSplitPoints;
        this.datatypes = datatypes;
        this.primaryKey = primaryKey;
        this.notNull = notNull;
        this.foriegnKey = fkeys;
        this.unique = unique;
        this.dataTypeSizeTable = dataTypeSizeTable;
    }

    // how to handle check constraints?
    
    /**
     * Instantiates a schema object from the specification
     */
    public Schema getSchema(){
        Schema ans = new Schema("SchemaInstance");

        ArrayList<Column> allColumns = new ArrayList<Column>();

        // store all of the columns for constraints
        HashMap<Column,Table> tabLookup = new HashMap<Column,Table>();

        // convert list of table boundries to a frequency table 
        // of how many columns are in each table
        int[] columnsPtable = Util.group(columns);

        int curCol = 0;

        // add a table to the schema for each table
        for (int t = 0; t < tables; t++){
            // add the current table
            Table curTab = ans.createTable("Table "+t);
            // for each table, process every column 
            // first, create some lists for constraints to which columns may belong
            ArrayList<Column> pkey = new ArrayList<Column>();
            for (int c = 0; c < columnsPtable[t]; c++){
                Column currentColumn = curTab.createColumn("Column "+curCol,dataType(datatypes[curCol],dataTypeSizeTable[curCol]));
                tabLookup.put(currentColumn,curTab);
                allColumns.add(currentColumn);
                // check if this column belongs to a primary key
                if(primaryKey[curCol]){
                    pkey.add(currentColumn);
                }
                // check if this column is not null
                if(notNull[curCol]){
                    ans.createNotNullConstraint(curTab, currentColumn);
                }
                curCol++;
            }
            // create the primary key if needed
            if (pkey.size() > 0){
                ans.createPrimaryKeyConstraint(curTab,pkey);
            }

        }

        // now do the hard constraints...
        // first, uniques
        for (UniqueSpec u : unique){
            ArrayList<Column> cols = new ArrayList<Column>();
            if (u.columns.size()>0){
            Table t = tabLookup.get(allColumns.get(u.getColumns().get(0)));
            for (Integer c : u.getColumns()){
                Column curColumn = allColumns.get(c);
                if (t != tabLookup.get(curColumn))
                    throw new SchemaGenException("Cannot create UNIQUE across tables.");
                cols.add(curColumn);
            }
            ans.createUniqueConstraint(t,cols);
            }else{
                 throw new SchemaGenException("Illegal UniqueSpec.");
            }
        }

        // now fkeys
        for (ForiegnKeySpec f : foriegnKey){
            if (f.getSourceColumns().size()>0 && f.getDestColumns().size()>0){
                ArrayList<Column> sc = new ArrayList<Column>();
                Table st = tabLookup.get(allColumns.get(f.getSourceColumns().get(0)));
            for (Integer s : f.getSourceColumns()){
                Column curColumn = allColumns.get(s);
                 if (st != tabLookup.get(curColumn)){
                     System.err.println(f);
                     for (int count = 0; count < columnsPtable.length; count++){
                         System.err.print(columnsPtable[count]);
                     }
                     throw new SchemaGenException("Cannot create compound fkey across tables.");
                 }

                 sc.add(curColumn);
                 
            }
                ArrayList<Column> dc = new ArrayList<Column>();
                Table dt = tabLookup.get(allColumns.get(f.getDestColumns().get(0)));
            for (Integer d : f.getDestColumns()){
                Column curColumn = allColumns.get(d);
                 if (dt != tabLookup.get(curColumn))
                     throw new SchemaGenException("Cannot create compound fkey across tables.");

                 dc.add(curColumn);
                 
            }

            ans.createForeignKeyConstraint(st,sc,dt,dc);

            }else{
                 throw new SchemaGenException("Illegal ForiegnKey Spec.");
            }

            //TODO checks
        }


        return ans;
    }

    /**
     * Provides a SchemaAnalyst DataType object from an int value
     * note that typeID must be in range of 0 - # of methods in DataTypeFactory-1
     */
    private static DataType dataType(int typeID,int size){
        Class<? extends DataTypeFactory> f = (new DataTypeFactory()).getClass();
        Method methods[] = f.getMethods();

        try{
            Object dt;
            if(methods[typeID].getParameterTypes().length==1)
                dt = methods[typeID].invoke(null,size);
            else if (methods[typeID].getParameterTypes().length==2)
                dt = methods[typeID].invoke(null,size,size); 
            else
                dt = methods[typeID].invoke(null);

            return (DataType) dt;
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(InvocationTargetException e){
            e.printStackTrace();
        }

        throw new SchemaGenException("Provided invalid datatype ID.");

    }

}
