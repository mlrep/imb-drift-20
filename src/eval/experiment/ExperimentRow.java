package eval.experiment;
import cls.SimpleEnsemble;
import framework.Framework;
import moa.classifiers.Classifier;
import moa.classifiers.functions.SGDMultiClass;
import moa.classifiers.lazy.kNN;
import moa.classifiers.lazy.kNNwithPAW;
import moa.classifiers.meta.*;
import moa.options.ClassOption;
import moa.classifiers.trees.HoeffdingAdaptiveTree;
import moa.classifiers.trees.RandomHoeffdingTree;
import moa.classifiers.trees.iadem.Iadem2;
import moa.classifiers.bayes.NaiveBayes;
import moa.classifiers.functions.Perceptron;
import moa.classifiers.rules.RuleClassifier;
public class ExperimentRow {

    public Framework framework;
    public String label;
    public String subLabel;

    public ExperimentRow(Framework framework, String label) {
        this.framework = framework;
        this.label = label;
        this.subLabel = "";
    }

    public ExperimentRow(Framework framework, String label, String subLabel) {
        this.framework = framework;
        this.label = label;
        this.subLabel = subLabel;
    }

    public static Classifier getExperimentClassifier() {
//        OnlineSmoothBoost cls = new OnlineSmoothBoost();
//        RCD cls = new RCD();
//        AccuracyWeightedEnsemble cls = new AccuracyWeightedEnsemble();
//        System.out.println(cls.baseLearnerOption.getValueAsCLIString());
//        AdaptiveRandomForest cls = new AdaptiveRandomForest();
//        OzaBag cls = new OzaBag();

//        cls.learnerOption = new ClassOption("baseLearner", 'l', "Classifier to train.", Classifier.class, "functions.Perceptron");

//        kNN cls = new kNN();
//        Perceptron cls = new Perceptron();

        HoeffdingAdaptiveTree cls = new HoeffdingAdaptiveTree();
//        Iadem2 cls = new Iadem2();
//        NaiveBayes cls = new NaiveBayes();
//        SGDMultiClass cls = new SGDMultiClass();

        return cls;
    }
}
