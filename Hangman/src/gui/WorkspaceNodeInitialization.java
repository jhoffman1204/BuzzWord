package gui;

import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.IOException;

/**
 * Created by James on 11/7/2016.
 */


public class WorkspaceNodeInitialization
{

    public ChoiceBox createSelectLevel()
    {
        ChoiceBox selectLevel = new ChoiceBox();
        selectLevel.getItems().addAll("Select Mode","General","Animals","Countries");
        selectLevel.getSelectionModel().selectFirst();
        selectLevel.setMinHeight(100);
        selectLevel.setMinWidth(200);
        selectLevel.setStyle("-fx-background-color: #FFF7C0;-fx-font-size: 25px;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        return selectLevel;
    }
    public Button createProfileButton()
    {

        Button button = new Button("Create Profile");
        button.setMinHeight(100);
        button.setMinWidth(200);
        button.setStyle("-fx-background-color: #FFF7C0;-fx-font-size: 25px;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        button.setPadding(new Insets(20,20,20,20));
        return button;
    }
    public Button createLoginButton()
    {
        Button button = new Button("Login");
        button.setMinHeight(100);
        button.setMinWidth(200);
        button.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        button.setOnMouseDragEntered(event -> System.out.println("test"));
        return button;
    }
    public Button createLogoutnButton()
    {
        Button button = new Button("Logout");
        button.setMinHeight(100);
        button.setMinWidth(200);
        button.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        return button;
    }
    public Button createviewHelpButton()
    {
        Button button = new Button("View Help");
        button.setMinHeight(100);
        button.setMinWidth(200);
        button.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        return button;
    }
    public Button createHomeButton()
    {
        Button button = new Button("Home");
        button.setMinHeight(100);
        button.setMinWidth(200);
        button.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        return button;
    }
    public Button createBackButton()
    {
        Button button = new Button("Back");
        button.setMinHeight(100);
        button.setMinWidth(200);
        button.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        return button;
    }
    public Button createPlayerProfileButton()
    {
        Button button = new Button("Player Profile");
        button.setMinHeight(100);
        button.setMinWidth(200);
        button.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        return button;
    }
    public Button createBuzzWordButton(int counter)
    {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Button button = new Button(alphabet.charAt(counter) + "");
        button.setMinSize(120,120);
        button.setStyle("-fx-font-weight: bold; -fx-font-size: 30px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        return button;
    }
    public Button createBuzzWordButton(String text)
    {
        Button button = new Button(text);
        button.setMinSize(120,120);
        button.setStyle("-fx-background-radius: 30px;-fx-font-weight: bold; -fx-font-size: 30px;-fx-background-color: #FFF7C0;-fx-border-radius:30px;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        return button;
    }
    public Button createLevelSelectionButton()
    {
        Button button = new Button();
        button.setMinSize(120,120);
        button.setStyle("-fx-font-weight: bold; -fx-font-size: 30px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        return button;
    }
    public Button createChangeColor()
    {
        Button button = new Button("Edit Acct Info");
        button.setMinHeight(50);
        button.setMinWidth(100);
        button.setStyle("-fx-font-size: 15px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 4px;-fx-border-insets: -2px");
        return button;
    }

    public Button createExitButton()
    {
        Button button = new Button("X");
        button.setStyle("-fx-border-color: black;");
        button.setMaxSize(20,20);
        return button;
    }
    public HBox createCurrentLetterPane()
    {
        HBox pane = new HBox();
        pane.setMaxSize(400,80);
        Label label = new Label("B U");
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 25px;");
        pane.getChildren().add(label);
        return pane;
    }
    public HBox totalPointsPane()
    {
        HBox pane = new HBox();
        pane.setMinSize(400,80);
        pane.setMaxSize(400,80);
        pane.setStyle("-fx-background-color: beige;-fx-border-color: black;-fx-border-width: 4px;");
        Label label = new Label("Total" + "                                          " + "75");
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 25px;");
        pane.getChildren().add(label);
        return pane;
    }
    /**
     * things to add
     * Current letters selected
     * words guessed pane
     * total points
     * Target points pane
     */
}
