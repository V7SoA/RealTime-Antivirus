import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import javafx.application.Platform;

public class FileMonitor extends Thread {
    private final Path watchPath;
    private final File quarantineDir;
    private final AntivirusDashboardUI ui;

    public FileMonitor(AntivirusDashboardUI ui) {
        this.ui = ui;
        this.watchPath = Paths.get(System.getProperty("user.home"), "Downloads");
        this.quarantineDir = new File("quarantine");

        if (!quarantineDir.exists()) {
            quarantineDir.mkdir();
            System.out.println(" Created quarantine directory at " + quarantineDir.getAbsolutePath());
        }

        setDaemon(true);
        setName("FileMonitorThread");
    }

    @Override
    public void run() {
        System.out.println(" FileMonitor thread is running...");

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            watchPath.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY); // Watch for new + modified files

            FileScannerLogger logger = new FileScannerLogger("scan_log.txt");
            ThreatDatabase db = new ThreatDatabase(DatabaseConnection.getConnection());
            AntivirusApp app = new AntivirusApp(logger, db);

            while (!Thread.currentThread().isInterrupted()) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    Path createdFilePath = watchPath.resolve((Path) event.context());
                    File file = createdFilePath.toFile();

                    if (file.exists()) {
                        System.out.println(" Detected file: " + createdFilePath);

                        // Delay to avoid scanning incomplete downloads
                        Thread.sleep(1000);

                        Platform.runLater(() -> {
                            app.scanFile(file, quarantineDir);
                            ui.loadLogs();  // Refresh logs
                        });
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    System.out.println(" WatchKey no longer valid, stopping FileMonitor");
                    break;
                }
            }

        } catch (InterruptedException e) {
            System.out.println(" FileMonitor interrupted, shutting down...");
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
