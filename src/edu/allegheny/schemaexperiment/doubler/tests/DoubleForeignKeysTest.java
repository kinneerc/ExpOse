package edu.allegheny.schemaexperiment.doubler.tests;

import edu.allegheny.schemaexperiment.doubler.DoubleForeignKeys;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;

public class DoubleForeignKeysTest extends DoublerJUnitParent{

    SchemaDoubler schemaDoubler(){
        return new DoubleForeignKeys();
    }

    String checkDoublesValue(){
        return "f";
    }

}
