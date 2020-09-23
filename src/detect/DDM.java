package detect;

import com.yahoo.labs.samoa.instances.Instance;

import java.util.ArrayList;
import java.util.HashMap;

public class DDM implements DriftDetectionMethod {

    public StreamStateType state = StreamStateType.STATIC;
    public int minInstances = 30;

    private static final double DDM_DRIFT = 3.0;
    private static final double DDM_WARNING = 2.0;

    private int m_n = 0;
    private double m_p = 1;
    private double m_s = 0;
    private double m_pmin = Double.MAX_VALUE;
    private double m_smin = Double.MAX_VALUE;

    public DDM() {}

    @Override
    public void reset() {
        m_n = 0;
        m_p = 1;
        m_s = 0;
        m_pmin = Double.MAX_VALUE;
        m_smin = Double.MAX_VALUE;
        this.state = StreamStateType.STATIC;
    }

    @Override
    public void update(int predictedClass, int trueClass, int numClasses) {
        int p = (predictedClass != trueClass) ? 1 : 0;
        m_n++;

        m_p =  m_p + (p - m_p) / (double) m_n; // incremental average
        m_s = Math.sqrt(m_p * (1 - m_p) / (double) m_n);

        if (m_n < this.minInstances) return;

        if (m_p + m_s < m_pmin + m_smin) {
            m_pmin = m_p;
            m_smin = m_s;
        }

        if (m_p + m_s > m_pmin + DDM_DRIFT * m_smin) this.state = StreamStateType.DRIFT;
        else this.state = m_p + m_s > m_pmin + DDM_WARNING * m_smin ? StreamStateType.WARNING : StreamStateType.STATIC;
    }

    @Override
    public double checkState() {
        return this.state.ordinal();
    }

    @Override
    public HashMap<String, Double> getDetectorIndicators() {
        HashMap<String, Double> map = new HashMap<>();
        map.put("error", m_p + m_s);
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
