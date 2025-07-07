import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileScannerLogger {
    private final String logPath;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileScannerLogger(String logPath) {
        this.logPath = logPath;
    }

    /**
     * Logs a message with timestamp in a thread-safe manner.
     * @param message The log message
     */
    public synchronized void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        String fullMessage = "[" + timestamp + "] " + message;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logPath, true))) {
            writer.write(fullMessage);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Failed to write log message: " + e.getMessage());
        }
    }
}
