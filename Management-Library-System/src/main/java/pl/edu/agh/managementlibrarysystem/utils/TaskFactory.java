package pl.edu.agh.managementlibrarysystem.utils;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

public abstract class TaskFactory {
    public static Task<Integer> countingTask(int maxIterations, int sleepTime) {
        return new Task<>() {
            @Override
            protected Integer call() throws Exception {
                for (int i = 0; i <= maxIterations; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    updateProgress(i, maxIterations);
                    Thread.sleep(sleepTime);
                }
                return maxIterations;
            }
        };
    }

    public static Task<Integer> countingTaskForProgressBar(int maxIterations, int sleepTime, ProgressBar progressBar) {
        Task<Integer> task = countingTask(maxIterations, sleepTime);
        progressBar.progressProperty().bind(task.progressProperty());
        return task;
    }

    public static Task<Integer> countingTask(int maxIterations) {
        return countingTask(maxIterations, 100);
    }
}
