package strategies.divers;

import cls.SimpleEnsemble;
import clust.KMeans;
import com.yahoo.labs.samoa.instances.Instance;
import eval.experiment.ExperimentStream;
import moa.classifiers.trees.HoeffdingAdaptiveTree;
import moa.streams.ArffFileStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

class ClusteringStrategyTest {

    private static List<Instance> test2DNumericInstances = new ArrayList<>();

    @BeforeAll
    static void loadStreams() {
        ExperimentStream test2DNumericStream = new ExperimentStream(new ArffFileStream("tests/data/test2Dnumeric.arff", 3), "TEST2D", 16, 1);
        while (test2DNumericStream.stream.hasMoreInstances()) {
            test2DNumericInstances.add(test2DNumericStream.stream.nextInstance().getData());
        }
    }

    @Test
    void update() {
        ClusteringStrategy cs = new ClusteringStrategy(new KMeans(5));
        SimpleEnsemble ens = new SimpleEnsemble(5, new HoeffdingAdaptiveTree());

        cs.update(test2DNumericInstances.get(0), ens, new HashMap<>(Map.ofEntries(entry("error", 0.0))));
        assertEquals(1.0, cs.range);

        cs.update(test2DNumericInstances.get(0), ens, new HashMap<>(Map.ofEntries(entry("error", 0.5))));
        assertEquals(0.5, cs.range);

        cs.update(test2DNumericInstances.get(1), ens, new HashMap<>(Map.ofEntries(entry("error", 1.0))));
        assertEquals(0.0, cs.range);

        cs = new ClusteringStrategy(new KMeans(5)).setMaxRange(0.8);
        ens = new SimpleEnsemble(5, new HoeffdingAdaptiveTree());

        cs.update(test2DNumericInstances.get(0), ens, new HashMap<>(Map.ofEntries(entry("error", 0.0))));
        assertEquals(0.8, cs.range);

        cs.update(test2DNumericInstances.get(0), ens, new HashMap<>(Map.ofEntries(entry("error", 0.5))));
        assertEquals(0.4, cs.range);

        cs.update(test2DNumericInstances.get(1), ens, new HashMap<>(Map.ofEntries(entry("error", 1.0))));
        assertEquals(0.0, cs.range);
    }
}