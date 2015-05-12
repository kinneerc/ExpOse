package edu.allegheny.schemaexperiment.schemagen;

import java.util.*;

public class UniqueSpec {
    List<Integer> columns;

    public UniqueSpec(List<Integer> cols){
        this.columns = cols;
    }

    public List<Integer> getColumns(){
        return columns;
    }
}
