package edu.allegheny.schemaexperiment.schemagen;

import org.schemaanalyst.sqlrepresentation.*;
import org.schemaanalyst.sqlrepresentation.datatype.*;
import java.util.*;
import java.lang.reflect.*;

public class Generator{
public Schema generate(int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, int checks, int uniques){

    Schema ans = new Schema("rs");

    // determine how columns will be assigned to tables
    // index = table, value = num of columns
    int[] map = new int[tables];


    // add a table to the schema for each table
    for (int t = 0; t < tables; t++){
        Table cur = ans.createTable("rt");
        // for each table, create the decided number of columns
        // add a column to the schema for each col
        for (int c = 0; c < map[t]; c++){
            cur.createColumn("rc",randDataType());
        }

    }

    return ans;

}

private int[] group(int groups, int individuals){

    int ans[]= new int[groups];

    ArrayList<Integer> splitpoints = groupSplitPoints(groups,individuals);

    // given a list of split points, calculate population for each group
    int gi = 0;
    int ii = 0;
    for (int sp : splitpoints){
        ans[gi++] = sp - ii;
        ii = sp;
    }
    // assign anything after last sp to final group
    ans[gi] = individuals - ii;

    return ans;

}

private ArrayList<Integer> groupSplitPoints(int groups,int individuals){

    ArrayList<Integer> ans = new ArrayList<Integer>();

    Random rand = new Random();
    for (int i = 0; i < groups; i++){
        ans.add(rand.nextInt(groups));
    }

    Collections.sort(ans);

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
