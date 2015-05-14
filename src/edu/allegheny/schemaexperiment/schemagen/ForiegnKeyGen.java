package edu.allegheny.schemaexperiment.schemagen;

import java.util.*;

/**
 * This class will help generate foriegn keys by keeping track of
 * what foreign keys are possible / already taken
 *
 * note, we will only allow a column to be involved in a single fkey
 * going from tab 1 - tab 2
 */
public class ForiegnKeyGen {

    int columnCount;

    // stores datatype frequency for each table
    HashMap<Integer,HashMap<Integer,List<Column>>> typeTables;

    // stores table graph for dependency detection
    ArrayList<Table> graph;

    public ForiegnKeyGen(){
        typeTables = new HashMap<Integer,HashMap<Integer,List<Column>>>();
        graph = new ArrayList<Table>();
        columnCount = 0;
    }

    public List<PotentialForeignKey> getAllPotFKeys(){
        ArrayList<PotentialForeignKey> pfkeys = new ArrayList<PotentialForeignKey>();

        // consider all table sources and destinations
        for (int s : typeTables.keySet()){
            for (int d : typeTables.keySet()){
                // cant have an FKey to itself
                if(s != d){
                                    

                PotentialForeignKey pfk = getPotFKey(s,d);
                if(pfk.getMaxSize()>0)
                    pfkeys.add(pfk);
                }

            }
        }

        return pfkeys;
    }

    public PotentialForeignKey getPotFKey(int source, int dest){

        return new PotentialForeignKey(source,dest,graph,getMatchTab(source,dest),typeTables.get(source), typeTables.get(dest));

    }

    /**
     * Returns a type table of the intersection of the match tables
     * Also, this should be expressed as a map to remove empty rows
     */
    public Map<Integer,Integer> getMatchTab(int source, int dest){

        HashMap<Integer,Integer> ans = new HashMap<Integer,Integer>();

        HashMap<Integer,List<Column>> sTab = typeTables.get(source);
        HashMap<Integer,List<Column>> fTab = typeTables.get(dest);

        // trim sTab by availibility
        HashMap<Integer,List<Column>> sTabSA = new HashMap<Integer,List<Column>>();

        for (Integer x : sTab.keySet()){
            List<Column> cols = sTab.get(x);
            ArrayList<Column> aCols = new ArrayList<Column>();

            for (int count = 0; count < cols.size(); count++){
                Column c = cols.get(count);
            if (c.availible(dest))
                aCols.add(c);
            }

            sTabSA.put(x,aCols);
        }


        // now use the map of availible columns to produce match table
        for (Integer x : sTabSA.keySet()){
            int sSize, dSize;
            List<Column> lcol = sTabSA.get(x);
            if (lcol==null){
                sSize = 0;
            }else{
                sSize = lcol.size();
            }
             lcol = fTab.get(x);
            if (lcol==null){
                dSize = 0;
            }else{
                dSize = lcol.size();
            }

            ans.put(x,Math.min(sSize,dSize));
        }

        return ans;
    }

    public void put(int table,int datatype){

        if(graph.size()<table+1){
            Table t = new Table(table);
            graph.add(t);
        }

        if(!typeTables.containsKey(table))
             typeTables.put(table,new HashMap<Integer,List<Column>>());
        

        HashMap<Integer,List<Column>> typeTable = typeTables.get(table);

        if (!typeTable.containsKey(datatype))
            typeTable.put(datatype,new ArrayList<Column>());

        Column ccol = new Column(table);
        ccol.datatype = datatype;
        ccol.name = columnCount++;

        typeTable.get(datatype).add(ccol);

       }

    public static class Table {
        public int name;
        public ArrayList<Table> connections;

        public Table(int name){
        	this.name = name;
            connections = new ArrayList<Table>();
        }

        @SuppressWarnings("unchecked")
        public Table clone(){
            Table t = new Table(name);
            t.connections = (ArrayList<Table>) connections.clone();
            return t;
        }
    }

    public static class Column {
        public int table;
        public int datatype;
        public int name;
        // store all of the tables that this column points to as an fkey 
        public HashSet<Integer> tabDests;

        public Column(int table){
            tabDests = new HashSet<Integer>();
            this.table = table;
        }

        public boolean availible(int dest){
            return !tabDests.contains(dest);
        }

        public void inKey(Column dest){
            tabDests.add(dest.table);
        }

    }

}
