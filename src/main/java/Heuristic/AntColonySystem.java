package Heuristic;

import java.util.Arrays;
import java.util.Random;

import Utility.City;
import Utility.Parameters;
import Utility.Tour;
import Utility.Utility;

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
    private double[][] probability;
    private int resetCounter = 0;
    private double[] edgesProbability;

    public AntColonySystem(Parameters params, int[][] matrix, Tour tour, Random r){
        this.params = params;
        this.r = r;
        this.size = tour.getTuor().length;
        this.matrix = matrix;
        this.pheromone = new double[size][size];
        this.bestTour = new int[size];
        this.probability = new double[size][size];
        for (int i = 0; i < size; i++) {
            this.bestTour[i] = tour.getTuor()[i].getIndex();
        }
        this.bestTourCost = tour.getCost();
        this.initialPheromone =  1d / (size * bestTourCost);
        this.edgesProbability = new double[size];
        Arrays.fill(edgesProbability, 1);
    }

    public void initPheromone(){
        pheromone = new double[size][size];
        for (int i = 0; i < pheromone.length; i++) {
            for (int j = i + 1; j < pheromone.length; j++) {
                pheromone[i][j] = initialPheromone;
                pheromone[j][i] = initialPheromone;
            }
        }

        for (int i = 0; i < pheromone.length; i++) {
            for (int j = i + 1; j < pheromone.length; j++) {
                probability[i][j] = randomProportionalRule(i, j);
                probability[j][i] = randomProportionalRule(j, i);
            }
        }

    }

    public double localProbability(int r, int s){
       return (1d - params.getPheromoneHeuristic()) * pheromone[r][s] + params.getPheromoneHeuristic() * initialPheromone;
    }

    public void localUpdate(int r, int s){
        pheromone[r][s] = (1d - params.getPheromoneHeuristic()) * pheromone[r][s] + params.getPheromoneHeuristic() * initialPheromone;
        probability[r][s] = randomProportionalRule(r,s);
    }

    private double randomProportionalRule(int previous, int next) {
        return Utility.pow(pheromone[previous][next], params.getAlfa()) * Utility.pow(1d / (matrix[previous][next] + 0.0001), params.getBeta());
    }

    public void globalUpdate(){
        double tao = 1d / bestTourCost;
        int r, s;

//        for (int i = 0; i < pheromone.length; i++) {
//            for (int j = i + 1; j < pheromone.length; j++) {
//                double update = (( 1d - params.getAlfa()) * pheromone[i][j]);
//                pheromone[i][j] = update;
////                pheromone[j][i] = update;
//                probability[i][j] = randomProportionalRule(i,j);
////                probability[j][i] = probability[i][j];
//            }
//        }

        for (int i = 0; i < this.bestTour.length - 1; i = i + 1) {
            r = this.bestTour[i];
            s = this.bestTour[i+1];
            pheromone[r][s] = (( 1d - params.getAlfa()) * pheromone[r][s]) + (params.getAlfa() * tao);
            probability[r][s] = randomProportionalRule(r,s);
        }

        pheromone[bestTour[bestTour.length - 1]][bestTour[0]] = (( 1d - params.getAlfa()) * pheromone[bestTour[bestTour.length - 1]][bestTour[0]]) + (params.getAlfa() * tao);
        probability[bestTour[bestTour.length - 1]][bestTour[0]] = randomProportionalRule((bestTour.length - 1),bestTour[0]);

        if(resetCounter > 350)
            stagnationRecovery();
    }

    public Ant[] initAnts(){
        Ant[] ants = new Ant[params.getAnts()];
        for (int i = 0; i < params.getAnts(); i++) {
            ants[i] = (new Ant(params, r, matrix,pheromone, size, this, probability));
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
        resetCounter++;

        for (int i = 0; i < ants.length; i++) {
            if(ants[i].getTotalDistance() < bestTourCost){
                System.out.println("New Best Tour Found: = " + ants[i].getTotalDistance());
                this.bestTour = ants[i].getLocalTour();
                this.bestTourCost = ants[i].getTotalDistance();
                resetCounter = 0;
                Arrays.fill(edgesProbability, 1);
            }
        }
    }
    
    public void stagnationRecovery(){
        System.out.println("STAGNATION RECOVERY!!");

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < bestTour.length - 1; i++) {
            if(matrix[i][i+1] > max){
                max = matrix[i][i + 1];
            }
        }

        int toReduce = size / 10;
        for (int i = 0; i < bestTour.length - 1; i++) {
            edgesProbability[i] = edgesProbability[i] * 0.9 * (matrix[i][i + 1] / max);
        }

        Arrays.sort(edgesProbability);

        for (int i = 0; i < edgesProbability.length / 2; i++) {
            double temp = edgesProbability[i]; // swap numbers
            edgesProbability[i] = edgesProbability[edgesProbability.length - 1 - i];
            edgesProbability[edgesProbability.length - 1 - i] = temp;
        }

        for (int i = 0; i <= toReduce; i++) {
            pheromone[i][i+1] = params.getPheromoneHeuristic() * initialPheromone;
        }

        resetCounter = 0;
//        Arrays.fill(edgesProbability, 1);
    }

    public int getBestTourCost() {
        return bestTourCost;
    }

    public int[] getBestTour() {
        return bestTour;
    }
}
