package edu.allegheny.schemaexperiment.doubler;


import org.schemaanalyst.sqlrepresentation.*; //Import for Schema
import org.schemaanalyst.sqlrepresentation.constraint.*;


//The proper way to use this class is to setSchema(<SchemaToBeDoubled>);
//Then, doubleSchema()
//Then, getSchema() will return the doubled Schema

/**
 *   
 *  
 * @author Luke Smith
 */
public class DoubleTables implements SchemaDoubler{


	private Schema originalSchema; //Schema to be doubled

	public void setSchema(Schema input){
		originalSchema = input;

	}
	public void doubleSchema(){

		Schema copy = originalSchema.duplicate();

		int numColumns = 0;
		for (Table table : copy.getTables()){
			for (Column column : table.getColumns()){
				numColumns++;
			}
		}

		int numTables = copy.getTables().size();
		Schema blank = new Schema("blank");

		for (int i = 0; i < (numTables*2); i++){
			Table newTable = new Table(Integer.toString(i));
			blank.addTable(newTable);
		}
		for (int z = 0; z < (numTables); z++){
			blank.getTables().get(z).setName(copy.getTables().get(z).getName());
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

		for (PrimaryKeyConstraint constraint : originalSchema.getPrimaryKeyConstraints()){
			blank.createPrimaryKeyConstraint(constraint.getName(), constraint.getTable(), constraint.getColumns());			
		}

		for (UniqueConstraint constraint : originalSchema.getUniqueConstraints()){
			blank.addUniqueConstraint(constraint);
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
			originalSchema = blank; //Merge the two

	}

	public Schema getSchema(){
		return originalSchema;
	}

	public int getN(){
	return Metrics.tables(originalSchema);
	}

}

