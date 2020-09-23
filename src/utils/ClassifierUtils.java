package utils;
import moa.core.DoubleVector;
import moa.core.Utils;

import java.util.Arrays;

public class ClassifierUtils {

    static public double combinePredictionsMax(double[] predictionValues) {
        double outPosterior;

        if (predictionValues.length > 1) {
            DoubleVector vote = new DoubleVector(predictionValues);

            if (vote.sumOfValues() > 0.0) {
                vote.normalize();
            }

            predictionValues = vote.getArrayRef();
            outPosterior = (predictionValues[Utils.maxIndex(predictionValues)]);

        } else {
            outPosterior = 0.0;
        }

        return Double.isInfinite(outPosterior) || Double.isNaN(outPosterior) ? 0 : outPosterior;
    }
}
