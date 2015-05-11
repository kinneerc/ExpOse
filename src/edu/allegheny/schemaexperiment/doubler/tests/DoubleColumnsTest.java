package edu.allegheny.schemaexperiment.doubler.tests;

import edu.allegheny.schemaexperiment.doubler.DoubleColumns;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;

public class DoubleColumnsTest extends DoublerJUnitParent{

    SchemaDoubler schemaDoubler(){
        return new DoubleColumns();
    }

    String checkDoublesValue(){
        return "o";
    }

}
