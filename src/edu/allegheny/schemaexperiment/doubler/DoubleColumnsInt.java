package edu.allegheny.schemaexperiment.doubler;

import org.schemaanalyst.sqlrepresentation.*; //Import for Schema
import org.schemaanalyst.sqlrepresentation.datatype.*;

/**
 * This program will double Columns, only.
 * First, it copies the original schema.
 * Then, for every table in the schema, and for every column in the table, it will create
 * a new column with an Int datatype.
 *
 * If table 1 has 3 columns and table 2 has 5 columns,
 * After double, table 1 will have 6 columns and table 2 will have 10.
 *
 * If table 1 has datatypes abc and table 2 has datatypes efghi,
 * After double, table 1 will have 6 int datatypes and table 2 will have 10 int datatypes. 
 *
 * @author Luke Smith
 */
public class DoubleColumnsInt implements SchemaDoubler{
	private Schema originalSchema; //Schema to be doubled

	public void setSchema(Schema input){
		originalSchema = input;
	}

	public void doubleSchema(){
		Schema copy2 = originalSchema.duplicate();
		IntDataType intDataType = new IntDataType();
		

		int count = 0;
		for (Table table : copy2.getTables()){//For every table in the Schema
			for (Column column : table.getColumns()){//For every column in the table
				table.createColumn("b"+Integer.toString(count)+"c", //createcolumn
					            intDataType); //makes datatype Int
				count++;
				column.setName("x"+column.getName()+"o"); //Set name for merging conflicts
			}
		}
		count = 0;
		for (Table table : copy2.getTables()){ //Set name for merging conficts
			for (Column column : table.getColumns()){
				column.setName(Integer.toString(count)+column.getName());
				count++;
			}
		}
		originalSchema = copy2;
	}

	public Schema getSchema(){
		return originalSchema;
	}

	public int getN(){
		return Metrics.columns(originalSchema);
	}

}
