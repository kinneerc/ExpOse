package edu.allegheny.schemaexperiment.doubler.tests;

import edu.allegheny.schemaexperiment.doubler.DoubleChecksSemantic;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;

public class DoubleChecksSemanticTest extends DoublerJUnitParent{

    SchemaDoubler schemaDoubler(){
        return new DoubleChecksSemantic();
    }

    String checkDoublesValue(){
        return "c";
    }

}
