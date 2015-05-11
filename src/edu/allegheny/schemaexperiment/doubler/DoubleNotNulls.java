package edu.allegheny.schemaexperiment.doubler;

import org.schemaanalyst.sqlrepresentation.*; //Import for Schema
import org.schemaanalyst.sqlrepresentation.constraint.*;

import java.util.*;


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
public class DoubleNotNulls implements SchemaDoubler{


	private Schema originalSchema; //Schema to be doubled

	public void setSchema(Schema input){
		originalSchema = input;

	}

	public void doubleSchema() throws SchemaDoublerException {

		Schema copy = originalSchema.duplicate();

		int numNotNulls = copy.getNotNullConstraints().size();

		for (int j = 0; j < (numNotNulls);){
			for (Table table : copy.getTables()){

				if (Metrics.columns(copy) == copy.getNotNullConstraints().size()){
					throw new SchemaDoublerException("All columns are Not Null"); 
					//If our current columns equals our current NotNullConstraints, don't make fake ones.
				}

				List<Column> GoodColumns = new ArrayList<Column>();
				List<String> BadColumns = new ArrayList<String>();

				for (NotNullConstraint NNC : copy.getNotNullConstraints()){
					if (table.getName().equals(NNC.getTable().getName())){
						BadColumns.add(NNC.getColumn().getName()); 
						//Iterate through the constraints and add the Column name of the current table to BadColumns list
					}
				}

				for (Column column : table.getColumns()){
					if (BadColumns.contains(column.getName())){
					    //For every column in the current table, check to see if it's bad or good.
					}
					else{
						GoodColumns.add(column);
						//If the name of the column does not appear in BadColums, create a new list of GoodColumns
					}
				}

				if (GoodColumns.size() > 0){
				    //If there are any good colums...
					Random rand = new Random();
					int ColumnsSize = GoodColumns.size();
					int random = rand.nextInt(ColumnsSize);
					//Get a random integer from 1 - #of GoodColumns
					int ColumnPicker = random % ColumnsSize;
					//I like to use modulo in my programs #professional
					if (j < numNotNulls){
						if (BadColumns.contains(GoodColumns.get(ColumnPicker).getName())){
						    //Used as an extra safety net to make sure nothing is fake
						}
						else{
							copy.createNotNullConstraint(Integer.toString(j), table, GoodColumns.get(ColumnPicker));
							//Create a non-fake NotNullConstraint
							j++;
						}
					}
				}
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


