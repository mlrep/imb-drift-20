package eval.cases.al;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.BlindFramework;
import moa.classifiers.Classifier;
import moa.classifiers.trees.HoeffdingAdaptiveTree;
import java.util.ArrayList;
import java.util.List;

public class AL_UncertaintyStrategiesBudgetExperiment extends Experiment {

    public AL_UncertaintyStrategiesBudgetExperiment(String inputDir, String outputDir) {
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
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 1.0)
                ),
                "1.0"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.5)
                ),
                "0.5"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.2)
                ),
                "0.2"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1)
                ),
                "0.1"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.05)
                ),
                "0.0"
        ));

        return rows;
    }
}
