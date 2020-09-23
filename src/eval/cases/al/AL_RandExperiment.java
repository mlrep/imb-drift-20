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

public class AL_RandExperiment extends Experiment {

    public AL_RandExperiment(String inputDir, String outputDir) {
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
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.01)
                ),
                "AL-R", "0.01"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.05)
                ),
                "AL-R", "0.05"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1)
                ),
                "AL-R", "0.1"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.2)
                ),
                "AL-R", "0.2"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.5)
                ),
                "AL-R", "0.5"
        ));

        return rows;
    }
}
