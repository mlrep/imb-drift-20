package gen;
import utils.FileUtils;
import java.util.ArrayList;
import java.util.Arrays;

public interface StreamGenerator {

    static void main(String[] args) {

        // SEA
        System.out.println("Generating SEA_r1_c1");
        SEAGenerator SEA = new SEAGenerator(2, 1000000, new ArrayList<>(Arrays.asList(0.8, 0.2)), 0.05, 1);
        SEA.generateStream();
        FileUtils.writeArffToFile(SEA.getHeader(), SEA.getStream(), "../Data/streams/imbalanced/synth_base/SEA_r1_c1.arff");

        System.out.println("Generating SEA_r2_c1");
        SEA = new SEAGenerator(2, 1000000, new ArrayList<>(Arrays.asList(0.2, 0.8)), 0.05, 2);
        SEA.generateStream();
        FileUtils.writeArffToFile(SEA.getHeader(), SEA.getStream(), "../Data/streams/imbalanced/synth_base/SEA_r2_c1.arff");

        System.out.println("Generating SEA_r2_c2");
        SEA = new SEAGenerator(3, 1000000, new ArrayList<>(Arrays.asList(0.2, 0.8)), 0.05, 3);
        SEA.generateStream();
        FileUtils.writeArffToFile(SEA.getHeader(), SEA.getStream(), "../Data/streams/imbalanced/synth_base/SEA_r2_c2.arff");

        System.out.println("Generating SEA_r3_c1");
        SEA = new SEAGenerator(2, 1000000, new ArrayList<>(Arrays.asList(0.5, 0.5)), 0.05, 4);
        SEA.generateStream();
        FileUtils.writeArffToFile(SEA.getHeader(), SEA.getStream(), "../Data/streams/imbalanced/synth_base/SEA_r3_c1.arff");

        System.out.println("Generating SEA_r3_c2");
        SEA = new SEAGenerator(3, 1000000, new ArrayList<>(Arrays.asList(0.5, 0.5)), 0.05, 5);
        SEA.generateStream();
        FileUtils.writeArffToFile(SEA.getHeader(), SEA.getStream(), "../Data/streams/imbalanced/synth_base/SEA_r3_c2.arff");

        // STAGGER
        System.out.println("Generating STAGGER_r1_c1");
        STAGGERGenerator STAGGER = new STAGGERGenerator(1, 1000000, new ArrayList<>(Arrays.asList(0.8, 0.2)), 0.05, 1);
        STAGGER.generateStream();
        FileUtils.writeArffToFile(STAGGER.getHeader(), STAGGER.getStream(), "../Data/streams/imbalanced/synth_base/STAGGER_r1_c1.arff");

        System.out.println("Generating STAGGER_r2_c1");
        STAGGER = new STAGGERGenerator(1, 1000000, new ArrayList<>(Arrays.asList(0.2, 0.8)), 0.05, 2);
        STAGGER.generateStream();
        FileUtils.writeArffToFile(STAGGER.getHeader(), STAGGER.getStream(), "../Data/streams/imbalanced/synth_base/STAGGER_r2_c1.arff");

        System.out.println("Generating STAGGER_r2_c2");
        STAGGER = new STAGGERGenerator(2, 1000000, new ArrayList<>(Arrays.asList(0.2, 0.8)), 0.05, 3);
        STAGGER.generateStream();
        FileUtils.writeArffToFile(STAGGER.getHeader(), STAGGER.getStream(), "../Data/streams/imbalanced/synth_base/STAGGER_r2_c2.arff");

        System.out.println("Generating STAGGER_r3_c1");
        STAGGER = new STAGGERGenerator(1, 1000000, new ArrayList<>(Arrays.asList(0.5, 0.5)), 0.05, 4);
        STAGGER.generateStream();
        FileUtils.writeArffToFile(STAGGER.getHeader(), STAGGER.getStream(), "../Data/streams/imbalanced/synth_base/STAGGER_r3_c1.arff");

        System.out.println("Generating STAGGER_r3_c2");
        STAGGER = new STAGGERGenerator(2, 1000000, new ArrayList<>(Arrays.asList(0.5, 0.5)), 0.05, 5);
        STAGGER.generateStream();
        FileUtils.writeArffToFile(STAGGER.getHeader(), STAGGER.getStream(), "../Data/streams/imbalanced/synth_base/STAGGER_r3_c2.arff");

        // SINE
        System.out.println("Generating SINE_r1_c1");
        SINEGenerator SINE = new SINEGenerator(1, 1000000, new ArrayList<>(Arrays.asList(0.8, 0.2)), 0.05, 1);
        SINE.generateStream();
        FileUtils.writeArffToFile(SINE.getHeader(), SINE.getStream(), "../Data/streams/imbalanced/synth_base/SINE_r1_c1.arff");

        System.out.println("Generating SINE_r2_c1");
        SINE = new SINEGenerator(1, 1000000, new ArrayList<>(Arrays.asList(0.2, 0.8)), 0.05, 2);
        SINE.generateStream();
        FileUtils.writeArffToFile(SINE.getHeader(), SINE.getStream(), "../Data/streams/imbalanced/synth_base/SINE_r2_c1.arff");

        System.out.println("Generating SINE_r2_c2");
        SINE = new SINEGenerator(2, 1000000, new ArrayList<>(Arrays.asList(0.2, 0.8)), 0.05, 3);
        SINE.generateStream();
        FileUtils.writeArffToFile(SINE.getHeader(), SINE.getStream(), "../Data/streams/imbalanced/synth_base/SINE_r2_c2.arff");

        System.out.println("Generating SINE_r3_c1");
        SINE = new SINEGenerator(1, 1000000, new ArrayList<>(Arrays.asList(0.5, 0.5)), 0.05, 4);
        SINE.generateStream();
        FileUtils.writeArffToFile(SINE.getHeader(), SINE.getStream(), "../Data/streams/imbalanced/synth_base/SINE_r3_c1.arff");

        System.out.println("Generating SINE_r3_c2");
        SINE = new SINEGenerator(2, 1000000, new ArrayList<>(Arrays.asList(0.5, 0.5)), 0.05, 5);
        SINE.generateStream();
        FileUtils.writeArffToFile(SINE.getHeader(), SINE.getStream(), "../Data/streams/imbalanced/synth_base/SINE_r3_c2.arff");

        // WAVE
        System.out.println("Generating WAVE_r1_c1");
        WAVEGenerator WAVE = new WAVEGenerator(1000000, new ArrayList<>(Arrays.asList(0.7, 0.25, 0.05)), 1);
        WAVE.generateStream();
        FileUtils.writeArffToFile(WAVE.getHeader(), WAVE.getStream(), "../Data/streams/imbalanced/synth_base/WAVE_r1_c1.arff");

        System.out.println("Generating WAVE_r2_c1");
        WAVE = new WAVEGenerator(1000000, new ArrayList<>(Arrays.asList(0.05, 0.25, 0.7)), 2);
        WAVE.generateStream();
        FileUtils.writeArffToFile(WAVE.getHeader(), WAVE.getStream(), "../Data/streams/imbalanced/synth_base/WAVE_r2_c1.arff");

        System.out.println("Generating WAVE_r3_c1");
        WAVE = new WAVEGenerator(1000000, new ArrayList<>(Arrays.asList(0.34, 0.33, 0.33)), 3);
        WAVE.generateStream();
        FileUtils.writeArffToFile(WAVE.getHeader(), WAVE.getStream(), "../Data/streams/imbalanced/synth_base/WAVE_r3_c1.arff");

        // TREE
        System.out.println("Generating TREE_r1_c1");
        TREEGenerator TREE = new TREEGenerator(1, 1000000, 6, new ArrayList<>(Arrays.asList(0.55, 0.33, 0.16, 0.03, 0.02, 0.01)),
                5, 10, 10, 5, 3, 0.2, 1);
        TREE.generateStream();
        FileUtils.writeArffToFile(TREE.getHeader(), TREE.getStream(), "../Data/streams/imbalanced/synth_base/TREE_r1_c1.arff");

        System.out.println("Generating TREE_r2_c1");
        TREE = new TREEGenerator(1, 1000000, 6, new ArrayList<>(Arrays.asList(0.01, 0.02, 0.03, 0.16, 0.33, 0.55)),
                5, 10, 10, 5, 3, 0.2, 2);
        TREE.generateStream();
        FileUtils.writeArffToFile(TREE.getHeader(), TREE.getStream(), "../Data/streams/imbalanced/synth_base/TREE_r2_c1.arff");

        System.out.println("Generating TREE_r2_c2");
        TREE = new TREEGenerator(2, 1000000, 6, new ArrayList<>(Arrays.asList(0.01, 0.02, 0.03, 0.16, 0.33, 0.55)),
                5, 10, 10, 5, 3, 0.2, 3);
        TREE.generateStream();
        FileUtils.writeArffToFile(TREE.getHeader(), TREE.getStream(), "../Data/streams/imbalanced/synth_base/TREE_r2_c2.arff");

        System.out.println("Generating TREE_r3_c1");
        TREE = new TREEGenerator(1, 1000000, 6, new ArrayList<>(Arrays.asList(0.17, 0.17, 0.17, 0.17, 0.16, 0.16)),
                5, 10, 10, 5, 3, 0.2, 4);
        TREE.generateStream();
        FileUtils.writeArffToFile(TREE.getHeader(), TREE.getStream(), "../Data/streams/imbalanced/synth_base/TREE_r3_c1.arff");

        System.out.println("Generating TREE_r3_c2");
        TREE = new TREEGenerator(2, 1000000, 6, new ArrayList<>(Arrays.asList(0.17, 0.17, 0.17, 0.17, 0.16, 0.16)),
                5, 10, 10, 5, 3, 0.2, 5);
        TREE.generateStream();
        FileUtils.writeArffToFile(TREE.getHeader(), TREE.getStream(), "../Data/streams/imbalanced/synth_base/TREE_r3_c2.arff");

        // RBF
        System.out.println("Generating RBF_r1_c1");
        RBFGenerator RBF = new RBFGenerator(1, 1000000, 6, new ArrayList<>(Arrays.asList(0.55, 0.33, 0.16, 0.03, 0.02, 0.01)),
                15, 50, 1);
        RBF.generateStream();
        FileUtils.writeArffToFile(RBF.getHeader(), RBF.getStream(), "../Data/streams/imbalanced/synth_base/RBF_r1_c1.arff");

        System.out.println("Generating RBF_r2_c1");
        RBF = new RBFGenerator(1, 1000000, 6, new ArrayList<>(Arrays.asList(0.01, 0.02, 0.03, 0.16, 0.33, 0.55)),
                15, 50, 2);
        RBF.generateStream();
        FileUtils.writeArffToFile(RBF.getHeader(), RBF.getStream(), "../Data/streams/imbalanced/synth_base/RBF_r2_c1.arff");

        System.out.println("Generating RBF_r2_c2");
        RBF = new RBFGenerator(2, 1000000, 6, new ArrayList<>(Arrays.asList(0.01, 0.02, 0.03, 0.16, 0.33, 0.55)),
                15, 50, 3);
        RBF.generateStream();
        FileUtils.writeArffToFile(RBF.getHeader(), RBF.getStream(), "../Data/streams/imbalanced/synth_base/RBF_r2_c2.arff");

        System.out.println("Generating RBF_r3_c1");
        RBF = new RBFGenerator(1, 1000000, 6, new ArrayList<>(Arrays.asList(0.17, 0.17, 0.17, 0.17, 0.16, 0.16)),
                15, 50, 4);
        RBF.generateStream();
        FileUtils.writeArffToFile(RBF.getHeader(), RBF.getStream(), "../Data/streams/imbalanced/synth_base/RBF_r3_c1.arff");

        System.out.println("Generating RBF_r3_c2");
        RBF = new RBFGenerator(2, 1000000, 6, new ArrayList<>(Arrays.asList(0.17, 0.17, 0.17, 0.17, 0.16, 0.16)),
                15, 50, 5);
        RBF.generateStream();
        FileUtils.writeArffToFile(RBF.getHeader(), RBF.getStream(), "../Data/streams/imbalanced/synth_base/RBF_r3_c2.arff");

        System.exit(0);
    }
}
