package eval.evaluators;
import com.yahoo.labs.samoa.instances.Instance;
import eval.Evaluator;
import eval.experiment.ExperimentResult;
import eval.experiment.ExperimentRow;
import eval.experiment.ExperimentStream;
import framework.Framework;
import moa.streams.ArffFileStream;
import utils.ResourcesMetrics;
import java.util.Date;

public class ResourcesEvaluator implements Evaluator {

    @Override
    public ExperimentResult evaluate(ExperimentRow experimentRow, ExperimentStream experimentStream) {
        System.out.println(new Date() + ": [" + experimentStream.streamName + " & " + experimentRow.label +
                (experimentRow.subLabel.isEmpty() ? "" : "#" + experimentRow.subLabel) + "]");

        ExperimentResult result = new ExperimentResult(experimentRow.label, experimentRow.subLabel);
        ArffFileStream stream = experimentStream.stream;
        Framework framework = experimentRow.framework;
        framework.classifier.setModelContext(stream.getHeader());

        if (!framework.ready) {
            System.out.println("Framework is not prepared, you need to reset it before using");
            return result;
        }

        ResourcesMetrics rm = new ResourcesMetrics().setAllocationMemoryMeasurement(framework.allocatedMemory);

        int i = 0;
        while (stream.hasMoreInstances()) {
            Instance instance = stream.nextInstance().getData();

            rm.startTimer();

            double[] votes = framework.classifier.getVotesForInstance(instance);

            rm.addClassificationTimeMeasurement(rm.stopTimer());

            rm.startMem();
            rm.startTimer();

            framework.update(instance.copy(), votes, i);

            rm.addMemoryUsageMeasurement(rm.stopMem());
            rm.addUpdateTimeMeasurement(rm.stopTimer());

            i++;
        }

        result.seriesMeasurements.put(ExperimentResult.CLASSIFICATION_TIME_SERIES, rm.getClassificationTimeMeasurements());
        result.seriesMeasurements.put(ExperimentResult.UPDATE_TIME_SERIES, rm.getUpdateTimeMeasurements());
        result.seriesMeasurements.put(ExperimentResult.MEMORY_USAGE_SERIES, rm.getMemoryUsageMeasurements());
        result.averageMeasurements.put(ExperimentResult.CLASSIFICATION_TIME, rm.getAvgClassificationTime());
        result.averageMeasurements.put(ExperimentResult.UPDATE_TIME, rm.getAvgUpdateTime());
        result.averageMeasurements.put(ExperimentResult.MEMORY_USAGE, rm.getAvgMemoryUsage());
        result.averageMeasurements.put(ExperimentResult.ALLOCATION_MEMORY_USAGE, rm.getAllocationMemoryUsageMeasurement());

        framework.ready = false;

        return result;
    }
}
