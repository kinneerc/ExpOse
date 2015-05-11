package edu.allegheny.schemaexperiment.doubler.tests;

import edu.allegheny.schemaexperiment.doubler.DoubleTables;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;

public class DoubleTablesTest extends DoublerJUnitParent{

    SchemaDoubler schemaDoubler(){
        return new DoubleTables();
    }

    String checkDoublesValue(){
        return "t";
    }

}
