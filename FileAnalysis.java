import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class FileAnalysis {
    private static final String[] SUSPICIOUS_EXTENSIONS = { ".exe", ".jar", ".bat", ".vbs" };

    /**
     * Checks if the file has a suspicious extension.
     * @param file File to check
     * @return true if suspicious, false otherwise
     */
    public static boolean isSuspicious(File file) {
        if (file == null) return false;

        String name = file.getName().toLowerCase();
        for (String ext : SUSPICIOUS_EXTENSIONS) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates SHA-256 hash of a file.
     * @param file File to hash
     * @return Hex string of hash, or null if error
     */
    public static String calculateFileHash(File file) {
        if (file == null || !file.exists()) return null;

        try (FileInputStream fis = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            byte[] hashBytes = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            System.err.println("Error hashing file '" + file.getName() + "': " + e.getMessage());
            return null;
        }
    }
}
