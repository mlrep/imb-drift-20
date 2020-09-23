package utils;

import com.yahoo.labs.samoa.instances.Instance;

import java.util.ArrayList;
import java.util.HashMap;

public interface TrackableFramework {
    HashMap<String, Double> getTrackableParameters(Instance instance);
    ArrayList<String> getParameterNames();
}
