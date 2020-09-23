package cls;

import com.github.javacliparser.IntOption;
import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.Classifier;
import moa.core.InstanceExample;
import moa.core.Measurement;
import moa.core.MiscUtils;
import moa.evaluation.WindowAUCImbalancedPerformanceEvaluator;
import utils.Trackable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class BalancingBagging extends AbstractClassifier implements Trackable {

    private SimpleEnsemble uobEnsemble;
    private SimpleEnsemble oobEnsemble;

    private double firstClassSize = 0.0;
    private double secondClassSize = 0.0;
    private double decayFactor = 0.9;

    private WindowAUCImbalancedPerformanceEvaluator uobGMean;
    private WindowAUCImbalancedPerformanceEvaluator oobGMean;

    private HashMap<String, Double> trackableParameters = new HashMap<>();

    public BalancingBagging(int size, Classifier classifierTemplate, int gMeanSize) {
        this.uobEnsemble = new SimpleEnsemble(size, classifierTemplate.copy());
        this.oobEnsemble = new SimpleEnsemble(size, classifierTemplate.copy());

        this.uobGMean = new WindowAUCImbalancedPerformanceEvaluator();
        this.uobGMean.widthOption = new IntOption("width", 'w', "Size of Window", gMeanSize);
        this.oobGMean = new WindowAUCImbalancedPerformanceEvaluator();
        this.oobGMean.widthOption = new IntOption("width", 'w', "Size of Window", gMeanSize);

        this.classifierRandom = new Random();
    }

    @Override
    public void resetLearningImpl() {
        this.uobEnsemble.resetLearningImpl();
        this.oobEnsemble.resetLearningImpl();
        this.firstClassSize = 0.0;
        this.secondClassSize = 0.0;
        this.uobGMean.reset(2);
        this.oobGMean.reset(2);
        this.trackableParameters.clear();
    }

    @Override
    public void trainOnInstanceImpl(Instance instance) {
        int classLabel = (int)instance.classValue();
        if (classLabel == 0) {
            this.firstClassSize = decayFactor * this.firstClassSize + 1 * (1 - this.decayFactor);
            this.secondClassSize = decayFactor * this.secondClassSize;
        }
        else {
            this.secondClassSize = decayFactor * this.secondClassSize + 1 * (1 - this.decayFactor);
            this.firstClassSize = decayFactor * this.firstClassSize;
        }

        this.uobGMean.addResult(new InstanceExample(instance), this.uobEnsemble.getVotesForInstance(instance));
        this.oobGMean.addResult(new InstanceExample(instance), this.oobEnsemble.getVotesForInstance(instance));

        this.trainOobEnsemble(instance, classLabel);
        this.trainUobEnsemble(instance, classLabel);
    }

    private void trainOobEnsemble(Instance instance, int classLabel) {
        for (int i = 0; i < this.oobEnsemble.getSize(); i++) {
            double lambda = getLambda(classLabel, this.firstClassSize < this.secondClassSize, this.firstClassSize > this.secondClassSize);
            int k = MiscUtils.poisson(lambda, this.classifierRandom);

            for (int j = 0; j < k; j++) {
                this.oobEnsemble.trainClassifierOnInstance(i, instance);
            }
        }
    }

    private void trainUobEnsemble(Instance instance, int classLabel) {
        for (int i = 0; i < this.uobEnsemble.getSize(); i++) {
            double lambda = getLambda(classLabel, this.firstClassSize > this.secondClassSize, this.firstClassSize < this.secondClassSize);
            int k = MiscUtils.poisson(lambda, this.classifierRandom);

            for (int j = 0; j < k; j++) {
                this.uobEnsemble.trainClassifierOnInstance(i, instance);
            }
        }
    }

    private double getLambda(int classLabel, boolean b1, boolean b2) {
        double lambda = 1.0;
        if ((classLabel == 0) && b1) {
            lambda = this.secondClassSize / this.firstClassSize;
        } else if ((classLabel == 1) && b2) {
            lambda = this.firstClassSize / this.secondClassSize;
        }

        return lambda;
    }

    @Override
    public double[] getVotesForInstance(Instance instance) {
        double uobWeight = this.uobGMean.getAucEstimator().getGMean();
        double oobWeight = this.oobGMean.getAucEstimator().getGMean();

        if (uobWeight > oobWeight) return this.uobEnsemble.getVotesForInstance(instance);
        else return this.oobEnsemble.getVotesForInstance(instance);
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
        this.oobEnsemble.prepareForUse();
        this.uobEnsemble.prepareForUse();
    }

    public BalancingBagging setDecayFactor(double decayFactor) {
        this.decayFactor = decayFactor;
        return this;
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators) {
        this.trackableParameters.put("c1_size", this.firstClassSize);
        this.trackableParameters.put("c2_size", this.secondClassSize);
        return this.trackableParameters;
    }

    @Override
    public ArrayList<String> getParameterNames() {
        return new ArrayList<>(Arrays.asList("c1_size", "c2_size"));
    }
}
