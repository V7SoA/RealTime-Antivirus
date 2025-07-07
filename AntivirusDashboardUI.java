import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AntivirusDashboardUI extends Application {
    private TextArea logArea;
    private Label scannedFilesLabel;

    @Override
    public void start(Stage stage) {
        logArea = new TextArea();
        logArea.setEditable(false);

        scannedFilesLabel = new Label("Files Scanned: 0");

        Button refreshButton = new Button("Refresh Logs");
        refreshButton.setOnAction(e -> loadLogs());

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(scannedFilesLabel, logArea, refreshButton);

        root.setPrefSize(700, 500);

        stage.setScene(new Scene(root));
        stage.setTitle("Real-Time Antivirus");
        stage.show();

        createLogFileIfMissing();
        loadLogs();

        FileMonitor monitor = new FileMonitor(this);
        monitor.start();
        System.out.println("📡 FileMonitor started from GUI");
    }

    private void createLogFileIfMissing() {
        File logFile = new File("scan_log.txt");
        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
        } catch (IOException e) {
            logArea.setText("Unable to create log file.");
        }
    }

    public void loadLogs() {
        try {
            String content = Files.readString(Paths.get("scan_log.txt"));
            logArea.setText(content);

            // Count how many times "Scanned safe file" or "Allowed" or "Blocked" appears as an indicator
            long scannedCount = content.lines()
                .filter(line -> line.contains("Scanned safe file") || line.contains("Allowed") || line.contains("Blocked"))
                .count();

            scannedFilesLabel.setText("Files Scanned: " + scannedCount);
        } catch (IOException e) {
            logArea.setText("No logs available or unable to read logs.");
        }
    }
}
