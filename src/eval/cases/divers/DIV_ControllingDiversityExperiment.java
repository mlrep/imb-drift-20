package eval.cases.divers;

import cls.DecisionScheme;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import cls.SimpleEnsemble;
import clust.KMeans;
import detect.WindowedErrorIndicator;
import strategies.divers.BaggingStrategy;
import strategies.divers.ClusteringStrategy;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.BlindFramework;
import framework.DiversityFramework;
import moa.classifiers.Classifier;
import strategies.os.SingleExpositionStrategy;
import strategies.os.strategies.GenerationsNumStrategy;

import java.util.ArrayList;
import java.util.List;

public class DIV_ControllingDiversityExperiment extends Experiment {

    public DIV_ControllingDiversityExperiment(String inputDir, String outputDir) {
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
                new DiversityFramework(
                        new SimpleEnsemble(cls.copy()).setDecisionScheme(DecisionScheme.WEIGHTED),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new ClusteringStrategy(new KMeans(10)).setBeta(0.8),
                        new WindowedErrorIndicator(100)
                ),
                "ENS", "CLUST-DIV"
        ));

        rows.add(new ExperimentRow(
                new DiversityFramework(
                        new SimpleEnsemble(cls.copy()).setDecisionScheme(DecisionScheme.WEIGHTED),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new ClusteringStrategy(new KMeans(10)).setBeta(-0.8),
                        new WindowedErrorIndicator(100)
                ),
                "ENS", "CLUST-STAB"
        ));

        rows.add(new ExperimentRow(
                new DiversityFramework(
                        new SimpleEnsemble(10, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new BaggingStrategy().setBeta(0.8),
                        new WindowedErrorIndicator(100)
                ),
                "ENS", "BAG-DIV"
        ));

        rows.add(new ExperimentRow(
                new DiversityFramework(
                        new SimpleEnsemble(10, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new BaggingStrategy().setBeta(-0.8),
                        new WindowedErrorIndicator(100)
                ),
                "ENS", "BAG-STAB"
        ));

        return rows;
    }
}
