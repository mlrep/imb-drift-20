#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\new"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.LEDGeneratorDrift -n 5 -d 3 -s)
-f $DATA_DIR/LED_1.arff -m 500000"

sed -i 's/,$//' $DATA_DIR\\*.arff

cd $DIR
