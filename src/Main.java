import producerConsumer.BlockingQueueSolution;
import producerConsumer.Semaphore;
import salarySociety.EmployeeService;

import java.io.File;
import java.util.Optional;
import java.util.Scanner;

import static util.HelperMethods.scanDirectory;

public class Main {
    public static void main(String[] args) {
        // First task
        sortTask();
        // Second task
        //scanTask();
        // Third task
        fetchAndPrintHiredEmployeesWithSalariesTask();
        // Fourth task Semaphore Solution
        producerConsumerSemaphoreTask();
        // Fourth task BlockingQueue Solution
        producerConsumerBlockingQueueTask();
    }

    private static void sortTask(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of random numbers you want to sort:");
        int totalNumbersToSort = scanner.nextInt();
        sorting.ParallelMergeSort test = new sorting.ParallelMergeSort(totalNumbersToSort);
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
}