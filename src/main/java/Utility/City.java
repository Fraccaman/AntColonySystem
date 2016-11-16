package Utility;

/**
 * Created by FraccaMan on 02/11/16.
 */
public class City {

    private int index;
    private double x;
    private double y;
    private Boolean visited = false;

    public City(int index, double x, double y) {
        this.index = index;
        this.x = x;
        this.y = y;
    }

    // Getter and Setter

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public void setVisited() {
        visited = true;
    }

    // Helpers

    @Override
    public String toString() {
        return "City{" +
                "index=" + index +
                '}';
    }

}
