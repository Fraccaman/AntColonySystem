package Heuristic;

import com.sun.tools.javac.util.Pair;

import LocalSearch.TwoOpt;

/**
 * Created by FraccaMan on 08/11/16.
 */
public interface Heuristic {

    Pair<int[], Integer> run(AntColonySystem antColonySystem, TwoOpt twoOpt, long startTime, long endTime);
}
