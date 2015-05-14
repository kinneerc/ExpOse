package edu.allegheny.schemaexperiment.schemagen;

import java.util.*;

import edu.allegheny.schemaexperiment.schemagen.ForiegnKeyGen.Column;

public class PotentialUnique {
    public int table;
    public List<Column> cols;

    public PotentialUnique(int t, List<Column> c){
        this.table = t;
        this.cols = c;
    }

    public int getMaxSize(){
        return cols.size();
    }

    public UniqueSpec getUniqueBySize(int size){
        Collections.shuffle(cols);

        ArrayList<Integer> ans = new ArrayList<Integer>();

        for (int count = 0; count < size; count++){
            ans.add(cols.remove(0).name);
        }

        return new UniqueSpec(ans);
    }
}
