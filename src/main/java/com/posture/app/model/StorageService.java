package com.posture.app.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class StorageService {
    private final String DB_URL = "jdbc:sqlite:data/posture.db";
    private final String JSON_FILE = "data/records.json";
    private final Gson gson = new Gson();

    public StorageService() {
        initializeStorage();
    }

    private void initializeStorage() {
        // データディレクトリの作成
        new File("data").mkdirs();

        // SQLiteデータベースの初期化
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String createTable = """
                CREATE TABLE IF NOT EXISTS posture_records (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    date TEXT NOT NULL,
                    rating INTEGER NOT NULL,
                    comment TEXT
                )
            """;
            conn.createStatement().execute(createTable);
        } catch (SQLException e) {
            System.err.println("データベースの初期化中にエラーが発生しました: " + e.getMessage());
        }

        // JSONファイルの初期化
        File jsonFile = new File(JSON_FILE);
        if (!jsonFile.exists()) {
            try (FileWriter writer = new FileWriter(jsonFile)) {
                writer.write("[]");
            } catch (IOException e) {
                System.err.println("JSONファイルの初期化中にエラーが発生しました: " + e.getMessage());
            }
        }
    }

    public void saveRecord(PostureRecord record) {
        // SQLiteに保存
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO posture_records (date, rating, comment) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, record.getDate().toString());
                pstmt.setInt(2, record.getRating());
                pstmt.setString(3, record.getComment());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("SQLiteへの保存中にエラーが発生しました: " + e.getMessage());
        }

        // JSONに保存
        try {
            List<PostureRecord> records = loadRecordsFromJson();
            records.add(record);
            try (FileWriter writer = new FileWriter(JSON_FILE)) {
                gson.toJson(records, writer);
            }
        } catch (IOException e) {
            System.err.println("JSONへの保存中にエラーが発生しました: " + e.getMessage());
        }
    }

    private List<PostureRecord> loadRecordsFromJson() {
        try (FileReader reader = new FileReader(JSON_FILE)) {
            Type listType = new TypeToken<List<PostureRecord>>(){}.getType();
            List<PostureRecord> records = gson.fromJson(reader, listType);
            return records != null ? records : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("JSONからの読み込み中にエラーが発生しました: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Optional<PostureRecord> load(LocalDate date) {
        String sql = "SELECT rating, comment FROM posture_records WHERE date = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date.toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                PostureRecord record = new PostureRecord();
                record.setDate(date);
                record.setRating(rs.getInt("rating"));
                record.setComment(rs.getString("comment"));
                return Optional.of(record);
            }
        } catch (SQLException e) {
            System.err.println("データの読み込み中にエラーが発生しました: " + e.getMessage());
        }
        return Optional.empty();
    }

    public List<PostureRecord> loadAll() {
        List<PostureRecord> list = new ArrayList<>();
        String sql = "SELECT date, rating, comment FROM posture_records";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PostureRecord record = new PostureRecord();
                record.setDate(LocalDate.parse(rs.getString("date")));
                record.setRating(rs.getInt("rating"));
                record.setComment(rs.getString("comment"));
                list.add(record);
            }
        } catch (SQLException e) {
            System.err.println("データの読み込み中にエラーが発生しました: " + e.getMessage());
        }
        return list;
    }

    public boolean delete(LocalDate date) {
        String sql = "DELETE FROM posture_records WHERE date = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, date.toString());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("データの削除中にエラーが発生しました: " + e.getMessage());
            return false;
        }
    }

    public void update(PostureRecord record) {
        saveRecord(record);
    }
}
