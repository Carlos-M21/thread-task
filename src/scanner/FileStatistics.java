package scanner;

public class FileStatistics {
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
