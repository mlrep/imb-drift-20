package gen;
import com.yahoo.labs.samoa.instances.*;
import moa.core.FastVector;
import moa.core.InstanceExample;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TREEGenerator {

    private int treeRandomSeed;
    private int numInstances;
    private int numClasses;
    private ArrayList<Double> classesRatio;
    private int numNominals;
    private int numNumerics;
    private int numValsPerNominal;
    private int maxTreeDepth;
    private int firstLeafLevel;
    private double leafFraction;

    private ArrayList<InstanceExample> stream;
    private Node treeRoot;
    private InstancesHeader streamHeader;
    private Random instanceRandom;

    public TREEGenerator(int treeRandomSeed, int numInstances, int numClasses, ArrayList<Double> classesRatio,
                         int numNominals, int numNumerics, int numValsPerNominal, int maxTreeDepth, int firstLeafLevel,
                         double leafFraction, int instanceRandomSeed) {
        this.treeRandomSeed = treeRandomSeed;
        this.numInstances = numInstances;
        this.numClasses = numClasses;
        this.classesRatio = classesRatio;
        this.numNominals = numNominals;
        this.numNumerics = numNumerics;
        this.numValsPerNominal = numValsPerNominal;
        this.maxTreeDepth = maxTreeDepth;
        this.firstLeafLevel = firstLeafLevel;
        this.leafFraction = leafFraction;

        this.generateHeader();
        this.generateRandomTree();
        this.instanceRandom = new Random((long)instanceRandomSeed);
    }

    private void generateHeader() {
        FastVector<Attribute> attributes = new FastVector<>();
        FastVector<String> nominalAttVals = new FastVector<>();

        int i;
        for(i = 0; i < this.numValsPerNominal; ++i) {
            nominalAttVals.addElement("value" + (i + 1));
        }

        for(i = 0; i < this.numNominals; ++i) {
            attributes.addElement(new Attribute("nominal" + (i + 1), nominalAttVals));
        }

        for(i = 0; i < this.numNumerics; ++i) {
            attributes.addElement(new Attribute("numeric" + (i + 1)));
        }

        FastVector<String> classLabels = new FastVector<String>();

        for(i = 0; i < this.numClasses; ++i) {
            classLabels.addElement("class" + (i + 1));
        }

        attributes.addElement(new Attribute("class", classLabels));
        this.streamHeader = new InstancesHeader(new Instances("", attributes, 0));
        this.streamHeader.setClassIndex(this.streamHeader.numAttributes() - 1);
    }

    private void generateRandomTree() {
        Random treeRand = new Random((long)this.treeRandomSeed);
        ArrayList<Integer> nominalAttCandidates = new ArrayList<>(this.numNominals);

        for(int i = 0; i < this.numNominals; ++i) {
            nominalAttCandidates.add(i);
        }

        double[] minNumericVals = new double[this.numNumerics];
        double[] maxNumericVals = new double[this.numNumerics];

        for(int i = 0; i < this.numNumerics; ++i) {
            minNumericVals[i] = 0.0D;
            maxNumericVals[i] = 1.0D;
        }

        this.treeRoot = this.generateTreeNode(0, nominalAttCandidates, minNumericVals, maxNumericVals, treeRand);
    }

    private Node generateTreeNode(int currentDepth, ArrayList<Integer> nominalAttCandidates, double[] minNumericVals, double[] maxNumericVals, Random treeRand) {
        Node node;
        if (currentDepth < this.maxTreeDepth && (currentDepth < this.firstLeafLevel || this.leafFraction < 1.0D - treeRand.nextDouble())) {
            node = new Node();
            int chosenAtt = treeRand.nextInt(nominalAttCandidates.size() + this.numNumerics);
            if (chosenAtt < nominalAttCandidates.size()) {
                node.splitAttIndex = nominalAttCandidates.get(chosenAtt);
                node.children = new Node[this.numValsPerNominal];
                ArrayList<Integer> newNominalCandidates = new ArrayList<>(nominalAttCandidates);
                newNominalCandidates.remove(new Integer(node.splitAttIndex));
                newNominalCandidates.trimToSize();

                for(int i = 0; i < node.children.length; ++i) {
                    node.children[i] = this.generateTreeNode(currentDepth + 1, newNominalCandidates, minNumericVals, maxNumericVals, treeRand);
                }
            } else {
                int numericIndex = chosenAtt - nominalAttCandidates.size();
                node.splitAttIndex = this.numNominals + numericIndex;
                double minVal = minNumericVals[numericIndex];
                double maxVal = maxNumericVals[numericIndex];
                node.splitAttValue = (maxVal - minVal) * treeRand.nextDouble() + minVal;
                node.children = new Node[2];
                double[] newMaxVals = maxNumericVals.clone();
                newMaxVals[numericIndex] = node.splitAttValue;
                node.children[0] = this.generateTreeNode(currentDepth + 1, nominalAttCandidates, minNumericVals, newMaxVals, treeRand);
                double[] newMinVals = minNumericVals.clone();
                newMinVals[numericIndex] = node.splitAttValue;
                node.children[1] = this.generateTreeNode(currentDepth + 1, nominalAttCandidates, newMinVals, maxNumericVals, treeRand);
            }

            return node;
        } else {
            node = new Node();
            node.classLabel = treeRand.nextInt(this.numClasses);
            return node;
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
        double[] attVals = new double[this.numNominals + this.numNumerics];
        InstancesHeader header = this.getHeader();
        Instance inst = new DenseInstance((double)header.numAttributes());

        do { // todo
            for(int i = 0; i < attVals.length; ++i) {
                attVals[i] = i < this.numNominals ? (double)this.instanceRandom.nextInt(this.numValsPerNominal) : this.instanceRandom.nextDouble();
                inst.setValue(i, attVals[i]);
            }
        } while(this.classifyInstance(this.treeRoot, attVals) != classLabel);

        inst.setDataset(header);
        inst.setClassValue((double)classLabel);

        return new InstanceExample(inst);
    }

    private int classifyInstance(Node node, double[] attVals) {
        if (node.children == null) {
            return node.classLabel;
        } else {
            return node.splitAttIndex < this.numNominals ?
                    this.classifyInstance(node.children[(int)attVals[node.splitAttIndex]], attVals) :
                    this.classifyInstance(node.children[attVals[node.splitAttIndex] < node.splitAttValue ? 0 : 1], attVals);
        }
    }

    public InstancesHeader getHeader() {
        return this.streamHeader;
    }

    public ArrayList<InstanceExample> getStream() {
        return this.stream;
    }

    protected class Node implements Serializable {
        private static final long serialVersionUID = 1L;
        int classLabel;
        int splitAttIndex;
        double splitAttValue;
        public Node[] children;

        protected Node() {
        }
    }
}
