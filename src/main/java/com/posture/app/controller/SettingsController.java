package com.posture.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import com.posture.app.util.ThemeManager;
import com.posture.app.model.AppSettings;
import com.posture.app.model.RecordManager;

public class SettingsController {

    @FXML private ComboBox<String> themeComboBox;
    @FXML private ComboBox<String> storageComboBox;
    @FXML private CheckBox autoSaveCheckBox;

    @FXML private RadioButton lightThemeRadio;
    @FXML private RadioButton darkThemeRadio;
    @FXML private ToggleGroup themeToggleGroup;

    @FXML private RadioButton jsonRadio;
    @FXML private RadioButton sqliteRadio;
    @FXML private ToggleGroup storageToggleGroup;

    private AppSettings settings;

    @FXML
    public void initialize() {
        settings = new AppSettings();
        setupThemeControls();

        // 保存形式初期化
        String currentStorage = RecordManager.getInstance().getStorageMode();
        if ("sqlite".equals(currentStorage)) {
            sqliteRadio.setSelected(true);
        } else {
            jsonRadio.setSelected(true);
        }

        // テーマ設定
        themeComboBox.getItems().addAll("light", "dark");
        themeComboBox.setValue(settings.getTheme());
        themeComboBox.setOnAction(e -> {
            String theme = themeComboBox.getValue();
            settings.setTheme(theme);
            ThemeManager.applyTheme(theme);
        });
        
        // 保存形式設定
        storageComboBox.getItems().addAll("json", "sqlite");
        storageComboBox.setValue(settings.getStorageType());
        storageComboBox.setOnAction(e -> {
            settings.setStorageType(storageComboBox.getValue());
        });
        
        // 自動保存設定
        autoSaveCheckBox.setSelected(settings.isAutoSave());
        autoSaveCheckBox.setOnAction(e -> {
            settings.setAutoSave(autoSaveCheckBox.isSelected());
        });
    }

    private void setupThemeControls() {
        String currentTheme = ThemeManager.getCurrentTheme();
        if ("dark".equals(currentTheme)) {
            darkThemeRadio.setSelected(true);
        } else {
            lightThemeRadio.setSelected(true);
        }

        themeToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == lightThemeRadio) {
                ThemeManager.applyTheme("light");
            } else if (newVal == darkThemeRadio) {
                ThemeManager.applyTheme("dark");
            }
        });
    }

    @FXML
    public void handleSaveSettings() {
        // テーマ保存
        String selectedTheme = lightThemeRadio.isSelected() ? "light" : "dark";
        ThemeManager.applyTheme(selectedTheme);

        // 保存形式更新
        String selectedStorage = jsonRadio.isSelected() ? "json" : "sqlite";
        RecordManager.getInstance().setStorageMode(selectedStorage);

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("設定完了");
        alert.setHeaderText(null);
        alert.setContentText("設定が保存されました。再起動後に反映されます。");
        alert.showAndWait();
    }

    @FXML
    public void handleSave() {
        settings.save();
    }
}
