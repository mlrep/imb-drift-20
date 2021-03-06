package eval.cases.os.prob_win;

import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import detect.WindowedErrorIndicator;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.OversamplingFramework;
import moa.classifiers.Classifier;
import strategies.os.ProbabilisticWindowStrategy;
import strategies.os.strategies.ProbabilisticStrategyType;

import java.util.ArrayList;
import java.util.List;

public class OS_ProbabilisticWindowSigmoidExperiment extends Experiment {

    public OS_ProbabilisticWindowSigmoidExperiment(String inputDir, String outputDir) {
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
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.01),
                        new ProbabilisticWindowStrategy(10, ProbabilisticStrategyType.SIGMOID).setIntensity(100),
                        new WindowedErrorIndicator(10)
                ),
                "PW-sigm", "0.01"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.05),
                        new ProbabilisticWindowStrategy(50, ProbabilisticStrategyType.SIGMOID).setIntensity(20),
                        new WindowedErrorIndicator(50)
                ),
                "PW-sigm", "0.05"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.1),
                        new ProbabilisticWindowStrategy(100, ProbabilisticStrategyType.SIGMOID).setIntensity(10),
                        new WindowedErrorIndicator(100)
                ),
                "PW-sigm", "0.1"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.2),
                        new ProbabilisticWindowStrategy(200, ProbabilisticStrategyType.SIGMOID).setIntensity(5),
                        new WindowedErrorIndicator(200)
                ),
                "PW-sigm", "0.2"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.5),
                        new ProbabilisticWindowStrategy(500, ProbabilisticStrategyType.SIGMOID).setIntensity(2),
                        new WindowedErrorIndicator(500)
                ),
                "PW-sigm", "0.5"
        ));

        return rows;
    }
}
