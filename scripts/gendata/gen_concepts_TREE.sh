#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\new"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 1) -f $DATA_DIR/TREE_concept_1.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 2) -f $DATA_DIR/TREE_concept_2.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 3) -f $DATA_DIR/TREE_concept_3.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 4) -f $DATA_DIR/TREE_concept_4.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 1) -f $DATA_DIR/TREE_concept_1_12m.arff -m 1200000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 2) -f $DATA_DIR/TREE_concept_2_12m.arff -m 1200000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 3) -f $DATA_DIR/TREE_concept_3_12m.arff -m 1200000"

sed -i 's/,$//' $DATA_DIR\\TREE_concept_*.arff

cd $DIR