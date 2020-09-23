#!/bin/bash
DIR=$PWD
DATA_DIR="D:\\Computer_Science\\Projects\\Data\\streams\\synthetic\\new"
cd D:\\Computer_Science\\Projects\\Libs\\moa-2014

# TREE stream 0:
# I->II 300000 50, II->III 600000 5000

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile 
-s (ConceptDriftStream 
  -s (generators.RandomTreeGenerator -c 2 -o 0 -u 2 -r 1)
  -p 150000 -w 50
  -d (ConceptDriftStream 
    -s (generators.RandomTreeGenerator -c 2 -o 0 -u 2 -r 2)
    -d (generators.RandomTreeGenerator -c 2 -o 0 -u 2 -r 3)
    -p 150000 -w 5000
  )
) -f $DATA_DIR/TREE_0.arff -m 500000"

# TREE stream 1:
# I->II 250000 100, II->III 500000 100, II->III 750000 100

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile 
-s (ConceptDriftStream 
  -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 2)
  -p 25000 -w 100
  -d (ConceptDriftStream 
    -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 1)
    -p 25000 -w 100
    -d (ConceptDriftStream 
      -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 3)
      -d (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 4)
      -p 25000 -w 100
    )
  )
) -f $DATA_DIR/TREE_1.arff -m 100000"

# TREE stream 2:
# I->II 250000 10000, II->III 500000 10000, II->III 750000 10000

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile 
-s (ConceptDriftStream 
  -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 2)
  -p 25000 -w 1000
  -d (ConceptDriftStream 
    -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 1)
    -p 25000 -w 1000
    -d (ConceptDriftStream 
      -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 3)
      -d (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 4)
      -p 25000 -w 1000
    )
  )
) -f $DATA_DIR/TREE_2.arff -m 100000"

# TREE stream 3:
# I->II 300000 50000, II->III 600000 50000

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile 
-s (ConceptDriftStream 
  -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 2)
  -p 30000 -w 5000
  -d (ConceptDriftStream 
    -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 1)
    -d (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 3)
    -p 30000 -w 5000
  )
) -f $DATA_DIR/TREE_3.arff -m 100000"

# TREE stream 4:
# I->II 250000 100000, II->III 650000 100000

java -cp moa.jar -javaagent:sizeofag.jar moa.DoTask \
"WriteStreamToARFFFile 
-s (ConceptDriftStream 
  -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 2)
  -p 25000 -w 10000
  -d (ConceptDriftStream 
    -s (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 1)
    -d (generators.RandomTreeGenerator -c 5 -o 0 -u 15 -r 3)
    -p 40000 -w 10000
  )
) -f $DATA_DIR/TREE_4.arff -m 100000"

sed -i 's/,$//' $DATA_DIR\\*.arff

cd $DIR