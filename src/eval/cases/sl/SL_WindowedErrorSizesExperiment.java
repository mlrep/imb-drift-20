package eval.cases.sl;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import detect.WindowedErrorIndicator;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.InformedFramework;
import moa.classifiers.Classifier;
import moa.classifiers.trees.HoeffdingAdaptiveTree;
import strategies.sl.SelfLabelingStrategy;
import strategies.sl.SelfLabelingStrategyType;

import java.util.ArrayList;
import java.util.List;

public class SL_WindowedErrorSizesExperiment extends Experiment {

    public SL_WindowedErrorSizesExperiment(String inputDir, String outputDir) {
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
                new InformedFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.CDDM),
                        new WindowedErrorIndicator(10),
                        false
                ),
                "cWin", "10"
        ));
        rows.add(new ExperimentRow(
                new InformedFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.CDDM),
                        new WindowedErrorIndicator(100),
                        false
                ),
                "cWin", "100"
        ));
        rows.add(new ExperimentRow(
                new InformedFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.CDDM),
                        new WindowedErrorIndicator(1000),
                        false
                ),
                "cWin", "1000"
        ));

        return rows;
    }
}
