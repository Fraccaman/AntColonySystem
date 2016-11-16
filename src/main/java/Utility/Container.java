package Utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static java.lang.Character.isDigit;

/**
 * Created by FraccaMan on 17/10/16.
 */
public class Container {

    private int best;
    private int dimension;
    private String name;

    private City[] cities;
    private int[][] matrix;

    private int seed;

    public Container(int dimension, String name, String seed) {
        this.dimension = dimension;
        this.name = name;
        this.seed = Integer.parseInt(seed);
        System.out.println("Started problem " + name + " with seed = " + seed);

        // initialize arrays
        cities = new City[dimension];
        matrix = new int[dimension][dimension];
    }

    // Getter and Setter

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getBest() {
        return best;
    }

    public void setBest(int best) {
        this.best = best;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City[] getNodes() {
        return cities;
    }

    public void setNodes(City[] cities) {
        this.cities = cities;
    }

    public City getNode(int idx) {
        return cities[idx];
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    // Helpers

    @Override
    public String toString() {
        return "Container{" +
                "best=" + best +
                ", dimension=" + dimension +
                ", name='" + name + '\'' +
                '}';
    }

    // Private Methods

    private int _addNode(String content, int idx) {
        String[] nodeInfo = content.split(" ");
        City city = new City(Integer.parseInt(nodeInfo[0]) - 1, Double.parseDouble(nodeInfo[1]), Double.parseDouble(nodeInfo[2]));
        cities[idx++] = city;
        return idx;
    }

    private double _distance(City x, City y) {
        return Math.round(Utility.sqrt(Math.pow(x.getX() - y.getX(), 2) + Math.pow(x.getY() - y.getY(), 2)));
    }

    // costum distance function
    private double _distanceFast(City x, City y) {
        return Math.round(Utility.sqrt(Utility.pow(x.getX() - y.getX(), 2) + Utility.pow(x.getY() - y.getY(), 2)));
    }

    // Public Methods

    public void populateContainer(String filePath) throws IOException {
        String line;
        int idx = 0;

        FileReader in = new FileReader(filePath);
        BufferedReader br = new BufferedReader(in);
        while ((line = br.readLine()) != null) {
            if (isDigit(line.charAt(0))) idx = _addNode(line, idx);
            else if (line.contains("BEST_KNOWN"))
                setBest(Integer.valueOf(line.split(":")[1].trim()));
        }
        br.close();

    }

    public void populateMatrix() {
        for (int i = 0; i < getDimension(); i++) {
            for (int j = i + 1; j < getDimension(); j++) {
                int distance = (i == j) ? 0 : (int) _distance(getNode(i), getNode(j));
                matrix[j][i] = distance;
                matrix[i][j] = distance;
            }
        }
    }
}
