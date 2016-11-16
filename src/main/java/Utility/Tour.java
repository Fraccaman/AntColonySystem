package Utility;

import java.util.Arrays;

/**
 * Created by FraccaMan on 02/11/16.
 */
public class Tour {

    private City[] tuor;
    private int cost;

    public Tour(City[] tuor) {
        this.tuor = tuor;
    }

    // Getter and Setter

    public City[] getTuor() {
        return tuor;
    }

    public void setTuor(City[] tuor) {
        this.tuor = tuor;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public City getNode(int idx) {
        return getTuor()[idx];
    }

    public void setNode(int idx, City node) {
        getTuor()[idx] = node;
    }

    // Private Methods

    public int getTourCost(City[] cities, Container container) {
        int cost = 0;

        for (int i = 0; i < cities.length - 1; i++) {
            cost += container.getMatrix()[getTuor()[i].getIndex()][getTuor()[i + 1].getIndex()];
        }
        cost += container.getMatrix()[getTuor()[getTuor().length - 1].getIndex()][getTuor()[0].getIndex()];
        return cost;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "tuor=" + Arrays.toString(tuor) +
                ", cost=" + cost +
                '}';
    }
// Public Methods
}
