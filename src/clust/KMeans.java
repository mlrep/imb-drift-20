package clust;

import com.yahoo.labs.samoa.instances.Instance;
import com.yahoo.labs.samoa.instances.Instances;
import javafx.util.Pair;
import utils.InstanceUtils;
import utils.MathUtils;
import utils.windows.WindowedInstances;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class KMeans implements Clusterer {

    private ArrayList<WindowedInstances> centroids = new ArrayList<>();
    protected Random random = new Random();

    protected int k;
    private int n = 0;
    private ArrayList<Double> distances = new ArrayList<>();
    protected boolean clustersInitialized = false;

    protected double f = 0.0;
    private int q = 0;

    public KMeans(int k) {
        this.k = (int)Math.ceil((k - 15) / 5.0);
    }

    @Override
    public Pair<ArrayList<Integer>, Integer> update(Instance instance) {
        if (!this.clustersInitialized) {
            this.initializingClusters(instance);
            return new Pair<>(new ArrayList<>(), 1);
        }

        return this.updateCentroids(instance);
    }

    private void initializingClusters(Instance instance) {
        for (WindowedInstances centroid : this.centroids) {
            this.distances.add(MathUtils.euclideanDist(instance, centroid.getCentroid()));
        }

        this.centroids.add(new WindowedInstances(WindowedInstances.WINDOW_ESTIMATOR_WIDTH, instance, true)); // todo: false? compare diversity

        if (++this.n >= this.k + 10) {
            this.clustersInitialized = true;
            this.initializeCost();
        }
    }

    private void initializeCost() {
        Collections.sort(this.distances);
        for (int i = 0; i < 10; i++) {
            double dist = this.distances.get(i);
            this.f += dist*dist;
        }

        this.f /= 2.0;
    }

    private Pair<ArrayList<Integer>, Integer> updateCentroids(Instance instance) {
        ArrayList<Integer> removed = new ArrayList<>();
        int added = 0;

        Pair<Integer, Double> minIndexDist = InstanceUtils.findClosestInstance(instance, this.getCentroids());
        int minIndex = minIndexDist.getKey();
        double minDist = minIndexDist.getValue();
        double p = Math.min(minDist*minDist / this.f, 1.0);

        if (p >= this.random.nextDouble()) {
            this.addNewCluster(instance);
            added++;
        } else {
            WindowedInstances centroidWindow = this.centroids.get(minIndex);
            centroidWindow.add(instance);
            this.centroids.set(minIndex, centroidWindow);
        }

        // todo: removing instances for the rest, so we can also remove old clusters?
        return new Pair<>(removed, added);
    }

    private void addNewCluster(Instance instance) {
        this.centroids.add(new WindowedInstances(WindowedInstances.WINDOW_ESTIMATOR_WIDTH, instance, true));
        this.q++;

        if (q >= this.k) {
            this.q = 0;
            this.f *= 10; // todo: after removing (window) cluster: f *= 0.1, so adding new is more likely
        }
    }

    @Override
    public Instances getCentroids() {
        if (this.centroids.size() == 0) return null;
        Instances centroids = InstanceUtils.createInstances(this.centroids.get(0).getCentroid());
        this.centroids.stream().map(WindowedInstances::getCentroid).forEach(centroids::add);

        return centroids;
    }

    @Override
    public void reset() {
        this.centroids.clear();

        this.n = 0;
        this.distances.clear();
        this.clustersInitialized = false;

        this.f = 0;
        this.q = 0;
    }
}
