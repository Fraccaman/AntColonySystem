package Heuristic;

import java.util.Hashtable;
import java.util.Random;

import LocalSearch.TwoOpt;
import Utility.Parameters;
import Utility.Tour;
import Utility.Pair;
import Utility.XSRandom;

/**
 * Created by FraccaMan on 08/11/16.
 */
public class AntColonySystem implements Heuristic {

    private Parameters params;
    private int[][] matrix;
    private double[][] pheromone;
    private XSRandom r;
    private int[] bestTour;
    private int bestTourCost, size;
    private double initialPheromone;

    public Hashtable<Double, Double> pows;

    public AntColonySystem(Parameters params, int[][] matrix, Tour tour, XSRandom r) {
        this.params = params;
        this.r = r;
        this.size = tour.getTuor().length;
        this.matrix = matrix;
        this.pheromone = new double[size][size];
        this.bestTour = new int[size];
        for (int i = 0; i < size; i++) {
            this.bestTour[i] = tour.getTuor()[i].getIndex();
        }
        this.bestTourCost = tour.getCost();
        this.initialPheromone = 1d / (size * bestTourCost);
        this.pows = new Hashtable<>();
    }

    public void initPheromone() {
        pheromone = new double[size][size];
        for (int i = 0; i < pheromone.length; i++) {
            for (int j = i + 1; j < pheromone.length; j++) {
                pheromone[i][j] = initialPheromone;
                pheromone[j][i] = initialPheromone;
            }
        }
    }

    public void localUpdate(int r, int s) {
        pheromone[r][s] = (1d - params.getPheromoneHeuristic()) * pheromone[r][s] + params.getPheromoneHeuristic() * initialPheromone;
    }

    private void globalUpdate() {
        double tao = 1d / bestTourCost;
        int r, s;

        for (int i = 0; i < pheromone.length; i++) {
            for (int j = i + 1; j < pheromone.length; j++) {
                pheromone[i][j] = ((1d - params.getAlfa()) * pheromone[i][j]);
                pheromone[j][i] = ((1d - params.getAlfa()) * pheromone[i][j]);
            }
        }

        for (int i = 0; i < this.bestTour.length - 1; i = i + 1) {
            r = this.bestTour[i];
            s = this.bestTour[i + 1];
            pheromone[r][s] += (params.getAlfa() * tao);
        }

        pheromone[bestTour[bestTour.length - 1]][bestTour[0]] += (params.getAlfa() * tao);
    }

    private Ant[] initAnts() {
        Ant[] ants = new Ant[params.getAnts()];
        for (int i = 0; i < params.getAnts(); i++) {
            ants[i] = (new Ant(params, r, matrix, pheromone, size, this));
        }
        return ants;
    }

    private void move(Ant[] ants) {
        for (int i = 0; i < ants.length; i++) {
            ants[i].next();
        }
    }

    private int getBestTourCost() {
        return bestTourCost;
    }

    private int[] getBestTour() {
        return bestTour;
    }

    private void setBestTour(Ant[] ants) {
        int max = Integer.MIN_VALUE;
        int idx = 0;

        for (int i = 0; i < ants.length; i++) {
            if (ants[i].getTotalDistance() < bestTourCost) {
                this.bestTour = ants[i].getLocalTour();
                this.bestTourCost = ants[i].getTotalDistance();
            }
        }
    }

    @Override
    public Pair<int[], Integer> ElementaryMyDearWatson(AntColonySystem antColonySystem, TwoOpt twoOpt, long startTime, long endTime) {

        int loops = 0;

        while (((System.currentTimeMillis() - startTime) < endTime)) {

            loops += 1;

            Ant[] ants = antColonySystem.initAnts();

            for (int i = 0; i < size; i++) {
                antColonySystem.move(ants);
            }

            for (int i = 0; i < params.getAnts(); i++) {
                ants[i].setTotalDistance(twoOpt.ElementaryMyDearWatson(ants[i].getLocalTour()));
            }

            antColonySystem.setBestTour(ants);
            antColonySystem.globalUpdate();
        }

        System.out.println("loops: " + loops);

        return new Pair(antColonySystem.getBestTour(), antColonySystem.getBestTourCost());
    }
}
