package eval.cases.os.se;

import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import detect.WindowedErrorIndicator;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.OversamplingFramework;
import moa.classifiers.Classifier;
import strategies.os.SingleExpositionStrategy;
import strategies.os.strategies.GenerationsNumStrategy;

import java.util.ArrayList;
import java.util.List;

public class OS_SingleExpositionExperiment extends Experiment {

    public OS_SingleExpositionExperiment(String inputDir, String outputDir) {
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
                        new SingleExpositionStrategy(GenerationsNumStrategy.FIXED).setIntensity(100),
                        new WindowedErrorIndicator(10)
                ),
                "SE", "0.01"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.05),
                        new SingleExpositionStrategy(GenerationsNumStrategy.FIXED).setIntensity(20),
                        new WindowedErrorIndicator(50)
                ),
                "SE", "0.05"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.1),
                        new SingleExpositionStrategy(GenerationsNumStrategy.FIXED).setIntensity(10),
                        new WindowedErrorIndicator(100)
                ),
                "SE", "0.1"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.2),
                        new SingleExpositionStrategy(GenerationsNumStrategy.FIXED).setIntensity(5),
                        new WindowedErrorIndicator(200)
                ),
                "SE", "0.2"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.5),
                        new SingleExpositionStrategy(GenerationsNumStrategy.FIXED).setIntensity(2),
                        new WindowedErrorIndicator(500)
                ),
                "SE", "0.5"
        ));

        return rows;
    }
}
