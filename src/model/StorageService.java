package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StorageService {
    private final String dbUrl;

    public StorageService(String dbFilePath) {
        this.dbUrl = "jdbc:sqlite:" + dbFilePath;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS posture_records (
                    date TEXT PRIMARY KEY,
                    rating INTEGER NOT NULL,
                    comment TEXT
                );
            """;
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean save(PostureRecord record) {
        String sql = "INSERT OR REPLACE INTO posture_records (date, rating, comment) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, record.getDate().toString());
            pstmt.setInt(2, record.getRating());
            pstmt.setString(3, record.getComment());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Optional<PostureRecord> load(LocalDate date) {
        String sql = "SELECT rating, comment FROM posture_records WHERE date = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int rating = rs.getInt("rating");
                String comment = rs.getString("comment");
                return Optional.of(new PostureRecord(date, rating, comment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<PostureRecord> loadAll() {
        List<PostureRecord> list = new ArrayList<>();
        String sql = "SELECT date, rating, comment FROM posture_records";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("date"));
                int rating = rs.getInt("rating");
                String comment = rs.getString("comment");
                list.add(new PostureRecord(date, rating, comment));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean delete(LocalDate date) {
        String sql = "DELETE FROM posture_records WHERE date = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date.toString());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(PostureRecord record) {
        return save(record); // SQLiteでは INSERT OR REPLACE で代用可
    }
}
