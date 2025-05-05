package com.posture.app.model;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.posture.app.util.StorageService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * 姿勢記録の保存・管理クラス
 * JSON と SQLite の切り替え保存機能を提供
 */
public class RecordManager {
    private static RecordManager instance;
    private AppSettings settings;  // 設定を保持するクラス
    private Gson gson;
    private List<PostureRecord> postureRecords;
    private StorageService storageService;
    private final ObservableList<PostureRecord> records;

    private RecordManager() {
        this.settings = new AppSettings();
        this.postureRecords = new ArrayList<>();
        this.storageService = new StorageService();
        this.records = FXCollections.observableArrayList();
        
        // LocalDate用のTypeAdapterを設定
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
            }
        });
        gsonBuilder.registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
            }
        });
        gson = gsonBuilder.create();
        
        loadRecords();
    }

    public static RecordManager getInstance() {
        if (instance == null) {
            instance = new RecordManager();
        }
        return instance;
    }

    /**
     * 記録を読み込む（保存方法に応じて切り替え）
     */
    public void loadRecords() {
        if ("sqlite".equals(settings.getStorageType())) {
            loadFromSQLite();
        } else if ("json".equals(settings.getStorageType())) {
            loadFromJSON();
        }
    }

    /**
     * 記録を保存する（保存方法に応じて切り替え）
     */
    public void saveRecords() {
        if ("sqlite".equals(settings.getStorageType())) {
            saveToSQLite();
        } else if ("json".equals(settings.getStorageType())) {
            saveToJSON();
        }
    }

    /**
     * SQLiteから記録を読み込む
     */
    private void loadFromSQLite() {
        String url = "jdbc:sqlite:data/posture.db";
        String query = "SELECT * FROM posture_records";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            postureRecords.clear();
            while (rs.next()) {
                LocalDate date = LocalDate.parse(rs.getString("date"));
                int rating = rs.getInt("rating");
                String comment = rs.getString("comment");
                postureRecords.add(new PostureRecord(date, rating, comment));
            }
        } catch (SQLException e) {
            System.err.println("SQLiteからデータを読み込む際にエラーが発生しました: " + e.getMessage());
        }
    }

    /**
     * JSONファイルから記録を読み込む
     */
    private void loadFromJSON() {
        File file = new File("data/records.json");
        if (!file.exists()) {
            return; // ファイルが存在しない場合、何もしない
        }

        try (FileReader reader = new FileReader(file)) {
            Type listType = new TypeToken<List<PostureRecord>>(){}.getType();
            postureRecords = gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.err.println("JSONからデータを読み込む際にエラーが発生しました: " + e.getMessage());
        }
    }

    /**
     * SQLiteに記録を保存する
     */
    private void saveToSQLite() {
        String url = "jdbc:sqlite:data/posture.db";
        String insertQuery = "INSERT INTO posture_records (date, rating, comment) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            for (PostureRecord record : postureRecords) {
                pstmt.setString(1, record.getDate().toString());
                pstmt.setInt(2, record.getRating());
                pstmt.setString(3, record.getComment());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("SQLiteにデータを保存する際にエラーが発生しました: " + e.getMessage());
        }
    }

    /**
     * JSONファイルに記録を保存する
     */
    private void saveToJSON() {
        File file = new File("data/records.json");
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(postureRecords, writer);
        } catch (IOException e) {
            System.err.println("JSONにデータを保存する際にエラーが発生しました: " + e.getMessage());
        }
    }

    /**
     * 記録を追加
     */
    public void addRecord(PostureRecord record) {
        postureRecords.add(record);
        saveRecords();
    }

    /**
     * 記録の取得
     */
    public List<PostureRecord> getPostureRecords() {
        return postureRecords;
    }

    public void saveRecord(PostureRecord record) {
        postureRecords.add(record);
        storageService.saveRecord(record);
    }

    public List<PostureRecord> getAllRecords() {
        return new ArrayList<>(postureRecords);
    }

    public List<PostureRecord> getRecordsByDate(LocalDate date) {
        return postureRecords.stream()
                .filter(record -> record.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public void setStorageMode(String mode) {
        if ("sqlite".equals(mode) || "json".equals(mode)) {
            settings.setStorageType(mode);
            loadRecords(); // 新しいストレージモードでデータを再読み込み
        }
    }

    public String getStorageMode() {
        return settings.getStorageType();
    }

    public ObservableList<PostureRecord> getRecords() {
        return records;
    }

    public void updateRecord(PostureRecord oldRecord, PostureRecord newRecord) {
        int index = postureRecords.indexOf(oldRecord);
        if (index != -1) {
            postureRecords.set(index, newRecord);
            saveRecords();
        }
    }

    public void deleteRecord(PostureRecord record) {
        postureRecords.remove(record);
        saveRecords();
    }
}
