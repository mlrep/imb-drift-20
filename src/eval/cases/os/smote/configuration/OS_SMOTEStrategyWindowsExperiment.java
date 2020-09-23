package eval.cases.os.smote.configuration;
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

public class OS_SMOTEStrategyWindowsExperiment extends Experiment {

    public OS_SMOTEStrategyWindowsExperiment(String inputDir, String outputDir) {
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
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new SMOTEStrategy(100, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED_UNIFORM,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(0.6)
                                .setFixedNumNeighbors(10),
                        new WindowedErrorIndicator()
                ),
                "SMOTE", "w=100"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new SMOTEStrategy(1000, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED_UNIFORM,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(0.6)
                                .setFixedNumNeighbors(10),
                        new WindowedErrorIndicator()
                ),
                "SMOTE", "w=1000"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new SMOTEStrategy(10000, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED_UNIFORM,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setLuRatioThreshold(0.6)
                                .setFixedNumNeighbors(10),
                        new WindowedErrorIndicator()
                ),
                "SMOTE", "w=10000"
        ));

        return rows;
    }
}

