package utils;

import com.yahoo.labs.samoa.instances.Instance;

import java.util.ArrayList;
import java.util.HashMap;

public interface Trackable {
    HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators);
    ArrayList<String> getParameterNames();
}
