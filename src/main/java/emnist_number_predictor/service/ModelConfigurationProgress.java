package emnist_number_predictor.service;
import static emnist_number_predictor.util.Const.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModelConfigurationProgress {

    private static final String INIT_MODEL_CONFIGURATION_PROGRESS_TEXT = "";
    private static final double INIT_MODEL_CONFIGURATION_PROGRESS_PERCENT = 0.0;

    private static final double CONFIGURATION_WEIGHT_PERCENTAGE = 0.2;
    private static final double TRAINING_WEIGHT_PERCENTAGE = 0.7;
    private static final double EVALUATION_WEIGHT_PERCENTAGE = 0.1;

    private static final double CONFIGURATION_TOTAL = 4;
    private static final double EVALUATION_TOTAL = 3;

    public static enum PROGRESS {
        CONFIGURATION(CONFIGURATION_WEIGHT_PERCENTAGE, CONFIGURATION_TOTAL), 
        TRAINING(TRAINING_WEIGHT_PERCENTAGE, (double) EPOCH_NUM), 
        EVALUATION(EVALUATION_WEIGHT_PERCENTAGE, EVALUATION_TOTAL);
        
        private final double weight;
        private final double total;
        private double count;
        private double progress;

        private PROGRESS(double weight, double total) {
            this.weight = weight;
            this.total = total;
            this.count = 0;
        }

        public double getProgress() {
            this.count += 1.0;
            this.progress = weight * (count / total);
            double totalProgress = PROGRESS.CONFIGURATION.weight + PROGRESS.TRAINING.weight + PROGRESS.EVALUATION.weight;
            double currentProgress = PROGRESS.CONFIGURATION.progress + PROGRESS.TRAINING.progress + PROGRESS.EVALUATION.progress;
            return currentProgress / totalProgress;
        }

    };

    public static void initialize() {
        LoadingService.setConfigurationText(INIT_MODEL_CONFIGURATION_PROGRESS_TEXT);
        LoadingService.setConfigurationProgress(INIT_MODEL_CONFIGURATION_PROGRESS_PERCENT);
    }

    public static void setConfigurationText(String configurationText) {
        log.info(configurationText);
        LoadingService.setConfigurationText(configurationText);
    }

    public static void setConfigurationProgress(PROGRESS enumValue) {
        double percentComplete = enumValue.getProgress();
        LoadingService.setConfigurationProgress(percentComplete);
    }

}
