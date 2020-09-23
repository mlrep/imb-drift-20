package detect;
import com.yahoo.labs.samoa.instances.Instance;
import utils.windows.WindowedValue;

import java.util.ArrayList;
import java.util.HashMap;

public class WindowedErrorIndicator implements DriftDetectionMethod {

    private double meanError = 1.0;
    private WindowedValue estimator = new WindowedValue(WindowedValue.WINDOW_ESTIMATOR_WIDTH);

    public WindowedErrorIndicator() {}

    public WindowedErrorIndicator(int windowSize) {
        this.estimator = new WindowedValue(windowSize);
    }

    @Override
    public void reset() {
        this.meanError = 1.0;
        this.estimator = new WindowedValue(this.estimator.getWindowSize());
    }

    @Override
    public void update(int predictedClass, int trueClass, int numClasses) {
        int p = (predictedClass != trueClass ? 1 : 0);
        this.estimator.add(p);
        this.meanError = estimator.getAverage();
    }

    @Override
    public double checkState() {
        return StreamStateType.STATIC.ordinal();
    }

    @Override
    public HashMap<String, Double> getDetectorIndicators() {
        HashMap<String, Double> map = new HashMap<>();
        map.put("error", this.meanError);
        return map;
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
