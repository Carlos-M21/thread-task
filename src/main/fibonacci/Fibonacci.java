package main.fibonacci;

import java.util.concurrent.RecursiveTask;

public class Fibonacci extends RecursiveTask<Integer> {
    final int n;
    public Fibonacci(int n) { this.n = n; }

    public Integer compute() {
        if (n <= 10) {
            return linearFibonacci(n);
        }
        Fibonacci f1 = new Fibonacci(n - 1);
        f1.fork();
        Fibonacci f2 = new Fibonacci(n - 2);
        return f2.compute() + f1.join();
    }

    private int linearFibonacci(int n) {
        if (n <= 1) return n;
        int previous = 0, current = 1, next;
        for (int i = 2; i <= n; i++) {
            next = previous + current;
            previous = current;
            current = next;
        }
        return current;
    }
}