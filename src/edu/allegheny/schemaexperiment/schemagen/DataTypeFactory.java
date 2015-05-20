package edu.allegheny.schemaexperiment.schemagen;

import org.schemaanalyst.sqlrepresentation.datatype.*;

public class DataTypeFactory {

    public static final int numSupportedTypes = 15;

    public static BooleanDataType getBooleanDataType(){
        return (new BooleanDataType());
    }
    
    public static CharDataType getCharDataType(int size){
        return (new CharDataType(size));
    }

    public static DateDataType getDateDataType(){
        return new DateDataType();
    }

    public static DateTimeDataType getDateTimeDataType(){
        return new DateTimeDataType();
    }

    // TODO use precision and scale (currently just precision)
    public static DecimalDataType getDecimalDataType(int size){
        return new DecimalDataType(size); 
    }

    public static DoubleDataType getDoubleDataType(){
        return new DoubleDataType();
    }

    public static FloatDataType getFloatDataType(){
        return new FloatDataType();
    }

    public static IntDataType getIntDataType(){
        return new IntDataType();
    }

    // TODO use precision and scale (currently just precision)
    public static NumericDataType getNumericDataType(int size){
        return new NumericDataType(size);
    }

    public static RealDataType getRealDataType(){
        return new RealDataType();
    }

    public static SingleCharDataType getSingleCharDataType(){
        return new SingleCharDataType();
    }

    public static TextDataType getTextDataType(){
        return new TextDataType();
    }

    public static TimeDataType getTimeDataType(){
        return new TimeDataType();
    }

    public static TimestampDataType getTimestampDataType(){
        return new TimestampDataType();
    }

    public static VarCharDataType getVarCharDataType(int size){
        return new VarCharDataType(size);
    }

}
