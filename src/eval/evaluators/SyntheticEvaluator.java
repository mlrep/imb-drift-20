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
import strategies.divers.DiversityMeasure;
import utils.ConceptDrift;
import utils.DriftingStreamDefinition;
import utils.windows.WindowedValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SyntheticEvaluator implements Evaluator {

    @Override
    public ExperimentResult evaluate(ExperimentRow experimentRow, ExperimentStream experimentStream) {
        System.out.print("\n" + new Date() + ": [" + experimentStream.streamName + " & " + experimentRow.label +
                (experimentRow.subLabel.isEmpty() ? "" : "#" + experimentRow.subLabel) + "] ");

        ExperimentResult result = new ExperimentResult(experimentRow.label, experimentRow.subLabel);
        result.seriesMeasurements.put(ExperimentResult.ACCURACY_SERIES, new ArrayList<>());

        ArffFileStream stream = experimentStream.stream;
        Framework framework = experimentRow.framework.setInitInstances(
                experimentRow.framework.omitInit ? 0 : (int)(0.1 * experimentStream.streamSize)
        );
        framework.classifier.setModelContext(stream.getHeader());

        if (!framework.ready) {
            System.out.println("Framework is not prepared, you need to reset it before using");
            return result;
        }

        DriftingStreamDefinition driftingStreamDefinition = (DriftingStreamDefinition.createDriftDefinitionsMap()).get(experimentStream.streamName);
        ArrayList<ConceptDrift> drifts = driftingStreamDefinition.drifts;

        WindowClassificationPerformanceEvaluator windowMeasurements = new WindowClassificationPerformanceEvaluator();
        windowMeasurements.widthOption = new IntOption("width", 'w', "Size of Window", 1000);
        BasicClassificationPerformanceEvaluator averageMeasurements = new BasicClassificationPerformanceEvaluator();

        if (framework.collectTrackableParameters) {
            for (String key : framework.getParameterNames()) {
                result.seriesMeasurements.put(key, new ArrayList<>());
            }
        }

        WindowedValue disagreementDrifts = new WindowedValue(Integer.MAX_VALUE);
        WindowedValue disagreementStable = new WindowedValue(Integer.MAX_VALUE);
        WindowedValue doubleFaultDrifts = new WindowedValue(Integer.MAX_VALUE);
        WindowedValue doubleFaultStable = new WindowedValue(Integer.MAX_VALUE);
        WindowedValue iaDrifts = new WindowedValue(Integer.MAX_VALUE);
        WindowedValue iaStable = new WindowedValue(Integer.MAX_VALUE);

        int log = (int)(experimentStream.streamSize / 10.0);
        int i = 0;
        int currentDrift = 0;
        ConceptDrift cd = drifts.get(currentDrift);
        System.out.println(i + ": " + cd.concept + " " + cd.newConcept + " " + cd.p + " " + cd.width);

        while (stream.hasMoreInstances()) {
            Instance instance = stream.nextInstance().getData();
            //if ((i % log) == 0) System.out.print("#");

            double[] votes = framework.classifier.getVotesForInstance(instance);
            averageMeasurements.addResult(new InstanceExample(instance), votes);
            windowMeasurements.addResult(new InstanceExample(instance), votes);

            if (framework.activeLearningStrategy == null || i >= framework.initInstances) {
                HashMap<String, Double> parameters = framework.getTrackableParameters(instance);

                result.seriesMeasurements.get(ExperimentResult.ACCURACY_SERIES).add(windowMeasurements.getFractionCorrectlyClassified());

                if (framework.collectTrackableParameters) {
                    for (Map.Entry<String, Double> entry : parameters.entrySet()) {
                        result.seriesMeasurements.get(entry.getKey()).add(entry.getValue());
                    }

                    if (currentDrift < drifts.size() - 1 && i > cd.p + ((drifts.get(currentDrift + 1).p - cd.p) / 2.0)) {
                        cd = drifts.get(++currentDrift);
                        System.out.println(i + ": " + cd.concept + " " + cd.newConcept + " " + cd.p + " " + cd.width);
                    }

                    if (i > cd.p - (cd.width / 2.0) && i < cd.p + (cd.width / 2.0)) {
                        disagreementDrifts.add(parameters.get(DiversityMeasure.DISAGREEMENT));
                        doubleFaultDrifts.add(parameters.get(DiversityMeasure.DOUBLE_FAULT));
                        iaDrifts.add(parameters.get(DiversityMeasure.INTERRATER_AGREEMENT));
                    } else {
                        disagreementStable.add(parameters.get(DiversityMeasure.DISAGREEMENT));
                        doubleFaultStable.add(parameters.get(DiversityMeasure.DOUBLE_FAULT));
                        iaStable.add(parameters.get(DiversityMeasure.INTERRATER_AGREEMENT));
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

        result.averageMeasurements.put(DiversityMeasure.DISAGREEMENT + "_DRIFT", disagreementDrifts.getAverage());
        result.averageMeasurements.put(DiversityMeasure.DISAGREEMENT + "_STABLE", disagreementStable.getAverage());
        result.averageMeasurements.put(DiversityMeasure.DOUBLE_FAULT + "_DRIFT", doubleFaultDrifts.getAverage());
        result.averageMeasurements.put(DiversityMeasure.DOUBLE_FAULT + "_STABLE", doubleFaultStable.getAverage());
        result.averageMeasurements.put(DiversityMeasure.INTERRATER_AGREEMENT + "_DRIFT", iaDrifts.getAverage());
        result.averageMeasurements.put(DiversityMeasure.INTERRATER_AGREEMENT + "_STABLE", iaStable.getAverage());

        result.queriesFactor = (double) framework.activeLearningStrategy.labeledInstances / i;
        framework.ready = false;

        return result;
    }
}
