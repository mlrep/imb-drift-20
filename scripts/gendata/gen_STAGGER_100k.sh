#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\new"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

# STAGGER stream 1:
# I->II 150000 100, II->III 300000 100, III->IV 450000 100

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile 
-s (ConceptDriftStream 
  -s (generators.STAGGERGenerator -f 1)
  -p 15000 -w 100
  -d (ConceptDriftStream 
    -s (generators.STAGGERGenerator -f 2)
    -p 15000 -w 100
    -d (ConceptDriftStream 
      -s (generators.STAGGERGenerator -f 3)
      -p 15000 -w 100
      -d (generators.STAGGERGenerator -f 1)
    )
  )
) -f $DATA_DIR/STAGGER_1.arff -m 60000"

# STAGGER stream 2:
# I->II 150000 10000, II->III 300000 10000, III->IV 450000 10000

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile 
-s (ConceptDriftStream 
  -s (generators.STAGGERGenerator -f 1)
  -p 15000 -w 1000
  -d (ConceptDriftStream 
    -s (generators.STAGGERGenerator -f 2)
    -p 15000 -w 1000
    -d (ConceptDriftStream 
      -s (generators.STAGGERGenerator -f 3)
      -p 15000 -w 1000
      -d (generators.STAGGERGenerator -f 1)
    )
  )
) -f $DATA_DIR/STAGGER_2.arff -m 60000"

sed -i 's/,$//' $DATA_DIR\\STAGGER*.arff

cd $DIR