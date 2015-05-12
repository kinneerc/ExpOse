package edu.allegheny.schemaexperiment.schemagen;

import java.util.*;

public class ForiegnKeySpec {
    List<Integer> sourceColumns;
    List<Integer> destColumns;

    public ForiegnKeySpec(List<Integer> scols, List<Integer> destColumns){
        this.sourceColumns = scols;
        this.destColumns = destColumns;
    }

    public List<Integer> getSourceColumns(){
        return sourceColumns;
    }
    public List<Integer> getDestColumns(){
        return destColumns;
    }
}
