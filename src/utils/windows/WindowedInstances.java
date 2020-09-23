package utils.windows;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import utils.InstanceUtils;

import java.util.ArrayList;

public class WindowedInstances {
    private int windowSize;
    private Instances window;
    private Instance centroid;

    public static int WINDOW_ESTIMATOR_WIDTH = 1000;

    public WindowedInstances(int windowSize, Instance instanceTemplate, boolean insert) {
        this.windowSize = windowSize;
        this.window = InstanceUtils.createInstances(instanceTemplate);
        this.centroid = instanceTemplate.copy();

        if (insert) {
            this.window.add(instanceTemplate.copy());
        }
        else {
            for (int i = 0; i < this.centroid.numAttributes() - 1; i++) {
                this.centroid.setValue(i, 0);
            }
        }
    }

    public void add(Instance instance) {
        this.update(instance);

        if (this.window.size() == windowSize) {
            this.window.delete(0);
        }
        this.window.add(instance);
    }

    private void update(Instance instance) {
        for (int i = 0; i < instance.numAttributes() - 1; i++) {
            double newAttributeAverage = this.centroid.value(i);

            if (this.window.size() == windowSize) {
                newAttributeAverage += ((instance.value(i) / this.windowSize) - (this.window.get(0).value(i) / this.windowSize));
            }
            else newAttributeAverage += ((instance.value(i) - newAttributeAverage) / (this.window.size() + 1));

            this.centroid.setValue(i, newAttributeAverage);
        }
    }

    public Instance getInstance(int index) {
        return this.window.get(index);
    }

    public Instances getInstances() {
        return this.window;
    }

    public Instance getCentroid() {
        return this.centroid;
    }

    public int getWindowLength() {
        return this.window.size();
    }

    public int getWindowSize() {
        return this.windowSize;
    }
}
