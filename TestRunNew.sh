#!/bin/bash

declare -a criterions=(
"ICC"
)

declare -a datagenerators=(
"directedRandom"
)

declare -a doublersWSub=(
"columns"
"primaryKeys"
"foriegnKeys"
"uniques"
"all"
)

declare -a doublersNSub=(
"tables"
"notNulls"
)


declare -a subDoublers=(
"number"
"size"
"both"
)

export CLASSPATH="bin:lib/*:."

classname="edu.allegheny.schemaexperiment.schemagen.SchemaGenExperiment"

for criterion in "${criterions[@]}" 
do
    for datagenerator in "${datagenerators[@]}"
    do
        for doubler in "${doublersWSub[@]}"
        do
            for subDoubler in "${subDoublers[@]}"
            do
                java $classname --criterion $criterion --datagenerator $datagenerator --doubler $doubler --subFeature $subDoubler --maxTime 50 --minDoubles 15 --overwrite
            done
        done
        for doubler in "${doublersNSub[@]}"
        do
            java $classname --criterion $criterion --datagenerator $datagenerator --doubler $doubler --maxTime 50 --subFeature number --minDoubles 15 --overwrite
        done
    done
done
