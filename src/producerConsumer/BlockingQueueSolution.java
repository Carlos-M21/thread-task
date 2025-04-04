package producerConsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class BlockingQueueSolution {
    private static final int BUFFER_SIZE = 5;
    private final BlockingQueue<Integer> buffer = new ArrayBlockingQueue<>(BUFFER_SIZE);
    private final long durationMillis;

    public BlockingQueueSolution(long durationSeconds) {
        this.durationMillis = durationSeconds * 1000;
    }

    public Producer createProducer() {
        return new Producer();
    }

    public Consumer createConsumer() {
        return new Consumer();
    }

    class Producer implements Runnable {
        @Override
        public void run() {
            try {
                int value = 0;
                long startTime = System.currentTimeMillis();
                while (System.currentTimeMillis() - startTime < durationMillis) {
                    buffer.put(value);
                    System.out.println("Produced: " + value);
                    value++;
                    Thread.sleep(1000);
                }
                System.out.println("Producer finished execution.");
            } catch (InterruptedException e) {
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
                    int value = buffer.take();
                    System.out.println("Consumed: " + value);
                    Thread.sleep(1500);
                }
                System.out.println("Consumer finished execution.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}