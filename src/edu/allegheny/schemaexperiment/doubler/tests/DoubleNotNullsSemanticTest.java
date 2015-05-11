package edu.allegheny.schemaexperiment.doubler.tests;

import edu.allegheny.schemaexperiment.doubler.DoubleNotNullsSemantic;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;

public class DoubleNotNullsSemanticTest extends DoublerJUnitParent{

    SchemaDoubler schemaDoubler(){
        return new DoubleNotNullsSemantic();
    }

    String checkDoublesValue(){
        return "n";
    }

}
