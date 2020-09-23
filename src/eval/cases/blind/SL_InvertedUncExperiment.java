package eval.cases.blind;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.BlindFramework;
import moa.classifiers.Classifier;
import strategies.sl.SelfLabelingStrategy;
import strategies.sl.SelfLabelingStrategyType;
import java.util.ArrayList;
import java.util.List;

public class SL_InvertedUncExperiment extends Experiment {

    public SL_InvertedUncExperiment(String inputDir, String outputDir) {
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
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.01).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.INVERTED_UNCERTAINTY)
                ),
                "InvUnc", "0.01"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.05).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.INVERTED_UNCERTAINTY)
                ),
                "InvUnc", "0.05"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.INVERTED_UNCERTAINTY)
                ),
                "InvUnc", "0.1"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.2).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.INVERTED_UNCERTAINTY)
                ),
                "InvUnc", "0.2"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.5).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.INVERTED_UNCERTAINTY)
                ),
                "InvUnc", "0.5"
        ));

        return rows;
    }
}
