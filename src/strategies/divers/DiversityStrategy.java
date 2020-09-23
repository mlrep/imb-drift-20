package strategies.divers;

import cls.SimpleEnsemble;
import com.yahoo.labs.samoa.instances.Instance;
import utils.Trackable;

import java.util.HashMap;
import java.util.Random;

public abstract class DiversityStrategy implements Trackable {
    abstract public void update(Instance instance, SimpleEnsemble ensemble, HashMap<String, Double> driftIndicators);
    abstract public void diversify(Instance instance, SimpleEnsemble ensemble);
    abstract public void reset();
    protected Random random = new Random();
}
