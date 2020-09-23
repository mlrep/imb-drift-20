package strategies.os;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import eval.experiment.ExperimentStream;
import moa.streams.ArffFileStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import strategies.os.strategies.GenerationsNumStrategy;
import strategies.os.strategies.GenerationStrategyType;
import strategies.os.strategies.NeighborsNumStrategyType;
import strategies.os.strategies.UnlabeledControlStrategyType;
import utils.InstanceUtils;
import utils.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

class SMOTEStrategyTest {

    private static List<Instance> test2DNumericInstances = new ArrayList<>();

    @BeforeAll
    static void loadStreams() {
        ExperimentStream test2DNumericStream = new ExperimentStream(new ArffFileStream("tests/data/test2Dnumeric.arff", 3),
                "TEST2D", 16, 1);
        while (test2DNumericStream.stream.hasMoreInstances()) {
            test2DNumericInstances.add(test2DNumericStream.stream.nextInstance().getData());
        }
    }

    @Test
    void unlabeledControlStrategy() {
        SMOTEStrategy smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE).setLuRatioThreshold(0.9);
        assertEquals(false, smote.unlabeledControlStrategy(0.5, null));
        assertEquals(false, smote.unlabeledControlStrategy(0.9, null));
        assertEquals(true, smote.unlabeledControlStrategy(1.0, null));

        smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.ERROR_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE);
        assertEquals(false, smote.unlabeledControlStrategy(0.5, new HashMap<>(Map.ofEntries(entry("error", 1.0)))));
        assertEquals(true, smote.unlabeledControlStrategy(1.0, new HashMap<>(Map.ofEntries(entry("error", 1.0)))));
        assertEquals(true, smote.unlabeledControlStrategy(1.0, new HashMap<>(Map.ofEntries(entry("error", 0.5)))));
    }

    @Test
    void neighborsNumStrategy() {
        SMOTEStrategy smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE);
        assertEquals(10, smote.setFixedNumNeighbors(10).neighborsNumStrategy(new HashMap<>(Map.ofEntries(entry("error", 0.5)))));
        assertEquals(1, smote.setFixedNumNeighbors(1).neighborsNumStrategy(new HashMap<>(Map.ofEntries(entry("error", 0.5)))));

        smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.ERROR_DRIVEN, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE).setFixedNumNeighbors(10);
        assertEquals(10, smote.neighborsNumStrategy(new HashMap<>(Map.ofEntries(entry("error", 0.0)))));
        assertEquals(5, smote.neighborsNumStrategy(new HashMap<>(Map.ofEntries(entry("error", 0.5)))));
        assertEquals(4, smote.neighborsNumStrategy(new HashMap<>(Map.ofEntries(entry("error", 0.65)))));
        assertEquals(0, smote.neighborsNumStrategy(new HashMap<>(Map.ofEntries(entry("error", 1.0)))));
    }

    @Test
    void intensityStrategy() {
        SMOTEStrategy smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE);
        assertEquals(25, smote.setIntensity(25).intensityStrategy(0, new HashMap<>(Map.ofEntries(entry("error", 0.5)))));
        assertEquals(1, smote.setIntensity(1).intensityStrategy(0, new HashMap<>(Map.ofEntries(entry("error", 0.5)))));

        smote = (SMOTEStrategy)new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.ERROR_DRIVEN, GenerationStrategyType.LINE).setIntensity(10);
        assertEquals(10, smote.intensityStrategy(0, new HashMap<>(Map.ofEntries(entry("error", 0.0)))));
        assertEquals(6, smote.intensityStrategy(0, new HashMap<>(Map.ofEntries(entry("error", 0.45)))));
        assertEquals(5, smote.intensityStrategy(0, new HashMap<>(Map.ofEntries(entry("error", 0.5)))));
        assertEquals(0, smote.intensityStrategy(0, new HashMap<>(Map.ofEntries(entry("error", 1.0)))));

        smote = (SMOTEStrategy)new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.RATIO_DRIVEN, GenerationStrategyType.LINE).setIntensity(10);
        assertEquals(0, smote.intensityStrategy(0, new HashMap<>(Map.ofEntries(entry("error", 0.0)))));
    }

    @Test
    void generationLineStrategy() {
        SMOTEStrategy smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE);
        Instance instance = test2DNumericInstances.get(1);
        Instance neighbor = test2DNumericInstances.get(5);
        Instances generatedInstances = InstanceUtils.createInstances(instance);

        smote.generationStrategy(instance, neighbor, 5, generatedInstances);
        assertEquals(5, generatedInstances.numInstances());

        for (int i = 0; i < 5; i++) {
            Instance generatedInstance = generatedInstances.get(i);
            double distInstanceNeighbor = MathUtils.euclideanDist(instance, neighbor);
            double distInstanceGeneratedInstance = MathUtils.euclideanDist(instance, generatedInstance);
            double distGeneratedInstanceNeighbor = MathUtils.euclideanDist(generatedInstance, neighbor);
            assertEquals(distInstanceNeighbor, distInstanceGeneratedInstance + distGeneratedInstanceNeighbor, 0.0001);
        }
    }

    @Test
    void generationGaussStrategy() {
        SMOTEStrategy smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.GAUSS);
        Instance instance = test2DNumericInstances.get(1);
        Instance neighbor = test2DNumericInstances.get(5);
        Instances generatedInstances = InstanceUtils.createInstances(instance);

        smote.generationStrategy(instance, neighbor, 10, generatedInstances);
        assertEquals(10, generatedInstances.numInstances());

        double xmin = instance.value(0); int xminCounter = 0;
        double xmax = neighbor.value(0); int xmaxCounter = 0;
        double ymin = instance.value(1); int yminCounter = 0;
        double ymax = neighbor.value(1); int ymaxCounter = 0;

        for (int i = 0; i < 10; i++) {
            Instance generatedInstance = generatedInstances.get(i);
            if (generatedInstance.value(0) < xmin) xminCounter++;
            if (generatedInstance.value(0) > xmax) xmaxCounter++;
            if (generatedInstance.value(1) < ymin) yminCounter++;
            if (generatedInstance.value(1) > ymax) ymaxCounter++;
        }

        assertTrue(xminCounter <= 2);
        assertTrue(xmaxCounter <= 2);
        assertTrue(yminCounter <= 2);
        assertTrue(ymaxCounter <= 2);
    }

    @Test
    void generateNeighborIndices() {
        SMOTEStrategy smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE);
        Instances nearestNeighbors = InstanceUtils.createInstances(test2DNumericInstances.get(0));
        for (int i = 0; i < 5; i++) nearestNeighbors.add(test2DNumericInstances.get(i));

        List<Integer> nearestNeighborsIndices = smote.generateNeighborIndices(nearestNeighbors);
        assertTrue(nearestNeighborsIndices.size() <= nearestNeighbors.size());
        for (int i : nearestNeighborsIndices) {
            assertTrue(i >= 0 && i <= (nearestNeighbors.size() - 1));
        }

        nearestNeighborsIndices = smote.setNeighborsRandomness(false).generateNeighborIndices(nearestNeighbors);
        assertEquals(5, nearestNeighborsIndices.size());
        assertArrayEquals(new int[] {0, 1, 2, 3, 4}, nearestNeighborsIndices.stream().mapToInt(Integer::intValue).toArray());

        smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_POISSON, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE);
        for (int i = 0; i < 10; i++) {
            nearestNeighborsIndices = smote.generateNeighborIndices(nearestNeighbors);
            int size = nearestNeighborsIndices.size();
            assert(size <= 5 && size >= 1);
        }
    }

    @Test
    void updateLabeled() {
        SMOTEStrategy smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE);

        for (Instance instance : test2DNumericInstances) smote.updateLabeled(instance);
        assertEquals(8, smote.nnWindowed.get(0).getInstancesNum());
        assertEquals(8, smote.nnWindowed.get(1).getInstancesNum());
        assertEquals(0.2, smote.labeledClassProportions.get(0).getAverage(), 0.1);
        assertEquals(0.8, smote.labeledClassProportions.get(1).getAverage(), 0.1);
        assertEquals(0.8, smote.maxClassProportion, 0.1);
    }

    @Test
    void updateUnlabeled() {
        SMOTEStrategy smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE).setLuRatioThreshold(0.2);

        Instance firstClassInstance = test2DNumericInstances.get(0);
        smote.updateUnlabeled(InstanceUtils.prepareUnlabeled(firstClassInstance), null, 0.5);
        assertNull(smote.nnWindowed.get(0));
        assertNull(smote.nnWindowed.get(1));

        smote.updateLabeled(firstClassInstance);
        Instance secondClassInstance = test2DNumericInstances.get(10);
        smote.updateUnlabeled(InstanceUtils.prepareUnlabeled(firstClassInstance), null, 0.5);
        smote.updateUnlabeled(InstanceUtils.prepareUnlabeled(secondClassInstance), null, 0.5);
        assertEquals(3, smote.nnWindowed.get(0).getInstancesNum());
        assertEquals(0.333, smote.nnWindowed.get(0).getLuRatio(), 0.001);
        assertNull(smote.nnWindowed.get(1));

        smote.updateLabeled(secondClassInstance);
        smote.updateUnlabeled(InstanceUtils.prepareUnlabeled(firstClassInstance), null, 0.5);
        smote.updateUnlabeled(InstanceUtils.prepareUnlabeled(secondClassInstance), null, 0.5);
        assertEquals(5, smote.nnWindowed.get(0).getInstancesNum());
        assertEquals(0.2, smote.nnWindowed.get(0).getLuRatio());
        assertEquals(3, smote.nnWindowed.get(1).getInstancesNum());
        assertEquals(0.333, smote.nnWindowed.get(1).getLuRatio(), 0.001);

        smote.updateUnlabeled(InstanceUtils.prepareUnlabeled(firstClassInstance), null, 0.5);
        assertEquals(5, smote.nnWindowed.get(0).getInstancesNum());
        assertEquals(0.2, smote.nnWindowed.get(0).getLuRatio());
        assertEquals(4, smote.nnWindowed.get(1).getInstancesNum());
        assertEquals(0.25, smote.nnWindowed.get(1).getLuRatio());

        smote.setUnlabeledUncertaintyThreshold(0.9);
        smote.updateUnlabeled(InstanceUtils.prepareUnlabeled(secondClassInstance), null, 0.5);
        assertEquals(4, smote.nnWindowed.get(1).getInstancesNum());
    }

    @Test
    void generateInstances() {
        SMOTEStrategy smote = new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE).setFixedNumNeighbors(5);
        for (Instance instance : test2DNumericInstances) smote.updateLabeled(instance);

        Instance firstClassInstance = test2DNumericInstances.get(0);
        assertEquals(0.0, firstClassInstance.classValue());
        Instances generatedInstances = smote.generateInstances(firstClassInstance, null);
        assertTrue(generatedInstances.numInstances() <= 5);

        smote.setNeighborsRandomness(false);
        generatedInstances = smote.setIntensity(2).generateInstances(firstClassInstance, null);
        assertEquals(10, generatedInstances.numInstances());

        Instance secondClassInstance = test2DNumericInstances.get(10);
        assertEquals(1.0, secondClassInstance.classValue());
        generatedInstances = smote.setIntensity(3).generateInstances(secondClassInstance, null);
        assertEquals(15, generatedInstances.numInstances());

        Instance anotherClassInstance = test2DNumericInstances.get(0).copy();
        anotherClassInstance.setClassValue(123);
        generatedInstances = smote.generateInstances(anotherClassInstance, null);
        assertNull(generatedInstances);

        smote = (SMOTEStrategy)new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.ERROR_DRIVEN, GenerationsNumStrategy.ERROR_DRIVEN, GenerationStrategyType.LINE)
                .setFixedNumNeighbors(10).setIntensity(10);
        for (Instance instance : test2DNumericInstances) smote.updateLabeled(instance);

        generatedInstances = smote.generateInstances(firstClassInstance, new HashMap<>(Map.ofEntries(entry("error", 0.5))));
        assertEquals(25, generatedInstances.numInstances());

        generatedInstances = smote.generateInstances(firstClassInstance, new HashMap<>(Map.ofEntries(entry("error", 1.0))));
        assertNull(generatedInstances);

        smote = new SMOTEStrategy(100, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE)
                .setLuRatioThreshold(0.0).setFixedNumNeighbors(10);
        smote.updateLabeled(firstClassInstance);
        for (Instance instance : test2DNumericInstances) smote.updateUnlabeled(InstanceUtils.prepareUnlabeled(instance), null, 0.9);
        generatedInstances = smote.generateInstances(firstClassInstance, null);

        assertEquals(10, generatedInstances.numInstances());
        for (int i = 0; i < 10; i++) {
            Instance generatedInstance = generatedInstances.get(i);
            assertNotEquals((int)generatedInstance.classValue(), SMOTEStrategy.UNLABELED_LABEL);
            assertEquals((int)generatedInstance.classValue(), (int)firstClassInstance.classValue());
        }

        smote = (SMOTEStrategy)new SMOTEStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED, GenerationsNumStrategy.RATIO_DRIVEN, GenerationStrategyType.LINE)
                .setFixedNumNeighbors(1).setIntensity(10);
        for (Instance instance : test2DNumericInstances) smote.updateLabeled(instance);

        generatedInstances = smote.generateInstances(firstClassInstance, null);
        assertEquals(8, generatedInstances.numInstances());

        generatedInstances = smote.generateInstances(secondClassInstance, null);
        assertEquals(0, generatedInstances.numInstances());
    }
}