package main.producerConsumer;

import java.util.LinkedList;
import java.util.Queue;

public class Semaphore {
    private static final int BUFFER_SIZE = 5;
    private final Queue<Integer> buffer = new LinkedList<>();
    private final java.util.concurrent.Semaphore emptySlots = new java.util.concurrent.Semaphore(BUFFER_SIZE);
    private final java.util.concurrent.Semaphore filledSlots = new java.util.concurrent.Semaphore(0);
    private final java.util.concurrent.Semaphore mutex = new java.util.concurrent.Semaphore(1);
    private final long durationMillis;

    public Semaphore(long durationSeconds) {
        this.durationMillis = durationSeconds * 1000;
    }

    public Producer createProducer(){
        return new Producer();
    }

    public Consumer createConsumer(){
        return new Consumer();
    }

    class Producer implements Runnable {
        @Override
        public void run() {
            try {
                int value = 0;
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < durationMillis) {
                    emptySlots.acquire();
                    mutex.acquire();
                    buffer.add(value);
                    System.out.println("Produced: " + value);
                    value++;
                    mutex.release();
                    filledSlots.release();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Producer finished execution.");
                Thread.currentThread().interrupt();
            }
        }
    }

    class Consumer implements Runnable {
        @Override
        public void run() {
            try {
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < durationMillis) {
                    filledSlots.acquire();
                    mutex.acquire();
                    int value = buffer.poll();
                    System.out.println("Consumed: " + value);
                    mutex.release();
                    emptySlots.release();
                    Thread.sleep(1500);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                System.out.println("Consumer finished execution.");
                Thread.currentThread().interrupt();
            }
        }
    }
}
