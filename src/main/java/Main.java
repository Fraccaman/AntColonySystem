import com.google.common.base.Stopwatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.regex.Pattern;

import Building.NearestNeighbor;
import Heuristic.Ant;
import Heuristic.AntColonySystem;
import LocalSearch.CandidateList;
import LocalSearch.TwoOpt;
import LocalSearch.TwoOptCandidateList;
import Utility.Container;
import Utility.Debug;
import Utility.Parameters;
import Utility.Tour;

/**
 * Created by FraccaMan on 08/11/16.
 */
public class Main {

    private static final Pattern COMPILE = Pattern.compile("\\\\");
    private static final Pattern PATTERN = Pattern.compile("\\D+");

    public static void run(String[] args) throws IOException, InterruptedException {


        long startTime = System.currentTimeMillis();
        long endTime = 180000;
        int lastBest = Integer.MAX_VALUE;
        int bestSeed = 0;

        // Start timer
        Stopwatch stopwatch = Stopwatch.createStarted();

        Random seed = new Random();
        int seedInt = seed.nextInt(1000000);
//        int seedInt = 15639;
        System.out.println("seed is: " + seedInt);

        Random r = new Random(seedInt);

        // Set debug environment
        Debug debug = new Debug(true);
        int iterations = 0;

        // Check if path is valid
        if (!isValidPath(args)) System.exit(0);

        // Setup container
        Container container = new Container(getDimension(args[0]), getFileName(args[0]));
        container.populateContainer(args[0]);
        container.populateMatrix();

        NearestNeighbor nearestNeighbor = new NearestNeighbor(container.getMatrix(), r);
        Tour tour = nearestNeighbor.ElementaryMyDearWatson(container.getNodes(), -1);
        tour.setCost(tour.getTourCost(tour.getTuor(), container));
//        System.out.println("Cost NearestNeighbor = " + tour.getCost());

        CandidateList candidates = new CandidateList(container.getMatrix(), 30);
        int[][] candidateList = candidates.buildCandidates();

        TwoOpt twoOpt = new TwoOpt(container.getMatrix());
        TwoOptCandidateList twoOptCandidateList = new TwoOptCandidateList(candidateList, container.getMatrix(), 20);
//        twoOptCandidateList.ElementaryMyDearWatson(tour.getTuor());
//        tour.setCost(tour.getTourCost(tour.getTuor(), container));
//        System.out.println("Cost TwoOpt = " + tour.getCost());

        Parameters params = new Parameters();

        AntColonySystem antColonySystem = new AntColonySystem(params, container.getMatrix(), tour, r);
        antColonySystem.initPheromone();

        int loop = 0;

//        Ant[] ants = antColonySystem.initAnts();


        while (((System.currentTimeMillis() - startTime) < endTime)) {

            loop++;

            Ant[] ants = antColonySystem.initAnts();

            for (int i = 0; i < container.getDimension(); i++) {
                antColonySystem.move(ants);
            }

//            System.out.println("-----------");

            for (int i = 0; i < params.getAnts(); i++) {
                ants[i].setTotalDistance(twoOpt.ElementaryMyDearWatson(ants[i].getLocalTour()));
//                System.out.println("ants[i].getTotalDistance() = " + ants[i].getTotalDistance());
            }

            antColonySystem.setBestTour(ants);
            antColonySystem.globalUpdate();
//            System.out.println("loop: " + loop );
        }
        writeSeed(container.getName(), container.getBest(), seedInt, antColonySystem.getBestTourCost());
//        System.out.println("Error: " + ( (double) Math.abs(container.getBest() - antColonySystem.getBestTourCost()) / container.getBest()) * 100 );
        System.out.println("Iteration over seconds: " + loop / ((System.currentTimeMillis() - startTime) / 1000.0));
//        System.out.println(("time: " + stopwatch)); // formatted string like "12.3 ms"
    }

    private static void writeSeed(String filename, int bestCost, int seed, int antBestCost) throws IOException {

        File file = new File("/Users/FraccaMan/Desktop/cup/ACS_TSP/src/main/resources/Solutions/" + filename + ".txt");
        int bestYet;
        if(file.exists()){
            BufferedReader brTest = new BufferedReader(new FileReader("/Users/FraccaMan/Desktop/cup/ACS_TSP/src/main/resources/Solutions/" + filename + ".txt"));
            String test =  brTest.readLine().split(" | ")[9];
            bestYet = Integer.parseInt(test.trim());
        } else {
            bestYet = Integer.MAX_VALUE;
        }
        if(bestYet > antBestCost){
            PrintWriter outputStream = new PrintWriter("/Users/FraccaMan/Desktop/cup/ACS_TSP/src/main/resources/Solutions/" + filename + ".txt");
            outputStream.print("BestKnown | Seed | Cost: " + bestCost + " | " + seed + " | " + antBestCost + "\n");
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
        if (args.length != 1) {
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


}
