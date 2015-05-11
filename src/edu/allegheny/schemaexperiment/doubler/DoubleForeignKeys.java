package edu.allegheny.schemaexperiment.doubler;

import org.schemaanalyst.sqlrepresentation.*; //Import for Schema
import org.schemaanalyst.sqlrepresentation.constraint.*;

import java.util.*;

/**
 * This program will double ForeignKeys, only.
 * It cycles through the tables and randomly picks a column that is not assigned a Forein Key Constraint in that table
 * A Foreign Key Constraint is then created on that column and maps it to a random column in the table.
 * If there is no column in the table that does not have a Foreign Key constraint, it goes to the next table.
 * If no columns in the Schema are not Foreign Key Constraints, then it throws an exception.
 *
 *
 * @author Luke Smith
 */
public class DoubleForeignKeys implements SchemaDoubler{


	private Schema originalSchema; //Schema to be doubled

	public void setSchema(Schema input){
		originalSchema = input;

	}

	public void doubleSchema() throws SchemaDoublerException {

		Schema copy = originalSchema.duplicate();

		int numForKeys = copy.getForeignKeyConstraints().size();
		List<Column> AllColumns = new ArrayList<Column>();

		for (Table table : copy.getTables()){
			for (Column column : table.getColumns()){
				AllColumns.add(column);
			}
		}

		for (int j = 0; j < (numForKeys);){
			for (Table table : copy.getTables()){

				if (Metrics.columns(copy) == copy.getForeignKeyConstraints().size()){
					throw new SchemaDoublerException("All columns are Foreign Key"); 
					//If our current columns equals our current Foreign Key Constraints, don't make fake ones.
				}

				List<Column> GoodColumns = new ArrayList<Column>();
				List<String> BadColumns = new ArrayList<String>();

				for (ForeignKeyConstraint FKC : copy.getForeignKeyConstraints()){
					if (table.getName().equals(FKC.getTable().getName())){
						for (Column column : FKC.getColumns()){
							BadColumns.add(column.getName()); 
							//Iterate through the constraints and add the Column name of the current table to BadColumns list
						}
					}
				}

				for (Column column : table.getColumns()){
					if (BadColumns.contains(column.getName())){
						//For every column in the current table, check to see if it's bad or good.
					}else{
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
						if (BadColumns.contains(GoodColumns.get(ColumnPicker).getName())){
							//Used as an extra safety net to make sure nothing is fake
						}
						else{
							int random2 = rand.nextInt(AllColumns.size());
							Column refCol = AllColumns.get(random2);
							while (table.hasColumn(refCol)){
								random2 = rand.nextInt(AllColumns.size());
								refCol = AllColumns.get(random2);
							}
							for (Table table2 : copy.getTables()){
								if (table2.hasColumn(refCol)){
									if (j < numForKeys){
										//if (GoodColumns.get(ColumnPicker).getDataType() == refCol.getDataType())
										//{
									copy.createForeignKeyConstraint(Integer.toString(j), table, GoodColumns.get(ColumnPicker), table2, refCol);
									//Create a non-fake ForeignKeyConstraint and map it to a random column in the table.
									j++;
									break;
									//}
									}
							}
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
		return Metrics.fKeys(originalSchema);
	}

}


