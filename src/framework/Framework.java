package framework;

import strategies.al.ActiveLearningStrategy;
import com.yahoo.labs.samoa.instances.Instance;
import moa.classifiers.Classifier;
import strategies.sl.SelfLabelingStrategy;
import utils.Trackable;
import utils.TrackableFramework;

import java.util.HashMap;

abstract public class Framework implements TrackableFramework {

    public Classifier classifier;
    public ActiveLearningStrategy activeLearningStrategy;
    public SelfLabelingStrategy selfLabelingStrategy;
    public boolean ready = true;
    public int initInstances;
    public boolean omitInit = false;
    public boolean collectTrackableParameters = true;
    public long allocatedMemory = -1;

    abstract public void reset();
    abstract public void update(Instance instance, double[] votes, int i);
    abstract public HashMap<String, Double> getTrackableParameters(Instance instance);

    public Framework setInitInstances(int initInstancesNum) {
        this.initInstances = initInstancesNum;
        return this;
    }

    public Framework setOmitInit(boolean omitInit) {
        this.omitInit = omitInit;
        return this;
    }

    public Framework setCollectTrackableParameters(boolean collectTrackableParameters) {
        this.collectTrackableParameters = collectTrackableParameters;
        return this;
    }
}
