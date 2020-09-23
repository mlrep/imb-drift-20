package cls;
import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.Classifier;
import moa.core.Measurement;
import moa.core.MiscUtils;
import utils.Trackable;

import java.util.*;

public class MulticlassBalancingBagging extends AbstractClassifier implements Trackable {

    private SimpleEnsemble ensemble;
    private BalancingType type;
    private ArrayList<Double> classSizes;
    private double decayFactor = 0.9;
    private double lastLambda = -1;
    private boolean initialized = false;

    private HashMap<String, Double> trackableParameters = new HashMap<>();

    public MulticlassBalancingBagging(int size, BalancingType type, Classifier classifierTemplate) {
        this.ensemble = new SimpleEnsemble(size, classifierTemplate.copy());
        this.type = type;
        this.classSizes = new ArrayList<>();
        this.classifierRandom = new Random();
    }

    @Override
    public void resetLearningImpl() {
        this.ensemble.resetLearningImpl();
        this.classSizes = new ArrayList<>();
        this.lastLambda = -1;
        this.initialized = false;
        this.trackableParameters.clear();
    }

    @Override
    public void trainOnInstanceImpl(Instance instance) {
        if (!this.initialized) this.init(instance.numClasses());
        int classLabel = (int)instance.classValue();

        for (int i = 0; i < this.classSizes.size(); i++) {
            double cs = this.classSizes.get(i);
            this.classSizes.set(i, cs * this.decayFactor + (1 - this.decayFactor) * (i == classLabel ? 1 : 0));
        }

        if (type == BalancingType.OVERSAMPLING) {
            double maxSize = Collections.max(this.classSizes);
            this.lastLambda = maxSize / (this.classSizes.get(classLabel) + Double.MIN_VALUE);
        } else {
            double minSize = Collections.min(this.classSizes);
            this.lastLambda = minSize / (this.classSizes.get(classLabel) + Double.MIN_VALUE);
        }

        for (int i = 0; i < this.ensemble.getSize(); i++) {
            int k = MiscUtils.poisson(this.lastLambda, this.classifierRandom);
            for (int j = 0; j < k; j++) {
                this.ensemble.trainClassifierOnInstance(i, instance);
            }
        }
    }

    private void init(int numClasses) {
        for (int i = 0; i < numClasses; i++) {
            this.classSizes.add(0.0);
        }
        this.initialized = true;
    }

    @Override
    public double[] getVotesForInstance(Instance instance) {
        return this.ensemble.getVotesForInstance(instance);
    }

    @Override
    protected Measurement[] getModelMeasurementsImpl() {
        return new Measurement[0];
    }

    @Override
    public void getModelDescription(StringBuilder stringBuilder, int i) {}

    @Override
    public boolean isRandomizable() {
        return false;
    }

    @Override
    public void prepareForUse() {
        this.ensemble.prepareForUse();
    }

    public MulticlassBalancingBagging setDecayFactor(double decayFactor) {
        this.decayFactor = decayFactor;
        return this;
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators) {
        this.trackableParameters.put("lambda", this.lastLambda);
        return this.trackableParameters;
    }

    @Override
    public ArrayList<String> getParameterNames() {
        return new ArrayList<>(Collections.singletonList("lambda"));
    }
}
