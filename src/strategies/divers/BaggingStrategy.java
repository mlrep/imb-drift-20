package strategies.divers;

import cls.SimpleEnsemble;
import com.yahoo.labs.samoa.instances.Instance;
import moa.core.MiscUtils;
import utils.MathUtils;
import utils.Trackable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class BaggingStrategy extends DiversityStrategy implements Trackable {

    private double maxLambda = 1.0;
    protected double lambda = -1;
    private double beta = 0.0;

    private HashMap<String, Double> trackableParameters = new HashMap<>();

    public BaggingStrategy() {}

    @Override
    public void reset() {
        this.lambda = -1;
    }

    @Override
    public void update(Instance instance, SimpleEnsemble ensemble, HashMap<String, Double> driftIndicators) {
        this.lambda = this.lambdaStrategy(driftIndicators.get("error"));
    }

    private double lambdaStrategy(double driftDetectorFactor) {
        return this.maxLambda * MathUtils.sigmoid(1 - driftDetectorFactor, this.beta);
    }

    @Override
    public void diversify(Instance instance, SimpleEnsemble ensemble) {
        for (int i = 0; i < ensemble.getSize(); i++) {
            int k = MiscUtils.poisson(this.lambda, this.random);

            for (int j = 0; j < k; j++) {
                ensemble.trainClassifierOnInstance(i, instance);
            }
        }
    }

    public BaggingStrategy setMaxLambda(double lambda) {
        this.maxLambda = lambda;
        return this;
    }

    public BaggingStrategy setBeta(double beta) {
        this.beta = beta;
        return this;
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators) {
        this.trackableParameters.put("lambda", this.lambda);
        return this.trackableParameters;
    }

    @Override
    public ArrayList<String> getParameterNames() {
        return new ArrayList<>(Collections.singletonList("lambda"));
    }
}
