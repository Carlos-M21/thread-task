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

class FileStatistics {
    private final int fileCount;
    private final int folderCount;
    private final long totalSize;

    public FileStatistics() {
        this(0, 0, 0);
    }

    public FileStatistics(int fileCount, int folderCount, long totalSize) {
        this.fileCount = fileCount;
        this.folderCount = folderCount;
        this.totalSize = totalSize;
    }

    public FileStatistics add(FileStatistics other) {
        return new FileStatistics(
                this.fileCount + other.fileCount,
                this.folderCount + other.folderCount,
                this.totalSize + other.totalSize
        );
    }

    public FileStatistics addFile(long size) {
        return new FileStatistics(this.fileCount + 1, this.folderCount, this.totalSize + size);
    }

    public FileStatistics addFolder() {
        return new FileStatistics(this.fileCount, this.folderCount + 1, this.totalSize);
    }

    @Override
    public String toString() {
        return "Files: " + fileCount + ", Folders: " + folderCount + ", Total Size: " + totalSize + " bytes";
    }
}