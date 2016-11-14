import com.google.common.base.Stopwatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.regex.Pattern;

import Building.NearestNeighbor;
import Heuristic.Ant;
import Heuristic.AntColonySystem;
import LocalSearch.TwoOpt;
import Utility.Container;
import Utility.Parameters;
import Utility.Tour;

/**
 * Created by FraccaMan on 08/11/16.
 */
public class Main {

    private static final Pattern COMPILE = Pattern.compile("\\\\");
    private static final Pattern PATTERN = Pattern.compile("\\D+");

    public static void main(String[] args) throws IOException, InterruptedException {


        long startTime = System.currentTimeMillis();
        long endTime = 180000;

        // Check if path is valid
        if (!isValidPath(args)) System.exit(0);

        // Setup container
        Container container = new Container(getDimension(args[0]), getFileName(args[0]), args[1]);
        container.populateContainer(args[0]);
        container.populateMatrix();

        Random r = new Random(container.getSeed());

        NearestNeighbor nearestNeighbor = new NearestNeighbor(container.getMatrix(), r);
        Tour tour = nearestNeighbor.ElementaryMyDearWatson(container.getNodes(), -1);
        tour.setCost(tour.getTourCost(tour.getTuor(), container));

        TwoOpt twoOpt = new TwoOpt(container.getMatrix());
        Parameters params = setupParameters(container.getName());
        System.out.println(params.toString());


        AntColonySystem antColonySystem = new AntColonySystem(params, container.getMatrix(), tour, r);
        antColonySystem.initPheromone();

        while (((System.currentTimeMillis() - startTime) < endTime)) {

            Ant[] ants = antColonySystem.initAnts();

            for (int i = 0; i < container.getDimension(); i++) {
                antColonySystem.move(ants);
            }

            for (int i = 0; i < params.getAnts(); i++) {
                ants[i].setTotalDistance(twoOpt.ElementaryMyDearWatson(ants[i].getLocalTour()));
            }

            antColonySystem.setBestTour(ants);
            antColonySystem.globalUpdate();
        }
        writeSeed(container.getName(), container.getBest(), container.getSeed(), antColonySystem.getBestTourCost(), params, antColonySystem.getBestTour());
        System.out.println("Best Tour found with cost: " + antColonySystem.getBestTourCost());
    }

    private static void writeSeed(String filename, int bestCost, int seed, int antBestCost, Parameters params, int[] cities) throws IOException {

        File file = new File(filename + ".txt");
        int bestYet;
        if(file.exists()){
            BufferedReader brTest = new BufferedReader(new FileReader(filename + ".txt"));
            String test =  brTest.readLine().split(" | ")[9];
            bestYet = Integer.parseInt(test.trim());
        } else {
            bestYet = Integer.MAX_VALUE;
        }
        if(bestYet >= antBestCost){
            PrintWriter outputStream = new PrintWriter(filename + ".txt");
            outputStream.print("BestKnown | Seed | Cost: " + bestCost + " | " + seed + " | " + antBestCost + "\n");
            outputStream.print("Error: " + ( (double) Math.abs((bestCost - antBestCost))/ bestCost ) * 100 + "\n");
            outputStream.print("Alfa: " + params.getAlfa() + " Beta: " + params.getBeta() + " Q: " + params.getQ() + " Ph: " + params.getPheromoneHeuristic() + " Ants: " + params.getAnts() + "\n");
            outputStream.print("Tour: ");
            for (int i = 0; i < cities.length; i++) {
                outputStream.print(cities[i] + " ");
            }
            outputStream.close();
        }
    }

    // Private Methods

    private static void _usage() {
        System.out.println("java Main <file>");
    }

    // Public Methods

    public static String getFileName(String filepath) {
        int idx = COMPILE.matcher(filepath).replaceAll("/").lastIndexOf("/");
        return idx >= 0 ? filepath.substring(idx + 1) : filepath;
    }

    public static int getDimension(String filepath) {
        String filename = getFileName(filepath);
        return Integer.parseInt(PATTERN.matcher(filename).replaceAll(""));
    }

    public static Boolean isValidPath(String[] args) {
        if (args.length != 2) {
            _usage();
            return false;
        }

        File f = new File(args[0]);
        if (!f.exists() || f.isDirectory()) {
            System.out.println("File " + args[0] + " does not exist!");
            _usage();
            return false;
        } else {
            return true;
        }
    }

    public static Parameters setupParameters(String filename){

        if(filename.equals("eil76.tsp")) {
            return new Parameters(0.2d, 2d, 0.9d, 0.1d, 5);
        } else if (filename.equals("kroA100.tsp")){
            return new Parameters(0.2d, 2d, 0.9d, 0.1d, 5);
        } else if (filename.equals("ch130.tsp")) {
            return new Parameters(0.2d, 2d, 0.9d, 0.1d, 4);
        } else if (filename.equals("d198.tsp")) {
            return new Parameters(0.5d, 5d,0.9d, 0.1d, 5);
        } else if(filename.equals("lin318.tsp")) {
            return new Parameters(0.12, 2d, 0.9d, 0.1d, 5);
        } else if(filename.equals("pr439.tsp")) {
            return new Parameters(0.2d, 2d, 0.85d, 0.1d, 5);
        } else if(filename.equals("pcb442.tsp")) {
            return new Parameters(0.2d,2d,0.9d, 0.1d, 5);
        } else if(filename.equals("rat783.tsp")) {
            return new Parameters(0.2d, 2d, 0.85d, 0.1d, 5);
        } else if(filename.equals("u1060.tsp")) {
            return new Parameters(0.5d, 5d, 0.9d, 0.1d, 5);
        } else if(filename.equals("fl1577.tsp")) {
            return new Parameters(0.5d, 7d, 0.85d, 0.1d, 10);
        }
        System.out.println("DEFAULT PARAMETER");
        return new Parameters(0.1d, 2d, 0.9d, 0.1d, 5);
    }
}
