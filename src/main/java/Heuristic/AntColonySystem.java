package Heuristic;

import java.util.Random;

import Utility.City;
import Utility.Parameters;
import Utility.Tour;

/**
 * Created by FraccaMan on 08/11/16.
 */
public class AntColonySystem implements Heuristic {

    private Parameters params;
    private int[][] matrix;
    private double[][] pheromone;
    private Random r;
    private int[] bestTour;
    private int bestTourCost, size;
    private double initialPheromone;

    public AntColonySystem(Parameters params, int[][] matrix, Tour tour, Random r){
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
        this.initialPheromone =  1d / (size * bestTourCost);
    }

    public void initPheromone(){
        pheromone = new double[size][size];
        for (int i = 0; i < pheromone.length; i++) {
            for (int j = i + 1; j < pheromone.length; j++) {
                pheromone[i][j] = initialPheromone;
                pheromone[j][i] = initialPheromone;
            }
        }
    }

    public void localUpdate(int r, int s){
        pheromone[r][s] = (1d - params.getPheromoneHeuristic()) * pheromone[r][s] + params.getPheromoneHeuristic() * initialPheromone;
    }

    public void globalUpdate(){
        double tao = 1d / bestTourCost;
        int r, s;

        for (int i = 0; i < pheromone.length; i++) {
            for (int j = i + 1; j < pheromone.length; j++) {
                pheromone[i][j] = (( 1d - params.getAlfa()) * pheromone[i][j]);
                pheromone[j][i] = (( 1d - params.getAlfa()) * pheromone[i][j]);
            }
        }

        for (int i = 0; i < this.bestTour.length - 1; i = i + 1) {
            r = this.bestTour[i];
            s = this.bestTour[i+1];
            pheromone[r][s] += (params.getAlfa() * tao);
        }

        pheromone[bestTour[bestTour.length - 1]][bestTour[0]] += (params.getAlfa() * tao);
    }

    public Ant[] initAnts(){
        Ant[] ants = new Ant[params.getAnts()];
        for (int i = 0; i < params.getAnts(); i++) {
            ants[i] = (new Ant(params, r, matrix,pheromone, size, this));
        }
        return ants;
    }

    public void move(Ant[] ants) {
        for (int i = 0; i < ants.length; i++) {
            ants[i].next();
        }
    }

    public void setBestTour(Ant[] ants) {
        int max = Integer.MIN_VALUE;
        int idx = 0;

        for (int i = 0; i < ants.length; i++) {
            if(ants[i].getTotalDistance() < bestTourCost){
                System.out.println("New Best Tour Found: = " + ants[i].getTotalDistance());
                this.bestTour = ants[i].getLocalTour();
                this.bestTourCost = ants[i].getTotalDistance();
            }
        }
    }

    public int getBestTourCost() {
        return bestTourCost;
    }

    public int[] getBestTour() {
        return bestTour;
    }
}
