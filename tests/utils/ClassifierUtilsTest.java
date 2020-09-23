package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClassifierUtilsTest {

    @Test
    void combinePredictionsMax() {
        assertEquals(0.55, ClassifierUtils.combinePredictionsMax(new double[]{0.0, 0.45, 0.55}));
        assertEquals(1.0, ClassifierUtils.combinePredictionsMax(new double[]{0.0, 1.0}));
        assertEquals(0.0, ClassifierUtils.combinePredictionsMax(new double[]{1.0}));
        assertEquals(0.6666, ClassifierUtils.combinePredictionsMax(new double[]{0.0, 0.5, 1.0}), 0.0001);
        assertEquals(0.5, ClassifierUtils.combinePredictionsMax(new double[]{10.0, 15.0, 25.0}));
    }
}