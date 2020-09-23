package eval.cases.divers;

import cls.DecisionScheme;
import cls.SimpleEnsemble;
import clust.KMeans;
import com.github.javacliparser.IntOption;
import detect.WindowedErrorIndicator;
import eval.Evaluator;
import eval.experiment.Experiment;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.BlindFramework;
import framework.DiversityFramework;
import moa.classifiers.Classifier;
import moa.classifiers.meta.*;
import moa.options.ClassOption;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;
import strategies.divers.BaggingStrategy;
import strategies.divers.ClusteringStrategy;
import strategies.os.SingleExpositionStrategy;
import strategies.os.strategies.GenerationsNumStrategy;
import moa.classifiers.meta.RCD;
import moa.classifiers.meta.OnlineSmoothBoost;

import java.util.ArrayList;
import java.util.List;

public class DIV_EnsemblesComparisonExperiment extends Experiment {

    public DIV_EnsemblesComparisonExperiment(String inputDir, String outputDir) {
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
                        new ClusteringStrategy(new KMeans(10)).setBeta(0.0),
                        new WindowedErrorIndicator(100)
                ),
                "ENS", "CLUST"
        ));

        rows.add(new ExperimentRow(
                new DiversityFramework(
                        new SimpleEnsemble(cls.copy()).setDecisionScheme(DecisionScheme.WEIGHTED),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new ClusteringStrategy(new KMeans(10)).setBeta(0.0),
                        new WindowedErrorIndicator(100),
                        new SingleExpositionStrategy(GenerationsNumStrategy.FIXED).setIntensity(20).setWindowSize(100)
                ),
                "ENS", "CLUST-SE"
        ));

        rows.add(new ExperimentRow(
                new DiversityFramework(
                        new SimpleEnsemble(cls.copy()).setDecisionScheme(DecisionScheme.WEIGHTED),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new ClusteringStrategy(new KMeans(10)).setBeta(0.0),
                        new WindowedErrorIndicator(100),
                        new SingleExpositionStrategy(GenerationsNumStrategy.ERROR_DRIVEN_INV).setIntensity(20).setWindowSize(100)
                ),
                "ENS", "CLUST-SERR"
        ));

        rows.add(new ExperimentRow(
                new DiversityFramework(
                        new SimpleEnsemble(cls.copy()).setDecisionScheme(DecisionScheme.WEIGHTED),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new ClusteringStrategy(new KMeans(10)).setBeta(0.8),
                        new WindowedErrorIndicator(100),
                        new SingleExpositionStrategy(GenerationsNumStrategy.FIXED).setIntensity(20).setWindowSize(100)
                ),
                "ENS", "CLUST-DIV+SE"
        ));

        rows.add(new ExperimentRow(
                new DiversityFramework(
                        new SimpleEnsemble(cls.copy()).setDecisionScheme(DecisionScheme.WEIGHTED),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new ClusteringStrategy(new KMeans(10)).setBeta(0.8),
                        new WindowedErrorIndicator(100),
                        new SingleExpositionStrategy(GenerationsNumStrategy.ERROR_DRIVEN_INV).setIntensity(20).setWindowSize(100)
                ),
                "ENS", "CLUST-DIV+SERR"
        ));

        rows.add(new ExperimentRow(
                new DiversityFramework(
                        new SimpleEnsemble(cls.copy()).setDecisionScheme(DecisionScheme.WEIGHTED),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new ClusteringStrategy(new KMeans(10)).setBeta(-0.8),
                        new WindowedErrorIndicator(100),
                        new SingleExpositionStrategy(GenerationsNumStrategy.FIXED).setIntensity(20).setWindowSize(100)
                ),
                "ENS", "CLUST-STAB+SE"
        ));

        rows.add(new ExperimentRow(
                new DiversityFramework(
                        new SimpleEnsemble(cls.copy()).setDecisionScheme(DecisionScheme.WEIGHTED),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new ClusteringStrategy(new KMeans(10)).setBeta(-0.8),
                        new WindowedErrorIndicator(100),
                        new SingleExpositionStrategy(GenerationsNumStrategy.ERROR_DRIVEN_INV).setIntensity(20).setWindowSize(100)
                ),
                "ENS", "CLUST-STAB+SERR"
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

        rows.add(new ExperimentRow(
                new DiversityFramework(
                        new SimpleEnsemble(10, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1),
                        new BaggingStrategy().setBeta(0.0),
                        new WindowedErrorIndicator(100)
                ),
                "ENS", "BAG"
        ));

        LearnNSE lnse = new LearnNSE();
        lnse.baseLearnerOption = new ClassOption("baseLearner", 'l', "Classifier to train.", Classifier.class, "trees.HoeffdingTree");
        lnse.ensembleSizeOption = new IntOption("ensembleSize", 'e', "Ensemble size.", 10, 1, 2147483647);

        rows.add(new ExperimentRow(
                new BlindFramework(
                        lnse,
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1)
                ),
                "ENS", "LNSE-0.1"
        ));

        DynamicWeightedMajority dwm = new DynamicWeightedMajority();
        dwm.baseLearnerOption = new ClassOption("baseLearner", 'l', "Classifier to train.", Classifier.class, "trees.HoeffdingTree");

        rows.add(new ExperimentRow(
                new BlindFramework(
                        dwm,
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1)
                ),
                "ENS", "DWM-0.1"
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new AccuracyUpdatedEnsemble(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1)
                ),
                "ENS", "AUE-0.1"
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new ADOB(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1)
                ),
                "ENS", "ADOB-0.1"
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new OzaBagASHT(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1)
                ),
                "ENS", "OZABAG-ASHT-0.1"
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new OnlineSmoothBoost(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1)
                ),
                "ENS", "OSB-0.1"
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new RCD(),
                        new UncertaintyStrategy(UncertaintyStrategyType.RANDOM, 0.1)
                ),
                "ENS", "RCD-0.1"
        ));

        return rows;
    }
}
