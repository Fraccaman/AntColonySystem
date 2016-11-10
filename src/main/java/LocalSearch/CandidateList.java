package LocalSearch;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by FraccaMan on 09/11/16.
 */
public class CandidateList {

    private int[][] matrix;
    private int[][] candidates;
    private int size;
    private int candidateSize;

    public CandidateList(int[][] matrix, int candidateSize) {
        this.matrix = matrix;
        this.candidates = new int[matrix.length][matrix.length];
        this.size = matrix.length;
        this.candidateSize = candidateSize;
    }

    public int[][] buildCandidates() {

        Map<Integer, Integer> tmp = new HashMap<Integer, Integer>(size);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (i != j)
                    tmp.put(j, matrix[i][j]);
            }

            Map<Integer, Integer> result = new LinkedHashMap<>(candidateSize);
            tmp.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .limit(candidateSize)
                    .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));

            int index = 0;
            for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
                    candidates[i][index++] = entry.getKey();
//                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
        }
        return candidates;
    }

}
