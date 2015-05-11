package edu.allegheny.schemaexperiment.doubler;


import org.schemaanalyst.sqlrepresentation.*; //Import for Schema
import org.schemaanalyst.mutation.analysis.util.SchemaMerger; //Import for Merger
import org.schemaanalyst.sqlrepresentation.constraint.ForeignKeyConstraint;
import org.schemaanalyst.sqlrepresentation.constraint.UniqueConstraint;
import org.schemaanalyst.sqlrepresentation.constraint.NotNullConstraint;
import org.schemaanalyst.sqlrepresentation.constraint.CheckConstraint;



//The proper way to use this class is to setSchema(<SchemaToBeDoubled>);
//Then, doubleSchema()
//Then, getSchema() will return the doubled Schema

/**
 *   
 *  
 * @author Luke Smith
 */
public class DoubleTablesColumnsSemantic implements SchemaDoubler{


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
			copy.getTables().get(i).setName("x" + copy.getTables().get(i).getName() + "c");
		}

		for (CheckConstraint constraint : copy2.getCheckConstraints()){
			copy2.removeCheckConstraint(constraint);
		}

		for (ForeignKeyConstraint constraint : copy2.getForeignKeyConstraints()){
			copy2.removeForeignKeyConstraint(constraint);
		}

		for (NotNullConstraint constraint : copy2.getNotNullConstraints()){
			copy2.removeNotNullConstraint(constraint);
		}

		for (Table table : copy2.getTables()){
			copy2.removePrimaryKeyConstraint(table);
		}

		for (UniqueConstraint constraint : copy2.getUniqueConstraints()){
			copy2.removeUniqueConstraint(constraint);
		}

		originalSchema = SchemaMerger.merge(copy2, copy); //Merge the two

	}


	public Schema getSchema(){
		return originalSchema;
	}

	public int getN(){
	return (Metrics.columns(originalSchema) + Metrics.tables(originalSchema));
	}

}



