package deeplearning_test;

import org.deeplearning4j.nn.api.Model;
import org.deeplearning4j.optimize.api.BaseTrainingListener;

import java.io.Serializable;

public class CustomScoreIterationListener extends BaseTrainingListener implements Serializable {
    private int printIterations = 10;

    /**
     * @param printIterations    frequency with which to print scores (i.e., every printIterations parameter updates)
     */
    public CustomScoreIterationListener(int printIterations) {
        this.printIterations = printIterations;
    }

    /** Default constructor printing every 10 iterations */
    public CustomScoreIterationListener() {}

    @Override
    public void iterationDone(Model model, int iteration, int epoch) {
        if (printIterations <= 0)
            printIterations = 1;
        if (iteration % printIterations == 0) {
            double score = model.score();
            System.out.println("Score at iteration " + iteration + " is " + (Math.round(score * 100.0) / 100.0));
        }
    }

    @Override
    public String toString(){
        return "ScoreIterationListener(" + printIterations + ")";
    }
}