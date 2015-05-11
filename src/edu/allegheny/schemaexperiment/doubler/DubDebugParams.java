package edu.allegheny.schemaexperiment.doubler; // Package name

import com.beust.jcommander.Parameter; // Class for jcommander used to define parameters

public class DubDebugParams{

    // What constraints to include
    @Parameter(names = {"-constraints","-c"}, description = "Select which constraints to use")
        public String constraints = null; // Default to null
    // Print column data
    @Parameter(names = {"-column","-columns"}, description = "Show column stats")
        public boolean columns = false;
    // Print table data
    @Parameter(names = {"-table","-tables"}, description = "Show table stats")
        public boolean tables = false;
    @Parameter(names = {"-d","-doubler","-dub"})
        public String dubName = null; // Default to null
    @Parameter(names = {"-n", "-doubles"})
        public int doubles = 1; // Default at one
    @Parameter(names = {"-package"})
        public String pack = "edu.allegheny.worstcaseanalyst.schemadoubler.";

}
