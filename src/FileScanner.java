import java.io.File;
import java.util.concurrent.RecursiveTask;

public class FileScanner extends RecursiveTask<FileStatistics> {
    private final File directory;

    public FileScanner(File directory) {
        this.directory = directory;
    }

    @Override
    protected FileStatistics compute() {
        FileStatistics stats = new FileStatistics();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    FileScanner task = new FileScanner(file);
                    task.fork();
                    stats.add(task.join());
                } else {
                    stats.addFile(file.length());
                }
            }
        }
        return stats;
    }
}

class FileStatistics {
    private int fileCount;
    private int folderCount;
    private long totalSize;

    public synchronized void add(FileStatistics other) {
        this.fileCount += other.fileCount;
        this.folderCount += other.folderCount;
        this.totalSize += other.totalSize;
    }

    public synchronized void addFile(long size) {
        this.fileCount++;
        this.totalSize += size;
    }

    public synchronized void addFolder() {
        this.folderCount++;
    }

    @Override
    public String toString() {
        return "Files: " + fileCount + ", Folders: " + folderCount + ", Total Size: " + totalSize + " bytes";
    }
}
