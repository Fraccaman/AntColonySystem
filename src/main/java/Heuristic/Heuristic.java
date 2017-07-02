package Heuristic;

import LocalSearch.TwoOpt;
import Utility.Pair;

/**
 * Created by FraccaMan on 08/11/16.
 */
public interface Heuristic {

    Pair<int[], Integer> ElementaryMyDearWatson(AntColonySystem antColonySystem, TwoOpt twoOpt, long startTime, long endTime);
}
