package com.virtual_hex.examples;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

// Completely Java 8 working example
public class Java8RunnableFuture extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        final String[] effectivelyFinal = new String[1];

        Runnable r = () -> effectivelyFinal[0] = "Some task, return, anything";

        RunnableFuture<String[]> task = new FutureTask<>(r, effectivelyFinal);

        // JavaFX 8 run later, can be any type of run later
        Platform.runLater(task);

        // Since we do not wanna hold up the platform startup
        // lets wrap this in a thread, just for this example to work
        Thread t = new Thread(() -> {
            try {
                String[] strings = task.get();// this will block until Runnable completes
                String string = strings[0];
                System.out.println(string);
                System.exit(0);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        // Want to make sure out application does not exist
        // when the platform setup thread exits back to the main thread
        t.setDaemon(false);
        t.start();
    }
}
