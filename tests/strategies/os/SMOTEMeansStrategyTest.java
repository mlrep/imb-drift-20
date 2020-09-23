package strategies.os;

import com.yahoo.labs.samoa.instances.Instance;
import eval.experiment.ExperimentStream;
import moa.streams.ArffFileStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import strategies.os.strategies.GenerationsNumStrategy;
import strategies.os.strategies.GenerationStrategyType;
import strategies.os.strategies.NeighborsNumStrategyType;
import strategies.os.strategies.UnlabeledControlStrategyType;
import utils.InstanceUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SMOTEMeansStrategyTest {

    private static List<Instance> test2DNumericInstances = new ArrayList<>();

    @BeforeAll
    static void loadStreams() {
        ExperimentStream test2DNumericStream = new ExperimentStream(new ArffFileStream("tests/data/test2Dnumeric.arff", 3), "TEST2D", 16, 1);
        while (test2DNumericStream.stream.hasMoreInstances()) {
            test2DNumericInstances.add(test2DNumericStream.stream.nextInstance().getData());
        }
    }

    @Test
    void findClosestCentroidClass() {
        SMOTEMeansStrategy smoteMeans = new SMOTEMeansStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE);

        Instance unlabeledInstance33 = InstanceUtils.prepareUnlabeled(test2DNumericInstances.get(0).copy());
        unlabeledInstance33.setValue(0, 3.0);
        unlabeledInstance33.setValue(1, 3.0);
        int nearestCentroidClassValue = smoteMeans.findClosestCentroidClass(unlabeledInstance33);
        assertEquals(-1, nearestCentroidClassValue);

        for (Instance instance : test2DNumericInstances) smoteMeans.updateLabeled(instance);
        nearestCentroidClassValue = smoteMeans.findClosestCentroidClass(unlabeledInstance33);
        assertEquals(0.0, nearestCentroidClassValue);

        unlabeledInstance33.setValue(0, -3.0);
        unlabeledInstance33.setValue(1, -3.0);
        nearestCentroidClassValue = smoteMeans.findClosestCentroidClass(unlabeledInstance33);
        assertEquals(1.0, nearestCentroidClassValue);
    }

    @Test
    void updateUnlabeled() {
        SMOTEMeansStrategy smoteMeans = new SMOTEMeansStrategy(10, UnlabeledControlStrategyType.FIXED_RATIO_THRESHOLD,
                NeighborsNumStrategyType.FIXED_UNIFORM, GenerationsNumStrategy.FIXED, GenerationStrategyType.LINE);
        for (Instance instance : test2DNumericInstances) smoteMeans.updateLabeled(instance);

        Instance unlabeledInstance33 = InstanceUtils.prepareUnlabeled(test2DNumericInstances.get(0).copy());
        unlabeledInstance33.setValue(0, 3.0);
        unlabeledInstance33.setValue(1, 3.0);
        smoteMeans.updateUnlabeled(unlabeledInstance33, null, 0.5);
        smoteMeans.updateUnlabeled(unlabeledInstance33, null, 0.5);
        assertEquals(10, smoteMeans.nnWindowed.get(0).getInstancesNum());
        assertEquals(0.8, smoteMeans.nnWindowed.get(0).getLuRatio());
        assertEquals(8, smoteMeans.nnWindowed.get(1).getInstancesNum());
        assertEquals(1.0, smoteMeans.nnWindowed.get(1).getLuRatio());

        unlabeledInstance33.setValue(0, -3.0);
        unlabeledInstance33.setValue(1, -3.0);
        smoteMeans.updateUnlabeled(unlabeledInstance33, null, 0.5);
        smoteMeans.updateUnlabeled(unlabeledInstance33, null, 0.5);
        assertEquals(10, smoteMeans.nnWindowed.get(0).getInstancesNum());
        assertEquals(0.8, smoteMeans.nnWindowed.get(0).getLuRatio());
        assertEquals(10, smoteMeans.nnWindowed.get(1).getInstancesNum());
        assertEquals(0.8, smoteMeans.nnWindowed.get(1).getLuRatio());
    }
}