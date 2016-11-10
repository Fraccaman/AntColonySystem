package LocalSearch;

/**
 * Created by FraccaMan on 09/11/16.
 */
public class TwoOptCandidateList implements LocalSearch {

    private int[][] matrix;
    private int dimension;
    private int[][] candidateList;
    private int size;

    public TwoOptCandidateList(int[][] candidateList, int[][] matrix, int size){
        this.matrix = matrix;
        this.dimension = matrix.length;
        this.candidateList = candidateList;
        this.size = size;
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
                for (int j = 0; j < size; j++) {

                    // points to form 2 edges
                    a = cities[i];
                    c = cities[j];
                    d = cities[((j+1) % dimension)];
                    d = 0;

                    int diocane = 0;

                    for (int k = 0; k < cities.length; k++) {
                        if(cities[k] == candidateList[i][j]) {
                            c = cities[k];
                            d = cities[(k+1) % cities.length];
                            diocane = k;
                            break;
                        }
                    }


//                    c = cities[matrix[i][j]];
//                    d = cities[(((matrix[i][j])) + 1) % dimension];

                    // if not ac > bc && not dc > ca then there is no improve due to triangle inequality
                    if(!(matrix[a][b] > matrix[b][c]) && !(matrix[c][d] > matrix[c][a])) continue;

                    localGain = getMoveCost(a, b, c, d, matrix);
                    if (localGain < bestGain) {
                        first = i;
                        second = diocane;
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
