package eval.evaluators;

import com.github.javacliparser.IntOption;
import com.yahoo.labs.samoa.instances.Instance;
import eval.Evaluator;
import eval.experiment.ExperimentResult;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.Framework;
import moa.core.InstanceExample;
import moa.core.Utils;
import moa.evaluation.BasicAUCImbalancedPerformanceEvaluator;
import moa.evaluation.WindowAUCImbalancedPerformanceEvaluator;
import moa.streams.ArffFileStream;
import strategies.al.BalancingStrategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ImbalancedBinaryEvaluator implements Evaluator {

    public ExperimentResult evaluate(ExperimentRow experimentRow, ExperimentStream experimentStream) {
        System.out.println(new Date() + ": [" + experimentStream.streamName + " & " + experimentRow.label +
                (experimentRow.subLabel.isEmpty() ? "" : "#" + experimentRow.subLabel) + "]");

        ExperimentResult result = new ExperimentResult(experimentRow.label, experimentRow.subLabel);
        result.seriesMeasurements.put(ExperimentResult.ACCURACY_SERIES, new ArrayList<>());
        result.seriesMeasurements.put(ExperimentResult.G_MEAN_SERIES, new ArrayList<>());
        ArffFileStream stream = experimentStream.stream;
        Framework framework = experimentRow.framework.setInitInstances(
                experimentRow.framework.omitInit ? 0 : (int)(0.1 * experimentStream.streamSize)
        );
        framework.classifier.setModelContext(stream.getHeader());

        if (!framework.ready) {
            System.out.println("Framework is not prepared, you need to reset it before using");
            return result;
        }

        BasicAUCImbalancedPerformanceEvaluator averageMeasurements = new BasicAUCImbalancedPerformanceEvaluator();
        averageMeasurements.calculateAUC.set();
        WindowAUCImbalancedPerformanceEvaluator windowMeasurements = new WindowAUCImbalancedPerformanceEvaluator();
        windowMeasurements.widthOption = new IntOption("width", 'w', "Size of Window", 1000);

        if (framework.collectTrackableParameters) {
            for (String key : framework.getParameterNames()) {
                result.seriesMeasurements.put(key, new ArrayList<>());
            }
        }

        int i = 0;
        while (stream.hasMoreInstances()) {
            Instance instance = stream.nextInstance().getData();

            double[] votes = framework.classifier.getVotesForInstance(instance);
            averageMeasurements.addResult(new InstanceExample(instance), votes);
            windowMeasurements.addResult(new InstanceExample(instance), votes);

            if (framework.activeLearningStrategy == null || i >= framework.initInstances) {

                result.seriesMeasurements.get(ExperimentResult.ACCURACY_SERIES).add(windowMeasurements.getAucEstimator().getAccuracy());
                if (!Double.isNaN(windowMeasurements.getAucEstimator().getGMean()))
                    result.seriesMeasurements.get(ExperimentResult.G_MEAN_SERIES).add(windowMeasurements.getAucEstimator().getGMean());

                if (framework.collectTrackableParameters) {
                    for (Map.Entry<String, Double> entry : framework.getTrackableParameters(instance).entrySet()) {
                        result.seriesMeasurements.get(entry.getKey()).add(entry.getValue());
                    }
                }
            }

            framework.update(instance.copy(), votes, i);
            i++;
        }

        result.averageMeasurements.put(ExperimentResult.ACCURACY, averageMeasurements.getAucEstimator().getAccuracy());
        result.averageMeasurements.put(ExperimentResult.RECALL, averageMeasurements.getAucEstimator().getRecall());
        result.averageMeasurements.put(ExperimentResult.KAPPA, averageMeasurements.getAucEstimator().getKappa());
        result.averageMeasurements.put(ExperimentResult.AUC, averageMeasurements.getAucEstimator().getAUC());
        result.averageMeasurements.put(ExperimentResult.AUC_SCORED, averageMeasurements.getAucEstimator().getScoredAUC());
        result.averageMeasurements.put(ExperimentResult.G_MEAN, averageMeasurements.getAucEstimator().getGMean());

        result.queriesFactor = (double) framework.activeLearningStrategy.labeledInstances / (i - framework.initInstances);
        System.out.println("Queried: " + result.queriesFactor);
        framework.ready = false;

        return result;
    }
}
