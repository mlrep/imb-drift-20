package gen;
import com.yahoo.labs.samoa.instances.*;
import moa.core.FastVector;
import moa.core.InstanceExample;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class STAGGERGenerator {
    private int function;
    private int numInstances;
    private ArrayList<Double> classesRatio;
    private double noisePercentage;

    private static STAGGERGenerator.ClassFunction[] classificationFunctions = new ClassFunction[]{
       new ClassFunction() {
        public int determineClass(int size, int color, int shape) {
            return size == 0 && color == 0 ? 1 : 0;
        }
    }, new ClassFunction() {
        public int determineClass(int size, int color, int shape) {
            return color != 2 && shape != 0 ? 0 : 1;
        }
    }, new ClassFunction() {
        public int determineClass(int size, int color, int shape) {
            return size != 1 && size != 2 ? 0 : 1;
        }
    }};

    private ArrayList<InstanceExample> stream;
    private InstancesHeader streamHeader;
    private Random instanceRandom;

    public STAGGERGenerator(int function, int numInstances, ArrayList<Double> classesRatio, double noisePercentage, int instanceRandomSeed) {
        this.function = function;
        this.numInstances = numInstances;
        this.classesRatio = classesRatio;
        this.noisePercentage = noisePercentage;

        this.generateHeader();
        this.instanceRandom = new Random((long)instanceRandomSeed);
    }

    private void generateHeader() {
        FastVector<Attribute> attributes = new FastVector<>();
        FastVector<String> sizeLabels = new FastVector<>();
        sizeLabels.addElement("small");
        sizeLabels.addElement("medium");
        sizeLabels.addElement("large");
        attributes.addElement(new Attribute("size", sizeLabels));
        FastVector<String> colorLabels = new FastVector<String>();
        colorLabels.addElement("red");
        colorLabels.addElement("blue");
        colorLabels.addElement("green");
        attributes.addElement(new Attribute("color", colorLabels));
        FastVector<String> shapeLabels = new FastVector<>();
        shapeLabels.addElement("circle");
        shapeLabels.addElement("square");
        shapeLabels.addElement("triangle");
        attributes.addElement(new Attribute("shape", shapeLabels));
        FastVector<String> classLabels = new FastVector<>();
        classLabels.addElement("false");
        classLabels.addElement("true");
        attributes.addElement(new Attribute("class", classLabels));

        this.streamHeader = new InstancesHeader(new Instances("", attributes, 0));
        this.streamHeader.setClassIndex(this.streamHeader.numAttributes() - 1);
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
        int size, color, shape, group;

        do {
            size = this.instanceRandom.nextInt(3);
            color = this.instanceRandom.nextInt(3);
            shape = this.instanceRandom.nextInt(3);
            group = classificationFunctions[this.function - 1].determineClass(size, color, shape);

            if (this.instanceRandom.nextDouble() <= this.noisePercentage) {
                group = (group == 0 ? 1 : 0);
            }
        } while (group != classLabel);

        InstancesHeader header = this.getHeader();
        Instance inst = new DenseInstance((double)header.numAttributes());
        inst.setValue(0, (double)size);
        inst.setValue(1, (double)color);
        inst.setValue(2, (double)shape);
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
        int determineClass(int var1, int var2, int var3);
    }
}
