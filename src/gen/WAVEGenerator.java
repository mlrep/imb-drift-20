package gen;
import com.yahoo.labs.samoa.instances.*;
import moa.core.FastVector;
import moa.core.InstanceExample;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class WAVEGenerator {

    private int numInstances;
    private ArrayList<Double> classesRatio;
    private final int numAtts = 21;
    private final int numNoiseAtts = 9;
    private static final int[][] hFunctions = new int[][]{{0, 1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1, 0}, {0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1, 0, 0, 0, 0, 0}};

    private ArrayList<InstanceExample> stream;
    private InstancesHeader streamHeader;
    private Random instanceRandom;

    public WAVEGenerator(int numInstances, ArrayList<Double> classesRatio, int instanceRandomSeed) {
        this.numInstances = numInstances;
        this.classesRatio = classesRatio;

        this.generateHeader();
        this.instanceRandom = new Random((long)instanceRandomSeed);
    }

    private void generateHeader() {
        FastVector<Attribute> attributes = new FastVector<>();
        int numAtts = this.numAtts + this.numNoiseAtts;

        for(int i = 0; i < numAtts; ++i) {
            attributes.addElement(new Attribute("att" + (i + 1)));
        }

        FastVector<String> classLabels = new FastVector<>();

        for(int i = 0; i < 3; ++i) {
            classLabels.addElement("class" + (i + 1));
        }

        attributes.addElement(new Attribute("class", classLabels));
        this.streamHeader = new InstancesHeader(new Instances("", attributes, 0));
        this.streamHeader.setClassIndex(this.streamHeader.numAttributes() - 1);
    }

    public void generateStream() {
        ArrayList<InstanceExample> stream = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            double ratio = this.classesRatio.get(i);
            for (int j = 0; j < this.numInstances * ratio; j++) {
                stream.add(this.generateInstance(i));
            }
        }

        Collections.shuffle(stream);
        this.stream = stream;
    }

    private InstanceExample generateInstance(int classLabel) {
        int choiceA, choiceB;

        switch(classLabel) {
            case 0:
                choiceA = 0;
                choiceB = 1;
                break;
            case 1:
                choiceA = 0;
                choiceB = 2;
                break;
            case 2:
                choiceA = 1;
                choiceB = 2;
                break;
            default:
                choiceA = -1;
                choiceB = -1;
        }

        double multiplierA = this.instanceRandom.nextDouble();
        double multiplierB = 1.0D - multiplierA;

        InstancesHeader header = this.getHeader();
        Instance inst = new DenseInstance((double)header.numAttributes());
        inst.setDataset(header);

        for(int i = 0; i < this.numAtts; ++i) {
            inst.setValue(i, multiplierA * (double)hFunctions[choiceA][i] + multiplierB * (double)hFunctions[choiceB][i] + this.instanceRandom.nextGaussian());
        }

        for(int i = this.numAtts; i < this.numAtts + this.numNoiseAtts; ++i) {
            inst.setValue(i, this.instanceRandom.nextGaussian());
        }

        inst.setClassValue((double)classLabel);

        return new InstanceExample(inst);
    }

    public InstancesHeader getHeader() {
        return this.streamHeader;
    }

    public ArrayList<InstanceExample> getStream() {
        return this.stream;
    }
}
