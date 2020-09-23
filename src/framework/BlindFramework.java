package framework;

import strategies.al.ActiveLearningStrategy;
import cls.SimpleEnsemble;
import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.Classifier;
import moa.core.Utils;
import strategies.sl.SelfLabelingStrategy;
import utils.Trackable;
import utils.TrackableFramework;

import java.util.ArrayList;
import java.util.HashMap;

public class BlindFramework extends Framework implements TrackableFramework {

    public BlindFramework(Classifier classifier, ActiveLearningStrategy activeLearningStrategy,
                     SelfLabelingStrategy selfLabelingStrategy) {
        this.classifier = classifier;
        this.classifier.prepareForUse();
        this.activeLearningStrategy = activeLearningStrategy;
        this.selfLabelingStrategy = selfLabelingStrategy;
    }

    public BlindFramework(Classifier classifier, ActiveLearningStrategy activeLearningStrategy) {
        this.classifier = classifier; this.classifier.prepareForUse();
        this.activeLearningStrategy = activeLearningStrategy;
    }

    @Override
    public void reset() {
        this.classifier.resetLearning();
        if (this.activeLearningStrategy != null) this.activeLearningStrategy.reset();
        if (this.selfLabelingStrategy != null) this.selfLabelingStrategy.reset();
        this.ready = true;
    }

    @Override
    public void update(Instance instance, double[] votes, int i) {
        if (!this.omitInit && i <= this.initInstances) {
            this.classifier.trainOnInstance(instance);
            this.activeLearningStrategy.update(instance, true);
            return;
        }

        HashMap<String, Double> activeLearningFactors = null;
        boolean learnActively = false;

        if (this.activeLearningStrategy != null) {
            learnActively = this.activeLearningStrategy.queryLabel(instance, votes, null);
            this.activeLearningStrategy.update(instance, learnActively);
            activeLearningFactors = this.activeLearningStrategy.getTrackableParameters(instance, null);
        }

        if (learnActively) this.classifier.trainOnInstance(instance);
        else if (this.selfLabelingStrategy != null && this.selfLabelingStrategy.assignLabel(instance, votes,
                activeLearningFactors, new HashMap<>())) {
            instance.setClassValue(Utils.maxIndex(votes));
            this.classifier.trainOnInstance(instance);
        }
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance) {
        if (!this.collectTrackableParameters) return null;
        else {
            HashMap<String, Double> parameters = new HashMap<>();
            if (this.classifier instanceof Trackable)
                parameters.putAll(((Trackable) this.classifier).getTrackableParameters(instance, null));
            parameters.putAll(this.activeLearningStrategy.getTrackableParameters(instance, null));

            return parameters;
        }
    }

    @Override
    public ArrayList<String> getParameterNames() {
        if (!this.collectTrackableParameters) return null;
        else {
            ArrayList<String> parameterNames = new ArrayList<>(this.activeLearningStrategy.getParameterNames());
            if (this.classifier instanceof Trackable)
                parameterNames.addAll(((Trackable) this.classifier).getParameterNames());

            return parameterNames;
        }
    }
}
