package edu.allegheny.schemaexperiment.doubler;

import org.schemaanalyst.sqlrepresentation.*; //Import for Schema
import org.schemaanalyst.sqlrepresentation.constraint.*;


/**
 *
 *
 * @author Luke Smith
 */
public class DoubleUniquesSemantic implements SchemaDoubler{


	private Schema originalSchema; //Schema to be doubled

	public void setSchema(Schema input){
		originalSchema = input;

	}

	public void doubleSchema() throws SchemaDoublerException {

		Schema copy = originalSchema.duplicate();

		for (UniqueConstraint constraint : originalSchema.getUniqueConstraints()){
			copy.addUniqueConstraint(constraint);
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


