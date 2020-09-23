# Experiments for: *Online Oversampling for Sparsely Labeled Imbalanced and Non-Stationary Data Streams*

## Data

Two sets of streams are required: the real streams and the semi-synthetic streams. The former can be downloaded from websites given below. The latter can be generated based on the real ones, using a given script. 

Send us an email in case of any problem: *mlrep.contact@gmail.com*

### Real streams

* ACTIVITY and ACTIVITY_RAW: http://www.cis.fordham.edu/wisdm/dataset.php
* CONNECT4: http://archive.ics.uci.edu/ml/datasets/connect-4
* COVERTYPE: http://www.openml.org/d/150
* CRIMES: https://www.kaggle.com/c/sf-crime/data
* DJ30: (in the repository)
* EEG: https://archive.ics.uci.edu/ml/datasets/EEG+Eye+State
* ELEC: http://moa.cms.waikato.ac.nz/datasets
* GAS: http://archive.ics.uci.edu/ml/datasets/Gas+Sensor+Array+Drift+Dataset
* OLYMPIC: https://www.kaggle.com/heesoo37/120-years-of-olympic-history-athletes-and-results
* POKER: http://moa.cms.waikato.ac.nz/datasets
* SENSOR: http://www.cse.fau.edu/~xqzhu/stream.html
* TAGS: https://archive.ics.uci.edu/ml/datasets/Localization+Data+for+Person+Activity

All the data streams should be converted to ARFF files.

### Semi-synthetic streams

* Make sure that all the real streams are in: **streams/real**.
* Each data stream should be in: *streams/real/STREAM_NAME.arff*.
* New data streams will be generated using the following formulas in `create_real_drifting_streams()`:
```python
arff_data = arff.load_arff('real/CONNECT4', root_dir, False)
ds = create_real_drifting_stream(arff_data['data'], 
    [{'p': 20000, 'w': 100, 'c': ['m1', 'm2']}, {'p': 50000, 'w': 100, 'c': ['m2', 'm3']}], 
    {
        'm1': {'win': '1', 'loss': 2, 'draw': 3},
        'm2': {'win': '3', 'loss': 2, 'draw': 1},
        'm3': {'win': '2', 'loss': 1, 'draw': 3}
    }, 67557)
```
Where the second argument (array) consists of definitions of drifts based on the sigmoid function given in MOA (https://moa.cms.waikato.ac.nz), e.g.: `{'p': 20000, 'w': 100, 'c': ['m1', 'm2']}` means that the first drift has its peak at 20000 (p), takes 100 instances (w) and transforms between concept m1 and m2 (c). The script includes complete definitions for all the semi-synthetic streams.
* Some classes in the original real streams may require renaming - check the definitions, mentioned above, in the *synth.py* script. 
* Run (Python 3.x): `python synth.py`.
* The semi-synthetic streams should be generated into: **streams/imbalanced/dynamic/semi-synth**.
* The directory should consist of the following streams: ACTIVITY-D1, ACTIVITY-RAW-D1, CONNECT4-D1, COVERTYPE-D1, CRIMES-D1, DJ30-D1, ELEC-D1, GAS-D1, OLYMPIC-D1, POKER-D1, SENSOR-D1, TAGS-D1.
* Do not change the file paths - they should be organized in this way when running the experiments in the next step.

### Package

We can also provide the whole package with all the data streams on request: *mlrep.contact@gmail.com*

## Experiments

* Install Java 8.
* Run: `java -jar experiments.jar`.
* Configurations described in the paper are given in *config/ExperimentConfig.java*
* The experiments use: MOA 2018.6.0, WEKA 3.8.1 and JFreeChart 1.0.19.
* The final results should be now available in the **results/all/summary** directory.

### Source code
- To conduct own experiments set paths in ***src/eval/Evaluator.java*** and run it. You can pick between different experiments in *runBalancingQueryExperiments()*.
* Details of experiments are defined in ***src/eval/cases/imb***. 
* To change the base learner uncomment a selected classifier in ***src/eval/experiment/ExperimentRow.java***
* To run selected rows on specific data uncomment streams in ***src/eval/experiment/ExperimentStream.java***
* The final results should be now available in the ***results*** directory.

### Testing
All unit tests can be found in ***tests***.
