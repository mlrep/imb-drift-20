package eval.cases.informed.DDM;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import detect.DDM;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.InformedFramework;
import moa.classifiers.Classifier;
import strategies.sl.SelfLabelingStrategy;
import strategies.sl.SelfLabelingStrategyType;
import java.util.ArrayList;
import java.util.List;

public class SL_cDDMExperiment extends Experiment {

    public SL_cDDMExperiment(String inputDir, String outputDir) {
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
                new InformedFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.01).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.CDDM),
                        new DDM(),
                        false
                ),
                "cDDM", "0.01"
        ));
        rows.add(new ExperimentRow(
                new InformedFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.05).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.CDDM),
                        new DDM(),
                        false
                ),
                "cDDM", "0.05"
        ));
        rows.add(new ExperimentRow(
                new InformedFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.1).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.CDDM),
                        new DDM(),
                        false
                ),
                "cDDM", "0.1"
        ));
        rows.add(new ExperimentRow(
                new InformedFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.2).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.CDDM),
                        new DDM(),
                        false
                ),
                "cDDM", "0.2"
        ));
        rows.add(new ExperimentRow(
                new InformedFramework(
                        cls.copy(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, 0.5).setVariableThresholdStep(0.01),
                        new SelfLabelingStrategy(SelfLabelingStrategyType.CDDM),
                        new DDM(),
                        false
                ),
                "cDDM", "0.5"
        ));

        return rows;
    }
}

