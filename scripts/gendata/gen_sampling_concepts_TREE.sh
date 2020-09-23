#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\new"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 10 -o 0 -u 50 -r 1) -f $DATA_DIR/TREE_concept_1.arff -m 2000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 10 -o 0 -u 50 -r 2) -f $DATA_DIR/TREE_concept_2.arff -m 2000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 10 -o 0 -u 50 -r 3) -f $DATA_DIR/TREE_concept_3.arff -m 2000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 10 -o 0 -u 50 -r 4) -f $DATA_DIR/TREE_concept_4.arff -m 2000000"

sed -i 's/,$//' $DATA_DIR\\TREE_concept_*.arff

cd $DIR