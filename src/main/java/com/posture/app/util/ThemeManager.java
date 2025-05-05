package com.posture.app.util;

import java.util.prefs.Preferences;
import javafx.scene.Scene;

public class ThemeManager {

    private static final String PREF_KEY = "theme";
    private static final String LIGHT_THEME = "/css/light-theme.css";
    private static final String DARK_THEME = "/css/dark-theme.css";

    private static String currentTheme = "light"; // デフォルト

    private static Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);

    /**
     * シーンにテーマを適用する（起動時や切り替え時に使用）
     */
    public static void applyTheme(Scene scene) {
        if (scene == null) return;

        scene.getStylesheets().clear();
        String cssFile = currentTheme.equals("dark") ? DARK_THEME : LIGHT_THEME;
        scene.getStylesheets().add(ThemeManager.class.getResource(cssFile).toExternalForm());
    }

    /**
     * テーマを設定・保存
     */
    public static void applyTheme(String themeName) {
        if (!themeName.equals("light") && !themeName.equals("dark")) return;

        currentTheme = themeName;
        prefs.put(PREF_KEY, themeName);
    }

    /**
     * 永続設定からテーマを読み込む（初回呼び出し推奨）
     */
    public static void loadSavedTheme() {
        currentTheme = prefs.get(PREF_KEY, "light");
    }

    /**
     * 現在のテーマ（"light" or "dark"）
     */
    public static String getCurrentTheme() {
        return currentTheme;
    }
}