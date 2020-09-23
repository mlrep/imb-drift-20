package strategies.os;
import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.lazy.neighboursearch.LinearNNSearch;
import strategies.os.strategies.GenerationsNumStrategy;
import strategies.os.strategies.GenerationStrategyType;
import strategies.os.strategies.NeighborsNumStrategyType;
import strategies.os.strategies.UnlabeledControlStrategyType;
import utils.InstanceUtils;
import utils.MathUtils;
import utils.Trackable;

import java.util.HashMap;

public class SMOTEMeansStrategy extends SMOTEStrategy implements Trackable {

    public SMOTEMeansStrategy(int windowSize, UnlabeledControlStrategyType unlabeledControlStrategy, NeighborsNumStrategyType neighborsNumStrategy,
                              GenerationsNumStrategy generationNumStrategy, GenerationStrategyType generationStrategy) {
        super(windowSize, unlabeledControlStrategy, neighborsNumStrategy, generationNumStrategy, generationStrategy);
    }

    @Override
    public void updateUnlabeled(Instance instance, HashMap<String, Double> driftIndicators, double predictionValue) {
        if (this.unlabeledUncertaintyThreshold > predictionValue) return;
        int minDistClassValue = findClosestCentroidClass(instance);

        if (minDistClassValue != -1 && this.unlabeledControlStrategy(this.nnWindowed.get(minDistClassValue).getLuRatio(), driftIndicators)) {
            Instance unlabeledInstance = InstanceUtils.prepareUnlabeled(instance);
            this.nnWindowed.get(minDistClassValue).insert(unlabeledInstance);
        }
    }

    int findClosestCentroidClass(Instance instance) {
        int classValues = instance.numClasses();
        int minDistClassValue = -1;
        double minDist = Double.MAX_VALUE;

        for (int classValue = 0; classValue < classValues; classValue++) {
            if (!this.nnWindowed.containsKey(classValue)) continue;

            double dist = MathUtils.euclideanDist(instance, this.nnWindowed.get(classValue).getCentroid());
            if (minDist > dist) {
                minDist = dist;
                minDistClassValue = classValue;
            }
        }

        return minDistClassValue;
    }
}
