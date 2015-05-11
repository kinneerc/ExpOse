package edu.allegheny.schemaexperiment.doubler;

import org.schemaanalyst.sqlrepresentation.*; //Import for Schema
import org.schemaanalyst.sqlrepresentation.constraint.*;

import java.util.*;




//The proper way to use this class is to setSchema(<SchemaToBeDoubled>);
//Then, doubleSchema()
//Then, getSchema() will return the doubled Schema

/**
 * This program will double NotNullConstrains, only.
 * It cycles through the tables and randomly picks a column that is not assigned a not null constraint in that table
 * A not null constraint is created one on that column.
 * If there is no column in the table that does not have a not null constraint, it goes to the next table.
 * If no columns in the Schema are not Not Null, then it throws an exception(hopefully)
 *
 *
 * @author Luke Smith
 */
public class DoublePrimaryKeys implements SchemaDoubler{


	private Schema originalSchema; //Schema to be doubled

	public void setSchema(Schema input){
		originalSchema = input;

	}

	public void doubleSchema() {

		Schema copy = originalSchema.duplicate();

		int numPrimaryKeys = copy.getPrimaryKeyConstraints().size();
		for (Table table : copy.getTables()){
			for (Column column : table.getColumns()){
		copy.createPrimaryKeyConstraint(table, column);
			}
		}

		originalSchema = copy;
	}


	public Schema getSchema(){
		return originalSchema;
	}

	public int getN(){
		return Metrics.notNulls(originalSchema);
	}

}


