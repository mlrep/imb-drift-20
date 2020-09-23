package framework;
import cls.SimpleEnsemble;
import strategies.al.ActiveLearningStrategy;
import com.yahoo.labs.samoa.instances.Instance;
import detect.DriftDetectionMethod;
import detect.StreamStateType;
import moa.classifiers.Classifier;
import moa.core.Utils;
import strategies.sl.SelfLabelingStrategy;
import utils.Trackable;

import java.util.ArrayList;
import java.util.HashMap;

public class InformedFramework extends BlindFramework {

    private DriftDetectionMethod driftDetector;
    private boolean discrete = false;

    public InformedFramework(Classifier classifier, ActiveLearningStrategy activeLearningStrategy,
                     SelfLabelingStrategy selfLabelingStrategy, DriftDetectionMethod driftDetector, boolean discrete) {
        super(classifier, activeLearningStrategy, selfLabelingStrategy);
        this.driftDetector = driftDetector;
        this.discrete = discrete;
    }

    public InformedFramework(Classifier classifier, ActiveLearningStrategy activeLearningStrategy,
                             DriftDetectionMethod driftDetector, boolean discrete) {
        super(classifier, activeLearningStrategy);
        this.driftDetector = driftDetector;
        this.discrete = discrete;
    }

    @Override
    public void reset() {
        this.driftDetector.reset();
        super.reset();
    }

    @Override
    public void update(Instance instance, double[] votes, int instanceNum) {
        if (!this.omitInit && instanceNum < this.initInstances) {
            this.classifier.trainOnInstance(instance);
            this.activeLearningStrategy.update(instance, true);
            this.driftDetector.update(Utils.maxIndex(votes), (int)instance.classValue(), instance.numClasses());
            return;
        }

        HashMap<String, Double> driftIndicators = this.driftDetector.getDetectorIndicators();
        HashMap<String, Double> activeLearningFactors = null;
        boolean learnActively = false;

        if (this.activeLearningStrategy != null) {
            activeLearningFactors = this.activeLearningStrategy.getTrackableParameters(instance, driftIndicators);
            learnActively = this.activeLearningStrategy.queryLabel(instance, votes, driftIndicators);
            this.activeLearningStrategy.update(instance, learnActively);
        }

        if (learnActively) {
            this.driftDetector.update(Utils.maxIndex(votes), (int)instance.classValue(), instance.numClasses());
            if ((int) this.driftDetector.checkState() == StreamStateType.DRIFT.ordinal()) {
                if (this.discrete) this.classifier.resetLearning();
                this.driftDetector.reset();
            }
            this.classifier.trainOnInstance(instance);
        }
        else if (this.selfLabelingStrategy != null && this.selfLabelingStrategy.assignLabel(instance, votes,
                activeLearningFactors, driftIndicators)) {
            instance.setClassValue(Utils.maxIndex(votes));
            this.classifier.trainOnInstance(instance);
        }
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance) {
        if (!this.collectTrackableParameters) return null;
        else {
            HashMap<String, Double> parameters = new HashMap<>();
            HashMap<String, Double> driftIndicators = this.driftDetector.getDetectorIndicators();
            if (this.classifier instanceof Trackable) parameters.putAll(
                    ((Trackable) this.classifier).getTrackableParameters(instance, driftIndicators));
            parameters.putAll(this.activeLearningStrategy.getTrackableParameters(instance, driftIndicators));

            return parameters;
        }
    }

    @Override
    public ArrayList<String> getParameterNames() {
        if (!this.collectTrackableParameters) return null;
        else {
            ArrayList<String> parameterNames = new ArrayList<>(super.getParameterNames());
            parameterNames.addAll(this.driftDetector.getParameterNames());

            return parameterNames;
        }
    }
}
