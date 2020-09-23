package gen;
import com.yahoo.labs.samoa.instances.*;
import moa.core.FastVector;
import moa.core.InstanceExample;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SEAGenerator {

    private int function;
    private int numInstances;
    private ArrayList<Double> classesRatio;
    private double noisePercentage;

    private static SEAGenerator.ClassFunction[] classificationFunctions = new SEAGenerator.ClassFunction[]{
       new SEAGenerator.ClassFunction() {
        public int determineClass(double attrib1, double attrib2, double attrib3) {
            return attrib1 + attrib2 <= 8.0D ? 0 : 1;
        }
    }, new SEAGenerator.ClassFunction() {
        public int determineClass(double attrib1, double attrib2, double attrib3) {
            return attrib1 + attrib2 <= 9.0D ? 0 : 1;
        }
    }, new ClassFunction() {
        public int determineClass(double attrib1, double attrib2, double attrib3) {
            return attrib1 + attrib2 <= 7.0D ? 0 : 1;
        }
    }, new ClassFunction() {
        public int determineClass(double attrib1, double attrib2, double attrib3) {
            return attrib1 + attrib2 <= 9.5D ? 0 : 1;
        }
    }};

    private ArrayList<InstanceExample> stream;
    private InstancesHeader streamHeader;
    private Random instanceRandom;

    public SEAGenerator(int function, int numInstances, ArrayList<Double> classesRatio, double noisePercentage, int instanceRandomSeed) {
        this.function = function;
        this.numInstances = numInstances;
        this.classesRatio = classesRatio;
        this.noisePercentage = noisePercentage;

        this.generateHeader();
        this.instanceRandom = new Random((long)instanceRandomSeed);
    }

    private void generateHeader() {
        FastVector<Attribute> attributes = new FastVector<>();
        attributes.addElement(new Attribute("attrib1"));
        attributes.addElement(new Attribute("attrib2"));
        attributes.addElement(new Attribute("attrib3"));
        FastVector<String> classLabels = new FastVector<String>();
        classLabels.addElement("groupA");
        classLabels.addElement("groupB");
        attributes.addElement(new Attribute("class", classLabels));

        this.streamHeader = new InstancesHeader(new Instances("", attributes, 0));
        this.streamHeader.setClassIndex(this.streamHeader.numAttributes() - 1);
    }

    public SEAGenerator generateStream() {
        ArrayList<InstanceExample> stream = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            double ratio = this.classesRatio.get(i);
            for (int j = 0; j < this.numInstances * ratio; j++) {
                stream.add(this.generateInstance(i));
            }
        }

        Collections.shuffle(stream);
        this.stream = stream;

        return this;
    }

    private InstanceExample generateInstance(int classLabel) {
        double attrib1, attrib2, attrib3;
        int group;

        do {
            attrib1 = 10.0D * this.instanceRandom.nextDouble();
            attrib2 = 10.0D * this.instanceRandom.nextDouble();
            attrib3 = 10.0D * this.instanceRandom.nextDouble();
            group = classificationFunctions[this.function - 1].determineClass(attrib1, attrib2, attrib3);

            if (this.instanceRandom.nextDouble() <= this.noisePercentage) {
                group = (group == 0 ? 1 : 0);
            }
        } while (group != classLabel);

        InstancesHeader header = this.getHeader();
        Instance inst = new DenseInstance((double)header.numAttributes());
        inst.setValue(0, attrib1);
        inst.setValue(1, attrib2);
        inst.setValue(2, attrib3);
        inst.setDataset(header);
        inst.setClassValue((double)group);

        return new InstanceExample(inst);
    }

    public InstancesHeader getHeader() {
        return this.streamHeader;
    }

    public ArrayList<InstanceExample> getStream() {
        return this.stream;
    }

    protected interface ClassFunction {
        int determineClass(double var1, double var3, double var5);
    }
}
