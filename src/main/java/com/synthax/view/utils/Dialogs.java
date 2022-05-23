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

    public static final int CANCEL_OPTION = 0;
    public static final int YES_OPTION = 1;
    public static final int NO_OPTION = 2;

    static int result;
    static Boolean okButtonPressed = false;
    static String fileName;

    public static String getTextInput(String title, String contextText, String promptText) {
        okButtonPressed = false;
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
            okButtonPressed = false;
            window.close();
        });
        btnOK.setOnAction(e-> {
            fileName = textField.getText();
            okButtonPressed = true;
            window.close();
        });
        window.setOnCloseRequest(e-> {
            fileName = "";
            okButtonPressed = false;
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
        while (fileName.equals("") && okButtonPressed) {
            window.showAndWait();
        }

        return fileName;
    }

    public static int getConfirmationYesNoCancel(String title, String contextText) {
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
        Button btnYes = new Button("Yes");
        Button btnNo = new Button("No");

        btnYes.setOnAction(e-> {
            result = YES_OPTION;
            window.close();
        });
        btnNo.setOnAction(e -> {
            result = NO_OPTION;
            window.close();
        });
        btnCancel.setOnAction(e -> {
            result = CANCEL_OPTION;
            window.close();
        });
        window.setOnCloseRequest(e-> {
            result = CANCEL_OPTION;
            window.close();
        });

        HBox topPane = new HBox(80);
        topPane.setPadding(new Insets(10,10,0,20));
        topPane.getChildren().addAll(context, iv);
        topPane.setAlignment(Pos.CENTER);

        HBox bottomPane = new HBox(10);
        bottomPane.setPadding(new Insets(0, 10, 10 ,0));
        bottomPane.getChildren().addAll(btnYes, btnNo, btnCancel);
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

    public static int getConfirmationYesCancel(String title, String contextText) {
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

        btnOK.setOnAction(e-> {
            result = YES_OPTION;
            window.close();
        });
        btnCancel.setOnAction(e -> {
            result = CANCEL_OPTION;
            window.close();
        });
        window.setOnCloseRequest(e-> {
            result = CANCEL_OPTION;
            window.close();
        });

        HBox topPane = new HBox(80);
        topPane.setPadding(new Insets(10,10,0,20));
        topPane.getChildren().addAll(context, iv);
        topPane.setAlignment(Pos.CENTER);

        HBox bottomPane = new HBox(10);
        bottomPane.setPadding(new Insets(0, 10, 10 ,0));
        bottomPane.getChildren().addAll(btnOK, btnCancel);
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
