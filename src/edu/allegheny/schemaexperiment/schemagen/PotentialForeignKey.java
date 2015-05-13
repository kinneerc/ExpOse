package edu.allegheny.schemaexperiment.schemagen;

import java.util.*;
import java.util.Map.Entry;

import edu.allegheny.schemaexperiment.schemagen.ForiegnKeyGen.Column;

public class PotentialForeignKey {

    int source;
    int dest;

    // how many of each datatype will match
    // datatype , availible
    Map<Integer,Integer> matchTable;
    // stores columns by datatype for source and dest tables
    HashMap<Integer,List<Column>> sCols;
    HashMap<Integer,List<Column>> dCols;

    public PotentialForeignKey(int source, int dest, Map<Integer,Integer> matchTable,HashMap<Integer,List<Column>> sourceCols,HashMap<Integer,List<Column>> destCols){
        this.source = source;
        this.dest = dest;
        this.matchTable = matchTable;
        this.sCols = sourceCols;

        // this is cloned so that we can remove Columns locally without
        // affecting the ForeignKeyGen
        this.dCols = (HashMap<Integer,List<Column>>) destCols.clone();
    }

    public int getMaxSize(){
        int sum = 0;
        for (Integer t : matchTable.keySet()){
            sum += matchTable.get(t);
        }
        return sum;
    }

    /**
     * Returns a foreign key of a desired number of columns
     */
    public ForiegnKeySpec getBySize(int numCols){

        ArrayList<Integer> scols = new ArrayList<Integer>();
        ArrayList<Integer> dcols = new ArrayList<Integer>();

        // put source columns into a list
        ArrayList<Column> sourceColumns = new ArrayList<Column>();

        for(Entry<Integer, List<Column>> e : sCols.entrySet()){
            if(matchTable.get(e.getKey())>0)
            sourceColumns.addAll(e.getValue());
        }

        Collections.shuffle(sourceColumns);

        // for each requested column
        for (int count = 0; count < numCols; count++){

            // choose a source column
        
            Column source;
            
            source = sourceColumns.get(count);
            
            int matchValue;
            while((matchValue=matchTable.get(source.datatype))==0){
            	source = sourceColumns.get(++count);
            }
            
            // when source is taken, decrement matchTable
            matchTable.put(source.datatype, --matchValue);
            
            
            // mark this column is being in a key with the dest table
            // this means we wont use it again if we want another
            // foreign key from source to dest
            source.inKey(dest);
            scols.add(sourceColumns.get(count).name);

            // now choose a matching dest column
            int datatype = source.datatype;

            ArrayList<Column> potentialDest = new ArrayList<Column>();
            potentialDest.addAll(dCols.get(datatype));
            Collections.shuffle(potentialDest);

            Column dest = potentialDest.get(0);

            dcols.add(dest.name);

            // we cannot choose this column as a dest again, 
            // so we must remove it from the list of choices
            potentialDest.remove(0);

            // now we'll overwrite the value in the table to preserve
            // this information.  This won't mess up ForeignKeyGen
            // because we've alredy cloned the table
            dCols.put(datatype,potentialDest);
        }

        // now that we've add all of the columns, return a ForeignKeySpec
        return new ForiegnKeySpec(scols,dcols);
    }

}
