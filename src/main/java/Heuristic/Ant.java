package Heuristic;

import java.util.Random;

import Utility.Parameters;
import Utility.Utility;

/**
 * Created by FraccaMan on 08/11/16.
 */
public class Ant {

    private Parameters params;
    private AntColonySystem acs;
    private Random r;
    private int[][] matrix;
    private static double[][] pheromone;
    private int[] localTour, cities;
    private Boolean[] visited;
    private int size, totalDistance, index, last;

    public Ant(Parameters params, Random random, int[][] matrix, double[][] pheromone, int size, AntColonySystem acs) {
        this.acs = acs;
        this.params = params;
        this.r = random;
        this.matrix = matrix;
        this.pheromone = pheromone;
        this.size = size;

        this.localTour = new int[size];
        this.cities = new int[size];
        this.visited = new Boolean[size];

        initAnt();
    }

    private void updateState(int next) {
        if (last != -1) {
            this.totalDistance += matrix[last][next];
        }
        visited[next] = true;
        localTour[index++] = next;
        last = next;
    }

    private void initAnt() {
        for (int i = 0; i < cities.length; i++) {
            cities[i] = i;
            visited[i] = false;
        }
        totalDistance = 0;
        last = -1;
        index = 0;
        int random = r.nextInt(size);
        updateState(random);
    }

    public double randomProportionalRule(int previous, int next) {
        return Utility.pow(pheromone[previous][next], params.getAlfa()) * Utility.pow(1d / (matrix[previous][next] + 0.0001), params.getBeta());
    }

    public int getNextNode() {

        if (index == size)
            return -1;

        if (r.nextDouble() < params.getQ()) {

            double max = Double.MIN_VALUE;
            int idx = -1;

            for (int i = 0; i < cities.length; i++) {
                if (!visited[i]) {
                    final double probability = randomProportionalRule(last, i);
                    if (probability > max) {
                        max = probability;
                        idx = i;
                    }
                }
            }
            return idx;
        } else {

                double sum = 0;

                for (int i = 0; i < cities.length; i++) {
                    if (!visited[i]) {
                        sum += randomProportionalRule(last, i);
                    }
                }

                double out = 1;
                double x;

                for (int i = 0; i < cities.length; i++) {
                    if (!visited[i]) {
                        x = r.nextDouble() * out;
                        final double probability = randomProportionalRule(last, i) / sum;
                        if (x < probability) {
                            return i;
                        }
                        out -= probability;
                    }
                }
            // should never happen!
            throw new RuntimeException("Error in exploration");
        }
    }

    public void next() {
        int next = getNextNode();
        if (next != -1) {
            acs.localUpdate(last, next);
            updateState(next);
        } else {
            totalDistance += matrix[last][localTour[0]];
            acs.localUpdate(last, localTour[0]);
        }
    }

    public int getTotalDistance() {
        return this.totalDistance;
    }

    public int[] getLocalTour() {
        return this.localTour;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    // public methods

    public void setLocalTour(int[] localTour) {
        this.localTour = localTour;
    }
}
