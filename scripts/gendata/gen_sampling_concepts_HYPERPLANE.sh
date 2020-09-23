#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\new"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.HyperplaneGenerator -a 30 -k 7 -c 10 -t 0.001 -n 5 -s 1)
-f $DATA_DIR/HYPERPLANE_1.arff -m 1000000"

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.HyperplaneGenerator -a 30 -k 7 -c 10 -t 0.01 -n 5 -s 1)
-f $DATA_DIR/HYPERPLANE_2.arff -m 1000000"

sed -i 's/,$//' $DATA_DIR\\*.arff

cd $DIR