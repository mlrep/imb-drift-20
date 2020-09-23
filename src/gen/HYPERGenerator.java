package gen;
import com.yahoo.labs.samoa.instances.*;
import moa.core.FastVector;
import moa.core.InstanceExample;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HYPERGenerator {

    private int numInstances;
    private ArrayList<Double> classesRatio;
    private int numAtts;
    private int numDriftAtts;
    private double magChange;
    private double noisePercentage;
    private double sigmaPercentage;

    private ArrayList<InstanceExample> stream;
    private InstancesHeader streamHeader;
    private Random instanceRandom;
    private double[] weights;
    private int[] sigma;

    public HYPERGenerator(int numInstances, ArrayList<Double> classesRatio, int numAtts, int numDriftAtts,
                          double magChange, double noisePercentage, double sigmaPercentage, int instanceRandomSeed) {
        this.numInstances = numInstances;
        this.classesRatio = classesRatio;
        this.numAtts = numAtts;
        this.numDriftAtts = numDriftAtts;
        this.magChange = magChange;
        this.noisePercentage = noisePercentage;
        this.sigmaPercentage = sigmaPercentage;

        this.generateHeader();
        this.instanceRandom = new Random((long)instanceRandomSeed);
        this.generateHyperplane();
    }

    private void generateHeader() {
        FastVector<Attribute> attributes = new FastVector<>();

        for(int i = 0; i < this.numAtts; ++i) {
            attributes.addElement(new Attribute("att" + (i + 1)));
        }

        FastVector<String> classLabels = new FastVector<>();

        for(int i = 0; i < 2; ++i) {
            classLabels.addElement("class" + (i + 1));
        }

        attributes.addElement(new Attribute("class", classLabels));
        this.streamHeader = new InstancesHeader(new Instances("", attributes, 0));
        this.streamHeader.setClassIndex(this.streamHeader.numAttributes() - 1);
    }

    private void generateHyperplane() {
        this.weights = new double[this.numAtts];
        this.sigma = new int[this.numAtts];

        for(int i = 0; i < this.numAtts; ++i) {
            this.weights[i] = this.instanceRandom.nextDouble();
            this.sigma[i] = i < this.numDriftAtts ? 1 : 0;
        }
    }

    public void generateStream() {
        ArrayList<InstanceExample> stream = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            double ratio = this.classesRatio.get(i);
            for (int j = 0; j < this.numInstances * ratio; j++) {
                stream.add(this.generateInstance(i));
            }
        }

        Collections.shuffle(stream);
        this.stream = stream;
    }

    private InstanceExample generateInstance(int classLabel) {
        int numAtts = this.numAtts;
        double[] attVals = new double[numAtts + 1];
        int c;

        do {
            double sum = 0.0D;
            double sumWeights = 0.0D;

            for(int i = 0; i < numAtts; i++) {
                attVals[i] = this.instanceRandom.nextDouble();
                sum += this.weights[i] * attVals[i];
                sumWeights += this.weights[i];
            }

            if (sum >= sumWeights * 0.5D) {
                c = 1;
            } else {
                c = 0;
            }

            if (this.instanceRandom.nextDouble() <= this.noisePercentage) {
                c = c == 0 ? 1 : 0;
            }
        } while (c != classLabel);

        Instance inst = new DenseInstance(1.0D, attVals);
        inst.setDataset(this.getHeader());
        inst.setClassValue((double)c);
        this.addDrift();

        return new InstanceExample(inst);
    }

    private void addDrift() {
        for(int i = 0; i < this.numDriftAtts; ++i) {
            this.weights[i] += (double)this.sigma[i] * this.magChange;
            if (this.instanceRandom.nextDouble() <= this.sigmaPercentage) {
                this.sigma[i] *= -1;
            }
        }
    }

    public InstancesHeader getHeader() {
        return this.streamHeader;
    }

    public ArrayList<InstanceExample> getStream() {
        return this.stream;
    }
}
