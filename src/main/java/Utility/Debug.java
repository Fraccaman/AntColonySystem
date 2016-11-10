package Utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by FraccaMan on 02/11/16.
 */
public class Debug<E> {

    public static List<Integer> ch130_1_nn = Arrays.asList(1, 41, 39, 71, 130, 50, 2, 118, 80, 46, 20, 35, 54, 17, 31, 34, 27, 19, 100, 116, 24, 15, 29, 95, 79, 38, 106, 58, 49, 53, 120, 60, 51, 42, 44, 40, 47, 37, 22, 23, 122, 55, 14, 10, 67, 96, 13, 33, 21, 18, 8, 108, 114, 3, 83, 30, 59, 121, 78, 90, 125, 85, 66, 28, 115, 62, 105, 112, 117, 128, 16, 45, 76, 109, 61, 129, 124, 26, 97, 70, 7, 63, 68, 98, 110, 89, 94, 77, 103, 81, 87, 12, 65, 56, 57, 9, 52, 75, 74, 99, 73, 92, 72, 91, 6, 102, 93, 4, 126, 5, 11, 64, 69, 88, 86, 127, 107, 104, 43, 48, 25, 113, 32, 36, 84, 119, 111, 123, 101, 82);
    public static List<Integer> ch130_5_nn = Arrays.asList(5, 11, 76, 109, 61, 129, 124, 26, 97, 70, 7, 63, 68, 98, 110, 89, 94, 29, 15, 24, 116, 95, 79, 38, 106, 58, 49, 53, 120, 60, 51, 42, 44, 40, 47, 37, 22, 23, 122, 55, 14, 10, 67, 96, 13, 33, 21, 18, 8, 108, 114, 3, 83, 30, 59, 121, 78, 90, 125, 85, 66, 28, 115, 62, 105, 112, 117, 39, 71, 41, 1, 130, 50, 2, 118, 80, 46, 20, 35, 54, 17, 31, 34, 27, 19, 100, 43, 104, 107, 127, 45, 16, 128, 126, 93, 4, 92, 73, 99, 74, 75, 52, 9, 57, 56, 65, 103, 77, 81, 87, 12, 82, 101, 123, 111, 119, 84, 36, 32, 113, 25, 48, 88, 69, 64, 86, 72, 91, 6, 102);

    private Boolean verbose;

    public Debug(Boolean bool) {
        verbose = bool;
    }

    // Getter and Setter

    public Boolean getVerbose() {
        return verbose;
    }

    public void setVerbose() {
        verbose = true;
    }

    // Private Methods

    private boolean _isVerbose() {
        return getVerbose();
    }

    // Public methods

    // compare a tour with a solution
    public void validateNN(City[] tour, List<Integer> solution, boolean verbose) {
        for (int i = 0; i < tour.length; i++) {
            if (tour[i].getIndex() + 1 == solution.get(i) && verbose && _isVerbose())
                System.out.println("ok: " + (tour[i].getIndex() + 1) + " / " + solution.get(i));
            else if (tour[i].getIndex() + 1 != solution.get(i) && _isVerbose()) {
                System.out.println("wrong: " + (tour[i].getIndex() + 1) + " / " + solution.get(i));
            }
        }
    }

    // debug an array of generic type
    public void debugArray(E[] array) {
        for (int i = 0; i < array.length; i++) {
            if (_isVerbose())
                System.out.println("Index: " + i + " Data: " + array[i]);
        }
    }

    // debug an arraylist
    public void debugArraylist(ArrayList<E> arraylist) {
        for (int i = 0; i < arraylist.size(); i++) {
            if (_isVerbose())
                System.out.println("Index: " + i + " Data: " + arraylist.get(i));
        }
    }

    public void computeError(Container container, Tour tour) {

        if(tour.getTuor().length != container.getDimension()) System.out.println("There are less/more cities!");

        for (int i = 0; i < tour.getTuor().length; i++) {
            for (int j = 0; j < tour.getTuor().length; j++) {
                if(tour.getTuor()[i] == tour.getTuor()[j] && i != j){
                    System.out.println("There is a duplicate!");
                    break;
                }
            }
        }

        System.out.println("tour.getCost() = " + tour.getCost());

        if((container.getBest() - tour.getCost()) > 0){
            System.out.println("negativo:" + (container.getBest() - tour.getCost() ));
        } else {
            double err = (double) Math.abs(container.getBest() - tour.getCost()) / container.getBest() * 100;
            System.out.println("err = " + err + "%");
        }
    }

    // debug object
    public void debug(E object) {
        System.out.println("object.toString() = " + object);
    }
    // create a ile container the indexes of the tour
    // ./main -answer="test.txt" -problem="/Users/FraccaMan/Desktop/cup/CUP_TSP/src/main/resources/Problems/ch130.tsp" -output_file="output.png"
    public void writepath(int[] list, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("/Users/FraccaMan/Desktop/cup/ACS_TSP/src/main/resources/Draw/" + filename + ".txt", "UTF-8");
        for (int i = 0; i < list.length; i++) {
            writer.print(list[i] + 1);
            writer.print(" ");
        }
        writer.close();
    }

    public void draw() throws IOException, InterruptedException {
        try {
            String[] args = new String[]{ "/Users/FraccaMan/Desktop/cup/CUP_TSP/src/main/resources/Draw/main", "-answer=", "/Users/FraccaMan/Desktop/cup/CUP_TSP/src/main/resources/Draw/test.txt", "-problem=", "/Users/FraccaMan/Desktop/cup/CUP_TSP/src/main/resources/Problems/ch130.tsp", "-output_file=", "/Users/FraccaMan/Desktop/cup/CUP_TSP/src/main/resources/Draw/output.png" };
            Process proc = new ProcessBuilder(args).start();
        } catch (Exception ex) {
            System.out.println("ex = " + ex);
        }
    }
}

