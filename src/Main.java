import main.Applyer;
import main.fibonacci.Fibonacci;
import main.forkBlur.ForkBlur;
import main.producerConsumer.BlockingQueueSolution;
import main.producerConsumer.Semaphore;
import main.salarySociety.EmployeeService;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

import static main.util.HelperMethods.createArray;
import static main.util.HelperMethods.scanDirectory;

public class Main {
    public static void main(String[] args) {
        // Second task
        sortTask();
        // Third task
        //scanTask();
        // Fourth task
        fetchAndPrintHiredEmployeesWithSalariesTask();
        // Fifth task Semaphore Solution
        producerConsumerSemaphoreTask();
        // Fifth task BlockingQueue Solution
        producerConsumerBlockingQueueTask();
        // Sixth task BlockingQueue Solution
        fibonacciTask();
        // Sixth task DoubleSquares Solution
        doubleSquaresParallelTask();
        // Sixth task DoubleSquares Solution
        doubleSquaresRegularTask();
        // Seventh task ForkBlur Solution
        forkBlurTask();
    }

    private static void sortTask(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of random numbers you want to sort:");
        int totalNumbersToSort = scanner.nextInt();
        main.sorting.ParallelMergeSort test = new main.sorting.ParallelMergeSort(totalNumbersToSort);
        test.testParallelMergeSort();
    }

    private static void scanTask(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the directory path to scan:");
        String path = scanner.nextLine();

        Optional.of(new File(path))
                .filter(File::exists)
                .filter(File::isDirectory)
                .ifPresentOrElse(directory -> scanDirectory(directory, scanner), () -> System.out.println("Invalid directory path."));
    }

    private static void fetchAndPrintHiredEmployeesWithSalariesTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of employees to create:");
        int numberOfEmployees = scanner.nextInt();

        EmployeeService employeeService = new EmployeeService();
        employeeService.fetchAndPrintHiredEmployeesWithSalaries(numberOfEmployees).toCompletableFuture().join();
    }

    private static void producerConsumerSemaphoreTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the duration (in seconds) for the Semaphore-based producer-consumer task:");
        long durationSeconds = scanner.nextLong();
        System.out.println("Starting Producer-Consumer using Semaphore for " + durationSeconds + " seconds...");
        Semaphore pc = new Semaphore(durationSeconds);
        Thread producer = new Thread(pc.createProducer());
        Thread consumer = new Thread(pc.createConsumer());

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void producerConsumerBlockingQueueTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the duration (in seconds) for the BlockingQueue-based producer-consumer task:");
        long durationSeconds = scanner.nextLong();
        System.out.println("Starting Producer-Consumer using BlockingQueue for " + durationSeconds + " seconds...");
        BlockingQueueSolution pc = new BlockingQueueSolution(durationSeconds);
        Thread producer = new Thread(pc.createProducer());
        Thread consumer = new Thread(pc.createConsumer());

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void fibonacciTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number Fibonacci position you want to compute:");
        int fibonacciNumber = scanner.nextInt();

        Fibonacci fibonacci = new Fibonacci(fibonacciNumber);
        System.out.println("Fibonacci in position " + fibonacciNumber + " is: " + fibonacci.compute());
    }

    private static void doubleSquaresParallelTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the length of the array (Parallel flow): ");
        int length = scanner.nextInt();

        double[] array = createArray(length);
        System.out.println("Array: " + Arrays.toString(array));
        Applyer task = new Applyer(array, 0, array.length, null);

        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(task);

        System.out.println("Sum of squares: " + task.getResult());
    }

    private static void doubleSquaresRegularTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the length of the array (Regular flow): ");
        int length = scanner.nextInt();

        double[] array = createArray(length);
        System.out.println("Array: " + Arrays.toString(array));
        double sum = 0;
        for (double v : array) {
            sum += v * v;
        }

        System.out.println("Sum of squares: " + sum);
    }

    private static void forkBlurTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the length of the array (ForkBlur): ");
        int length = scanner.nextInt();
        int[] source = new int[length];
        int[] destination = new int[length];
        Random random = new Random();

        // Populate the source array with random pixel values
        for (int i = 0; i < length; i++) {
            source[i] = random.nextInt(0xFFFFFF); // Random RGB value
        }

        // Create a ForkBlur task
        ForkBlur task = new ForkBlur(source, 0, source.length, destination);
        System.out.println("Source: " + Arrays.toString(source));
        System.out.println("Destination: " + Arrays.toString(source));
        // Execute the task using ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(task);

        // Output the result (for demonstration, we'll print the first 10 blurred values)
        for (int i = 0; i < length; i++) {
            System.out.printf("0x%08X\n", destination[i]);
        }
    }
}