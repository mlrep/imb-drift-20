package strategies.os;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import eval.experiment.ExperimentStream;
import moa.streams.ArffFileStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import strategies.os.strategies.ProbabilisticStrategyType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProbabilisticWindowStrategyTest {

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
    void updateLabeled() {
        ProbabilisticWindowStrategy probWindow = new ProbabilisticWindowStrategy(10, ProbabilisticStrategyType.UNIFORM);
        assertEquals(0, probWindow.getNumOfInstances());

        probWindow.updateLabeled(test2DNumericInstances.get(0));
        probWindow.updateLabeled(test2DNumericInstances.get(1));
        assertEquals(2, probWindow.getNumOfInstances());

        for (Instance instance : test2DNumericInstances) probWindow.updateLabeled(instance);
        assertEquals(10, probWindow.getNumOfInstances());
    }

    @Test
    void generateInstances() {
        ProbabilisticWindowStrategy probWindow = new ProbabilisticWindowStrategy(10,
                ProbabilisticStrategyType.SIGMOID).setIntensity(10);
        probWindow.updateLabeled(test2DNumericInstances.get(0));

        Instances generatedInstances = probWindow.generateInstances(test2DNumericInstances.get(0), null);
        assertEquals(10, generatedInstances.numInstances());
        for (int i = 0; i < 10; i++) {
            assertEquals(generatedInstances.get(i).value(0), test2DNumericInstances.get(0).value(0));
            assertEquals(generatedInstances.get(i).value(1), test2DNumericInstances.get(0).value(1));
        }

        for (Instance instance : test2DNumericInstances) probWindow.updateLabeled(instance);
        generatedInstances = probWindow.generateInstances(test2DNumericInstances.get(0), null);
        assertTrue(generatedInstances.numInstances() <= 170 && generatedInstances.numInstances() >= 10);
    }
}