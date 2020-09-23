package utils.windows;
import java.util.ArrayList;

public class WindowedCounterWithNaN {

    private int windowSize;
    private ArrayList<Double> window;
    private double sum = 0;
    private int trueSize = 0;

    WindowedCounterWithNaN() {}

    WindowedCounterWithNaN(int windowSize) {
        this.windowSize = windowSize;
        this.window = new ArrayList<>();
    }

    public void add(double value) {
        if (Double.isNaN(value)) addNaN();
        else addNumber(value);
    }

    private void addNaN() {
        if (this.window.size() == this.windowSize) {
            if (!Double.isNaN(this.window.get(0))) {
                this.sum -= this.window.get(0);
                this.trueSize--;
            }
            this.window.remove(0);
        }

        this.window.add(Double.NaN);
    }

    private void addNumber(double value) {
        if (this.window.size() < this.windowSize) {
            this.sum += value;
            this.trueSize++;
        } else {
            double first = this.window.get(0);
            boolean isNaN = Double.isNaN(first);

            this.sum += (value - (!isNaN ? first : 0.0));
            this.window.remove(0);

            if (isNaN) this.trueSize++;
        }

        this.window.add(value);
    }

    public double getAverage() {
        if (this.trueSize == 0) return Double.NaN;
        return this.sum / this.trueSize;
    }

    public double getSum() {
        if (this.trueSize == 0) return Double.NaN;
        return this.sum;
    }

    public int getSize() {
        return this.trueSize;
    }

    public ArrayList<Double> getValues() {
        return this.window;
    }
}
