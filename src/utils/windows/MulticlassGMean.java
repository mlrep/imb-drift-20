package utils.windows;
import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.InstanceImpl;
import moa.core.Example;
import moa.core.Utils;
import utils.MathUtils;

import java.util.ArrayList;

public class MulticlassGMean {

    private int numClasses;
    protected ArrayList<WindowedCounterWithNaN> windowTPValues = new ArrayList<>();
    protected ArrayList<WindowedCounterWithNaN> windowTNValues = new ArrayList<>();
    protected ArrayList<WindowedCounterWithNaN> globalTPValues = new ArrayList<>();
    protected ArrayList<WindowedCounterWithNaN> globalTNValues = new ArrayList<>();

    MulticlassGMean() {}

    public MulticlassGMean(int windowSize, int numClasses) {
        this.numClasses = numClasses;

        for (int i = 0; i < this.numClasses; i++) {
            this.windowTPValues.add(new WindowedCounterWithNaN(windowSize));
            this.windowTNValues.add(new WindowedCounterWithNaN(windowSize));
            this.globalTPValues.add(new WindowedCounterWithNaN(Integer.MAX_VALUE));
            this.globalTNValues.add(new WindowedCounterWithNaN(Integer.MAX_VALUE));
        }
    }

    public void addResult(Example<Instance> exampleInstance, double[] classVotes) {
        InstanceImpl inst = (InstanceImpl)exampleInstance.getData();
        this.add(Utils.maxIndex(classVotes), (int)inst.classValue());
    }

    public void add(int predictedClass, int trueClass) {
        int correct = (predictedClass == trueClass ? 1 : 0);

        for (int i = 0; i < this.numClasses; i++) {
            if (i == trueClass) {
                this.windowTPValues.get(i).add(correct);
                this.windowTNValues.get(i).add(Double.NaN);
                this.globalTPValues.get(i).add(correct);
            } else {
                int negCorrect = (predictedClass != i ? 1 : 0);
                this.windowTPValues.get(i).add(Double.NaN);
                this.windowTNValues.get(i).add(negCorrect);
                this.globalTNValues.get(i).add(negCorrect);
            }
        }
    }

    public double getWindowGMean() {
        double g = 1.0;
        int ignored = 0;

        for (WindowedCounterWithNaN windowTPValue : this.windowTPValues) {
            double sensitivity = windowTPValue.getAverage();
            if (Double.isNaN(sensitivity)) {
                ignored++;
                continue;
            }
            g *= sensitivity;
        }

        return (ignored == this.numClasses ? Double.NaN : Math.pow(g, 1.0 / (this.numClasses - ignored)));
    }

    public double getWindowGMeanForClass(int classLabel) {
        double sensitivity = this.windowTPValues.get(classLabel).getAverage();
        double specificity = this.windowTNValues.get(classLabel).getAverage();
        return MathUtils.gmean(sensitivity, specificity);
    }

    public double getWindowSensitivityForClass(int classLabel) {
        return this.windowTPValues.get(classLabel).getAverage();
    }

    public double getWindowSpecificityForClass(int classLabel) {
        return this.windowTNValues.get(classLabel).getAverage();
    }

    public double getGlobalGMean() {
        double g = 1.0;
        int ignored = 0;

        for (WindowedCounterWithNaN globalTPValue : this.globalTPValues) {
            double sensitivity = globalTPValue.getAverage();

            if (Double.isNaN(sensitivity)) {
                ignored++;
                continue;
            }

            g *= sensitivity;
        }

        //System.out.println(" Missing classes: " + ignored);
        return (ignored == this.numClasses ? Double.NaN : Math.pow(g, 1.0 / this.numClasses));
    }
}
