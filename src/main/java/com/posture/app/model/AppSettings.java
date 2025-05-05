package com.posture.app.model;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * ユーザー設定（テーマ、保存先など）を保持・管理するクラス
 */
public class AppSettings {
    private static final String SETTINGS_FILE = "data/appsettings.json";
    private static final String PREF_KEY_THEME = "theme";
    private static final String PREF_KEY_STORAGE = "storage";

    private Preferences prefs;
    private String theme = "light"; // デフォルトはライトテーマ
    private String storageType = "json"; // デフォルトはJSON
    private boolean autoSave;

    public AppSettings() {
        this.prefs = Preferences.userNodeForPackage(AppSettings.class);
        loadSettings();
    }

    private void loadSettings() {
        theme = prefs.get(PREF_KEY_THEME, "light");
        storageType = prefs.get(PREF_KEY_STORAGE, "json");
    }

    public void saveSettings() {
        prefs.put(PREF_KEY_THEME, theme);
        prefs.put(PREF_KEY_STORAGE, storageType);
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
        saveSettings();
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
        saveSettings();
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    /**
     * 設定をファイルから読み込む
     */
    public static AppSettings load() {
        try (FileReader reader = new FileReader(SETTINGS_FILE)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, AppSettings.class);
        } catch (IOException e) {
            System.out.println("設定ファイルが見つからないか読み込みに失敗しました。デフォルト設定を使用します。");
            return new AppSettings();
        }
    }

    /**
     * 設定をファイルに保存する
     */
    public void save() {
        try (FileWriter writer = new FileWriter(SETTINGS_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this, writer);
        } catch (IOException e) {
            System.err.println("設定の保存に失敗しました: " + e.getMessage());
        }
    }
}
