package gen;
import com.yahoo.labs.samoa.instances.*;
import moa.core.InstanceExample;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SINEGenerator {
    private int function;
    private int numInstances;
    private ArrayList<Double> classesRatio;
    private double noisePercentage;

    private static ClassFunction[] classificationFunctions = new ClassFunction[]{
       new ClassFunction() {
        public int determineClass(double x, double y) {
            return y < Math.sin(x) ? 0 : 1;
        }
    }, new ClassFunction() {
        public int determineClass(double x, double y) {
            return y >= Math.sin(x) ? 0 : 1;
        }
    }, new ClassFunction() {
        public int determineClass(double x, double y) {
            return y < 0.5D + 0.3D * Math.sin(9.42477796076938D * x) ? 0 : 1;
        }
    }, new ClassFunction() {
        public int determineClass(double x, double y) {
            return y >= 0.5D + 0.3D * Math.sin(9.42477796076938D * x) ? 0 : 1;
        }
    }};

    private ArrayList<InstanceExample> stream;
    private InstancesHeader streamHeader;
    private Random instanceRandom;

    public SINEGenerator(int function, int numInstances, ArrayList<Double> classesRatio, double noisePercentage, int instanceRandomSeed) {
        this.function = function;
        this.numInstances = numInstances;
        this.classesRatio = classesRatio;
        this.noisePercentage = noisePercentage;

        this.generateHeader();
        this.instanceRandom = new Random((long)instanceRandomSeed);
    }

    private void generateHeader() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        int numAtts = 4;

        for(int i = 0; i < numAtts; ++i) {
            attributes.add(new Attribute("att" + (i + 1)));
        }

        List<String> classLabels = new ArrayList<String>();
        classLabels.add("positive");
        classLabels.add("negative");
        Attribute classAtt = new Attribute("class", classLabels);
        attributes.add(classAtt);

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
        double a1, a2;
        int group;

        do {
            a1 = this.instanceRandom.nextDouble();
            a2 = this.instanceRandom.nextDouble();
            group = classificationFunctions[this.function - 1].determineClass(a1, a2);

            if (this.instanceRandom.nextDouble() <= this.noisePercentage) {
                group = (group == 0 ? 1 : 0);
            }
        } while (group != classLabel);

        InstancesHeader header = this.getHeader();
        Instance inst = new DenseInstance((double)header.numAttributes());
        inst.setValue(0, a1);
        inst.setValue(1, a2);
        inst.setDataset(header);

        for(int i = 0; i < 2; ++i) {
            inst.setValue(i + 2, this.instanceRandom.nextDouble());
        }

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
        int determineClass(double var1, double var3);
    }
}
