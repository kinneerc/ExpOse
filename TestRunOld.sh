#!/bin/bash

declare -a criterions=(
"ICC"
)

declare -a datagenerators=(
"directedRandom"
)

declare -a schemas=(
"UnixUsage"
)

declare -a doublers=(
"DoubleNotNullsSemantic"
"DoubleChecksSemantic"
"DoubleUniquesSemantic"
"DoubleAllSemantic"
"DoubleColumns"
"DoubleTables"
)

export CLASSPATH="bin:lib/*:."

classname="edu.allegheny.schemaexperiment.SchemaExperiment"

for criterion in "${criterions[@]}" 
do
    for datagenerator in "${datagenerators[@]}"
    do
        for schema in "${schemas[@]}"
        do
            for doubler in "${doublers[@]}"
            do

                echo "java $classname -criterion $criterion -datagenerator $datagenerator -schema $schema -doubler $doubler -maxTime 50"

                java $classname --criterion $criterion --datagenerator $datagenerator --schema $schema --doubler $doubler --maxTime 50

            done
        done
    done
done
