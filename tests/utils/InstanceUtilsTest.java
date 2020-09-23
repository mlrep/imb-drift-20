package utils;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import eval.experiment.ExperimentStream;
import javafx.util.Pair;
import moa.streams.ArffFileStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import strategies.os.SMOTEStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class InstanceUtilsTest {

    private static List<Instance> test2DNumericInstances = new ArrayList<>();

    @BeforeAll
    static void loadStreams() {
        ExperimentStream test2DNumericStream = new ExperimentStream(new ArffFileStream("tests/data/test2Dnumeric.arff", 3), "TEST2D", 16, 1);
        while (test2DNumericStream.stream.hasMoreInstances()) {
            test2DNumericInstances.add(test2DNumericStream.stream.nextInstance().getData());
        }
    }

    @Test
    void createInstances() {
        Instances instances = InstanceUtils.createInstances(test2DNumericInstances.get(0));
        assertEquals(0, instances.numInstances());
        assertEquals(3, instances.numAttributes());
        assertEquals(2, instances.classIndex());
    }

    @Test
    void instanceIndices() {
        Instances instances = InstanceUtils.createInstances(test2DNumericInstances.get(0));
        for (int i = 0; i < 3; i++) instances.add(test2DNumericInstances.get(i));

        List<Integer> indices = InstanceUtils.instanceIndices(instances);
        for (int i = 0; i < 3; i++) assertEquals(i, (int)indices.get(i));
    }

    @Test
    void prepareUnlabeled() {
        Instance unlabeled = InstanceUtils.prepareUnlabeled(test2DNumericInstances.get(0));
        assertEquals(3, unlabeled.numAttributes());
        assertEquals(2, unlabeled.classIndex());
        assertEquals(SMOTEStrategy.UNLABELED_LABEL, unlabeled.classValue());
    }

    @Test
    void findClosestInstance() {
        Instances instances = InstanceUtils.createInstances(test2DNumericInstances.get(0));
        for (int i = 0; i < 8; i++) instances.add(test2DNumericInstances.get(i));

        Instance instance = test2DNumericInstances.get(13);
        Pair<Integer, Double> indexDist = InstanceUtils.findClosestInstance(instance, instances);
        assertEquals(0, indexDist.getKey().intValue());
        assertEquals(3.605, indexDist.getValue(), 0.001);

        for (int i = 8; i < 16; i++) instances.add(test2DNumericInstances.get(i));
        indexDist = InstanceUtils.findClosestInstance(instance, instances);
        assertEquals(13, indexDist.getKey().intValue());
        assertEquals(0.0, indexDist.getValue(), 0.1);
    }

    @Test
    void getClosestIndices() {
        Instances instances = InstanceUtils.createInstances(test2DNumericInstances.get(0));
        instances.add(test2DNumericInstances.get(2)); // (1,2)
        instances.add(test2DNumericInstances.get(4)); // (0,2)
        instances.add(test2DNumericInstances.get(5)); // (2,2)
        instances.add(test2DNumericInstances.get(9)); // (-1,-1)
        instances.add(test2DNumericInstances.get(11)); // (-2,0)

        Instance instance = test2DNumericInstances.get(13); // (-2,-2)
        int[] closestIndices = InstanceUtils.getClosestIndices(instance, instances, 3);

        assertEquals(3, closestIndices.length);
        assertEquals(3, closestIndices[0]);
        assertEquals(4, closestIndices[1]);
        assertEquals(1, closestIndices[2]);
    }
}