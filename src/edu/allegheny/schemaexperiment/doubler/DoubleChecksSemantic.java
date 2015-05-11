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
public class DoubleChecksSemantic implements SchemaDoubler{


	private Schema originalSchema; //Schema to be doubled

	public void setSchema(Schema input){
		originalSchema = input;

	}
	public void doubleSchema(){

		Schema copy = originalSchema.duplicate();

		
		for (CheckConstraint constraint : originalSchema.getCheckConstraints()){
			copy.addCheckConstraint(constraint);
		}

			originalSchema = copy; //Merge the two

	}

	public Schema getSchema(){
		return originalSchema;
	}

	public int getN(){
	return Metrics.tables(originalSchema);
	}

}

