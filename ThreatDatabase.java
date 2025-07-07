import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ThreatDatabase {
    private Connection conn;

    public ThreatDatabase(Connection conn) {
        this.conn = conn;
    }

    // Save or update user's decision about a file hash
    public void saveDecision(String fileHash, boolean allow) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO threats (hash, decision) VALUES (?, ?) " +
                "ON CONFLICT (hash) DO UPDATE SET decision = EXCLUDED.decision"
            );
            stmt.setString(1, fileHash);
            stmt.setBoolean(2, allow);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Boolean getDecision(String hash) {
    try (PreparedStatement stmt = conn.prepareStatement("SELECT decision FROM threats WHERE hash = ?")) {
        stmt.setString(1, hash);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getBoolean("decision");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    // Check if the file hash is marked as a threat (decision = false)
    public boolean isThreat(String fileHash) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT decision FROM threats WHERE hash = ?"
            );
            stmt.setString(1, fileHash);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean decision = rs.getBoolean("decision");
                rs.close();
                stmt.close();
                return !decision;  // If decision is false, it's a threat
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Not found or error => treat as not threat
    }
}


