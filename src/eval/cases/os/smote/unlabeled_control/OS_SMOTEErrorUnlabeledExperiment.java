package eval.cases.os.smote.unlabeled_control;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import detect.WindowedErrorIndicator;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.OversamplingFramework;
import moa.classifiers.Classifier;
import strategies.os.SMOTEMeansStrategy;
import strategies.os.strategies.GenerationsNumStrategy;
import strategies.os.strategies.GenerationStrategyType;
import strategies.os.strategies.NeighborsNumStrategyType;
import strategies.os.SMOTEStrategy;
import strategies.os.strategies.UnlabeledControlStrategyType;
import java.util.ArrayList;
import java.util.List;

public class OS_SMOTEErrorUnlabeledExperiment extends Experiment {

    private double budget;

    public OS_SMOTEErrorUnlabeledExperiment(String inputDir, String outputDir) {
        this.inputDir = inputDir;
        this.outputDir = outputDir;
    }

    @Override
    public void run(Evaluator evaluator) {
        double[] budgets = {0.01, 0.1, 0.5};
        for (double budget : budgets) {
            this.budget = budget;
            this.conduct(this.createExperimentRows(), ExperimentStream.createExperimentStreams(this.inputDir), evaluator,
                    this.outputDir + "/" + this.budget);
        }
    }

    @Override
    public List<ExperimentRow> createExperimentRows() {
        Classifier cls = ExperimentRow.getExperimentClassifier();
        List<ExperimentRow> rows = new ArrayList<>();

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, this.budget),
                        new SMOTEStrategy(1000, UnlabeledControlStrategyType.ERROR_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setFixedNumNeighbors(25),
                        new WindowedErrorIndicator((int)(1000*this.budget))
                ),
                "SMOTE-k25fix-error" + "-b" + this.budget, "alone"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, this.budget),
                        new SMOTEStrategy(1000, UnlabeledControlStrategyType.ERROR_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setFixedNumNeighbors(25)
                                .setUnlabeledUncertaintyThreshold(0.95),
                        new WindowedErrorIndicator((int)(1000*this.budget))
                ),
                "SMOTE-k25fix-error" + "-b" + this.budget, "unc95"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, this.budget),
                        new SMOTEMeansStrategy(1000, UnlabeledControlStrategyType.ERROR_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setFixedNumNeighbors(25),
                        new WindowedErrorIndicator((int)(1000*this.budget))
                ),
                "SMOTE-k25fix-error" + "-b" + this.budget, "means"
        ));

        rows.add(new ExperimentRow(
                new OversamplingFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, this.budget),
                        new SMOTEMeansStrategy(1000, UnlabeledControlStrategyType.ERROR_RATIO_THRESHOLD, NeighborsNumStrategyType.FIXED,
                                GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                                .setFixedNumNeighbors(25)
                                .setUnlabeledUncertaintyThreshold(0.95),
                        new WindowedErrorIndicator((int)(1000*this.budget))
                ),
                "SMOTE-k25fix-error" + "-b" + this.budget, "means+unc95"
        ));

        return rows;
    }
}
