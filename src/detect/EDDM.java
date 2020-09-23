package detect;

import com.yahoo.labs.samoa.instances.Instance;

import java.util.ArrayList;
import java.util.HashMap;

public class EDDM implements DriftDetectionMethod {

    public StreamStateType state = StreamStateType.STATIC;
    public int minErrors = 30;
    public int minInstances = 30;

    public static final double EDDM_DRIFT = 0.9;
    public static final double EDDM_WARNING = 0.95;

    private double m_n_errors = 0;
    private int m_n = 0;
    private int m_last_error = 0;
    private double m_dist_mean = 0.0;
    private double m_var = 0.0;
    private double m_dist_mean2s_max = Double.MIN_VALUE;
    private double similarity = 1.0;

    public EDDM() {}

    @Override
    public void reset() {
        m_n = 1;
        m_n_errors = 0;
        m_last_error = 0;
        m_dist_mean = 0.0;
        m_var = 0.0;
        m_dist_mean2s_max = Double.MIN_VALUE;
        similarity = EDDM.EDDM_DRIFT;
        this.state = StreamStateType.STATIC;
    }

    @Override
    public void update(int predictedClass, int trueClass, int numClasses) {
        m_n++;

        if (predictedClass != trueClass) {
            m_n_errors++;

            int dist = m_n - m_last_error;
            m_last_error = m_n;

            double old_mean = m_dist_mean;
            m_dist_mean = m_dist_mean + ((double) dist - m_dist_mean) / m_n_errors; // incremental average
            m_var = m_var + (dist - m_dist_mean) * (dist - old_mean); // incremental variance

            if (m_n < minInstances) return;

            double std = Math.sqrt(m_var / m_n_errors);
            double m_dist_mean2s = m_dist_mean + 2 * std;

            if (m_dist_mean2s > m_dist_mean2s_max) {
                m_dist_mean2s_max = m_dist_mean2s;
            }

            if (m_n_errors < minErrors) return;
            similarity = m_dist_mean2s / m_dist_mean2s_max;

            if (similarity < EDDM_DRIFT) this.state = StreamStateType.DRIFT;
            else this.state = (similarity < EDDM_WARNING) ? StreamStateType.WARNING : StreamStateType.STATIC;
        }
    }

    @Override
    public double checkState() {
        return this.state.ordinal();
    }

    @Override
    public HashMap<String, Double> getDetectorIndicators() {
        HashMap<String, Double> map = new HashMap<>();
        map.put("similarity", similarity);
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
