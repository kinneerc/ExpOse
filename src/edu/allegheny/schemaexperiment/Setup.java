package edu.allegheny.schemaexperiment;

import edu.allegheny.schemaexperiment.doubler.*;

import org.schemaanalyst.data.generation.DataGeneratorFactory;
import org.schemaanalyst.sqlrepresentation.Schema;
import org.schemaanalyst.testgeneration.coveragecriterion.CoverageCriterionFactory;
import org.schemaanalyst.dbms.mysql.MySQLDBMS;


/**
 * Contains setting for experiment.
 */
public class Setup {

    public static void main(String[] args){
        try{
        Setup.validate();
        }catch(InstantiationException e){
            e.printStackTrace();
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static final String[] SCHEMAS = {
        /* "Cloc", */
        /* "JWhoisServer", */
        /* "NistWeather", */
        /* "NistXTS748", */
        /* "NistXTS749", */
        /* "RiskIt", */
        /* "UnixUsage", */
        "BioSQL"
            /* "iTrust" */
    };

    public static final String[] CRITERIA = {
        "ICC",
        "AICC",
        "CondAICC",
        "ClauseAICC",
        "NCC",
        "ANCC",
        "UCC",
        "AUCC"
    };


public static final String[] DATAGENERATORS = {
    "avsDefaults",
    "avs",
    "directedRandomDefaults",
    "directedRandom",
    "randomDefaults",
    "random"
};

public static final String[] DOUBLERS = {
    "DoubleNotNullsSemantic",
    "DoubleChecksSemantic",
    "DoubleUniquesSemantic",
    "DoubleAll",
    "DoubleColumns",
    "DoubleTablesColumns",
    "DoubleTables",
    "DoubleForeignKeys",
    "DoubleNotNulls"
};


/**
 * This method tries to instantiate all the strings to make sure they are valid
 * @throws ClassNotFoundException
 * @throws IllegalAccessException
 * @throws InstantiationException
 */
public static void validate() throws InstantiationException,
       IllegalAccessException, ClassNotFoundException {

           Schema schemaObject = null;
           for (String schema : SCHEMAS){

               schemaObject = SchemaExperiment
                   .instantiateSchema("parsedcasestudy." + schema);

           }

           for (String criterion : CRITERIA){
               CoverageCriterionFactory.instantiateSchemaCriterion(criterion,schemaObject, new MySQLDBMS());            
           }

           for (String datagen : DATAGENERATORS){
               DataGeneratorFactory.instantiate(datagen, 0L, 10000, schemaObject);
           }

           for (String doubler : DOUBLERS){
               SchemaExperiment.instantiateSchemaDoubler("edu.allegheny.worstcaseanalyst.schemadoubler."+doubler);
           }

}

}
