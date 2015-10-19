#!/bin/bash

# make needed directories if they do not exist
mkdir data
mkdir tuning

declare -a criterions=(
"criterionAPC"
"ICC"
"AICC"
"CondAICC"
"ClauseAICC"
"NCC"
"ANCC"
"UCC"
"AUCC"
)

declare -a datagenerators=(
"avsDefaults"
"avs"
"directedRandomDefaults"
"directedRandom"
"random"
"randomDefaults"
)

declare -a schemas=(
"UnixUsage"
"iTrust"
"BioSQL"
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
                java $classname --criterion $criterion --datagenerator $datagenerator --schema $schema --doubler $doubler --maxTime 50 --overwrite --minDoubles 15

            done
        done
    done
done
