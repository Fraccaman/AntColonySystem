package Utility;

/**
 * Created by FraccaMan on 02/11/16.
 */
public class Parameters {

    private double alfa;
    private double beta;
    private double q;
    private double pheromoneHeuristic;
    private double memory;
    private int ants;

    public Parameters(double alfa, double beta, double q, double pheromoneHeuristic, double memory, int ants) {
        this.alfa = alfa;
        this.beta = beta;
        this.q = q;
        this.pheromoneHeuristic = pheromoneHeuristic;
        this.ants = ants;
        this.memory = memory;
    }

//    this.alfa = 0.2d;
//        this.beta = 1d;
//        this.q = 0.85d;
//        this.pheromoneHeuristic = 0.1d;
//        this.ants = 4;
//        this.memory = 0.2d;

//        this.alfa = 0.5d;
//        this.beta = 5d;
//        this.q = 0.90d;
//        this.pheromoneHeuristic = 0.1d;
//        this.ants = 5;
//        this.memory = 0.15d;

//            this.alfa = 0.3d;
//        this.beta = 5d;
//        this.q = 0.90d;
//        this.pheromoneHeuristic = 0.1d;
//        this.ants = 5;
//        this.memory = 0.10d;

    public Parameters() {
        this.alfa = 0.2d;
        this.beta = 1d;
        this.q = 0.85d;
        this.pheromoneHeuristic = 0.1d;
        this.ants = 4;
        this.memory = 0.2d;
    }

    // Getters and Setters

    public double getAlfa() {
        return alfa;
    }

    public void setAlfa(double alfa) {
        this.alfa = alfa;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getQ() {
        return q;
    }

    public void setQ(double q) {
        this.q = q;
    }

    public double getPheromoneHeuristic() {
        return pheromoneHeuristic;
    }

    public void setPheromoneHeuristic(double pheromoneHeuristic) {
        this.pheromoneHeuristic = pheromoneHeuristic;
    }

    public int getAnts() {
        return ants;
    }

    public void setAnts(int ants) {
        this.ants = ants;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }
// Helpers

    @Override
    public String toString() {
        return "Parameters{" +
                "alfa=" + alfa +
                ", beta=" + beta +
                ", q=" + q +
                ", pheromoneHeuristic=" + pheromoneHeuristic +
                ", ants=" + ants +
                '}';
    }

    // Methods

    // paper
    public Parameters defaultParameters() {
        return new Parameters(0.1d,2d,0.9d, 0.1d,0.05, 100);
    }
}

