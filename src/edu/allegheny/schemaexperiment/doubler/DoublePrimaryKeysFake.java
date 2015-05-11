package edu.allegheny.schemaexperiment.doubler;

import org.schemaanalyst.sqlrepresentation.*; //Import for Schema
import org.schemaanalyst.sqlrepresentation.constraint.*;
import java.util.*;


/**
 *
 *
 * @author Luke Smith
 */
public class DoublePrimaryKeysFake implements SchemaDoubler{


	private Schema originalSchema; //Schema to be doubled

	public void setSchema(Schema input){
		originalSchema = input;

	}

	public void doubleSchema() throws SchemaDoublerException {

		Schema copy = originalSchema.duplicate();

		int numTables = copy.getTables().size();
		int numColumns = Metrics.columns(originalSchema);
		int numPrimKeys = Metrics.pKeys(originalSchema);
		Schema blank = new Schema("blank");

		for (int i = 0; i < (numTables); i++){
			Table newTable = new Table(Integer.toString(i));
			blank.addTable(newTable);
		}
		for (int z = 0; z < (numTables); z++){
			blank.getTables().get(z).setName(copy.getTables().get(z).getName());
		}

				int count = 0;
		for (int j = 0; j < (numColumns); j++){
			int tableNumber = count % (numTables);
			String copyTableName = copy.getTables().get(tableNumber).getName();
			if (copy.getTable(copyTableName).getColumns().size() > 0){
				int numColumnsInSaidTable = count % copy.getTable(copyTableName).getColumns().size();

				blank.getTables().get(tableNumber).addColumn(copy.getTable(copyTableName).getColumns().get(numColumnsInSaidTable));
				count = count + 1;
			}
			else if (copy.getTable(copyTableName).getColumns().size() <= 0){
				j = j -1;
				count = count + 1;
			}


		}

		
		for (CheckConstraint constraint : originalSchema.getCheckConstraints()){
			blank.addCheckConstraint(constraint);
		}

		for (ForeignKeyConstraint constraint : originalSchema.getForeignKeyConstraints()){
			blank.addForeignKeyConstraint(constraint);
		}

		for (NotNullConstraint constraint : originalSchema.getNotNullConstraints()){
			blank.addNotNullConstraint(constraint);
		}

		/* count = 0; */
		/* for (PrimaryKeyConstraint constraint : originalSchema.getPrimaryKeyConstraints()){ */
		/* 	Table table = originalSchema.getTables().get(count % originalSchema.getTables().size()); */
		/* 	blank.createPrimaryKeyConstraint("f"+constraint.getName()+"g"+count, table , table.getColumns().get(count % table.getColumns().size()));			 */
		/* } */
		
		/* for (PrimaryKeyConstraint constraint : originalSchema.getPrimaryKeyConstraints()){ */
		/* 	blank.createPrimaryKeyConstraint("e"+constraint.getName()+"h", constraint.getTable(), constraint.getColumns());			 */
		/* } */


		ArrayList<Column> Cols = new ArrayList<Column>();
		for (Table table : blank.getTables()){
			for (Column column : table.getColumns()){
				Cols.add(column);
			}
		}
int l = 0;
	outer:
		for (int j=0; j < numPrimKeys*2;){
		for (Table table : blank.getTables()){
			for (Column column : table.getColumns()){
				if (l >= 400){
					j = 10000;
					break outer;
				}

					else if (j < numPrimKeys){
				blank.createPrimaryKeyConstraint(Integer.toString(count), table, column);
				j=Metrics.pKeys(blank);
				l++;
				}
			}
		}
		}


		for (UniqueConstraint constraint : originalSchema.getUniqueConstraints()){
			blank.addUniqueConstraint(constraint);
		}


		originalSchema = blank;
	}


	public Schema getSchema(){
		return originalSchema;
	}

	public int getN(){
		return Metrics.fKeys(originalSchema);
	}

}


