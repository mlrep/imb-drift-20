package eval.cases.sl;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.BlindFramework;
import moa.classifiers.Classifier;
import moa.classifiers.trees.HoeffdingAdaptiveTree;
import strategies.sl.SelfLabelingStrategy;
import strategies.sl.SelfLabelingStrategyType;

import java.util.ArrayList;
import java.util.List;

public class SL_FixedThresholdsExperiment extends Experiment {

    public SL_FixedThresholdsExperiment(String inputDir, String outputDir) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
    }

    @Override
    public void run(Evaluator evaluator) {
        this.conduct(this.createExperimentRows(), ExperimentStream.createExperimentStreams(this.inputDir), evaluator, this.outputDir);
    }

    @Override
    public List<ExperimentRow> createExperimentRows() {
        Classifier cls = new HoeffdingAdaptiveTree();
        List<ExperimentRow> rows = new ArrayList<>();
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.FIXED).setFixedThreshold(0.6)
                ),
                "Fixed", "0.6"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.FIXED).setFixedThreshold(0.7)
                ),
                "Fixed", "0.7"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.FIXED).setFixedThreshold(0.8)
                ),
                "Fixed", "0.8"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.FIXED).setFixedThreshold(0.9)
                ),
                "Fixed", "0.9"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.FIXED).setFixedThreshold(0.95)
                ),
                "Fixed", "0.95"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.FIXED).setFixedThreshold(0.99)
                ),
                "Fixed", "0.99"
        ));

        return rows;
    }
}
