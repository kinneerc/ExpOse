package edu.allegheny.schemaexperiment.doubler.tests;

import edu.allegheny.schemaexperiment.doubler.DoubleUniquesSemantic;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;

public class DoubleUniquesSemanticTest extends DoublerJUnitParent{

    SchemaDoubler schemaDoubler(){
        return new DoubleUniquesSemantic();
    }

    String checkDoublesValue(){
        return "u";
    }

}
