#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\new"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomRBFGenerator -a 15 -c 5 -n 5 -r 1) -f $DATA_DIR/RBF_concept_1_n5.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomRBFGenerator -a 15 -c 5 -n 5 -r 2) -f $DATA_DIR/RBF_concept_2_n5.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomRBFGenerator -a 15 -c 5 -n 5 -r 4) -f $DATA_DIR/RBF_concept_4_n5.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomRBFGenerator -a 15 -c 5 -n 5 -r 5) -f $DATA_DIR/RBF_concept_5_n5.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomRBFGenerator -a 15 -c 5 -n 15 -r 11) -f $DATA_DIR/RBF_concept_11_n15_12m.arff -m 1200000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomRBFGenerator -a 15 -c 5 -n 15 -r 12) -f $DATA_DIR/RBF_concept_12_n15_12m.arff -m 1200000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.RandomRBFGenerator -a 15 -c 5 -n 15 -r 13) -f $DATA_DIR/RBF_concept_13_n15_12m.arff -m 1200000"

sed -i 's/,$//' $DATA_DIR\\RBF_concept_*.arff

cd $DIR