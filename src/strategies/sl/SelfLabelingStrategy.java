package strategies.sl;
import com.yahoo.labs.samoa.instances.Instance;
import utils.ClassifierUtils;
import utils.Trackable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SelfLabelingStrategy implements Trackable {

    private double fixedThreshold = 0.9;
    private double variableThreshold = 1.0;
    private double variableThresholdStep = 0.01;

    private SelfLabelingStrategyType strategy;

    HashMap<String, Double> trackableParameters = new HashMap<>();
    private Random random = new Random();

    public SelfLabelingStrategy(SelfLabelingStrategyType strategy) {
        this.strategy = strategy;
    }

    public SelfLabelingStrategy(SelfLabelingStrategyType strategy, double fixedThreshold, double variableThresholdStep) {
        this.strategy = strategy;
        this.fixedThreshold = fixedThreshold;
        this.variableThresholdStep = variableThresholdStep;
    }

    public boolean assignLabel(Instance instance, double[] predictionValues, HashMap<String, Double> activeLearningFactors,
                               HashMap<String, Double> driftIndicators) {
        boolean label;
        double predictionValue = ClassifierUtils.combinePredictionsMax(predictionValues);

        switch(this.strategy) {
            case RANDOM:
                label = this.labelRandom(); break;
            case FIXED:
                label = this.labelFixed(predictionValue); break;
            case UNIFORM:
                label = this.labelUniform(predictionValue); break;
            case UNIFORM_RAND:
                label = this.labelUniformRand(predictionValue); break;
            case INVERTED_UNCERTAINTY:
                label = this.labelInvertedUncertainty(predictionValue, activeLearningFactors.get("varThresh"), instance.numClasses()); break;
            case CDDM:
                label = this.labelContinuousDDM(predictionValue, driftIndicators.get("error"), instance.numClasses()); break;
            case CEDDM:
                label = this.labelContinuousEDDM(predictionValue, driftIndicators.get("similarity"), instance.numClasses()); break;
            default:
                return false;
        }

        return label;
    }

    public void reset() {
        this.variableThreshold = 1.0;
        this.trackableParameters.clear();
    }

    private boolean labelRandom() {
        return (this.random.nextDouble() > this.fixedThreshold);
    }

    private boolean labelFixed(double predictionValue) {
        return (predictionValue > this.fixedThreshold);
    }

    private boolean labelUniform(double predictionValue) {
        if (predictionValue >= this.variableThreshold) {
            this.variableThreshold *= (1 + this.variableThresholdStep);
            return true;
        }

        this.variableThreshold *= (1 - this.variableThresholdStep);
        return false;
    }

    private boolean labelUniformRand(double predictionValue) {
        predictionValue = predictionValue / (this.random.nextGaussian() + 1.0);
        return labelUniform(predictionValue);
    }

    private boolean labelInvertedUncertainty(double predictionValue, double uncertaintyThreshold, int numClasses) {
        this.variableThreshold = 1 - uncertaintyThreshold + 1.0 / numClasses;
        return (predictionValue >= this.variableThreshold);
    }

    private boolean labelContinuousDDM(double predictionValue, double meanError, int numClasses) {
        this.variableThreshold = Math.tanh(2 * (meanError + 1.0 / numClasses));
        return (predictionValue >= this.variableThreshold);
    }

    private boolean labelContinuousEDDM(double predictionValue, double similarity, int numClasses) {
        this.variableThreshold = 10 * (1.0 / numClasses - 1) * similarity + (10 - 9.0 / numClasses);
        return (predictionValue >= this.variableThreshold);
    }

    public SelfLabelingStrategy setFixedThreshold(double fixedThreshold) {
        this.fixedThreshold = fixedThreshold;
        return this;
    }

    public SelfLabelingStrategy setVariableThresholdStep(double variableThresholdStep) {
        this.variableThresholdStep = variableThresholdStep;
        return this;
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators) {
        return this.trackableParameters;
    }

    @Override
    public ArrayList<String> getParameterNames() {
        return new ArrayList<>();
    }
}
