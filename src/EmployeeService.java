import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class EmployeeService {

    public CompletionStage<List<Employee>> hiredEmployees() {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate fetching employees from a REST endpoint
            return Arrays.asList(
                    new Employee("1", "Messi"),
                    new Employee("2", "Cristiano"),
                    new Employee("3", "Suarez")
            );
        });
    }

    public CompletionStage<Double> getSalary(String hiredEmployeeId) {
        return CompletableFuture.supplyAsync(() -> {
            return switch (hiredEmployeeId) {
                case "1" -> 50000.0;
                case "2" -> 60000.0;
                case "3" -> 55000.0;
                default -> 0.0;
            };
        });
    }

    public void fetchAndPrintHiredEmployeesWithSalaries() {
        hiredEmployees().thenCompose(employees -> {
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

class Employee {
    private String id;
    private String name;
    private double salary;

    public Employee(String id, String name) {
        Random random = new Random();
        this.id = id;
        this.name = name;
        this.salary = random.nextInt(9999999);;
    }

    public Employee() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}