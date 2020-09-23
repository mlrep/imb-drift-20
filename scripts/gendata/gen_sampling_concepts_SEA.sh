#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\new"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.SEAGenerator -f 1 -n 5 -b) -f $DATA_DIR/SEA_concept_1.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.SEAGenerator -f 2 -n 5 -b) -f $DATA_DIR/SEA_concept_2.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.SEAGenerator -f 3 -n 5 -b) -f $DATA_DIR/SEA_concept_3.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.SEAGenerator -f 4 -n 5 -b) -f $DATA_DIR/SEA_concept_4.arff -m 1000000"

sed -i 's/,$//' $DATA_DIR\\SEA_concept_*.arff

cd $DIR