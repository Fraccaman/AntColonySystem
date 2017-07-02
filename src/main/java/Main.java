import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.regex.Pattern;

import Building.NearestNeighbor;
import Heuristic.AntColonySystem;
import LocalSearch.TwoOpt;
import Utility.Container;
import Utility.Parameters;
import Utility.Tour;
import Utility.XSRandom;
import Utility.Pair;

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

        // create a random generator

        XSRandom r = new XSRandom(container.getSeed());
//        Random r = new XSRandom(container.getSeed());
//        Random r = new Random(container.getSeed());

        // initial solution
        NearestNeighbor nearestNeighbor = new NearestNeighbor(container.getMatrix(), r);
        Tour tour = nearestNeighbor.ElementaryMyDearWatson(container.getNodes(), -1);
        tour.setCost(tour.getTourCost(tour.getTuor(), container));

        // intialize paramaters and local search
        TwoOpt twoOpt = new TwoOpt(container.getMatrix());
        Parameters params = setupParameters(container.getName());

        // intialize ant colony system
        AntColonySystem antColonySystem = new AntColonySystem(params, container.getMatrix(), tour, r);
        antColonySystem.initPheromone();

        // run ant colony system
        Pair result = antColonySystem.ElementaryMyDearWatson(antColonySystem, twoOpt, startTime, endTime);

        // write result
        writeSeed(container.getName(), container.getBest(), container.getSeed(), (int) result.snd, params, (int[]) result.fst);
    }

    // Private Methods

    private static void writeSeed(String filename, int bestCost, int seed, int antBestCost, Parameters params, int[] cities) throws IOException {

        File file = new File(filename + ".txt");
        System.out.println("antBestCost = " + antBestCost);
        int bestYet;
        if (file.exists()) {
            BufferedReader brTest = new BufferedReader(new FileReader(filename + ".txt"));
            String test = brTest.readLine().split(" | ")[9];
            bestYet = Integer.parseInt(test.trim());
        } else {
            bestYet = Integer.MAX_VALUE;
        }
        if (bestYet >= antBestCost) {
            PrintWriter outputStream = new PrintWriter(filename + ".txt");
            outputStream.print("BestKnown | Seed | Cost: " + bestCost + " | " + seed + " | " + antBestCost + "\n");
            outputStream.print("Error: " + ((double) Math.abs((bestCost - antBestCost)) / bestCost) * 100 + "\n");
            outputStream.print("Alfa: " + params.getAlfa() + " Beta: " + params.getBeta() + " Q: " + params.getQ() + " Ph: " + params.getPheromoneHeuristic() + " Ants: " + params.getAnts() + "\n");
            outputStream.print("Tour: ");
            for (int i = 0; i < cities.length; i++) {
                outputStream.print(cities[i] + " ");
            }
            outputStream.close();
        }
        System.out.println("A solution has been found! The report has been written to: " + filename + ".txt");
    }

    private static void _usage() {
        System.out.println("java Main <file>");
    }

    private static String getFileName(String filepath) {
        int idx = COMPILE.matcher(filepath).replaceAll("/").lastIndexOf("/");
        return idx >= 0 ? filepath.substring(idx + 1) : filepath;
    }

    private static int getDimension(String filepath) {
        String filename = getFileName(filepath);
        return Integer.parseInt(PATTERN.matcher(filename).replaceAll(""));
    }

    private static Boolean isValidPath(String[] args) {
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

    private static Parameters setupParameters(String filename) {

        if (filename.equals("eil76.tsp")) {
            return new Parameters(0.2d, 2d, 0.9d, 0.1d, 5);
        } else if (filename.equals("kroA100.tsp")) {
            return new Parameters(0.2d, 2d, 0.9d, 0.1d, 5);
        } else if (filename.equals("ch130.tsp")) {
            return new Parameters(0.2d, 2d, 0.9d, 0.1d, 4);
        } else if (filename.equals("d198.tsp")) {
            return new Parameters(0.5d, 5d, 0.9d, 0.1d, 5);
        } else if (filename.equals("lin318.tsp")) {
            return new Parameters(0.12, 2d, 0.9d, 0.1d, 5);
        } else if (filename.equals("pr439.tsp")) {
            return new Parameters(0.2d, 2d, 0.85d, 0.1d, 5);
        } else if (filename.equals("pcb442.tsp")) {
            return new Parameters(0.2d, 2d, 0.9d, 0.1d, 5);
        } else if (filename.equals("rat783.tsp")) {
            return new Parameters(0.2d, 2d, 0.85d, 0.1d, 5);
        } else if (filename.equals("u1060.tsp")) {
            return new Parameters(0.5d, 5d, 0.9d, 0.1d, 5);
        } else if (filename.equals("fl1577.tsp")) {
            return new Parameters(0.5d, 7d, 0.85d, 0.1d, 10);
        }
        System.out.println("DEFAULT PARAMETER");
        return new Parameters(0.1d, 2d, 0.9d, 0.1d, 5);
    }
}
