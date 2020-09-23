package utils;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import javafx.util.Pair;
import strategies.os.SMOTEStrategy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class InstanceUtils {

    public static Instances createInstances(Instance templateInstance) {
        Instances newInstances = new Instances(templateInstance.copy().dataset());
        newInstances.delete();
        return newInstances;
    }

    public static List<Integer> instanceIndices(Instances nearestNeighbors) {
        return Arrays.stream(IntStream.range(0, nearestNeighbors.numInstances()).toArray())
                .boxed()
                .collect(Collectors.toList());
    }

    public static Instance prepareUnlabeled(Instance instanceTemplate) {
        Instance unlabeledInstance = instanceTemplate.copy();
        unlabeledInstance.setClassValue(SMOTEStrategy.UNLABELED_LABEL);
        return unlabeledInstance;
    }

    public static Pair<Integer, Double> findClosestInstance(Instance instance, Instances instances) {
        double minDist = Double.MAX_VALUE;
        int minIndex = -1;

        for (int i = 0; i < instances.numInstances(); i++) {
            double dist = MathUtils.euclideanDist(instance, instances.get(i));
            if (dist < minDist) {
                minDist = dist;
                minIndex = i;
            }
        }

        return new Pair<>(minIndex, minDist);
    }

    public static int[] getClosestIndices(Instance instance, Instances otherInstances, int indicesNum) {
        int n = otherInstances.numInstances();
        Double[] distances = new Double[n];

        for (int i = 0; i < n; i++) {
            distances[i] = MathUtils.euclideanDist(instance, otherInstances.get(i));
        }

        return IntStream.range(0, n).boxed()
                .sorted(Comparator.comparing(i -> distances[i]))
                .mapToInt(ele -> ele)
                .limit(indicesNum)
                .toArray();
    }
}
