package edu.allegheny.schemaexperiment.doubler;


import org.schemaanalyst.sqlrepresentation.*; //Import for Schema
import org.schemaanalyst.mutation.analysis.util.SchemaMerger; //Import for Merger

/**
 * This program will double everything.
 * First, it copies the original schema.
 * Then, it copies the original again.
 * Then renames the tables so they can be merged without overlapping.
 * Then we merge the two.
 *
 * @author Luke Smith
 */
public class DoubleAllSemantic implements SchemaDoubler{


    private Schema originalSchema; //Schema to be doubled

    public void setSchema(Schema input){
        originalSchema = input;
    }

    public void doubleSchema(){

        Schema copy = originalSchema.duplicate();
        Schema copy2 = originalSchema.duplicate();

        for (int j = 0; j < copy2.getTables().size(); j++){
            copy2.getTables().get(j).setName("b" + copy2.getTables().get(j).getName() + "o");
        }

        for (int i = 0; i < copy.getTables().size(); i++){
            copy.getTables().get(i).setName("x" + copy2.getTables().get(i).getName() + "c");
        }

        originalSchema = SchemaMerger.merge(copy2, copy); //Merge the two
    }

    public Schema getSchema(){
        return originalSchema;
    }

    public int getN(){
        return (Metrics.columns(originalSchema) + Metrics.constraints(originalSchema) + Metrics.tables(originalSchema));
    }
}
