package Building;

import java.util.Random;

import Utility.City;
import Utility.Tour;
import Utility.XSRandom;

/**
 * Created by FraccaMan on 08/11/16.
 */
public class NearestNeighbor implements Building {

    private int[][] matrix;
    private int dimension;
    private XSRandom r;

    public NearestNeighbor(int[][] matrix, XSRandom r) {
        this.matrix = matrix;
        this.dimension = matrix.length;
        this.r = r;
    }

    public Tour ElementaryMyDearWatson(City[] cities, int seed) {
        return _computeNN(cities, seed);
    }

    private Tour _computeNN(City[] cities, int seed) {

        seed = r.nextInt(dimension);
        int best_cost = Integer.MAX_VALUE;
        City start = cities[seed];
        City next = null;

        City[] tour = new City[dimension];
        tour[0] = start;
        start.setVisited();

        for (int i = 1; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if ((best_cost >= matrix[start.getIndex()][j]) && (start.getIndex() != j) && (!cities[j].getVisited())) {
                    best_cost = matrix[start.getIndex()][j];
                    next = cities[j];
                }
            }
            best_cost = Integer.MAX_VALUE;
            start = next;
            start.setVisited();
            tour[i] = start;
        }
        return new Tour(tour);
    }

}
