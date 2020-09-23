#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\concepts"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.STAGGERGenerator -f 1) -f $DATA_DIR/STAGGER_concept_1.arff -m 600000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.STAGGERGenerator -f 2) -f $DATA_DIR/STAGGER_concept_2.arff -m 600000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.STAGGERGenerator -f 3) -f $DATA_DIR/STAGGER_concept_3.arff -m 600000"

sed -i 's/,$//' $DATA_DIR\\STAGGER_concept_*.arff

cd $DIR