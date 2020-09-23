package clust;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import javafx.util.Pair;

import java.util.ArrayList;

public interface Clusterer {
    Pair<ArrayList<Integer>, Integer> update(Instance instance);
    Instances getCentroids();
    void reset();
}
