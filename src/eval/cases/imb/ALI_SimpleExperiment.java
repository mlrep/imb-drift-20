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
import moa.classifiers.meta.OzaBag;
import moa.classifiers.meta.OzaBagAdwin;
import strategies.al.BalancingStrategy;
import strategies.al.BalancingStrategyType;
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

public class ALI_SimpleExperiment extends Experiment {

    public ALI_SimpleExperiment(String inputDir, String outputDir) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
    }

    @Override
    public void run(Evaluator evaluator) {
        this.conduct(this.createExperimentRows(), ExperimentStream.createExperimentStreams(this.inputDir), evaluator, this.outputDir);
    }

    @Override
    public List<ExperimentRow> createExperimentRows() {
        Classifier cls = ExperimentRow.getExperimentClassifier();
        List<ExperimentRow> rows = new ArrayList<>();

        double budget = 0.05;
        int windowSize = 50;
        int intensity = 100;

        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, budget)
                ),
                "ALI", "RAND"
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget).setVariableThresholdStep(0.01)
                ),
                "ALI", "RANDVAR"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget),
                        new SingleExpositionStrategy(GenerationsNumStrategy.RATIO_DRIVEN)
                                .setIntensity(intensity).setWindowSize(windowSize),
                        new WindowedErrorIndicator(windowSize)
                ),
                "ALI", "BSE_RATIO"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget),
                        new SingleExpositionStrategy(GenerationsNumStrategy.CLASS_ERROR_DRIVEN)
                                .setIntensity(intensity).setWindowSize(windowSize),
                        new MulticlassPerformanceIndicator(windowSize, PerformanceType.GMEAN)
                ),
                "ALI", "BSE_CERR"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget),
                        new SingleExpositionStrategy(GenerationsNumStrategy.HYBRID_RATIO_ERROR)
                                .setIntensity(intensity).setWindowSize(windowSize).setHybridCoefficients(0.5, 0.5),
                        new MulticlassPerformanceIndicator(windowSize, PerformanceType.GMEAN)
                ),
                "ALI", "BSE_HYBRID"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget),
                        new SMOTEStrategy(windowSize, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED,
                                GenerationsNumStrategy.RATIO_DRIVEN, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(1.0)
                                .setFixedNumNeighbors(10)
                                .setIntensity(intensity),
                        new WindowedErrorIndicator(windowSize)
                ),
                "ALI", "BSMOTE_RATIO"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget),
                        new SMOTEStrategy(windowSize, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED,
                                GenerationsNumStrategy.CLASS_ERROR_DRIVEN, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(1.0)
                                .setFixedNumNeighbors(10)
                                .setIntensity(intensity),
                        new MulticlassPerformanceIndicator(windowSize, PerformanceType.GMEAN)
                ),
                "ALI", "BSMOTE_CERR"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget),
                        new SMOTEStrategy(windowSize, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED,
                                GenerationsNumStrategy.HYBRID_RATIO_ERROR, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(1.0)
                                .setFixedNumNeighbors(10)
                                .setIntensity(intensity)
                                .setHybridCoefficients(0.5, 0.5),
                        new MulticlassPerformanceIndicator(windowSize, PerformanceType.GMEAN)
                ),
                "ALI", "BSMOTE_HYBRID"
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(10, BalancingType.OVERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "OMBB"
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(10, BalancingType.UNDERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "UMBB"
        ));

        return rows;
    }
}
