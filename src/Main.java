import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args) {
        // First task
/*
        ParallelMergeSort test = new ParallelMergeSort(1_000_000);
        test.testParallelMergeSort();
 */
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the directory path to scan:");
        String path = scanner.nextLine();
        File directory = new File(path);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Invalid directory path.");
            return;
        }

        ForkJoinPool pool = new ForkJoinPool();
        FileScanner task = new FileScanner(directory);

        Thread progressThread = new Thread(() -> {
            String[] animation = {"|", "/", "-", "\\"};
            int index = 0;
            while (!task.isDone()) {
                System.out.print("\rScanning " + animation[index++ % animation.length]);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        progressThread.start();

        FileStatistics result = pool.invoke(task);
        progressThread.interrupt();

        System.out.println("\nScan complete.");
        System.out.println(result);
    }

}