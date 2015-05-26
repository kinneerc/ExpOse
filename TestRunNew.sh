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

declare -a doublersWSub=(
"columns"
"notNulls"
"primaryKeys"
"foriegnKeys"
"uniques"
"all"
)

declare -a doublersNSub=(
"tables"
)


declare -a subDoublers=(
"min"
"max"
"random"
)

export CLASSPATH="bin:lib/*:."

classname="edu.allegheny.schemaexperiment.schemagen.SchemaGenExperiment"

for criterion in "${criterions[@]}" 
do
    for datagenerator in "${datagenerators[@]}"
    do
        for schema in "${schemas[@]}"
        do
            for doubler in "${doublersWSub[@]}"
            do
                for subDoubler in "${subDoublers[@]}"
                do
                    java $classname --criterion $criterion --datagenerator $datagenerator --schema $schema --doubler $doubler --subFeature $subDoubler --maxTime 50 
                done
            done
            for doubler in "${doublersNSub[@]}"
            do
                java $classname --criterion $criterion --datagenerator $datagenerator --schema $schema --doubler $doubler --maxTime 50
            done
        done
    done
done
