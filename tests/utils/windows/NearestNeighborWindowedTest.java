package utils.windows;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import eval.experiment.ExperimentStream;
import moa.classifiers.lazy.neighboursearch.LinearNNSearch;
import moa.streams.ArffFileStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.InstanceUtils;
import utils.windows.NearestNeighborWindowed;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NearestNeighborWindowedTest {

    private NearestNeighborWindowed nnWindowed;
    private static List<Instance> test2DNumericInstances = new ArrayList<>();

    @BeforeAll
    static void loadStreams() {
        ExperimentStream test2DNumericStream = new ExperimentStream(new ArffFileStream("tests/data/test2Dnumeric.arff", 3), "TEST2D", 16, 1);
        while (test2DNumericStream.stream.hasMoreInstances()) {
            test2DNumericInstances.add(test2DNumericStream.stream.nextInstance().getData());
        }
    }

    @Test
    void constructor() {
        this.nnWindowed = new NearestNeighborWindowed(new LinearNNSearch(), 5,  test2DNumericInstances.get(0));
        assertEquals( 0.0, this.nnWindowed.getLuRatio());

        this.nnWindowed.insert(test2DNumericInstances.get(0));
        assertEquals( 1.0, this.nnWindowed.getLuRatio());

        this.nnWindowed = new NearestNeighborWindowed(new LinearNNSearch(), 5, InstanceUtils.prepareUnlabeled(test2DNumericInstances.get(0)));
        assertEquals(0.0, this.nnWindowed.getLuRatio());

        this.nnWindowed = new NearestNeighborWindowed(new LinearNNSearch(), 5,  test2DNumericInstances.get(0));
        assertEquals(3, this.nnWindowed.getCentroid().numAttributes()); // with a class
        assertEquals(0.0, this.nnWindowed.getCentroid().value(0));
        assertEquals(0.0, this.nnWindowed.getCentroid().value(1));
    }

    @Test
    void insert() {
        this.nnWindowed = new NearestNeighborWindowed(new LinearNNSearch(), 5,  test2DNumericInstances.get(0));

        this.nnWindowed.insert(test2DNumericInstances.get(0));
        this.nnWindowed.insert(InstanceUtils.prepareUnlabeled(test2DNumericInstances.get(0)));
        assertEquals(2, this.nnWindowed.getInstancesNum());
        assertEquals(0.5, this.nnWindowed.getLuRatio());

        this.nnWindowed.insert(test2DNumericInstances.get(1));
        this.nnWindowed.insert(test2DNumericInstances.get(2));
        assertEquals(4, this.nnWindowed.getInstancesNum());
        assertEquals(0.75, this.nnWindowed.getLuRatio());

        this.nnWindowed.insert(test2DNumericInstances.get(3));
        this.nnWindowed.insert(test2DNumericInstances.get(4));
        assertEquals(5, this.nnWindowed.getInstancesNum());
        assertEquals(0.8, this.nnWindowed.getLuRatio());

        this.nnWindowed.insert(null);
        assertEquals(5, this.nnWindowed.getInstancesNum());
        assertEquals(0.8, this.nnWindowed.getLuRatio());
    }

    @Test
    void getNearestNeighbors() {
        this.nnWindowed = new NearestNeighborWindowed(new LinearNNSearch(), 8,  test2DNumericInstances.get(0));
        for (int i = 0; i < 8; i++) this.nnWindowed.insert(test2DNumericInstances.get(i));

        Instance instance33 = test2DNumericInstances.get(0).copy();
        instance33.setValue(0, 3.0);
        instance33.setValue(1, 3.0);

        Instances nearestNeighbors = this.nnWindowed.getNearestNeighbors(instance33, 1);
        assertEquals(1, nearestNeighbors.numInstances());
        assertEquals(2.0, nearestNeighbors.get(0).value(0));
        assertEquals(2.0, nearestNeighbors.get(0).value(1));

        Instance instance00 = test2DNumericInstances.get(0).copy();
        instance00.setValue(0, 0.0);
        instance00.setValue(1, 0.0);

        nearestNeighbors = this.nnWindowed.getNearestNeighbors(instance00, 2);
        assertEquals(2, nearestNeighbors.numInstances());
        assertEquals(1.0, nearestNeighbors.get(0).value(0));
        assertEquals(0.0, nearestNeighbors.get(0).value(1));
        assertEquals(0.0, nearestNeighbors.get(1).value(0));
        assertEquals(1.0, nearestNeighbors.get(1).value(1));

        Instance instance11 = test2DNumericInstances.get(0).copy();
        instance11.setValue(0, 1.0);
        instance11.setValue(1, 1.0);
        nearestNeighbors = this.nnWindowed.getNearestNeighbors(instance11, 2);
        assertEquals(2, nearestNeighbors.numInstances());

        for (int i = 0; i < 7; i++) this.nnWindowed.insert(instance33);
        this.nnWindowed.insert(instance00);

        nearestNeighbors = this.nnWindowed.getNearestNeighbors(instance00, 2);
        assertEquals(2, nearestNeighbors.numInstances());
        assertEquals(0.0, nearestNeighbors.get(0).value(0));
        assertEquals(0.0, nearestNeighbors.get(0).value(1));
        assertEquals(3.0, nearestNeighbors.get(1).value(0));
        assertEquals(3.0, nearestNeighbors.get(1).value(1));
    }

    @Test
    void clean() {
        this.nnWindowed = new NearestNeighborWindowed(new LinearNNSearch(), 5,  test2DNumericInstances.get(0));
        Instances nearestNeighbors = InstanceUtils.createInstances(test2DNumericInstances.get(0));
        for (int i = 0; i < 5; i++) nearestNeighbors.add(test2DNumericInstances.get(i));

        this.nnWindowed.clean(nearestNeighbors, 2);
        assertEquals(2, nearestNeighbors.numInstances());
        assertEquals(test2DNumericInstances.get(0).toString(), nearestNeighbors.get(0).toString());
        assertEquals(test2DNumericInstances.get(1).toString(), nearestNeighbors.get(1).toString());
    }

    @Test
    void updateCentroid() {
        this.nnWindowed = new NearestNeighborWindowed(new LinearNNSearch(), 2,  test2DNumericInstances.get(0));

        this.nnWindowed.insert(test2DNumericInstances.get(0));
        assertEquals(0.0, this.nnWindowed.getCentroid().value(0));
        assertEquals(1.0, this.nnWindowed.getCentroid().value(1));

        this.nnWindowed.insert(test2DNumericInstances.get(2));
        assertEquals(0.5, this.nnWindowed.getCentroid().value(0));
        assertEquals(1.5, this.nnWindowed.getCentroid().value(1));

        this.nnWindowed.insert(test2DNumericInstances.get(5));
        this.nnWindowed.insert(test2DNumericInstances.get(5));
        assertEquals(2.0, this.nnWindowed.getCentroid().value(0));
        assertEquals(2.0, this.nnWindowed.getCentroid().value(1));
    }

    @AfterEach
    void tearDown() {
        this.nnWindowed = null;
    }
}