package com.synthax.view.utils;

import com.synthax.MainApplication;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Dialogs {

    static Boolean result;
    static String fileName;

    public static String getTextInput(String title, String contextText, String promptText) {
        result = false;
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setResizable(false);

        Label context = new Label(contextText);
        TextField textField = new TextField(promptText);
        textField.setMaxWidth(170);
        context.setFont(new Font("Handel Gothic",14));
        ImageView iv = new ImageView(new Image(MainApplication.class.getResource("Images/fileIconsvart.png").toExternalForm()));
        iv.setFitHeight(80);
        iv.setFitWidth(70);

        Button btnCancel = new Button("Cancel");
        Button btnOK = new Button("OK");
        btnCancel.setOnAction(e -> {
            fileName = "";
            result = true;
            window.close();
        });
        btnOK.setOnAction(e-> {
            fileName = textField.getText();
            window.close();
        });
        window.setOnCloseRequest(e-> {
            fileName = "";
            result = true;
            window.close();
        });

        VBox textInputPane = new VBox(5);
        textInputPane.setPadding(new Insets(30,0,0,0));
        textInputPane.getChildren().addAll(context, textField);

        HBox topPane = new HBox(50);
        topPane.setPadding(new Insets(20,20,0,20));
        topPane.getChildren().addAll(textInputPane, iv);
        topPane.setAlignment(Pos.CENTER);

        HBox bottomPane = new HBox(10);
        bottomPane.setPadding(new Insets(0, 10, 10 ,0));
        bottomPane.getChildren().addAll(btnCancel, btnOK);
        bottomPane.setAlignment(Pos.BOTTOM_RIGHT);

        VBox layout = new VBox(20);
        layout.getChildren().addAll(topPane, bottomPane);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(MainApplication.class.getResource("skins.css").toExternalForm());

        window.setScene(scene);
        window.showAndWait();
        //While loop to prevent from clicking ok with empty filename
        while (fileName.equals("") && !result) {
            window.showAndWait();
        }

        return fileName;
    }

    public static Boolean getConfirmation(String title, String contextText) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(300);
        window.setResizable(false);

        Label context = new Label(contextText);
        context.setFont(new Font("Handel Gothic",17));
        ImageView iv = new ImageView(new Image(MainApplication.class.getResource("Images/alertIconOrange.png").toExternalForm()));
        iv.setFitHeight(80);
        iv.setFitWidth(80);

        Button btnCancel = new Button("Cancel");
        Button btnOK = new Button("OK");

        btnCancel.setOnAction(e -> {
            result = false;
            window.close();
        });
        btnOK.setOnAction(e-> {
            result = true;
            window.close();
        });
        window.setOnCloseRequest(e-> {
            result = false;
            window.close();
        });

        HBox topPane = new HBox(80);
        topPane.setPadding(new Insets(10,10,0,20));
        topPane.getChildren().addAll(context, iv);
        topPane.setAlignment(Pos.CENTER);

        HBox bottomPane = new HBox(10);
        bottomPane.setPadding(new Insets(0, 10, 10 ,0));
        bottomPane.getChildren().addAll(btnCancel, btnOK);
        bottomPane.setAlignment(Pos.BOTTOM_RIGHT);

        VBox layout = new VBox(20);
        layout.getChildren().addAll(topPane, bottomPane);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        scene.getStylesheets().add(MainApplication.class.getResource("skins.css").toExternalForm());

        window.setScene(scene);
        window.showAndWait();

        return result;
    }

}
