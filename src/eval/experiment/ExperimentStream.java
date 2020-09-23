package eval.experiment;
import moa.streams.ArffFileStream;
import utils.DriftingStreamDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExperimentStream {

    public ArffFileStream stream;
    public String streamName;
    public double streamSize;
    public int logGranularity;
    public ArrayList<Double> classRatios;

    public ExperimentStream(ArffFileStream stream, String streamName, double streamSize, int logGranularity) {
        this.stream = stream;
        this.streamName = streamName;
        this.streamSize = streamSize;
        this.logGranularity = logGranularity;
    }

    public ExperimentStream(ArffFileStream stream, String streamName, double streamSize, int logGranularity, ArrayList<Double> classRatios) {
        this.stream = stream;
        this.streamName = streamName;
        this.streamSize = streamSize;
        this.logGranularity = logGranularity;
        this.classRatios = classRatios;
    }

    public static List<ExperimentStream> createExperimentStreams(String rootDataDir) {
        List<ExperimentStream> streams = new ArrayList<>();

        streams.addAll(createRealStreams(rootDataDir + "/real"));
        streams.addAll(createDynamicImbalancedSemiSyntheticStreams(rootDataDir + "/imbalanced/dynamic"));

        //streams.addAll(createStaticBinaryImbalancedStreams(rootDataDir + "/imbalanced/static/binary"));
        //streams.addAll(createDynamicImbalancedSyntheticStreams(rootDataDir + "/imbalanced/dynamic/synth"));
        //streams.addAll(createStaticImbalancedSemiSyntheticStreams(rootDataDir + "/imbalanced/static"));
        //streams.addAll(createSyntheticStreams(rootDataDir + "/synthetic"));
        //streams.addAll(createConceptStreams(rootDataDir + "/synthetic"));

        return streams;
    }

    private static List<ExperimentStream> createRealStreams(String rootDataDir) {
        List<ExperimentStream> streams = new ArrayList<>();

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/GAS.arff", 129), "GAS", 13910, 100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/ELEC.arff", 9), "ELEC", 45312, 100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/ACTIVITY.arff", 44), "ACTIVITY", 10853, 100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/COVERTYPE.arff", 55), "COVERTYPE", 581012, 2500));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/ACTIVITY_RAW.arff", 4), "ACTIVITY_RAW", 1048570, 2500));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/EEG.arff", 15), "EEG", 14980, 100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SENSOR.arff", 6), "SENSOR", 2219802, 10000));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/POKER.arff", 11), "POKER", 829201, 3000));

        return streams;
    }

    private static List<ExperimentStream> createStaticBinaryImbalancedStreams(String rootDataDir) {
        List<ExperimentStream> streams = new ArrayList<>();

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/CENSUS/CENSUS.arff", 42), "CENSUS", 299284, 1500,
                new ArrayList<>(Arrays.asList(0.94, 0.06))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/CONNECT4_1-2vs3.arff", 43), "CONNECT4_1-2vs3", 67557,500));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/POWERSUPPLY_20-to-23vsAll.arff", 3), "POWERSUPPLY_20-to-23vsAll", 29929, 100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/GAS_1vsAll.arff", 129), "GAS_1vsAll", 13910, 100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/COVERTYPE_1-2vsAll.arff", 55), "COVERTYPE_1-2vsAll", 581012, 2500));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/POKER_1-2vsAll.arff", 11), "POKER_1-2vsAll", 829201, 3000));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SENSOR_1-to-10vsAll.arff", 6), "SENSOR_1-to-10vsAll", 2219804, 5000));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/ACTIVITY_RAW_4vsAll.arff", 4), "ACTIVITY_RAW_4vsAll", 1048575, 2500));
                streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/KDD99_4vsAll.arff", 42), "KDD99_4vsAll", 494020, 2500));

        return streams;
    }

    private static List<ExperimentStream> createDynamicImbalancedSyntheticStreams(String rootDataDir) {
        List<ExperimentStream> streams = new ArrayList<>();
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA_R_W100.arff", 4), "SEA_R_W100", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA_RC_W100.arff", 4), "SEA_RC_W100", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA_R_W100k.arff", 4), "SEA_R_W100k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA_RC_W100k.arff", 4), "SEA_RC_W100k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA_RB_W20k.arff", 4), "SEA_RB_W20k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA_RBC_W20k.arff", 4), "SEA_RBC_W20k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA_RS_W10k.arff", 4), "SEA_RS_W10k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA_RSC_W10k.arff", 4), "SEA_RSC_W10k", 500000,100));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER_R_W100.arff", 4), "STAGGER_R_W100", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER_RC_W100.arff", 4), "STAGGER_RC_W100", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER_R_W100k.arff", 4), "STAGGER_R_W100k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER_RC_W100k.arff", 4), "STAGGER_RC_W100k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER_RB_W20k.arff", 4), "STAGGER_RB_W20k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER_RBC_W20k.arff", 4), "STAGGER_RBC_W20k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER_RS_W10k.arff", 4), "STAGGER_RS_W10k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER_RSC_W10k.arff", 4), "STAGGER_RSC_W10k", 500000,100));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SINE_R_W100.arff", 5), "SINE_R_W100", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SINE_RC_W100.arff", 5), "SINE_RC_W100", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SINE_R_W100k.arff", 5), "SINE_R_W100k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SINE_RC_W100k.arff", 5), "SINE_RC_W100k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SINE_RB_W20k.arff", 5), "SINE_RB_W20k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SINE_RBC_W20k.arff", 5), "SINE_RBC_W20k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SINE_RS_W10k.arff", 5), "SINE_RS_W10k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SINE_RSC_W10k.arff", 5), "SINE_RSC_W10k", 500000,100));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF_R_W100.arff", 16), "RBF_R_W100", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF_RC_W100.arff", 16), "RBF_RC_W100", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF_R_W100k.arff", 16), "RBF_R_W100k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF_RC_W100k.arff", 16), "RBF_RC_W100k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF_RB_W20k.arff", 16), "RBF_RB_W20k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF_RBC_W20k.arff", 16), "RBF_RBC_W20k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF_RS_W10k.arff", 16), "RBF_RS_W10k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF_RSC_W10k.arff", 16), "RBF_RSC_W10k", 500000,100));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE_R_W100.arff", 16), "TREE_R_W100", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE_RC_W100.arff", 16), "TREE_RC_W100", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE_R_W100k.arff", 16), "TREE_R_W100k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE_RC_W100k.arff", 16), "TREE_RC_W100k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE_RB_W20k.arff", 16), "TREE_RB_W20k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE_RBC_W20k.arff", 16), "TREE_RBC_W20k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE_RS_W10k.arff", 16), "TREE_RS_W10k", 500000,100));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE_RSC_W10k.arff", 16), "TREE_RSC_W10k", 500000,100));

        return streams;
    }

    private static List<ExperimentStream> createDynamicImbalancedSemiSyntheticStreams(String rootDataDir) {
        List<ExperimentStream> streams = new ArrayList<>();

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/GAS-D1.arff", 129), "GAS-D1", 13910, 100,
                new ArrayList<>(Arrays.asList(0.48, 0.14, 0.38))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/ELEC-D1.arff", 9), "ELEC-D1", 45312, 100,
                new ArrayList<>(Arrays.asList(0.52, 0.48))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/CONNECT4-D1.arff", 43), "CONNECT4-D1", 67557, 500,
                new ArrayList<>(Arrays.asList(0.34, 0.31, 0.35))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/ACTIVITY-D1.arff", 44), "ACTIVITY-D1", 10853, 100,
                new ArrayList<>(Arrays.asList(0.34, 0.12, 0.37, 0.17))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/COVERTYPE-D1.arff", 55), "COVERTYPE-D1", 581012, 2500,
                new ArrayList<>(Arrays.asList(0.18, 0.32, 0.03, 0.47))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/POKER-D1.arff", 11), "POKER-D1", 829201, 3000,
                new ArrayList<>(Arrays.asList(0.33, 0.3, 0.03, 0.34))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/DJ30-D1.arff", 8), "DJ30-D1", 138166, 500,
                new ArrayList<>(Arrays.asList(0.29, 0.36, 0.11, 0.24))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/ACTIVITY_RAW-D1.arff", 4), "ACTIVITY_RAW-D1", 1048570, 2500,
                new ArrayList<>(Arrays.asList(0.39, 0.13, 0.11, 0.37))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/TAGS-D1.arff", 5), "TAGS-D1", 164860, 500,
                new ArrayList<>(Arrays.asList(0.3, 0.35, 0.1, 0.25))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/OLYMPIC-D1.arff", 8), "OLYMPIC-D1", 271116, 1000,
                new ArrayList<>(Arrays.asList(0.32, 0.35, 0.33))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/CRIMES-D1.arff", 4), "CRIMES-D1", 878049, 3000,
                new ArrayList<>(Arrays.asList(0.33, 0.26, 0.26, 0.15))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/SENSOR-D1.arff", 6), "SENSOR-D1", 2219802, 10000,
                new ArrayList<>(Arrays.asList(0.29, 0.37, 0.09, 0.25))));

        return streams;
    }

    private static List<ExperimentStream> createStaticImbalancedSemiSyntheticStreams(String rootDataDir) {
        List<ExperimentStream> streams = new ArrayList<>();

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/POKER-S1.arff", 11), "POKER-S1", 829201, 3000,
                new ArrayList<>(Arrays.asList(0.92, 0.07, 0.005, 0.005))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/DJ30-S1.arff", 8), "DJ30-S1", 138166, 500,
                new ArrayList<>(Arrays.asList(0.66, 0.20, 0.07, 0.07))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/ACTIVITY_RAW-S1.arff", 4), "ACTIVITY_RAW-S1", 1048570, 2500,
                new ArrayList<>(Arrays.asList(0.75, 0.21, 0.04))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/TAGS-S1.arff", 5), "TAGS-S1", 164860, 500,
                new ArrayList<>(Arrays.asList(0.69, 0.18, 0.07, 0.06))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/OLYMPIC-S1.arff", 8), "OLYMPIC-S1", 271116, 1000,
                new ArrayList<>(Arrays.asList(0.85, 0.1, 0.05))));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/semi-synth/CRIMES-S1.arff", 4), "CRIMES-S1", 878049, 3000,
                new ArrayList<>(Arrays.asList(0.54, 0.22, 0.13, 0.11))));

        return streams;
    }

    private static List<ExperimentStream> createSyntheticStreams(String rootDataDir) {
        List<ExperimentStream> streams = new ArrayList<>();
        int smallerLog = 100; //3000;
        int biggerLog = 100; //5000;

//        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/HEPATITIS/HEPATITIS.arff", 20), "HEPATITIS", 1000000, biggerLog));
//        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/LYMPH/LYMPH.arff", 19), "LYMPH", 1000000, biggerLog));
//        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/WINE/WINE.arff", 14), "WINE", 1000000, biggerLog));
//        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/ZOO/ZOO.arff", 18), "ZOO", 1000000, biggerLog));
//        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/BRIDGES/BRIDGES.arff", 13), "BRIDGES", 1000000, biggerLog));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA/SEA_1.arff", 4), "SEA_1", 600000, smallerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA/SEA_2.arff", 4), "SEA_2", 600000, smallerLog));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER/STAGGER_1.arff", 4), "STAGGER_1", 600000, smallerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER/STAGGER_2.arff", 4), "STAGGER_2", 600000, smallerLog));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_1.arff", 16), "TREE_1", 1000000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_2.arff", 16), "TREE_2", 1000000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_3.arff", 16), "TREE_3", 1200000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_4.arff", 16), "TREE_4", 1200000, biggerLog));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_1.arff", 16), "RBF_1", 1000000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_2.arff", 16), "RBF_2", 1000000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_3.arff", 16), "RBF_3", 1200000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_4.arff", 16), "RBF_4", 1200000, biggerLog));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/HYPERPLANE/HYPERPLANE_1.arff", 16), "HYPERPLANE_1", 500000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/HYPERPLANE/HYPERPLANE_2.arff", 16), "HYPERPLANE_2", 500000, biggerLog));

        return streams;
    }

    private static List<ExperimentStream> createConceptStreams(String rootDataDir) {
        List<ExperimentStream> streams = new ArrayList<>();
        int smallerLog = 100;
        int biggerLog = 100;

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA/SEA_concept_1.arff", 4), "SEA_concept_1", 600000, smallerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA/SEA_concept_2.arff", 4), "SEA_concept_2", 600000, smallerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA/SEA_concept_3.arff", 4), "SEA_concept_3", 600000, smallerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/SEA/SEA_concept_4.arff", 4), "SEA_concept_4", 600000, smallerLog));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER/STAGGER_concept_1.arff", 4), "STAGGER_concept_1", 600000, smallerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER/STAGGER_concept_2.arff", 4), "STAGGER_concept_2", 600000, smallerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/STAGGER/STAGGER_concept_3.arff", 4), "STAGGER_concept_3", 600000, smallerLog));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_concept_1.arff", 16), "TREE_concept_1", 1000000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_concept_2.arff", 16), "TREE_concept_2", 1000000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_concept_3.arff", 16), "TREE_concept_3", 1000000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_concept_4.arff", 16), "TREE_concept_4", 1000000, biggerLog));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_concept_1_12m.arff", 16), "TREE_concept_1_12m", 1200000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_concept_2_12m.arff", 16), "TREE_concept_2_12m", 1200000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/TREE/TREE_concept_3_12m.arff", 16), "TREE_concept_3_12m", 1200000, biggerLog));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_concept_1_n5.arff", 16), "RBF_concept_1_n5", 1000000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_concept_2_n5.arff", 16), "RBF_concept_2_n5", 1000000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_concept_4_n5.arff", 16), "RBF_concept_4_n5", 1000000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_concept_5_n5.arff", 16), "RBF_concept_5_n5", 1000000, biggerLog));

        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_concept_11_n15_12m.arff", 16), "RBF_concept_11_n15_12m", 1200000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_concept_12_n15_12m.arff", 16), "RBF_concept_12_n15_12m", 1200000, biggerLog));
        streams.add(new ExperimentStream(new ArffFileStream(rootDataDir + "/RBF/RBF_concept_13_n15_12m.arff", 16), "RBF_concept_13_n15_12m", 1200000, biggerLog));

        return streams;
    }
}
