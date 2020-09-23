#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\new"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

# SEA stream 1:
# I->II 150000 100, II->III 300000 100, III->IV 450000 100

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile 
-s (ConceptDriftStream 
  -s (generators.SEAGenerator -f 2 -n 5)
  -p 150000 -w 100
  -d (ConceptDriftStream 
    -s (generators.SEAGenerator -f 3 -n 5)
    -p 150000 -w 100
    -d (ConceptDriftStream 
      -s (generators.SEAGenerator -f 4 -n 5)
      -p 150000 -w 100
      -d (generators.SEAGenerator -f 3 -n 5) 
    )
  )
) -f $DATA_DIR/SEA_1.arff -m 600000"

# SEA stream 2:
# I->II 150000 10000, II->III 300000 10000, III->IV 450000 10000

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile 
-s (ConceptDriftStream 
  -s (generators.SEAGenerator -f 2 -n 5)
  -p 150000 -w 10000
  -d (ConceptDriftStream 
    -s (generators.SEAGenerator -f 3 -n 5)
    -p 150000 -w 10000
    -d (ConceptDriftStream 
      -s (generators.SEAGenerator -f 4 -n 5)
      -p 150000 -w 10000
      -d (generators.SEAGenerator -f 3 -n 5)
    )
  )
) -f $DATA_DIR/SEA_2.arff -m 600000"

sed -i 's/,$//' $DATA_DIR\\SEA_*.arff

cd $DIR