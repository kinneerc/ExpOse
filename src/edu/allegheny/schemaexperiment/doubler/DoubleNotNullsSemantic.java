package edu.allegheny.schemaexperiment.doubler;


import org.schemaanalyst.sqlrepresentation.*; //Import for Schema
import org.schemaanalyst.sqlrepresentation.constraint.*;


/**
 *   
 *  
 * @author Luke Smith
 */
public class DoubleNotNullsSemantic implements SchemaDoubler{


	private Schema originalSchema; //Schema to be doubled

	public void setSchema(Schema input){
		originalSchema = input;

	}
	public void doubleSchema(){

		Schema copy = originalSchema.duplicate();

		for (NotNullConstraint constraint : originalSchema.getNotNullConstraints()){
			copy.addNotNullConstraint(constraint);
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

