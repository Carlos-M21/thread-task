package salarySociety;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EmployeeService {

    private static final String[] NAMES = {"Messi", "Cristiano", "Suarez", "Neymar", "Mbappe", "Lewandowski", "Kane", "Hazard", "Salah", "Mane"};
    private final List<Employee> employees;

    public EmployeeService() {
        this.employees = new ArrayList<>();
    }

    public CompletionStage<List<Employee>> hiredEmployees(int numberOfEmployees) {
        return CompletableFuture.supplyAsync(() -> {
            Random random = new Random();
            return IntStream.range(0, numberOfEmployees)
                    .mapToObj(i -> {
                        String id = String.valueOf(i + 1);
                        String name = NAMES[random.nextInt(NAMES.length)];
                        return new Employee(id, name);
                    })
                    .toList();
        });
    }

    public CompletionStage<Double> getSalary(String employeeId) {
        return CompletableFuture.supplyAsync(() -> {
            Random random = new Random(employeeId.hashCode());
            return 30000.0 + (20000.0 * random.nextDouble());
        });
    }

    public CompletionStage<Void> fetchAndPrintHiredEmployeesWithSalaries(int numberOfEmployees) {
        return hiredEmployees(numberOfEmployees).thenCompose(employees -> {
            List<CompletableFuture<Employee>> employeeFutures = employees.stream()
                    .map(employee -> getSalary(employee.getId())
                            .thenApply(salary -> {
                                employee.setSalary(salary);
                                return employee;
                            })
                            .toCompletableFuture())
                    .toList();

            CompletableFuture<Void> allOf = CompletableFuture.allOf(employeeFutures.toArray(new CompletableFuture[0]));

            return allOf.thenApply(v -> employeeFutures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList()));
        }).thenAccept(employeesWithSalaries -> {
            employeesWithSalaries.forEach(employee ->
                    System.out.println("Employee: " + employee.getName() + ", Salary: " + employee.getSalary()));
        });
    }
}