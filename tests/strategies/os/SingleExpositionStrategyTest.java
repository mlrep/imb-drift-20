package strategies.os;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import eval.experiment.ExperimentStream;
import moa.streams.ArffFileStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import strategies.os.strategies.GenerationsNumStrategy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SingleExpositionStrategyTest {

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
    void generateInstances() {
        SingleExpositionStrategy single = new SingleExpositionStrategy(GenerationsNumStrategy.FIXED).setIntensity(100);
        Instances generatedInstances = single.generateInstances(test2DNumericInstances.get(0), null);

        assertEquals(100, generatedInstances.numInstances());
        for (int i = 0; i < 100; i++) {
            assertEquals(generatedInstances.get(i).value(0), test2DNumericInstances.get(0).value(0));
            assertEquals(generatedInstances.get(i).value(1), test2DNumericInstances.get(0).value(1));
        }

        single = new SingleExpositionStrategy(GenerationsNumStrategy.RATIO_DRIVEN).setIntensity(10).setWindowSize(10);
        for (Instance instance : test2DNumericInstances) single.updateLabeled(instance);

        generatedInstances = single.generateInstances(test2DNumericInstances.get(0), null);
        assertEquals(8, generatedInstances.numInstances());
        generatedInstances = single.generateInstances(test2DNumericInstances.get(10), null);
        assertEquals(0, generatedInstances.numInstances());
    }
}