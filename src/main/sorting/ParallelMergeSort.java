package main.sorting;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelMergeSort extends RecursiveTask<int[]> {
    private final int[] array;
    private final int start, end, threshold;

    public ParallelMergeSort(int[] array, int start, int end, int threshold) {
        this.array = array;
        this.start = start;
        this.end = end;
        this.threshold = threshold;
    }

    public ParallelMergeSort(int size) {
        int[] array = generateArray(size);
        this.array = array;
        this.end = array.length;
        this.start = 0;
        this.threshold = calculateThreshold(size); // avoid heap errors or bad main.sorting
    }

    @Override
    protected int[] compute() {
        if (end - start <= threshold) {
            return new int[]{array[start]};
        }

        int mid = (start + end) / 2;
        ParallelMergeSort leftTask = new ParallelMergeSort(array, start, mid, threshold);
        ParallelMergeSort rightTask = new ParallelMergeSort(array, mid, end, threshold);

        leftTask.fork();
        int[] rightResult = rightTask.compute();
        int[] leftResult = leftTask.join();

        return merge(leftResult, rightResult);
    }

    private int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }

        while (i < left.length) {
            result[k++] = left[i++];
        }

        while (j < right.length) {
            result[k++] = right[j++];
        }

        return result;
    }

    public void testParallelMergeSort(){
        ForkJoinPool pool = new ForkJoinPool();
        int[] sortedArray = pool.invoke(this);
        System.out.println(Arrays.toString(Arrays.copyOf(sortedArray, 100)));
       // System.out.println(Arrays.toString(sortedArray));
    }

    private  int[] generateArray(int size) {
        Random random = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100000);
        }
        return array;
    }

    private int calculateThreshold(int size) {
        if (size > 1000000) return 1000;
        else if (size > 10000) return 100;
        else if (size > 1000) return 10;
        else return 1;
    }
}
