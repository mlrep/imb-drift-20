package strategies.divers;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class DiversityMeasureTest {

    @Test
    void addClassifierMeasure() {
        DiversityMeasure dm = new DiversityMeasure(10, 0);
        dm.addClassifierMeasure();
        assertEquals(1, dm.matrices.size());
        assertEquals(1, dm.matrices.get(0).size());

        dm.addClassifierMeasure();
        dm.addClassifierMeasure();
        assertEquals(3, dm.matrices.size());
        assertEquals(1, dm.matrices.get(0).size());
        assertEquals(2, dm.matrices.get(1).size());
        assertEquals(3, dm.matrices.get(2).size());
    }

    @Test
    void removeClassifierMeasure() {
        DiversityMeasure dm = new DiversityMeasure(10, 0);
        dm.addClassifierMeasure();
        dm.removeClassifierMeasure(0);
        assertEquals(0, dm.matrices.size());

        dm.addClassifierMeasure();
        dm.addClassifierMeasure();
        dm.addClassifierMeasure();
        dm.removeClassifierMeasure(1);
        assertEquals(2, dm.matrices.size());
        assertEquals(1, dm.matrices.get(0).size());
        assertEquals(2, dm.matrices.get(1).size());
    }

    @Test
    void updateMatrix() {
        DiversityMeasure dm = new DiversityMeasure(10, 0);
        dm.addClassifierMeasure();
        HashMap<String, Double> measures = dm.getMeasures();
        assertEquals(Double.NaN, measures.get("D"), 0.1);
        assertEquals(Double.NaN, measures.get("KW"), 0.1);
        assertEquals(Double.NaN, measures.get("DF"), 0.1);
        assertEquals(Double.NaN, measures.get("IA"), 0.1);
        assertEquals(Double.NaN, measures.get("Q"), 0.1);

        dm.addClassifierMeasure();
        dm.addClassifierMeasure();
        dm.update(new ArrayList<>(Arrays.asList(0, 1, 2)), 1);

        ArrayList<DiversityMeasure.BinaryConfusionMatrix> d = dm.matrices.get(0);
        assertEquals(1, d.size());
        assertEquals(0.0, d.get(0).cc.getAverage());
        assertEquals(0.0, d.get(0).cw.getAverage());
        assertEquals(0.0, d.get(0).wc.getAverage());
        assertEquals(1.0, d.get(0).ww.getAverage());

        d = dm.matrices.get(1);
        assertEquals(2, d.size());
        assertEquals(0.0, d.get(0).cc.getAverage());
        assertEquals(1.0, d.get(0).cw.getAverage());
        assertEquals(0.0, d.get(0).wc.getAverage());
        assertEquals(0.0, d.get(0).ww.getAverage());

        assertEquals(1.0, d.get(1).cc.getAverage());
        assertEquals(0.0, d.get(1).cw.getAverage());
        assertEquals(0.0, d.get(1).wc.getAverage());
        assertEquals(0.0, d.get(1).ww.getAverage());

        d = dm.matrices.get(2);
        assertEquals(3, d.size());
        assertEquals(0.0, d.get(0).cc.getAverage());
        assertEquals(0.0, d.get(0).cw.getAverage());
        assertEquals(0.0, d.get(0).wc.getAverage());
        assertEquals(1.0, d.get(0).ww.getAverage());

        assertEquals(0.0, d.get(1).cc.getAverage());
        assertEquals(0.0, d.get(1).cw.getAverage());
        assertEquals(1.0, d.get(1).wc.getAverage());
        assertEquals(0.0, d.get(1).ww.getAverage());

        assertEquals(0.0, d.get(2).cc.getAverage());
        assertEquals(0.0, d.get(2).cw.getAverage());
        assertEquals(0.0, d.get(2).wc.getAverage());
        assertEquals(1.0, d.get(2).ww.getAverage());

        measures = dm.getMeasures();
        assertEquals(0.666, measures.get("D"), 0.001);
        assertEquals(0.222, measures.get("KW"), 0.001);
        assertEquals(0.333, measures.get("DF"), 0.001);
        assertEquals(-0.499, measures.get("IA"), 0.001);
        assertEquals(Double.NaN, measures.get("Q"), 0.1);

        dm.update(new ArrayList<>(Arrays.asList(1, 1, 2)), 1);

        d = dm.matrices.get(0);
        assertEquals(1, d.size());
        assertEquals(0.5, d.get(0).cc.getAverage());
        assertEquals(0.0, d.get(0).cw.getAverage());
        assertEquals(0.0, d.get(0).wc.getAverage());
        assertEquals(0.5, d.get(0).ww.getAverage());

        d = dm.matrices.get(1);
        assertEquals(2, d.size());
        assertEquals(0.5, d.get(0).cc.getAverage());
        assertEquals(0.5, d.get(0).cw.getAverage());
        assertEquals(0.0, d.get(0).wc.getAverage());
        assertEquals(0.0, d.get(0).ww.getAverage());

        assertEquals(1.0, d.get(1).cc.getAverage());
        assertEquals(0.0, d.get(1).cw.getAverage());
        assertEquals(0.0, d.get(1).wc.getAverage());
        assertEquals(0.0, d.get(1).ww.getAverage());

        d = dm.matrices.get(2);
        assertEquals(3, d.size());
        assertEquals(0.0, d.get(0).cc.getAverage());
        assertEquals(0.0, d.get(0).cw.getAverage());
        assertEquals(0.5, d.get(0).wc.getAverage());
        assertEquals(0.5, d.get(0).ww.getAverage());

        assertEquals(0.0, d.get(1).cc.getAverage());
        assertEquals(0.0, d.get(1).cw.getAverage());
        assertEquals(1.0, d.get(1).wc.getAverage());
        assertEquals(0.0, d.get(1).ww.getAverage());

        assertEquals(0.0, d.get(2).cc.getAverage());
        assertEquals(0.0, d.get(2).cw.getAverage());
        assertEquals(0.0, d.get(2).wc.getAverage());
        assertEquals(1.0, d.get(2).ww.getAverage());

        measures = dm.getMeasures();
        assertEquals(0.666, measures.get("D"), 0.001);
        assertEquals(0.222, measures.get("KW"), 0.001);
        assertEquals(0.166, measures.get("DF"), 0.001);
        assertEquals(-0.333, measures.get("IA"), 0.001);
        assertEquals(Double.NaN, measures.get("Q"), 0.1);

        dm.update(new ArrayList<>(Arrays.asList(2, 2, 2)), 1);
        dm.update(new ArrayList<>(Arrays.asList(1, 1, 1)), 1);
        assertEquals(1.0, measures.get("Q"), 0.1);

        dm.update(new ArrayList<>(Arrays.asList(0, 1, 1)), 1);
        assertEquals(0.777, measures.get("Q"), 0.001);
    }

    @Test
    void updateDynamicMatrix() {
        DiversityMeasure dm = new DiversityMeasure(10, 0);
        dm.addClassifierMeasure();
        dm.addClassifierMeasure();
        dm.addClassifierMeasure();
        dm.update(new ArrayList<>(Arrays.asList(0, 1, 2)), 1);

        dm.removeClassifierMeasure(1);
        HashMap<String, Double> measures = dm.getMeasures();
        assertEquals(0.0, measures.get("D"), 0.1);
        assertEquals(0.0, measures.get("KW"), 0.1);
        assertEquals(1.0, measures.get("DF"), 0.1);
        assertEquals(Double.NaN, measures.get("IA"), 0.1);
        assertEquals(Double.NaN, measures.get("Q"), 0.1);

        dm.addClassifierMeasure();
        dm.update(new ArrayList<>(Arrays.asList(0, 1, 2)), 1);
        measures = dm.getMeasures();
        assertEquals(0.5, measures.get("D"), 0.1);
        assertEquals(0.166, measures.get("KW"), 0.001);
        assertEquals(0.5, measures.get("DF"), 0.1);
        assertEquals(-0.799, measures.get("IA"), 0.001);
        assertEquals(Double.NaN, measures.get("Q"), 0.1);
    }

    @Test
    void updateMeasuresUsingMinCounts() {
        DiversityMeasure dm = new DiversityMeasure(10, 0.2);
        dm.addClassifierMeasure();
        dm.addClassifierMeasure();

        dm.update(new ArrayList<>(Arrays.asList(0, 1)), 1);
        HashMap<String, Double> measures = dm.getMeasures();
        assertEquals(Double.NaN, measures.get("D"), 0.1);
        assertEquals(Double.NaN, measures.get("KW"), 0.1);
        assertEquals(Double.NaN, measures.get("DF"), 0.1);
        assertEquals(Double.NaN, measures.get("IA"), 0.1);
        assertEquals(Double.NaN, measures.get("Q"), 0.1);

        dm.update(new ArrayList<>(Arrays.asList(0, 1)), 1);
        measures = dm.getMeasures();
        assertEquals(1.0, measures.get("D"), 0.1);
        assertEquals(0.25, measures.get("KW"), 0.01);
        assertEquals(0.0, measures.get("DF"), 0.1);
        assertEquals(-1.0, measures.get("IA"), 0.1);
        assertEquals(Double.NaN, measures.get("Q"), 0.1);

        dm.addClassifierMeasure();
        dm.update(new ArrayList<>(Arrays.asList(0, 1, 2)), 1);
        measures = dm.getMeasures();
        assertEquals(1.0, measures.get("D"), 0.1);
        assertEquals(0.25, measures.get("KW"), 0.01);
        assertEquals(0.0, measures.get("DF"), 0.1);
        assertEquals(-1.0, measures.get("IA"), 0.1);
        assertEquals(Double.NaN, measures.get("Q"), 0.1);

        dm.update(new ArrayList<>(Arrays.asList(0, 1, 2)), 1);
        measures = dm.getMeasures();
        assertEquals(0.666, measures.get("D"), 0.001);
        assertEquals(0.222, measures.get("KW"), 0.001);
        assertEquals(0.3, measures.get("DF"), 0.1);
        assertEquals(-0.499, measures.get("IA"), 0.001);
        assertEquals(Double.NaN, measures.get("Q"), 0.1);
    }
}