package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void notifyObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    public HoppersModel(String filename) throws IOException {

        System.out.println("Loeded: " + filename);
        currentConfig = new HoppersConfig(filename);
        System.out.println(currentConfig.getCols());

        System.out.println(toString());
        System.out.println(currentConfig.toString());



    }

    public String toString() {

        StringBuilder result = new StringBuilder(" ");


        for (int col = 0; col <currentConfig.getCols(); col++) {

            result.append(col);

        }

        for (int col = 0; col < currentConfig.getCols(); col++) {

            result.append("\n" + "--");

        }

        return result.toString();

    }

}
