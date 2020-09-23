package eval.cases.os.smote;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import detect.WindowedErrorIndicator;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.OversamplingFramework;
import moa.classifiers.Classifier;
import strategies.os.strategies.GenerationsNumStrategy;
import strategies.os.strategies.GenerationStrategyType;
import strategies.os.strategies.NeighborsNumStrategyType;
import strategies.os.SMOTEStrategy;
import strategies.os.strategies.UnlabeledControlStrategyType;

import java.util.ArrayList;
import java.util.List;

public class OS_SMOTEPoissonLabeledExperiment extends Experiment {

    public OS_SMOTEPoissonLabeledExperiment(String inputDir, String outputDir) {
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
        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.01),
                        new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED_POISSON,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(1.0)
                                .setFixedNumNeighbors(1)
                                .setIntensity(100),
                        new WindowedErrorIndicator(10)
                ),
                "SMOTE-lbl-poi", "0.01"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.05),
                        new SMOTEStrategy(50, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED_POISSON,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(1.0)
                                .setFixedNumNeighbors(5)
                                .setIntensity(20),
                        new WindowedErrorIndicator(50)
                ),
                "SMOTE-lbl-poi", "0.05"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new SMOTEStrategy(100, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED_POISSON,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(1.0)
                                .setFixedNumNeighbors(10)
                                .setIntensity(10),
                        new WindowedErrorIndicator(100)
                ),
                "SMOTE-lbl-poi", "0.1"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.2),
                        new SMOTEStrategy(200, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED_POISSON,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(1.0)
                                .setFixedNumNeighbors(20)
                                .setIntensity(5),
                        new WindowedErrorIndicator(200)
                ),
                "SMOTE-lbl-poi", "0.2"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.5),
                        new SMOTEStrategy(500, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED_POISSON,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(1.0)
                                .setFixedNumNeighbors(50)
                                .setIntensity(2),
                        new WindowedErrorIndicator(500)
                ),
                "SMOTE-lbl-poi", "0.5"
        ));

        return rows;
    }
}
