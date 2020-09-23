package eval.cases.imb;
import cls.BalancingType;
import cls.MulticlassBalancingBagging;
import detect.MulticlassPerformanceIndicator;
import detect.PerformanceType;
import detect.WindowedErrorIndicator;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.BlindFramework;
import framework.OversamplingFramework;
import moa.classifiers.Classifier;
import moa.classifiers.meta.*;
import moa.classifiers.trees.HoeffdingAdaptiveTree;
import moa.options.ClassOption;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import strategies.os.SMOTEStrategy;
import strategies.os.SingleExpositionStrategy;
import strategies.os.strategies.GenerationStrategyType;
import strategies.os.strategies.GenerationsNumStrategy;
import strategies.os.strategies.NeighborsNumStrategyType;
import strategies.os.strategies.UnlabeledControlStrategyType;

import java.util.ArrayList;
import java.util.List;

public class ALI_BudgetsExperiment extends Experiment {

    protected double budget;
    protected int windowSize;
    protected int intensity;
    protected int numNeighbors;

    public ALI_BudgetsExperiment(String inputDir, String outputDir) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
    }

    @Override
    public void run(Evaluator evaluator) {
        double[] budgets = new double[]{1.0, 0.5, 0.2, 0.1, 0.05, 0.01, 0.005, 0.001};
        int[] windowSizes = new int[]{1000, 500, 200, 100, 50, 10, 10, 10};
        int[] intensities = new int[]{100, 100, 100, 100, 100, 100, 100, 100};
        int[] numNeighbors = new int[]{10, 10, 10, 10, 10, 10, 10, 10};

        for (int i = 0; i < budgets.length; i++) {
            this.budget = budgets[i];
            this.windowSize = windowSizes[i];
            this.intensity = intensities[i];
            this.numNeighbors = numNeighbors[i];

            System.out.println("\nRunning for: " + this.budget);
            this.conduct(this.createExperimentRows(), ExperimentStream.createExperimentStreams(this.inputDir), evaluator, this.outputDir + "/" + this.budget);
        }
    }

    @Override
    public List<ExperimentRow> createExperimentRows() {
        Classifier cls = ExperimentRow.getExperimentClassifier();
        List<ExperimentRow> rows = new ArrayList<>();

        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, budget)
                ).setCollectTrackableParameters(false),
                "ALI", "RAND-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget).setVariableThresholdStep(0.01)
                ).setCollectTrackableParameters(false),
                "ALI", "RANDVAR-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.SAMPLING, budget)
                ).setCollectTrackableParameters(false),
                "ALI", "SAMP-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget),
                        new SingleExpositionStrategy(GenerationsNumStrategy.RATIO_DRIVEN)
                                .setIntensity(intensity).setWindowSize(windowSize),
                        new WindowedErrorIndicator(windowSize)
                ).setCollectTrackableParameters(false),
                "ALI", "BSE_RATIO-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget),
                        new SingleExpositionStrategy(GenerationsNumStrategy.HYBRID_RATIO_ERROR)
                                .setIntensity(intensity).setWindowSize(windowSize).setHybridCoefficients(0.5, 0.5),
                        new MulticlassPerformanceIndicator(windowSize, PerformanceType.GMEAN)
                ).setCollectTrackableParameters(false),
                "ALI", "BSE_HYBRID-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget),
                        new SMOTEStrategy(windowSize, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED,
                                GenerationsNumStrategy.RATIO_DRIVEN, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(1.0)
                                .setFixedNumNeighbors(numNeighbors)
                                .setIntensity(intensity),
                        new WindowedErrorIndicator(windowSize)
                ).setCollectTrackableParameters(false),
                "ALI", "BSMOTE_RATIO-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget),
                        new SMOTEStrategy(windowSize, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED,
                                GenerationsNumStrategy.HYBRID_RATIO_ERROR, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(1.0)
                                .setFixedNumNeighbors(numNeighbors)
                                .setIntensity(intensity)
                                .setHybridCoefficients(0.5, 0.5),
                        new MulticlassPerformanceIndicator(windowSize, PerformanceType.GMEAN)
                ).setCollectTrackableParameters(false),
                "ALI", "BSMOTE_HYBRID-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(10, BalancingType.OVERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ).setCollectTrackableParameters(false),
                "ALI", "MOOB-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(10, BalancingType.UNDERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ).setCollectTrackableParameters(false),
                "ALI", "MUOB-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new OzaBagASHT(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ).setCollectTrackableParameters(false),
                "ALI", "OZABAG-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new LeveragingBag(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ).setCollectTrackableParameters(false),
                "ALI", "LB-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new AdaptiveRandomForest(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ).setCollectTrackableParameters(false),
                "ALI", "ARF-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new OzaBoostAdwin(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ).setCollectTrackableParameters(false),
                "ALI", "OB_ADWIN-" + this.budget
        ));

        DynamicWeightedMajority dwm = new DynamicWeightedMajority();
        dwm.baseLearnerOption = new ClassOption("baseLearner", 'l', "Classifier to train.", Classifier.class, "trees.HoeffdingTree");

        rows.add(new ExperimentRow(
                new BlindFramework(
                        dwm,
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ).setCollectTrackableParameters(false),
                "ALI", "DWM-" + this.budget
        ));

        return rows;
    }
}
