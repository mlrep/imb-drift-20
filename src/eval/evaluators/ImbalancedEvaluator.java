package eval.evaluators;

import com.github.javacliparser.IntOption;
import com.yahoo.labs.samoa.instances.Instance;
import eval.Evaluator;
import eval.experiment.ExperimentResult;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.Framework;
import framework.OversamplingFramework;
import moa.core.InstanceExample;
import moa.evaluation.BasicClassificationPerformanceEvaluator;
import moa.evaluation.WindowClassificationPerformanceEvaluator;
import moa.streams.ArffFileStream;
import utils.ResourcesMetrics;
import utils.windows.MulticlassGMean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class ImbalancedEvaluator implements Evaluator {

    public ExperimentResult evaluate(ExperimentRow experimentRow, ExperimentStream experimentStream) {
        System.out.print("\n" + new Date() + ": [" + experimentStream.streamName + " & " + experimentRow.label +
                (experimentRow.subLabel.isEmpty() ? "" : "#" + experimentRow.subLabel) + "] ");

        ExperimentResult result = new ExperimentResult(experimentRow.label, experimentRow.subLabel);
        result.seriesMeasurements.put(ExperimentResult.ACCURACY_SERIES, new ArrayList<>());
        result.seriesMeasurements.put(ExperimentResult.G_MEAN_SERIES, new ArrayList<>());
        ArffFileStream stream = experimentStream.stream;
        Framework framework = experimentRow.framework.setInitInstances(
                experimentRow.framework.omitInit ? 0 : (int)(0.05 * experimentStream.streamSize)
        );
        framework.classifier.setModelContext(stream.getHeader());

        if (framework instanceof OversamplingFramework) ((OversamplingFramework) framework).setFixedClassRatios(experimentStream.classRatios);

        if (!framework.ready) {
            System.out.println("Framework is not prepared, you need to reset it before using");
            return result;
        }

        WindowClassificationPerformanceEvaluator windowMeasurements = new WindowClassificationPerformanceEvaluator();
        windowMeasurements.widthOption = new IntOption("width", 'w', "Size of Window", 1000);
        BasicClassificationPerformanceEvaluator averageMeasurements = new BasicClassificationPerformanceEvaluator();
        MulticlassGMean gMean = new MulticlassGMean(1000, stream.getHeader().numClasses());
        ResourcesMetrics rm = new ResourcesMetrics();

        if (framework.collectTrackableParameters) {
            for (String key : framework.getParameterNames()) {
                result.seriesMeasurements.put(key, new ArrayList<>());
            }
        }

        int i = 0;
        int log = (int)(experimentStream.streamSize / 10.0);

        while (stream.hasMoreInstances()) {
            Instance instance = stream.nextInstance().getData();
            if ((i % log) == 0) System.out.print("#");

            rm.startTimer();
            double[] votes = framework.classifier.getVotesForInstance(instance);
            rm.addClassificationTimeMeasurement(rm.stopTimer());

            averageMeasurements.addResult(new InstanceExample(instance), votes);
            windowMeasurements.addResult(new InstanceExample(instance), votes);
            gMean.addResult(new InstanceExample(instance), votes);

            if (framework.activeLearningStrategy == null || i >= framework.initInstances) {
                result.seriesMeasurements.get(ExperimentResult.ACCURACY_SERIES).add(windowMeasurements.getFractionCorrectlyClassified());

                double gMeanVal = gMean.getWindowGMean();
                if (gMeanVal >= 0)
                    result.seriesMeasurements.get(ExperimentResult.G_MEAN_SERIES).add(gMeanVal);

                if (framework.collectTrackableParameters) {
                    for (Map.Entry<String, Double> entry : framework.getTrackableParameters(instance).entrySet()) {
                        result.seriesMeasurements.get(entry.getKey()).add(entry.getValue());
                    }
                }
            }

            rm.startTimer();
            framework.update(instance.copy(), votes, i);
            rm.addUpdateTimeMeasurement(rm.stopTimer());

            i++;
        }

        result.averageMeasurements.put(ExperimentResult.ACCURACY, averageMeasurements.getFractionCorrectlyClassified());
        result.averageMeasurements.put(ExperimentResult.G_MEAN, gMean.getGlobalGMean());

        result.seriesMeasurements.put(ExperimentResult.CLASSIFICATION_TIME_SERIES, rm.getClassificationTimeMeasurements());
        result.seriesMeasurements.put(ExperimentResult.UPDATE_TIME_SERIES, rm.getUpdateTimeMeasurements());
        result.averageMeasurements.put(ExperimentResult.CLASSIFICATION_TIME, rm.getAvgClassificationTime());
        result.averageMeasurements.put(ExperimentResult.UPDATE_TIME, rm.getAvgUpdateTime());

        result.queriesFactor = (double) framework.activeLearningStrategy.labeledInstances / (i - framework.initInstances);
        //System.out.println("Queried: " + result.queriesFactor);
        framework.ready = false;

        return result;
    }
}
