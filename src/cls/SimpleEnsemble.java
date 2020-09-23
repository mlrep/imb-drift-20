package cls;

import com.yahoo.labs.samoa.instances.Instance;
import strategies.divers.DiversityMeasure;
import utils.MathUtils;
import utils.Trackable;
import utils.windows.WindowedValue;
import moa.classifiers.AbstractClassifier;
import moa.classifiers.Classifier;
import moa.core.DoubleVector;
import moa.core.Measurement;
import moa.core.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class SimpleEnsemble extends AbstractClassifier implements Trackable {

    private ArrayList<Classifier> classifiers = new ArrayList<>();
    private Classifier classifierTemplate;
    private ArrayList<WindowedValue> accuracies = new ArrayList<>();
    private DiversityMeasure diversityMeasure = new DiversityMeasure(WindowedValue.WINDOW_ESTIMATOR_WIDTH, 0.2);
    private boolean collectDiversityMeasures = false;
    private int ensembleSize;
    private boolean dynamic;
    private DecisionScheme decisionScheme = DecisionScheme.MAJORITY;

    public SimpleEnsemble(Classifier classifierTemplate) {
        this.classifierTemplate = classifierTemplate;
        this.ensembleSize = 0;
        this.dynamic = true;
        this.classifierRandom = new Random();
    }

    public SimpleEnsemble(int size, Classifier classifierTemplate) {
        this.classifierTemplate = classifierTemplate;
        this.ensembleSize = 0;
        this.dynamic = false;
        this.classifierRandom = new Random();

        for (int i = 0; i < size; i++) {
            this.addClassifier();
        }
    }

    public void addClassifier() {
        this.classifiers.add(this.classifierTemplate.copy());
        this.diversityMeasure.addClassifierMeasure();
        this.accuracies.add(new WindowedValue(100));
        this.ensembleSize++;
    }

    public void removeClassifier(int index) {
        this.classifiers.remove(index);
        this.diversityMeasure.removeClassifierMeasure(index);
        this.accuracies.remove(index);
        this.ensembleSize--;
    }

    public void trainClassifierOnInstance(int classifierIndex, Instance instance) {
        this.classifiers.get(classifierIndex).trainOnInstance(instance);
        if (this.decisionScheme == DecisionScheme.WEIGHTED) this.updateAccuracy(classifierIndex, instance);
    }

    private void updateAccuracy(int classifierIndex, Instance instance) {
        DoubleVector vote = new DoubleVector(this.classifiers.get(classifierIndex).getVotesForInstance(instance));
        int lastVote = Utils.maxIndex(vote.getArrayRef());
        accuracies.get(classifierIndex).add((lastVote == instance.classValue() ? 1.0 : 0.0));
    }

    public int getSize() {
        return this.ensembleSize;
    }

    @Override
    public void prepareForUse() {
        if (dynamic) {
            this.classifierTemplate.prepareForUse();
        } else {
            for (Classifier classifier : this.classifiers) {
                classifier.prepareForUse();
            }
        }
    }

    @Override
    public Classifier[] getSubClassifiers() {
        return this.classifiers.toArray(new Classifier[this.classifiers.size()]);
    }

    @Override
    public double[] getVotesForInstance(Instance instance) {
        DoubleVector combinedVote = new DoubleVector();
        ArrayList<Integer> lastVotes = new ArrayList<>();

        for (int i = 0; i < this.classifiers.size(); i++) {
            Classifier classifier = this.classifiers.get(i);
            DoubleVector vote = new DoubleVector(classifier.getVotesForInstance(instance));

            if (vote.sumOfValues() > 0.0) {
                vote.normalize();
                int lastVote = Utils.maxIndex(vote.getArrayRef());
                accuracies.get(i).add((lastVote == instance.classIndex() ? 1.0 : 0.0));

                switch (this.decisionScheme) {
                    case MAJORITY: combinedVote.addValues(vote); break;
                    case WEIGHTED:
                        vote.scaleValues(this.accuracies.get(i).getAverage());
                        combinedVote.addValues(vote);
                        break;
                }

                lastVotes.add(lastVote);
            }
        }

        if (this.collectDiversityMeasures) this.diversityMeasure.update(lastVotes, (int)instance.classValue());

        return combinedVote.getArrayRef();
    }

    @Override
    public void resetLearningImpl() {
        this.classifierTemplate.resetLearning();
        if (dynamic) {
            this.classifiers.clear();
            this.ensembleSize = 0;
        } else {
            for (Classifier classifier : this.classifiers) {
                classifier.resetLearning();
            }
        }

        this.diversityMeasure.reset();
        this.accuracies.clear();

        if (!this.dynamic) {
            for (int i = 0; i < this.ensembleSize; i++) {
                this.diversityMeasure.addClassifierMeasure();
                this.accuracies.add(new WindowedValue(100)); // todo: CHANGE IT FOR DIFFERENT BUDGETS
            }
        }
    }

    @Override
    public void trainOnInstanceImpl(Instance instance) {
        for (Classifier classifier : this.classifiers) {
            classifier.trainOnInstance(instance.copy());
        }
    }

    @Override
    protected Measurement[] getModelMeasurementsImpl() {
        return new Measurement[0];
    }

    @Override
    public void getModelDescription(StringBuilder stringBuilder, int i) {}

    @Override
    public boolean isRandomizable() {
        return false;
    }

    public SimpleEnsemble setDecisionScheme(DecisionScheme ds) {
        this.decisionScheme = ds;
        return this;
    }

    public SimpleEnsemble setCollectDiversityMeasures(boolean collectDiversityMeasures) {
        this.collectDiversityMeasures = collectDiversityMeasures;
        return this;
    }

    @Override
    public HashMap<String, Double> getTrackableParameters(Instance instance, HashMap<String, Double> driftIndicators) {
        return this.diversityMeasure.getMeasures();
    }

    @Override
    public ArrayList<String> getParameterNames() {
        return new ArrayList<>(this.diversityMeasure.getMeasures().keySet());
    }
}
