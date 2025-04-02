import java.io.File;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        // First task
/*
        ParallelMergeSort test = new ParallelMergeSort(1_000_000);
        test.testParallelMergeSort();
 */
        // Second task
 /*
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the directory path to scan:");
        String path = scanner.nextLine();

        Optional.of(new File(path))
                .filter(File::exists)
                .filter(File::isDirectory)
                .ifPresentOrElse(directory -> scanDirectory(directory, scanner), () -> System.out.println("Invalid directory path."));

 */
    }

    private static void scanDirectory(File directory, Scanner scanner) {
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

    private static Thread createProgressAnimationThread(FileScanner task) {
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

    private static Thread createQuitAppThread(Scanner scanner, FileScanner task, Thread progressThread) {
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