package edu.allegheny.schemaexperiment.schemagen;

import java.util.*;
import java.util.Map.Entry;

import edu.allegheny.schemaexperiment.schemagen.ForiegnKeyGen.Column;
import edu.allegheny.schemaexperiment.schemagen.ForiegnKeyGen.Table;

public class PotentialForeignKey {

    int source;
    int dest;

    // how many of each datatype will match
    // datatype , availible
    Map<Integer,Integer> matchTable;
    // stores columns by datatype for source and dest tables
    HashMap<Integer,List<Column>> sCols;
    HashMap<Integer,List<Column>> dCols;

    // stores table dependencies to avoid cycles
    ArrayList<Table> graph;

    @SuppressWarnings("unchecked")
    public PotentialForeignKey(int source, int dest, ArrayList<Table> graph, Map<Integer,Integer> matchTable,HashMap<Integer,List<Column>> sourceCols,HashMap<Integer,List<Column>> destCols){
        this.source = source;
        this.dest = dest;
        this.matchTable = matchTable;
        this.sCols = sourceCols;
        this.graph = graph;

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
    
    public Table getTable(int tab){
        Table ans;

        ans = graph.get(tab);
        
        return ans;
    }

    /**
     * Perform a DFS while looking for cycles
     */
    public boolean createsCycle(){
        @SuppressWarnings("unchecked")
        ArrayList<Table> hypoG = (ArrayList<Table>) graph.clone();

        Table tempSource = hypoG.get(source).clone();
        tempSource.connections.add(hypoG.get(dest));
        
        hypoG.remove(source);
        hypoG.add(source,tempSource);

       return (new CycleSearch(hypoG)).hasCycle();
    }



    public class CycleSearch {
    private boolean[] marked;        
    private int[] edgeTo;            
    private boolean[] onStack;       
    private Stack<Integer> cycle;   
    public CycleSearch(ArrayList<Table> G) {
        marked  = new boolean[G.size()];
        onStack = new boolean[G.size()];
        edgeTo  = new int[G.size()];
        for (int v = 0; v < G.size(); v++)
            if (!marked[v]) dfs(G, v);
    }

    private void dfs(ArrayList<Table> G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (Table w : G.get(v).connections) {

            // short circuit if directed cycle found
            if (cycle != null) return;

            //found new vertex, so recur
            else if (!marked[w.name]) {
                edgeTo[w.name] = v;
                dfs(G, w.name);
            }

            // trace back directed cycle
            else if (onStack[w.name]) {
                cycle = new Stack<Integer>();
                for (int x = v; x != w.name; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w.name);
                cycle.push(v);
            }
        }

        onStack[v] = false;
    }

    public boolean hasCycle() {
        return cycle != null;
    }


}

    /**
     * Returns a foreign key of a desired number of columns
     */
    public ForiegnKeySpec getBySize(int numCols){

        if (createsCycle())
            throw new SchemaGenException("Adding this FKey would create a cycle.");

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

            // now choose a matching dest column
            int datatype = source.datatype;

            ArrayList<Column> potentialDest = new ArrayList<Column>();
            potentialDest.addAll(dCols.get(datatype));
            Collections.shuffle(potentialDest);

            Column dest;
            
            dest = potentialDest.remove(0);
              
            dcols.add(dest.name);

            // now we'll overwrite the value in the table to preserve
            // this information.  This won't mess up ForeignKeyGen
            // because we've alredy cloned the table
            dCols.put(datatype,potentialDest);

            // when source is taken, decrement matchTable
            matchTable.put(source.datatype, --matchValue);

            // mark the source column is being in a key with the dest 
            // this means we wont use it again if we want another
            // foreign key from source to dest
            source.inKey(dest);
            scols.add(sourceColumns.get(count).name);
        }
        
        // now update the recieves map for cycle detection
        Table sourceT = getTable(this.source);
        sourceT.connections.add(getTable(this.dest));
        
        System.out.println(this.source+"=>"+this.dest);

        // now that we've add all of the columns, return a ForeignKeySpec
        return new ForiegnKeySpec(scols,dcols);
    }

}
