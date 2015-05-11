package edu.allegheny.schemaexperiment.doubler.tests;

import edu.allegheny.schemaexperiment.doubler.DoubleAll;
import edu.allegheny.schemaexperiment.doubler.SchemaDoubler;

public class DoubleAllTest extends DoublerJUnitParent{

    SchemaDoubler schemaDoubler(){
        return new DoubleAll();
    }

    String checkDoublesValue(){
        return "ncofput";
    }

}
