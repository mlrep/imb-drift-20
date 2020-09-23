package strategies.os;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import utils.Trackable;
import utils.windows.WindowedValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class OversamplingStrategy implements Trackable {

    abstract public void updateLabeled(Instance instance);
    abstract public void updateUnlabeled(Instance instance, HashMap<String, Double> driftIndicators, double predictionValue);
    abstract public Instances generateInstances(Instance instance, HashMap<String, Double> driftIndicators);
    abstract public void reset();
    abstract public boolean getUpdateFirst();

    protected int windowSize;
    protected double maxClassProportion = 0.0;
    protected Random random = new Random();
    protected HashMap<Integer, WindowedValue> labeledClassProportions = new HashMap<>();
    protected ArrayList<Double> fixedClassRatios;
    protected boolean collectProportions = true;

    void updateLabeledProportions(Instance instance, int classLabel) {
        double max = 0.0;

        for (int i = 0; i < instance.numClasses(); i++) {
            if (!labeledClassProportions.containsKey(i)) {
                labeledClassProportions.put(i, new WindowedValue(this.windowSize));
            }

            labeledClassProportions.get(i).add((i == classLabel) ? 1.0 : 0.0);

            double classProportion = this.labeledClassProportions.get(i).getAverage();
            if (classProportion > max) {
                max = classProportion;
            }
        }

        this.maxClassProportion = max;
    }

    public OversamplingStrategy setFixedClassRatios(ArrayList<Double> fixedClassRatios) {
        this.fixedClassRatios = fixedClassRatios;
        return this;
    }
}
