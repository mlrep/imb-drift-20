package utils.windows;
import java.util.ArrayList;

public class WindowedValue {

    private int windowSize;
    private ArrayList<Double> window;
    private double average = 0.0;
    private double varSum = 0.0;
    private double sum = 0.0;
    private boolean negFix = true;

    public static int WINDOW_ESTIMATOR_WIDTH = 1000;

    public WindowedValue(int windowSize) {
        this.windowSize = windowSize;
        this.window = new ArrayList<>();
    }

    public void add(double value) {
        this.update(value);
        if (value < 0.0) this.negFix = false;
    }

    private void update(double value) {
        double oldAverage = this.average;

        if (this.window.size() == windowSize) {
            this.sum += (value - this.window.get(0));
            this.average += ((value / this.windowSize) - (this.window.get(0) / this.windowSize));
            this.varSum += (value - oldAverage) * (value - this.average) - (this.window.get(0) - oldAverage) * (this.window.get(0) - this.average);
            this.window.remove(0);
        }
        else {
            this.sum += value;
            this.average += ((value - this.average) / (this.window.size() + 1));
            this.varSum = this.varSum + (value - this.average) * (value - oldAverage);
        }

        this.window.add(value);
        if (negFix && this.average < 0.0) this.average = 0.0; // approximation error
    }

    public double getSum() {
        return this.sum;
    }

    public double getAverage() {
        return this.average;
    }

    public double getStd() {
        return Math.sqrt(this.varSum / this.window.size());
    }

    public ArrayList<Double> getValues() {
        return this.window;
    }

    public int getWindowLength() {
        return this.window.size();
    }

    public int getWindowSize() {
        return this.windowSize;
    }
}
