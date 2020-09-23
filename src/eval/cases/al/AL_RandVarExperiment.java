package eval.cases.al;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.BlindFramework;
import moa.classifiers.Classifier;

import java.util.ArrayList;
import java.util.List;

public class AL_RandVarExperiment extends Experiment {

    public AL_RandVarExperiment(String inputDir, String outputDir) {
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
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.01).setVariableThresholdStep(0.01)
                ),
                "AL-RV", "0.01"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.05).setVariableThresholdStep(0.01)
                ),
                "AL-RV", "0.05"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.1).setVariableThresholdStep(0.01)
                ),
                "AL-RV", "0.1"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.2).setVariableThresholdStep(0.01)
                ),
                "AL-RV", "0.2"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.5).setVariableThresholdStep(0.01)
                ),
                "AL-RV", "0.5"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.FIXED, 1.0).setFixedThreshold(1.0)
                ),
                "AL-RV", "All"
        ));

        return rows;
    }
}
