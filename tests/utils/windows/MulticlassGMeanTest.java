package utils.windows;
import com.yahoo.labs.samoa.instances.Instance;
import eval.experiment.ExperimentStream;
import moa.core.InstanceExample;
import moa.streams.ArffFileStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MulticlassGMeanTest extends MulticlassGMean {
    private static List<Instance> test2DNumericInstances = new ArrayList<>();

    @BeforeAll
    static void loadStreams() {
        ExperimentStream test2DNumericStream = new ExperimentStream(new ArffFileStream("tests/data/test2Dnumeric.arff", 3), "TEST2D", 16, 1);
        while (test2DNumericStream.stream.hasMoreInstances()) {
            test2DNumericInstances.add(test2DNumericStream.stream.nextInstance().getData());
        }
    }

    @Test
    void addResult() {
        MulticlassGMean gMean = new MulticlassGMean(3, 2);
        Instance inst1 = test2DNumericInstances.get(0);
        Instance inst2 = test2DNumericInstances.get(10);

        gMean.addResult(new InstanceExample(inst1), new double[]{0.99, 0.01});
        assertEquals(2, gMean.windowTPValues.size());
        assertEquals(2, gMean.windowTNValues.size());
        assertEquals(2, gMean.globalTPValues.size());
        assertEquals(2, gMean.globalTNValues.size());

        assertEquals(1, gMean.windowTPValues.get(0).getValues().size());
        assertEquals(1, (double)gMean.windowTPValues.get(0).getValues().get(0));
        assertEquals(1, gMean.windowTPValues.get(1).getValues().size());
        assertEquals(Double.NaN, (double)gMean.windowTPValues.get(1).getValues().get(0));

        assertEquals(1, gMean.windowTNValues.get(0).getValues().size());
        assertEquals(Double.NaN, (double)gMean.windowTNValues.get(0).getValues().get(0));
        assertEquals(1, gMean.windowTNValues.get(1).getValues().size());
        assertEquals(1, (double)gMean.windowTNValues.get(1).getValues().get(0));

        assertEquals(1, gMean.globalTPValues.get(0).getSize());
        assertEquals(1, (double)gMean.globalTPValues.get(0).getValues().get(0));
        assertEquals(0, gMean.globalTPValues.get(1).getSize());

        assertEquals(0, gMean.globalTNValues.get(0).getSize());
        assertEquals(1, gMean.globalTNValues.get(1).getSize());
        assertEquals(1, (double)gMean.globalTNValues.get(1).getValues().get(0));

        gMean.addResult(new InstanceExample(inst2), new double[]{0.99, 0.01});
        assertEquals(2, gMean.windowTPValues.get(0).getValues().size());
        assertEquals(Double.NaN, (double)gMean.windowTPValues.get(0).getValues().get(1));
        assertEquals(2, gMean.windowTPValues.get(1).getValues().size());
        assertEquals(0, (double)gMean.windowTPValues.get(1).getValues().get(1));

        assertEquals(2, gMean.windowTNValues.get(0).getValues().size());
        assertEquals(0, (double)gMean.windowTNValues.get(0).getValues().get(1));
        assertEquals(2, gMean.windowTNValues.get(1).getValues().size());
        assertEquals(Double.NaN, (double)gMean.windowTNValues.get(1).getValues().get(1));

        assertEquals(1, gMean.globalTPValues.get(0).getSize());
        assertEquals(1, gMean.globalTPValues.get(1).getSize());
        assertEquals(0, (double)gMean.globalTPValues.get(1).getValues().get(0));

        assertEquals(1, gMean.globalTNValues.get(0).getSize());
        assertEquals(0, (double)gMean.globalTNValues.get(0).getValues().get(0));
        assertEquals(1, gMean.globalTNValues.get(1).getSize());
    }

    @Test
    void getGMean() {
        MulticlassGMean gMean = new MulticlassGMean(3, 2);
        Instance inst1 = test2DNumericInstances.get(0);
        Instance inst2 = test2DNumericInstances.get(10);

        gMean.addResult(new InstanceExample(inst1), new double[]{0.99, 0.01});
        assertEquals(1.0, gMean.getWindowGMean());
        assertEquals(Double.NaN, gMean.getWindowGMeanForClass(0));
        assertEquals(Double.NaN, gMean.getWindowGMeanForClass(1));
        assertEquals(Double.NaN, gMean.getGlobalGMean());

        gMean.addResult(new InstanceExample(inst2), new double[]{0.01, 0.99});
        assertEquals(1.0, gMean.getWindowGMean());
        assertEquals(1.0, gMean.getWindowGMeanForClass(0));
        assertEquals(1.0, gMean.getWindowGMeanForClass(1));
        assertEquals(1.0, gMean.getGlobalGMean());

        gMean.addResult(new InstanceExample(inst2), new double[]{0.99, 0.01});
        assertEquals(0.707, gMean.getWindowGMean(), 0.001);
        assertEquals(0.707, gMean.getWindowGMeanForClass(0), 0.001);
        assertEquals(0.707, gMean.getWindowGMeanForClass(1), 0.001);
        assertEquals(0.707, gMean.getGlobalGMean(), 0.001);

        gMean.addResult(new InstanceExample(inst1), new double[]{0.01, 0.99});
        assertEquals(0, gMean.getWindowGMean());
        assertEquals(0.0, gMean.getWindowGMeanForClass(0));
        assertEquals(0.0, gMean.getWindowGMeanForClass(1));
        assertEquals(0.5, gMean.getGlobalGMean(), 0.1);

        gMean.addResult(new InstanceExample(inst1), new double[]{0.99, 0.01});
        gMean.addResult(new InstanceExample(inst1), new double[]{0.99, 0.01});
        assertEquals(0.666, gMean.getWindowGMean(), 0.001);
        assertEquals(Double.NaN, gMean.getWindowGMeanForClass(0));
        assertEquals(Double.NaN, gMean.getWindowGMeanForClass(1));
        assertEquals(0.612, gMean.getGlobalGMean(), 0.001);
    }
}
