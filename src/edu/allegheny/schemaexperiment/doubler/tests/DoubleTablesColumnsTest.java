package edu.allegheny.schemaexperiment.doubler.tests;

import edu.allegheny.schemaexperiment.doubler.DoubleTablesColumns;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;

public class DoubleTablesColumnsTest extends DoublerJUnitParent{

    SchemaDoubler schemaDoubler(){
        return new DoubleTablesColumns();
    }

    String checkDoublesValue(){
        return "to";
    }

}
