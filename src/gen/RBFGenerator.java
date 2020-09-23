package gen;
import com.yahoo.labs.samoa.instances.*;
import moa.core.FastVector;
import moa.core.InstanceExample;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class RBFGenerator {

    private int modelRandomSeed;
    private int numInstances;
    private int numClasses;
    private ArrayList<Double> classesRatio;
    private int numAtts;
    private int numCentroids;

    private InstancesHeader streamHeader;
    private ArrayList<InstanceExample> stream;
    private CentroidRBF[] centroids;
    private double[] centroidWeights;
    private HashMap<Integer, ArrayList<Integer>> classCentroids = new HashMap<>();
    private HashMap<Integer, ArrayList<Double>> classCentroidWeights = new HashMap<>();
    private Random instanceRandom;

    public RBFGenerator(int modelRandomSeed, int numInstances, int numClasses, ArrayList<Double> classesRatio,
                        int numAtts, int numCentroids, int instanceRandomSeed) {
        this.modelRandomSeed = modelRandomSeed;
        this.numInstances = numInstances;
        this.classesRatio = classesRatio;
        this.numClasses = numClasses;
        this.numAtts = numAtts;
        this.numCentroids = numCentroids;

        this.generateHeader();
        this.generateCentroids();
        this.instanceRandom = new Random((long)instanceRandomSeed);
    }

    private void generateHeader() {
        FastVector<Attribute> attributes = new FastVector<>();

        for(int i = 0; i < this.numAtts; ++i) {
            attributes.addElement(new Attribute("att" + (i + 1)));
        }

        FastVector<String> classLabels = new FastVector<>();

        for(int i = 0; i < this.numClasses; ++i) {
            classLabels.addElement("class" + (i + 1));
        }

        attributes.addElement(new Attribute("class", classLabels));
        this.streamHeader = new InstancesHeader(new Instances("", attributes, 0));
        this.streamHeader.setClassIndex(this.streamHeader.numAttributes() - 1);
    }

    private void generateCentroids() {
        Random modelRand = new Random((long)this.modelRandomSeed);
        this.centroids = new CentroidRBF[this.numCentroids];
        this.centroidWeights = new double[this.centroids.length];
        int minCentroids = (int)(0.05 * this.numCentroids);

        int n = 0;
        for(int i = 0; i < this.numClasses; i++) {
            for(int j = 0; j < minCentroids; j++) {
                int idx = n++;
                this.generateCentroid(i, idx, modelRand);
            }
        }

        for(int i = n; i < this.centroids.length; i++) {
            this.generateCentroid(-1, i, modelRand);
        }

        this.createCentroidBuckets();
    }

    private void generateCentroid(int classLabel, int idx, Random modelRand) {
        this.centroids[idx] = new CentroidRBF();
        double[] randCentre = new double[this.numAtts];

        for(int k = 0; k < randCentre.length; k++) {
            randCentre[k] = modelRand.nextDouble();
        }

        this.centroids[idx].centre = randCentre;
        this.centroids[idx].classLabel = classLabel == -1 ? modelRand.nextInt(this.numClasses) : classLabel;
        this.centroids[idx].stdDev = modelRand.nextDouble();
        this.centroidWeights[idx] = modelRand.nextDouble();
    }

    private void createCentroidBuckets() {
        for (int i = 0; i < this.centroids.length; i++) {
            int classLabel = this.centroids[i].classLabel;
            if (!this.classCentroids.containsKey(classLabel)) {
                this.classCentroids.put(classLabel, new ArrayList<>());
                this.classCentroidWeights.put(classLabel, new ArrayList<>());
            }

            this.classCentroids.get(classLabel).add(i);
            this.classCentroidWeights.get(classLabel).add(this.centroidWeights[i]);
        }
    }

    public void generateStream() {
        ArrayList<InstanceExample> stream = new ArrayList<>();

        for (int i = 0; i < this.numClasses; i++) {
            double ratio = this.classesRatio.get(i);
            for (int j = 0; j < this.numInstances * ratio; j++) {
                stream.add(this.generateInstance(i));
            }
        }

        Collections.shuffle(stream);
        this.stream = stream;
    }

    private InstanceExample generateInstance(int classLabel) {
        int centroidIndex = this.classCentroids.get(classLabel).get(this.chooseRandomIndexBasedOnWeights(
                this.classCentroidWeights.get(classLabel), this.instanceRandom));
        CentroidRBF centroid = this.centroids[centroidIndex];

        int numAtts = this.numAtts;
        double[] attVals = new double[numAtts + 1];

        for(int i = 0; i < numAtts; ++i) {
            attVals[i] = this.instanceRandom.nextDouble() * 2.0D - 1.0D;
        }

        double magnitude = 0.0D;

        for(int i = 0; i < numAtts; ++i) {
            magnitude += attVals[i] * attVals[i];
        }

        magnitude = Math.sqrt(magnitude);
        double desiredMag = this.instanceRandom.nextGaussian() * centroid.stdDev;
        double scale = desiredMag / magnitude;

        for(int i = 0; i < numAtts; ++i) {
            attVals[i] = centroid.centre[i] + attVals[i] * scale;
        }

        Instance inst = new DenseInstance(1.0D, attVals);
        inst.setDataset(this.getHeader());
        inst.setClassValue((double)centroid.classLabel);

        return new InstanceExample(inst);
    }

    private int chooseRandomIndexBasedOnWeights(ArrayList<Double> weights, Random random) {
        double probSum = weights.stream().mapToDouble(a -> a).sum();
        double val = random.nextDouble() * probSum;
        int index = 0;

        double sum = 0.0;
        while (sum <= val && index < weights.size()) {
            sum += weights.get(index++);
        }

        return index - 1;
    }

    public InstancesHeader getHeader() {
        return this.streamHeader;
    }

    public ArrayList<InstanceExample> getStream() {
        return this.stream;
    }

    protected static class CentroidRBF implements Serializable {
        private static final long serialVersionUID = 1L;
        public double[] centre;
        int classLabel;
        double stdDev;

        CentroidRBF() {}
    }
}
