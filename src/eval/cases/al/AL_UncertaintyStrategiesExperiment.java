package eval.cases.al;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.BlindFramework;
import moa.classifiers.trees.HoeffdingAdaptiveTree;

import java.util.ArrayList;
import java.util.List;

public class AL_UncertaintyStrategiesExperiment extends Experiment {

    public AL_UncertaintyStrategiesExperiment(String inputDir, String outputDir) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
    }

    @Override
    public void run(Evaluator evaluator) {
        this.conduct(this.createExperimentRows(), ExperimentStream.createExperimentStreams(this.inputDir), evaluator, this.outputDir);
    }

    @Override
    public List<ExperimentRow> createExperimentRows() {
        List<ExperimentRow> rows = new ArrayList<>();
        rows.add(new ExperimentRow(
                new BlindFramework(
                        new HoeffdingAdaptiveTree(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1)
                ),
                "Random"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        new HoeffdingAdaptiveTree(),
                        new UncertaintyStrategy(UncertaintyStrategyType.VARIABLE, 0.1).setVariableThresholdStep(0.01)
                ),
                "Variable"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        new HoeffdingAdaptiveTree(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.1).setVariableThresholdStep(0.01)
                ),
                "RandVariable"
        ));
        rows.add(new ExperimentRow(
                new BlindFramework(
                        new HoeffdingAdaptiveTree(),
                        new UncertaintyStrategy(UncertaintyStrategyType.SAMPLING, 0.1)
                ),
                "Sampling"
        ));

        return rows;
    }
}
