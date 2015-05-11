package edu.allegheny.schemaexperiment.doubler.tests;

import edu.allegheny.schemaexperiment.doubler.DoubleForeignKeysSemantic;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;

public class DoubleForeignKeysSemanticTest extends DoublerJUnitParent{

    SchemaDoubler schemaDoubler(){
        return new DoubleForeignKeysSemantic();
    }

    String checkDoublesValue(){
        return "f";
    }

}
