import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;

public class AntivirusApp {
    private final FileScannerLogger logger;
    private final ThreatDatabase db;

    public AntivirusApp(FileScannerLogger logger, ThreatDatabase db) {
        this.logger = logger;
        this.db = db;
    }

    public void scanFile(File file, File quarantineDir) {
        String hash = FileAnalysis.calculateFileHash(file);
        if (hash == null) {
            logger.log(" Failed to hash file: " + file.getName());
            return;
        }

        // 🚨 Check if decision is already stored in DB
        Boolean knownDecision = db.getDecision(hash);
        if (knownDecision != null) {
            if (knownDecision) {
                logger.log(" Auto-allowed file: " + file.getName());
            } else {
                boolean deleted = file.delete();
                if (deleted) {
                    logger.log(" Auto-blocked and deleted: " + file.getName());
                } else {
                    logger.log(" Auto-blocked but failed to delete: " + file.getName());
                }
            }
            return;
        }

        // 🧠 No previous decision — do basic analysis
        boolean suspicious = FileAnalysis.isSuspicious(file);



        if (suspicious) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle(" Threat Detected!");
                alert.setHeaderText("This file may be dangerous: " + file.getName());
                alert.setContentText("Do you want to ALLOW or BLOCK this file?");

                ButtonType allow = new ButtonType("Allow");
                ButtonType block = new ButtonType("Block");
                alert.getButtonTypes().setAll(allow, block);

                alert.showAndWait().ifPresent(type -> {
                    if (type == block) {
                        boolean deleted = file.delete();
                        if (deleted) {
                            logger.log(" Blocked and deleted: " + file.getName());
                        } else {
                            logger.log(" Blocked but failed to delete: " + file.getName());
                        }
                        db.saveDecision(hash, false);
                    } else {
                        logger.log(" Allowed suspicious file: " + file.getName());
                        db.saveDecision(hash, true);
                    }
                });
            });
        } else {
            logger.log(" Scanned safe file: " + file.getName());
            db.saveDecision(hash, true);
        }
    }
}
