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
}