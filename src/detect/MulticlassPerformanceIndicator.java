package detect;
import com.yahoo.labs.samoa.instances.Instance;
import utils.windows.MulticlassGMean;

import java.util.ArrayList;
import java.util.HashMap;

public class MulticlassPerformanceIndicator implements DriftDetectionMethod {

    private int numClasses;
    private int windowSize;
    private MulticlassGMean performanceIndicators;
    private PerformanceType performanceType;
    private boolean initialized;

    public MulticlassPerformanceIndicator(int windowSize, PerformanceType performanceType) {
        this.windowSize = windowSize;
        this.performanceType = performanceType;
        this.performanceIndicators = new MulticlassGMean(windowSize, this.numClasses);
        this.initialized = false;
    }

    @Override
    public void update(int predictedClass, int trueClass, int numClasses) {
        if (!this.initialized) this.init(numClasses);
        this.performanceIndicators.add(predictedClass, trueClass);
    }

    private void init(int numClasses) {
        this.numClasses = numClasses;
        this.performanceIndicators = new MulticlassGMean(windowSize, this.numClasses);
        this.initialized = true;
    }

    @Override
    public double checkState() {
        return -1;
    }

    @Override
    public HashMap<String, Double> getDetectorIndicators() {
        HashMap<String, Double> classPerformanceIndicators = new HashMap<>();
        for (int i = 0; i < this.numClasses; i++) {
            double performanceIndicator;

            switch (this.performanceType) {
                case GMEAN: performanceIndicator = this.performanceIndicators.getWindowGMeanForClass(i); break;
                case SENSITIVTY: performanceIndicator = this.performanceIndicators.getWindowSensitivityForClass(i); break;
                case SPECIFICITY: performanceIndicator = this.performanceIndicators.getWindowSpecificityForClass(i); break;
                default: performanceIndicator = this.performanceIndicators.getWindowGMeanForClass(i);
            }

            classPerformanceIndicators.put(Integer.toString(i), performanceIndicator);
        }
        return classPerformanceIndicators;
    }

    @Override
    public void reset() {
        this.performanceIndicators = null;
        this.initialized = false;
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators) {
        return new HashMap<>();
    }

    @Override
    public ArrayList<String> getParameterNames() {
        return new ArrayList<>();
    }
}
