package edu.allegheny.schemaexperiment; 

import com.beust.jcommander.Parameter;

import edu.allegheny.expose.DoublingExperimentParams;

public class SchemaExpParams extends DoublingExperimentParams{

    @Parameter(names = {"-schema","-s"}, description = "Select which schema to use")
        public String schema = null;

    @Parameter(names = {"-criterion"}, description = "Select which criterion to use")
        public String criterion = "AICC";

    @Parameter(names = {"-datagenerator"}, description = "Select which schema to use")
        public String datagenerator = "directedRandom";

    @Parameter(names = {"-doublerpackage"}, description = "Select which package to use for doubler")
        public String doublerPackage = "edu.allegheny.worstcaseanalyst.schemadoubler.";

    @Parameter(names = {"-doubler"}, description = "Select which schemaDoubler to use")
        public String doubler = "DoubleAll";

    @Parameter(names = {"-schemapackage"}, description = "Select which package to use for schema")
        public String schemaPackage = "parsedcasestudy.";

}
