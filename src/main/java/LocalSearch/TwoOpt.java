package LocalSearch;

import Utility.City;
import Utility.Container;

/**
 * Created by FraccaMan on 08/11/16.
 */
public class TwoOpt implements LocalSearch {

    private int[][] matrix;
    private int dimension;

    public TwoOpt(int[][] matrix){
        this.matrix = matrix;
        this.dimension = matrix.length;
    }

    public int ElementaryMyDearWatson(int[] cities) {
        int bestGain = Integer.MAX_VALUE, localGain = 0;
        int first = -1, second = -1, iteration = 0, a = 0, b = 0, c = 0, d = 0;
        boolean repeat = true;

        while (repeat) {
            repeat = false;
            iteration++;
            for (int i = 0; i < dimension; i++) {
                bestGain = 0;
                for (int j = i + 2; j < dimension; j++) {

                    // points to form 2 edges
                    a = cities[i];
                    b = cities[i+1];
                    c = cities[j];
                    d = cities[((j+1) % dimension)];

                    // if not ac > bc && not dc > ca then there is no improve due to triangle inequality
                    if(!(matrix[a][b] > matrix[b][c]) && !(matrix[c][d] > matrix[c][a])) continue;

                    localGain = getMoveCost(a, b, c, d, matrix);
                    if (localGain < bestGain) {
                        first = i;
                        second = j;
                        bestGain = localGain;
                    }
                }
                if (bestGain < 0) {
                    repeat = true;
                    swap(first + 1, second, cities);
                }
            }
        }
        return getTourCost(cities);
    }

    // Private methods

    private int getMoveCost(int a, int b, int c, int d, int[][] matrix) {
        // (ac + bd) - (ab + cd)
        return ((matrix[a][c] + matrix[b][d]) - (matrix[a][b] + matrix[c][d]));
    }

    private void swap(int i, int j, int[] cities) {
        int tmp;
        while (i < j) {
            tmp = cities[i];
            cities[i] = cities[j];
            cities[j] = tmp;
            i++;
            j--;
        }
    }

    private int getTourCost(int[] cities){
        int cost = 0;
        for (int i = 0; i < cities.length -1; i++) {
            cost += matrix[cities[i]][cities[i+1]];
        }
        cost += matrix[cities[cities.length - 1]][cities[0]];
        return cost;
    }

}
