package eval.cases.imb;
import cls.BalancingType;
import cls.MulticlassBalancingBagging;
import eval.experiment.ExperimentRow;
import framework.BlindFramework;
import moa.classifiers.Classifier;
import strategies.al.UncertaintyStrategy;
import strategies.al.UncertaintyStrategyType;

import java.util.ArrayList;
import java.util.List;

public class ALI_OOBEnsembleSizes extends ALI_BudgetsExperiment {

    public ALI_OOBEnsembleSizes(String inputDir, String outputDir) {
        super(inputDir, outputDir);
    }

    @Override
    public List<ExperimentRow> createExperimentRows() {
        Classifier cls = ExperimentRow.getExperimentClassifier();
        List<ExperimentRow> rows = new ArrayList<>();

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(5, BalancingType.OVERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "OMBB5-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(10, BalancingType.OVERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "OMBB10-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(20, BalancingType.OVERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "OMBB20-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(30, BalancingType.OVERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "OMBB30-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(50, BalancingType.OVERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "OMBB50-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(5, BalancingType.UNDERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "UMBB5-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(10, BalancingType.UNDERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "UMBB10-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(20, BalancingType.UNDERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "UMBB20-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(30, BalancingType.UNDERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "UMBB30-" + this.budget
        ));

        rows.add(new ExperimentRow(
                new BlindFramework(
                        new MulticlassBalancingBagging(50, BalancingType.UNDERSAMPLING, cls.copy()),
                        new UncertaintyStrategy(UncertaintyStrategyType.RAND_VARIABLE, budget)
                ),
                "ALI", "UMBB50-" + this.budget
        ));

        return rows;
    }
}
