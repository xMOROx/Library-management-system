package pl.edu.agh.managementlibrarysystem.utils;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;

import java.util.List;
import java.util.function.Consumer;

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

    public static Task<Void> loadDataFromDatabase(ProgressBar progressBar, List<Consumer<Void>> consumers) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                consumers.forEach(
                        consumer -> consumer.accept(null)
                );
                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        consumers.forEach(
                consumer -> consumer.accept(null)
        );
        return task;
    }

    public static void startThread(Task<Void> task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
