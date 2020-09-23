package strategies.al;

import com.yahoo.labs.samoa.instances.Instance;
import javafx.util.Pair;
import utils.ClassifierUtils;
import utils.MathUtils;
import utils.Trackable;
import utils.windows.WindowedValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BalancingStrategy extends ActiveLearningStrategy implements Trackable {

    private BalancingStrategyType strategy;

    public BalancingStrategy(int windowSize, BalancingStrategyType strategy, double budget) {
        this.windowSize = windowSize;
        this.strategy = strategy;
        this.budget = budget;
        this.setCollectProportions(true);
    }

    @Override
    public void reset() {
        super.reset();
        this.labeledClassProportions.clear();
        this.missedClassProportions.clear();
    }

    @Override
    public boolean queryLabel(Instance instance, double[] predictionValues, HashMap<String, Double> driftIndicators) {
        this.allInstances++;
        this.currentCost = this.labeledInstances / (double) ++this.iterationCount;
        if (!this.updateMissedProportions((int)instance.classValue())) return false;

        boolean label = false;

        switch (this.strategy) {
            case PROPORTIONAL:
                label = this.labelProportional(predictionValues); break;
            case HYBRID:
                label = this.labelHybrid(predictionValues); break;
            case FIRST:
                label = this.labelFirst(); break;
        }

        if (label) this.labeledInstances++;

        return label;
    }

    private boolean labelProportional(double[] predictionValues) {
        int label = MathUtils.maxPair(predictionValues).getKey();

        if (!labeledClassProportions.containsKey(label)) return true;
        double p = labeledClassProportions.get(label).getAverage();

        return (1 - p) > this.random.nextDouble();
    }

    private boolean labelHybrid(double[] predictionValues) {
        int label = MathUtils.maxPair(predictionValues).getKey();
        double pv = ClassifierUtils.combinePredictionsMax(predictionValues);

        if (!labeledClassProportions.containsKey(label)) return true;
        double p = labeledClassProportions.get(label).getAverage();

        return (1 - pv) * (1 - p) > this.random.nextDouble();
    }

    private boolean labelFirst() {
        return true;
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators) {
        return super.getTrackableParameters(instance, driftIndicators);
    }

    @Override
    public ArrayList<String> getParameterNames() {
        return super.getParameterNames();
    }
}
