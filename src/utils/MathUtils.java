package utils;
import com.yahoo.labs.samoa.instances.Instance;
import javafx.util.Pair;

public class MathUtils {

    public static double euclideanDist(Instance a, Instance b) {
        double euclideanDist = 0.0;
        for (int i = 0; i < a.numAttributes() - 1; i++) {
            euclideanDist += Math.pow(a.value(i) - b.value(i), 2.0);
        }
        return Math.sqrt(euclideanDist);
    }

    public static int randomPoisson01(double lambda) {
        double L = Math.exp(-lambda);
        double t = 1.0;
        int k = 0;

        do {
            k++;
            t *= Math.random();
        } while (t > L);

        return (k > 1 ? k - 1 : k);
    }

    public static int randomZTP(double lambda) {
        int k = 1;
        double t = (Math.exp(-lambda) / (1 - Math.exp(-lambda))) * lambda;
        double s = t;
        double u = Math.random();

        while (s < u) {
            k += 1;
            t *= (lambda / k);
            s += t;
        }

        return k;
    }

    public static double[] multiplyVectors(double[] v1, double[] v2) {
        assert v1.length == v2.length;
        double[] result = new double[v1.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = v1[i] * v2[i];
        }

        return result;
    }

    public static double sigmoid(double x, double beta) {
        return x*(beta - 1) / (2.0*beta*x - beta - 1);
    }

    public static double gmean(double sensitivity, double specificity) {
        return Math.sqrt(sensitivity * specificity);
    }

    public static double root(double num, double root) {
        return Math.pow(Math.E, Math.log(num)/root);
    }

    public static double max(double[] a) {
        double max = Double.MIN_VALUE;
        for (double aVal : a) {
            if (aVal > max) max = aVal;
        }

        return max;
    }

    public static Pair<Integer, Double> maxPair(double[] a) {
        double max = Double.MIN_VALUE;
        int maxIndex = -1;

        for (int i = 0; i < a.length; i++) {
            if (a[i] > max) {
                max = a[i];
                maxIndex = i;
            }
        }

        return new Pair<>(maxIndex, max);
    }
}
