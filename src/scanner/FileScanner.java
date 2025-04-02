package scanner;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Stream;


public class FileScanner extends RecursiveTask<FileStatistics> {
    private final File directory;

    public FileScanner(File directory) {
        this.directory = directory;
    }

    @Override
    protected FileStatistics compute() {
        if (isCancelled() || Thread.currentThread().isInterrupted()) {
            return new FileStatistics();
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return new FileStatistics();
        }

        Stream<File> fileStream = Arrays.stream(files);
        FileStatistics stats = fileStream.map(file -> {
            if (file.isDirectory()) {
                return forkTask(file);
            } else {
                return createFileStatistics(file);
            }
        }).reduce(new FileStatistics(), FileStatistics::add);
        stats = stats.addFolder();
        return stats;
    }

    private FileStatistics forkTask(File file) {
        FileScanner task = new FileScanner(file);
        task.fork();
        return task.join();
    }

    private FileStatistics createFileStatistics(File file) {
        return new FileStatistics(1, 0, file.length());
    }
}

