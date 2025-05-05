package com.posture.app.controller;

import java.util.Objects;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import com.posture.app.util.ThemeManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainController {

    @FXML
    private TabPane mainTabPane;

    @FXML
    private VBox mainContainer;
    
    @FXML
    private Button inputButton;
    
    @FXML
    private Button calendarButton;
    
    @FXML
    private Button graphButton;
    
    @FXML
    private Button settingsButton;

    private boolean isDarkTheme = false;

    @FXML
    public void initialize() {
        setupButtonActions();
        // 必要に応じて初期化処理
        System.out.println("MainController initialized");
    }

    private void setupButtonActions() {
        inputButton.setOnAction(e -> loadScene("/view/input.fxml", "入力"));
        calendarButton.setOnAction(e -> loadScene("/view/calendar.fxml", "カレンダー"));
        graphButton.setOnAction(e -> loadScene("/view/graph.fxml", "グラフ"));
        settingsButton.setOnAction(e -> loadScene("/view/settings.fxml", "設定"));
    }

    @FXML
    private void handleToggleTheme() {
        // 現在のウィンドウ（Stage）を取得
        Scene scene = mainTabPane.getScene();
        if (scene == null) return;

        // CSSファイルを切り替え
        scene.getStylesheets().clear();

        if (isDarkTheme) {
            scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/light-theme.css")).toExternalForm()
            );
        } else {
            scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/dark-theme.css")).toExternalForm()
            );
        }

        isDarkTheme = !isDarkTheme;
    }

    private void loadScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
