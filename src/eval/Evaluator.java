package eval;

import eval.cases.divers.DIV_ClusteringCombinationsExperiment;
import eval.cases.divers.DIV_ControllingDiversityExperiment;
import eval.cases.divers.DIV_EnhancingDiversityExperiment;
import eval.cases.imb.ALI_BudgetsExperiment;
import eval.cases.imb.ALI_OOBEnsembleSizes;
import eval.cases.imb.ALI_SimpleExperiment;
import eval.cases.al.AL_RandExperiment;
import eval.cases.al.AL_RandVarExperiment;
import eval.cases.divers.DIV_EnsemblesComparisonExperiment;
import eval.cases.os.prob_win.OS_ProbabilisticWindowSigmoidExperiment;
import eval.cases.os.prob_win.OS_ProbabilisticWindowUniformExperiment;
import eval.cases.os.se.OS_SingleExpositionExperiment;
import eval.cases.os.smote.OS_SMOTELabeledExperiment;
import eval.cases.os.smote.OS_SMOTEPoissonLabeledExperiment;
import eval.evaluators.BalancedEvaluator;
import eval.evaluators.ImbalancedBinaryEvaluator;
import eval.evaluators.ImbalancedEvaluator;
import eval.experiment.ExperimentResult;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import output.ResultProcessor;

import java.util.*;

public interface Evaluator {

    //Random globalRandomSeed = new Random(123);

    static void runBalancingQueryExperiments(String inputDir, String rootOutputDir) {
        Evaluator evaluator = new ImbalancedEvaluator();
        (new ALI_BudgetsExperiment(inputDir, rootOutputDir + "/all")).run(evaluator);
    }

    ExperimentResult evaluate(ExperimentRow experimentRow, ExperimentStream experimentStream);

    static void main(String[] args) {
        System.out.println("Starting for AHT: " + new Date());

        Evaluator.runBalancingQueryExperiments("streams", "results");
        ResultProcessor.mergeResults("results/all", new String[]{"1.0", "0.5", "0.2", "0.1", "0.05", "0.01", "0.005", "0.001"}, "cmp");
        ResultProcessor.concatResults("results/all", "cmp", "summary");

        System.out.println("\nFinished: " + new Date());
        System.exit(0);

    }
}
