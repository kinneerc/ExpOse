# ExpOseSA
ExpOse extened to support research with SchemaAnalyst, SA cannot yet be published, so this version must be private

The `schemagen` branch contains modifications made after the SEKE'15 conference paper, including the schemagen tool for
automatically generating database schemas, and the tuning capabilities for deciding the number of executions per trial.

First, set your classpath: ```export CLASSPATH="lib/*:bin:."``` 
Then compile with ant: ```ant``` 

Then you can run experements on real-world database schemas using the available "syntheic" doublers like the following:

```
java edu.allegheny.schemaexperiment.SchemaExperiment --criterion APC --datagenerator directedRandom --schema BioSQL --doubler DoubleUniquesSemantic --maxTime 50 --overwrite --minDoubles 15
```

Or you could run experements using the schema generator like this example (note the differant Java class and package):

```
java edu.allegheny.schemaexperiment.schemagen.SchemaGenExperiment --criterion APC --datagenerator directedRandom --doubler uniques --subFeature number --maxTime 50 --overwrite --minDoubles 15
```

# schemagen in standalone mode
The schema generator can also be called on its own in order to generate a database schema with the desired characteristics.

## How to use it

```
java edu.allegheny.schemaexperiment.schemagen.Generator tables columns notnulls primaryKeys foriegnKeys uniques checks dataTypeSize compoundKeySize
```

CREATE TABLE statments will be output to stdout.  
Use ```java edu...Generator arg0...arg8 > file.sql``` to save the generated schema.

Java API
--------
Simply import the ``edu.allegheny.schemaexperiment.schemagen.Generator`` class, and call the static method
```
Generator.randomSASchema(int tables, int columns, int notnulls, int primaryKeys, int foriegnKeys, int uniques,int checks,int dataTypeSize, int compoundKeySize)
```
This method will return a SchemaAnalyst ```Schema``` object.
Note that this method may throw a ```SchemaGenException``` if the requested schema could not be generated. 
This is most often caused by an impossible request, such as asking for more tables than columns.

# Known Bugs
+ The ```checks``` argument is ignored
