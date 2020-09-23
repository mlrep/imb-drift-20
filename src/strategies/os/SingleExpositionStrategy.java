package strategies.os;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import strategies.os.strategies.GenerationsNumStrategy;
import utils.MathUtils;
import utils.Trackable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class SingleExpositionStrategy extends OversamplingStrategy implements Trackable {

    int intensity = 1;
    protected GenerationsNumStrategy intensityStrategy;
    protected double ratioCoef;
    protected double errorCoef;

    private HashMap<String, Double> trackableParameters = new HashMap<>();

    public SingleExpositionStrategy(GenerationsNumStrategy intensityStrategy) {
        this.intensityStrategy = intensityStrategy;
        if (this.intensityStrategy == GenerationsNumStrategy.RATIO_DRIVEN || this.intensityStrategy == GenerationsNumStrategy.CLASS_ERROR_DRIVEN
                || this.intensityStrategy == GenerationsNumStrategy.HYBRID_RATIO_ERROR) this.collectProportions = true;
    }

    @Override
    public void reset() {
        this.labeledClassProportions.clear();
        this.maxClassProportion = 0.0;
        this.trackableParameters.clear();
    }

    @Override
    public void updateLabeled(Instance instance) {
        if (this.collectProportions) this.updateLabeledProportions(instance, (int)instance.classValue());
    }

    @Override
    public void updateUnlabeled(Instance instance, HashMap<String, Double> driftIndicators, double predictionValue) {}

    @Override
    public Instances generateInstances(Instance instance, HashMap<String, Double> driftIndicators) {
        Instances generatedInstances = new Instances(instance.dataset());
        int intensity = this.intensityStrategy((int)instance.classValue(), driftIndicators);

        for (int j = 0; j < intensity; j++) generatedInstances.add(instance.copy());

        return generatedInstances;
    }

    int intensityStrategy(int classValue, HashMap<String, Double> driftIndicators) {
        switch (this.intensityStrategy) {
            case FIXED: return this.intensity;
            case ERROR_DRIVEN: return this.intensity - (int)(driftIndicators.get("error") * this.intensity);
            case ERROR_DRIVEN_INV: return (int)(Math.ceil(driftIndicators.get("error") * this.intensity));
            case ERROR_SIGM: return (int)(MathUtils.sigmoid(driftIndicators.get("error"), 1 - 2 * driftIndicators.get("error")) * this.intensity);
            case RATIO_DRIVEN: return this.ratioDriven(classValue);
            case CLASS_ERROR_DRIVEN: return this.classErrorDriven(classValue, driftIndicators);
            case HYBRID_RATIO_ERROR: return this.hybridClassErrorDriven(classValue, driftIndicators);
            case FIXED_RATIOS: return this.fixedRatio(classValue);
            default: return -1;
        }
    }

    private int ratioDriven(int classValue) {
        double maxRatio = this.labeledClassProportions.containsKey(classValue) ? this.labeledClassProportions.get(classValue).getAverage() / this.maxClassProportion : 1.0;
        return (int)Math.ceil((1 - maxRatio) * this.intensity);
    }

    private int classErrorDriven(int classValue, HashMap<String, Double> driftIndicators) { // todo: write tests
        double classError = driftIndicators.get(Integer.toString(classValue));
        if (Double.isNaN(classError)) classError = this.labeledClassProportions.get(classValue).getAverage();
        return (int)Math.ceil((1 - classError) * this.intensity);
    }

    private int hybridClassErrorDriven(int classValue, HashMap<String, Double> driftIndicators) {
        return (int)Math.ceil((this.ratioCoef * ratioDriven(classValue) + this.errorCoef * classErrorDriven(classValue, driftIndicators)));
    }

    private int fixedRatio(int classValue) {
        double maxClassRatio = MathUtils.max(this.fixedClassRatios.stream().mapToDouble(Double::doubleValue).toArray());
        double maxRatio = this.fixedClassRatios.get(classValue) / maxClassRatio;
        return (int)(this.intensity * (1 - maxRatio));
    }

    @Override
    public boolean getUpdateFirst() {
        return false;
    }

    public SingleExpositionStrategy setIntensity(int intensity) {
        this.intensity = intensity;
        return this;
    }

    public SingleExpositionStrategy setWindowSize(int windowSize) {
        this.windowSize = windowSize;
        return this;
    }

    public SingleExpositionStrategy setHybridCoefficients(double ratioCoef, double errorCoef) {
        this.ratioCoef = ratioCoef;
        this.errorCoef = errorCoef;
        return this;
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators) {
        this.trackableParameters.put("intensity", (double) this.intensityStrategy((int) instance.classValue(), driftIndicators));
        return this.trackableParameters;
    }

    @Override
    public ArrayList<String> getParameterNames() {
        return new ArrayList<>(Collections.singletonList("intensity"));
    }
}
