package strategies.divers;

import utils.windows.WindowedValue;

import java.util.ArrayList;
import java.util.HashMap;

public class DiversityMeasure {

    private HashMap<String, Double> measures = new HashMap<>();
    private int windowSize = 1000;
    private int minCounts;

    protected ArrayList<ArrayList<BinaryConfusionMatrix>> matrices = new ArrayList<>();

    public static final String DISAGREEMENT = "D";
    public static final String KW = "KW";
    public static final String DOUBLE_FAULT = "DF";
    public static final String INTERRATER_AGREEMENT = "IA";
    public static final String Q_STATISTIC = "Q";

    public DiversityMeasure(int windowSize, double minCounts) {
        this.windowSize = windowSize;
        this.minCounts = (int)(minCounts*this.windowSize);
        measures.put(DiversityMeasure.DISAGREEMENT, Double.NaN);
        measures.put(DiversityMeasure.KW, Double.NaN);
        measures.put(DiversityMeasure.DOUBLE_FAULT, Double.NaN);
        measures.put(DiversityMeasure.INTERRATER_AGREEMENT, Double.NaN);
        measures.put(DiversityMeasure.Q_STATISTIC, Double.NaN);
    }

    public void addClassifierMeasure() {
        ArrayList<BinaryConfusionMatrix> classifierMatrices = new ArrayList<>();
        for (int i = 0; i < this.matrices.size() + 1; i++) {
            classifierMatrices.add(new BinaryConfusionMatrix(this.windowSize));
        }
        this.matrices.add(classifierMatrices);
    }

    public void removeClassifierMeasure(int index) {
        for (int i = 0; i < this.matrices.size(); i++) {
            ArrayList<BinaryConfusionMatrix> m = this.matrices.get(i);
            if (m.size() > index) {
                m.remove(index);
                this.matrices.set(i, m);
            }
        }

        this.matrices.remove(index);
        this.updateMeasures();
    }

    public void update(ArrayList<Integer> votes, int correctLabel) {
        this.updateMatrix(votes, correctLabel);
        this.updateMeasures();
    }

    private void updateMatrix(ArrayList<Integer> votes, int correctLabel) {
        for (int i = 0; i < votes.size(); i++) {
            ArrayList<BinaryConfusionMatrix> m = this.matrices.get(i);
            boolean correct = (votes.get(i) == correctLabel);

            for (int j = 0; j <= i; j++) {
                boolean otherCorrect = (votes.get(j) == correctLabel);
                m.get(j).update(correct, otherCorrect);
            }

            this.matrices.set(i, m);
        }
    }

    private void updateMeasures() {
        int L = 0;
        double sumD = 0.0;
        double sumDF = 0.0;
        double sumQ = 0.0;
        double avgAcc = 0.0;

        for (int i = 0; i < this.matrices.size(); i++) {
            ArrayList<BinaryConfusionMatrix> m = this.matrices.get(i);
            if (m.get(0).getCounts() < this.minCounts) continue;
            L++;

            for (int j = 0; j < m.size(); j++) {
                if (i == j) {
                    avgAcc += m.get(j).getDoubleCorrect();
                    continue;
                }

                sumD += m.get(j).getDisagreement();
                sumDF += m.get(j).getDoubleFault();
                sumQ += m.get(j).getQ();
            }
        }

        avgAcc = avgAcc / L;
        double pairs = (L * (L - 1) / 2);

        measures.put(DiversityMeasure.DISAGREEMENT, sumD / pairs);
        measures.put(DiversityMeasure.KW, ((L - 1) / (2.0 * L)) * (sumD / pairs));
        measures.put(DiversityMeasure.DOUBLE_FAULT, sumDF / pairs);
        measures.put(DiversityMeasure.INTERRATER_AGREEMENT, (1 - (sumD / pairs) / (2 * avgAcc * (1 - avgAcc))));
        measures.put(DiversityMeasure.Q_STATISTIC, sumQ / pairs);
    }

    public void reset() {
        this.matrices.clear();
        measures.put(DiversityMeasure.DISAGREEMENT, Double.NaN);
        measures.put(DiversityMeasure.KW, Double.NaN);
        measures.put(DiversityMeasure.DOUBLE_FAULT, Double.NaN);
        measures.put(DiversityMeasure.INTERRATER_AGREEMENT, Double.NaN);
        measures.put(DiversityMeasure.Q_STATISTIC, Double.NaN);
    }

    public HashMap<String, Double> getMeasures() {
        return this.measures;
    }

    protected class BinaryConfusionMatrix {
        protected WindowedValue cc;
        protected WindowedValue cw;
        protected WindowedValue wc;
        protected WindowedValue ww;
        private int n = 0;

        BinaryConfusionMatrix(int windowSize) {
            this.cc = new WindowedValue(windowSize);
            this.cw = new WindowedValue(windowSize);
            this.wc = new WindowedValue(windowSize);
            this.ww = new WindowedValue(windowSize);
        }

        public void update(boolean firstCorrect, boolean secondCorrect) {
            this.cc.add(firstCorrect && secondCorrect ? 1.0 : 0.0);
            this.cw.add(firstCorrect && !secondCorrect ? 1.0 : 0.0);
            this.wc.add(!firstCorrect && secondCorrect ? 1.0 : 0.0);
            this.ww.add(!firstCorrect && !secondCorrect ? 1.0 : 0.0);
            if (this.n < windowSize) this.n++;
        }

        int getCounts() {
            return this.n;
        }

        double getDisagreement() {
            return this.cw.getAverage() + this.wc.getAverage();
        }

        double getDoubleFault() {
            return this.ww.getAverage();
        }

        double getDoubleCorrect() {
            return this.cc.getAverage();
        }

        double getQ() {
            double a = this.cc.getAverage();
            double b = this.cw.getAverage();
            double c = this.wc.getAverage();
            double d = this.ww.getAverage();

            return (a * d - b * c) / (a * d + b * c);
        }
    }
}
