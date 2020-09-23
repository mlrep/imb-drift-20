package strategies.os;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import moa.classifiers.core.driftdetection.ADWIN;
import strategies.os.strategies.GenerationsNumStrategy;
import strategies.os.strategies.ProbabilisticStrategyType;
import utils.MathUtils;
import utils.Trackable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ProbabilisticWindowStrategy extends SingleExpositionStrategy implements Trackable {

    private Instances windowInstances;
    private ProbabilisticStrategyType probabilisticStrategyType;

    private HashMap<String, Double> trackableParameters = new HashMap<>();

    public ProbabilisticWindowStrategy(int windowSize, ProbabilisticStrategyType probabilisticStrategyType) {
        super(GenerationsNumStrategy.FIXED);
        this.windowSize = windowSize;
        this.probabilisticStrategyType = probabilisticStrategyType;

    }

    @Override
    public void reset() {
        if (this.windowInstances != null) this.windowInstances.delete();
    }

    @Override
    public void updateLabeled(Instance instance) {
        if (this.windowInstances == null) this.init(instance);
        else if (this.windowInstances.numInstances() == windowSize) windowInstances.delete(0);
        windowInstances.add(instance);
        if (this.collectProportions) this.updateLabeledProportions(instance, (int)instance.classValue());
    }

    private void init(Instance instanceTemplate) {
        this.windowInstances = new Instances(instanceTemplate.copy().dataset());
    }

    @Override
    public void updateUnlabeled(Instance instance, HashMap<String, Double> driftIndicators, double predictionValue) {}

    @Override
    public Instances generateInstances(Instance instanceTemplate, HashMap<String, Double> driftIndicators) {
        Instances generatedInstances = new Instances(instanceTemplate.dataset());

        for (int i = 0; i < this.windowInstances.numInstances(); i++) {
            double x = (double)(i + 1) / this.windowInstances.numInstances();
            boolean add;

            switch (this.probabilisticStrategyType) {
                case SIGMOID: add = (Math.random() < MathUtils.sigmoid(x, 0.5)); break;
                case UNIFORM: add = (Math.random() < 1.0 / this.windowInstances.numInstances()); break;
                default: add = false;
            }

            if (add) {
                int intensity = super.intensityStrategy((int)instanceTemplate.classValue(), driftIndicators);
                for (int j = 0; j < intensity; j++) {
                    generatedInstances.add(this.windowInstances.get(i).copy());
                }
            }
        }

        return generatedInstances;
    }

    @Override
    public boolean getUpdateFirst() {
        return true;
    }

    public ProbabilisticWindowStrategy setIntensity(int intensity) {
        this.intensity = intensity;
        return this;
    }

    public int getNumOfInstances() {
        return this.windowInstances == null ? 0 : this.windowInstances.numInstances();
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators) {
        this.trackableParameters.put("instances", this.windowInstances == null ? 0 : (double)this.windowInstances.numInstances());
        return this.trackableParameters;
    }

    @Override
    public ArrayList<String> getParameterNames() {
        return new ArrayList<>(Collections.singletonList("instances"));
    }
}
