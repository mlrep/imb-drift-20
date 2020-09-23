package framework;

import moa.core.Utils;
import strategies.al.ActiveLearningStrategy;
import com.yahoo.labs.samoa.instances.Instances;
import detect.DriftDetectionMethod;
import detect.StreamStateType;
import strategies.os.OversamplingStrategy;
import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.Classifier;
import utils.ClassifierUtils;
import utils.Trackable;
import utils.TrackableFramework;

import java.util.ArrayList;
import java.util.HashMap;

public class OversamplingFramework extends Framework implements TrackableFramework {

    private OversamplingStrategy oversamplingStrategy;
    private DriftDetectionMethod driftDetector;
    private boolean updateFirst;

    public OversamplingFramework(Classifier classifier, ActiveLearningStrategy activeLearningStrategy,
                                 OversamplingStrategy oversamplingStrategy, DriftDetectionMethod driftDetector) {
        this.classifier = classifier;
        this.classifier.prepareForUse();
        this.activeLearningStrategy = activeLearningStrategy;
        this.oversamplingStrategy = oversamplingStrategy;
        this.driftDetector = driftDetector;
        this.updateFirst = this.oversamplingStrategy.getUpdateFirst();
    }

    @Override
    public void reset() {
        this.classifier.resetLearning();
        this.activeLearningStrategy.reset();
        this.oversamplingStrategy.reset();
        this.driftDetector.reset();
        this.ready = true;
    }

    @Override
    public void update(Instance instance, double[] votes, int n) {
        if (!this.omitInit && n <= this.initInstances) {
            this.classifier.trainOnInstance(instance);
            this.activeLearningStrategy.update(instance, true);
            this.oversamplingStrategy.updateLabeled(instance);
            this.driftDetector.update(Utils.maxIndex(votes), (int)instance.classValue(), instance.numClasses());
            return;
        }

        HashMap<String, Double> driftIndicators = this.driftDetector.getDetectorIndicators();
        boolean learnActively = false;

        if (this.activeLearningStrategy != null) {
            learnActively = this.activeLearningStrategy.queryLabel(instance, votes, driftIndicators);
            this.activeLearningStrategy.update(instance, learnActively);
        }

        if (learnActively) {
            this.driftDetector.update(Utils.maxIndex(votes), (int)instance.classValue(), instance.numClasses());
            if ((int)this.driftDetector.checkState() == StreamStateType.DRIFT.ordinal()) this.driftDetector.reset();
            this.classifier.trainOnInstance(instance);

            if (this.updateFirst) this.oversamplingStrategy.updateLabeled(instance);
            Instances generatedInstances = this.oversamplingStrategy.generateInstances(instance, driftIndicators);

            if (generatedInstances != null) {
                for (int i = 0; i < generatedInstances.numInstances(); i++) {
                    this.classifier.trainOnInstance(generatedInstances.get(i));
                }
            }

            if (!updateFirst) this.oversamplingStrategy.updateLabeled(instance);
        } else {
            this.oversamplingStrategy.updateUnlabeled(instance, driftIndicators, ClassifierUtils.combinePredictionsMax(votes));
        }
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance) {
        if (!this.collectTrackableParameters) return null;
        else {
            HashMap<String, Double> parameters = new HashMap<>();
            HashMap<String, Double> driftIndicators = this.driftDetector.getDetectorIndicators();
            if (this.classifier instanceof Trackable) parameters.putAll(((Trackable) this.classifier).getTrackableParameters(instance, driftIndicators));
            parameters.putAll(this.activeLearningStrategy.getTrackableParameters(instance, driftIndicators));
            parameters.putAll(this.oversamplingStrategy.getTrackableParameters(instance, driftIndicators));

            return parameters;
        }
    }

    @Override
    public ArrayList<String> getParameterNames() {
        if (!this.collectTrackableParameters) return null;
        else {
            ArrayList<String> parameterNames = new ArrayList<>(this.activeLearningStrategy.getParameterNames());
            if (this.classifier instanceof Trackable) parameterNames.addAll(((Trackable) this.classifier).getParameterNames());
            parameterNames.addAll(this.driftDetector.getParameterNames());
            parameterNames.addAll(this.oversamplingStrategy.getParameterNames());

            return parameterNames;
        }
    }

    public void setFixedClassRatios(ArrayList<Double> fixedClassRatios) {
        this.oversamplingStrategy.setFixedClassRatios(fixedClassRatios);
    }

}
