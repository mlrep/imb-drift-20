package framework;

import com.yahoo.labs.samoa.instances.Instances;
import moa.core.Utils;
import strategies.al.ActiveLearningStrategy;
import cls.SimpleEnsemble;
import com.yahoo.labs.samoa.instances.Instance;
import detect.DriftDetectionMethod;
import detect.StreamStateType;
import strategies.divers.DiversityStrategy;
import moa.classifiers.Classifier;
import strategies.os.OversamplingStrategy;
import utils.Trackable;
import utils.TrackableFramework;

import java.util.ArrayList;
import java.util.HashMap;

public class DiversityFramework extends Framework implements TrackableFramework {

    private DiversityStrategy diversityStrategy;
    private DriftDetectionMethod driftDetector;
    private OversamplingStrategy oversamplingStrategy;
    private boolean oversamplingUpdateFirst = false;

    public DiversityFramework(Classifier classifier, ActiveLearningStrategy activeLearningStrategy, DiversityStrategy diversityStrategy,
                       DriftDetectionMethod driftDetector) {
        this.classifier = classifier;
        this.classifier.prepareForUse();
        this.activeLearningStrategy = activeLearningStrategy;
        this.diversityStrategy = diversityStrategy;
        this.driftDetector = driftDetector;
    }

    public DiversityFramework(Classifier classifier, ActiveLearningStrategy activeLearningStrategy, DiversityStrategy diversityStrategy,
                              DriftDetectionMethod driftDetector, OversamplingStrategy oversamplingStrategy) {
        this.classifier = classifier;
        this.classifier.prepareForUse();
        this.activeLearningStrategy = activeLearningStrategy;
        this.diversityStrategy = diversityStrategy;
        this.driftDetector = driftDetector;
        this.oversamplingStrategy = oversamplingStrategy;
        this.oversamplingUpdateFirst = oversamplingStrategy.getUpdateFirst();
    }

    @Override
    public void reset() {
        this.classifier.resetLearning();
        this.diversityStrategy.reset();
        this.driftDetector.reset();
        this.ready = true;
    }

    @Override
    public void update(Instance instance, double[] votes, int n) {
        if (!this.omitInit && n <= this.initInstances) {
            this.diversityStrategy.update(instance, (SimpleEnsemble)this.classifier, this.driftDetector.getDetectorIndicators()); // todo: max val?
            this.diversityStrategy.diversify(instance, (SimpleEnsemble)this.classifier);
            this.activeLearningStrategy.update(instance, true);
            this.driftDetector.update(Utils.maxIndex(votes), (int)instance.classValue(), instance.numClasses());
            if (this.oversamplingStrategy != null) this.oversamplingStrategy.updateLabeled(instance);
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

            this.diversityStrategy.update(instance, (SimpleEnsemble)this.classifier, driftIndicators);

            if (this.oversamplingStrategy != null) {
                if (this.oversamplingUpdateFirst) this.oversamplingStrategy.updateLabeled(instance);

                Instances generatedInstances = this.oversamplingStrategy.generateInstances(instance, driftIndicators);
                if (generatedInstances != null) {
                    for (int i = 0; i < generatedInstances.numInstances(); i++) {
                        this.diversityStrategy.diversify(generatedInstances.get(i), (SimpleEnsemble)this.classifier);
                    }
                }

                if (!this.oversamplingUpdateFirst) this.oversamplingStrategy.updateLabeled(instance);

            } else {
                this.diversityStrategy.diversify(instance, (SimpleEnsemble)this.classifier);
            }
        }
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance) {
        if (!this.collectTrackableParameters) return null;
        else {
            HashMap<String, Double> parameters = new HashMap<>();
            HashMap<String, Double> driftIndicators = this.driftDetector.getDetectorIndicators();
            parameters.putAll(this.diversityStrategy.getTrackableParameters(instance, driftIndicators));
            parameters.putAll(((Trackable) this.classifier).getTrackableParameters(instance, driftIndicators));
            parameters.putAll(this.activeLearningStrategy.getTrackableParameters(instance, driftIndicators));

            return parameters;
        }
    }

    @Override
    public ArrayList<String> getParameterNames() {
        if (!this.collectTrackableParameters) return null;
        else {
            ArrayList<String> parameterNames = new ArrayList<>(this.diversityStrategy.getParameterNames());
            if (this.classifier instanceof Trackable)
                parameterNames.addAll(((Trackable) this.classifier).getParameterNames());
            parameterNames.addAll(this.activeLearningStrategy.getParameterNames());

            return parameterNames;
        }
    }
}
