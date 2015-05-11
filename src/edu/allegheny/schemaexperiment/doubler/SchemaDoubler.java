package edu.allegheny.schemaexperiment.doubler;

import org.schemaanalyst.sqlrepresentation.Schema;

/**
 * This interface defines how a class that systematically doubles a schema should behave
 *
 * @author Cody Kinneer
 */
public interface SchemaDoubler{

    /**
     * Should return this classes current schema state
     * @return The current schema
     */
		Schema getSchema()	;

    /**
     * This method changes the classes schema to the one specified.
     * Should be used for initializing the doubler
     * @param input The schema to set.
     */
    void setSchema(Schema input);

    /**
     * This method double's this classes schema.
     * The doubled schema should be accessed by the {@link #getSchema() getSchema} method
     */
    void doubleSchema() throws SchemaDoublerException;

    /**
     * This method should give information on the size of the parameter under study.
     * ie. DoubleNotNulls should return the number of not nulls in the current schema
     * @return The size of N
     */
    int getN();

}
