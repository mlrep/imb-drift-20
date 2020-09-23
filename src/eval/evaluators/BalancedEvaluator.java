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
import moa.evaluation.BasicClassificationPerformanceEvaluator;
import moa.evaluation.WindowClassificationPerformanceEvaluator;
import moa.streams.ArffFileStream;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class BalancedEvaluator implements Evaluator {

    public ExperimentResult evaluate(ExperimentRow experimentRow, ExperimentStream experimentStream) {
        System.out.print("\n" + new Date() + ": [" + experimentStream.streamName + " & " + experimentRow.label +
                (experimentRow.subLabel.isEmpty() ? "" : "#" + experimentRow.subLabel) + "] ");

        ExperimentResult result = new ExperimentResult(experimentRow.label, experimentRow.subLabel);
        result.seriesMeasurements.put(ExperimentResult.ACCURACY_SERIES, new ArrayList<>());
        result.seriesMeasurements.put(ExperimentResult.KAPPA_SERIES, new ArrayList<>());
        ArffFileStream stream = experimentStream.stream;
        Framework framework = experimentRow.framework.setInitInstances(
                experimentRow.framework.omitInit ? 0 : (int)(0.1 * experimentStream.streamSize)
        );
        framework.classifier.setModelContext(stream.getHeader());

        if (!framework.ready) {
            System.out.println("Framework is not prepared, you need to reset it before using");
            return result;
        }

        WindowClassificationPerformanceEvaluator windowMeasurements = new WindowClassificationPerformanceEvaluator();
        windowMeasurements.widthOption = new IntOption("width", 'w', "Size of Window", 1000);
        BasicClassificationPerformanceEvaluator averageMeasurements = new BasicClassificationPerformanceEvaluator();

        if (framework.collectTrackableParameters) {
            for (String key : framework.getParameterNames()) {
                result.seriesMeasurements.put(key, new ArrayList<>());
            }
        }

        int log = (int)(experimentStream.streamSize / 10.0);
        int i = 0;

        while (stream.hasMoreInstances()) {
            Instance instance = stream.nextInstance().getData();
            if ((i % log) == 0) System.out.print("#");

            double[] votes = framework.classifier.getVotesForInstance(instance);
            averageMeasurements.addResult(new InstanceExample(instance), votes);
            windowMeasurements.addResult(new InstanceExample(instance), votes);

            if (framework.activeLearningStrategy == null || i >= framework.initInstances) {
                result.seriesMeasurements.get(ExperimentResult.ACCURACY_SERIES).add(windowMeasurements.getFractionCorrectlyClassified());
                result.seriesMeasurements.get(ExperimentResult.KAPPA_SERIES).add(windowMeasurements.getKappaStatistic());

                if (framework.collectTrackableParameters) {
                    for (Map.Entry<String, Double> entry : framework.getTrackableParameters(instance).entrySet()) {
                        result.seriesMeasurements.get(entry.getKey()).add(entry.getValue());
                    }
                }
            }

            framework.update(instance.copy(), votes, i);
            i++;
        }

        result.averageMeasurements.put(ExperimentResult.ACCURACY, averageMeasurements.getFractionCorrectlyClassified());
        result.averageMeasurements.put(ExperimentResult.PRECISION, averageMeasurements.getPrecisionStatistic());
        result.averageMeasurements.put(ExperimentResult.RECALL, averageMeasurements.getRecallStatistic());
        result.averageMeasurements.put(ExperimentResult.F1, averageMeasurements.getF1Statistic());
        result.averageMeasurements.put(ExperimentResult.KAPPA, averageMeasurements.getKappaStatistic());

        result.queriesFactor = (double) framework.activeLearningStrategy.labeledInstances / i;
        //System.out.println("Queried: " + result.queriesFactor);
        framework.ready = false;

        return result;
    }
}
