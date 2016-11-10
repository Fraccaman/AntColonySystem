package Utility;

/**
 * Created by FraccaMan on 02/11/16.
 */
public class Parameters {

    private double alfa;
    private double beta;
    private double q;
    private double pheromoneHeuristic;
    private int ants;

    public Parameters(double alfa, double beta, double q, double pheromoneHeuristic, int ants) {
        this.alfa = alfa;
        this.beta = beta;
        this.q = q;
        this.pheromoneHeuristic = pheromoneHeuristic;
        this.ants = ants;
    }

    public Parameters() {
        this.alfa = 0.5d;
        this.beta = 7d;
        this.q = 0.85d;
        this.pheromoneHeuristic = 0.1d;
        this.ants = 4;
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
        return new Parameters(0.1d,2d,0.9d, 0.1d,100);
    }
}

