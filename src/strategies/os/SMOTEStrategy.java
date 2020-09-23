package strategies.os;
import com.yahoo.labs.samoa.instances.Instances;
import moa.classifiers.lazy.neighboursearch.LinearNNSearch;
import com.yahoo.labs.samoa.instances.Instance;
import strategies.os.strategies.*;
import utils.InstanceUtils;
import utils.MathUtils;
import utils.Trackable;
import utils.windows.NearestNeighborWindowed;
import utils.windows.WindowedValue;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SMOTEStrategy extends SingleExpositionStrategy implements Trackable {

    Map<Integer, NearestNeighborWindowed> nnWindowed = new HashMap<Integer, NearestNeighborWindowed>();
    private UnlabeledControlStrategyType unlabeledControlStrategy;
    private NeighborsNumStrategyType neighborsNumStrategy;
    private GenerationStrategyType generationStrategy;

    private int fixedNumNeighbors = 10;
    private double luRatioThreshold = 0.6;
    double unlabeledUncertaintyThreshold = 0.0;
    private boolean randomNeighbors = true;
    public static final double UNLABELED_LABEL = -1;

    private HashMap<String, Double> trackableParameters = new HashMap<>();

    public SMOTEStrategy(int windowSize, UnlabeledControlStrategyType unlabeledControlStrategy, NeighborsNumStrategyType neighborsNumStrategy,
                         GenerationsNumStrategy intensityStrategy, GenerationStrategyType generationStrategy) {
        super(intensityStrategy);
        this.windowSize = windowSize;
        this.unlabeledControlStrategy = unlabeledControlStrategy;
        this.neighborsNumStrategy = neighborsNumStrategy;
        this.generationStrategy = generationStrategy;

        if (this.neighborsNumStrategy == NeighborsNumStrategyType.FIXED
                || this.neighborsNumStrategy == NeighborsNumStrategyType.ERROR_DRIVEN) this.randomNeighbors = false;
    }

    @Override
    public void reset() {
        this.nnWindowed.clear();
        this.labeledClassProportions.clear();
        this.maxClassProportion = 0.0;
        this.trackableParameters.clear();
    }

    @Override
    public void updateLabeled(Instance instance) {
        int classValue = (int)instance.classValue();
        if (!this.nnWindowed.containsKey(classValue)) this.init(classValue, instance);
        this.nnWindowed.get(classValue).insert(instance);
        if (this.collectProportions) this.updateLabeledProportions(instance, classValue);
    }

    private void init(int classValue, Instance instanceTemplate) {
        this.nnWindowed.put(classValue, new NearestNeighborWindowed(new LinearNNSearch(), this.windowSize, instanceTemplate));
    }

    @Override
    public void updateUnlabeled(Instance instance, HashMap<String, Double> driftIndicators, double predictionValue) {
        if (this.unlabeledUncertaintyThreshold > predictionValue) return;
        for (int classValue = 0; classValue < instance.numClasses(); classValue++) {
            if (this.nnWindowed.containsKey(classValue) &&
                    this.unlabeledControlStrategy(this.nnWindowed.get(classValue).getLuRatio(), driftIndicators)) {
                Instance unlabeledInstance = InstanceUtils.prepareUnlabeled(instance);
                this.nnWindowed.get(classValue).insert(unlabeledInstance);
            }
        }
    }

    boolean unlabeledControlStrategy(double luRatio, HashMap<String, Double> driftIndicators) {
        switch (this.unlabeledControlStrategy) {
            case FIXED_RATIO_THRESHOLD: return luRatio > this.luRatioThreshold;
            case ERROR_RATIO_THRESHOLD: return luRatio >= driftIndicators.get("error");
            case SIGMOID_ERROR_DRIVEN: return Math.random() < MathUtils.sigmoid(luRatio, 2 * driftIndicators.get("error") - 1);
            default: return false;
        }
    }

    @Override
    public Instances generateInstances(Instance instance, HashMap<String, Double> driftIndicators) {
        int classValue = (int)instance.classValue();
        int numNeighborsToFind = this.neighborsNumStrategy(driftIndicators);
        if (!this.nnWindowed.containsKey(classValue) || numNeighborsToFind == 0) return null;

        Instances nearestNeighbors = this.nnWindowed.get(classValue).getNearestNeighbors(instance, numNeighborsToFind);

        if (nearestNeighbors != null && nearestNeighbors.numInstances() > 0) {
            Instances generatedInstances = InstanceUtils.createInstances(instance);
            List<Integer> neighborIndices = this.generateNeighborIndices(nearestNeighbors);

            for (int i : neighborIndices) {
                Instance randNeighbor = nearestNeighbors.get(i);
                this.generationStrategy(instance, randNeighbor, super.intensityStrategy(classValue, driftIndicators), generatedInstances);
            }
            return generatedInstances;
        }
        return null;
    }

    int neighborsNumStrategy(HashMap<String, Double> driftIndicators) {
        switch (this.neighborsNumStrategy) {
            case FIXED:
            case FIXED_UNIFORM:
            case FIXED_POISSON: return this.fixedNumNeighbors;
            case ERROR_DRIVEN:
            case ERROR_DRIVEN_UNIFORM:
            case ERROR_DRIVEN_POISSON: return (this.fixedNumNeighbors - (int)(driftIndicators.get("error") * this.fixedNumNeighbors));
            default: return -1;
        }
    }

    List<Integer> generateNeighborIndices(Instances nearestNeighbors) {
        List<Integer> neighborIndices = InstanceUtils.instanceIndices(nearestNeighbors);
        if (!this.randomNeighbors) return neighborIndices;

        Collections.shuffle(neighborIndices);
        int n = nearestNeighbors.numInstances();
        int r;

        switch (this.neighborsNumStrategy) {
            case FIXED_UNIFORM: r = ThreadLocalRandom.current().nextInt(0, n + 1); break;
            case FIXED_POISSON: r = (int)Math.ceil((double)n / MathUtils.randomPoisson01(1)); break;
            default: r = -1;
        }

        return neighborIndices.subList(0, r);
    }

    void generationStrategy(Instance instance, Instance neighbor, int numGenerations, Instances generatedInstances) {
        switch (this.generationStrategy) {
            case LINE: this.generateLineInstances(instance, neighbor, numGenerations, generatedInstances); break;
            case GAUSS: this.generateGaussInstances(instance, neighbor, numGenerations, generatedInstances); break;
            default:
        }
    }

    private void generateLineInstances(Instance instance, Instance neighbor, int numGenerations, Instances generatedInstances) {
        for (int i = 0; i < numGenerations; i++) {
            Instance newInstance = instance.copy();
            newInstance.setClassValue(instance.classValue());
            double gap = this.random.nextDouble();

            for (int j = 0; j < instance.numAttributes() - 1; j++) {
                if (instance.attribute(j).isNumeric()) {
                    newInstance.setValue(j, instance.value(j) + gap * (neighbor.value(j) - instance.value(j)));
                } else {
                    newInstance.setValue(j, this.random.nextBoolean() ? instance.value(j) : neighbor.value(j)); // todo: is it fine?
                }
            }
            generatedInstances.add(newInstance);
        }
    }

    private void generateGaussInstances(Instance instance, Instance neighbor, int numGenerations, Instances generatedInstances) {
        double radius = MathUtils.euclideanDist(instance, neighbor) / 2.0;

        for (int i = 0; i < numGenerations; i++) {
            Instance newInstance = instance.copy();
            newInstance.setClassValue(instance.classValue());

            for (int j = 0; j < instance.numAttributes() - 1; j++) {
                if (instance.attribute(j).isNumeric()) {
                    newInstance.setValue(j, this.random.nextGaussian() * (radius / 3.0) + ((instance.value(j) + neighbor.value(j)) / 2.0));
                } else {
                    newInstance.setValue(j, random.nextBoolean() ? instance.value(j) : neighbor.value(j));
                }
            }
            generatedInstances.add(newInstance);
        }
    }

    @Override
    public boolean getUpdateFirst() {
        return false;
    }

    public SMOTEStrategy setFixedNumNeighbors(int fixedNumNeighbors) {
        this.fixedNumNeighbors = fixedNumNeighbors;
        return this;
    }

    public SMOTEStrategy setLuRatioThreshold(double luRatioThreshold) {
        this.luRatioThreshold = luRatioThreshold;
        return this;
    }

    public SMOTEStrategy setNeighborsRandomness(boolean randomNeighbors)  {
        this.randomNeighbors = randomNeighbors;
        return this;
    }

    public SMOTEStrategy setUnlabeledUncertaintyThreshold(double unlabeledUncertaintyThreshold) {
        this.unlabeledUncertaintyThreshold = unlabeledUncertaintyThreshold;
        return this;
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators) {
        this.trackableParameters.put("nearestNeighborsNum", (double)this.neighborsNumStrategy(driftIndicators));
        this.trackableParameters.put("intensity", (double)super.intensityStrategy((int)instance.classValue(), driftIndicators));

        double averageLuRatio = 0.0, n = 0.0;
        for (int classValue = 0; classValue < instance.numClasses(); classValue++) {
            if (this.nnWindowed.containsKey(classValue)) {
                averageLuRatio += this.nnWindowed.get(classValue).getLuRatio();
                n++;
            }
        }
        this.trackableParameters.put("averageLuRatio", averageLuRatio / n);

        return this.trackableParameters;
    }

    @Override
    public ArrayList<String> getParameterNames() {
        return new ArrayList<>(Arrays.asList("nearestNeighborsNum", "intensity", "averageLuRatio"));
    }
}


