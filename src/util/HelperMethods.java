package util;

import scanner.FileScanner;
import scanner.FileStatistics;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class HelperMethods {

    public static void scanDirectory(File directory, Scanner scanner) {
        ForkJoinPool pool = new ForkJoinPool();
        FileScanner task = new FileScanner(directory);

        Thread progressThread = createProgressAnimationThread(task);
        Thread quitThread = createQuitAppThread(scanner, task, progressThread);
        try {
            FileStatistics result = pool.invoke(task);
            System.out.println("\nScan complete.");
            System.out.println(result);
        } catch (Exception e) {
            System.out.println("\nScan was interrupted.");
        } finally {
            progressThread.interrupt();
            quitThread.interrupt();
        }
    }

    public static Thread createProgressAnimationThread(FileScanner task) {
        Thread progressThread = new Thread(() -> {
            Stream.iterate(0, i -> !task.isDone(), i -> (i + 1) % 4)
                    .forEach(i -> {
                        System.out.print("\rScanning " + ".".repeat(i + 1));
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
        });

        progressThread.start();
        return progressThread;
    }

    public static Thread createQuitAppThread(Scanner scanner, FileScanner task, Thread progressThread) {
        Runnable inputRunnable = () -> {
            while (!task.isDone()) {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine();
                    if ("q".equalsIgnoreCase(input.trim())) {
                        task.cancel(true);
                        progressThread.interrupt();
                        System.out.println("\nScan cancelled by user.");
                        break;
                    }
                }
            }
        };

        Thread inputThread = new Thread(inputRunnable);
        inputThread.start();
        return inputThread;
    }
}