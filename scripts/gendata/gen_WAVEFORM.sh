#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\new"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

# WAVEFORM stream 1:

java -cp ./:moa.jar:weka.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile -s (generators.WaveformGeneratorDrift -d 10 -n)
-f $DATA_DIR/WAVEFORM_1.arff -m 500000"

sed -i 's/,$//' $DATA_DIR\\*.arff

cd $DIR
