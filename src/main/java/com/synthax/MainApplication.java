package com.synthax;

import com.synthax.view.SynthaxView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view/synthax-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(getClass().getResource("skins.css").toExternalForm());
        SynthaxView controller = fxmlLoader.getController();
        stage.setTitle("Synthax");
        stage.setScene(scene);
        stage.show();

        //controller.addOscillator();
        //controller.addOscillator(); j

        // auto close Synth when we exit window
        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}