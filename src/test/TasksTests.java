package test;

import main.fibonacci.Fibonacci;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TasksTests {
    @Test
    public void testFibonacci() {
        assertEquals(1134903170L, new Fibonacci(45).compute().longValue());
    }
}
