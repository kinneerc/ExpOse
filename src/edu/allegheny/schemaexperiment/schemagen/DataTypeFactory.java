package edu.allegheny.schemaexperiment.schemagen;

import org.schemaanalyst.sqlrepresentation.datatype.*;

public class DataTypeFactory {

    public static final int numSupportedTypes = 2;

    public static BooleanDataType getBooleanDataType(){
        return (new BooleanDataType());
    }
    
    //TODO fix the length! allow choice or something...
    public static CharDataType getCharDataType(){
        return (new CharDataType(10));
    }

}
