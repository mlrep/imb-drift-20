package detect;
import utils.Trackable;
import java.util.HashMap;

public interface DriftDetectionMethod extends Trackable {
    void update(int predictedClass, int trueClass, int numClasses);
    double checkState();
    HashMap<String, Double> getDetectorIndicators();
    void reset();
}
