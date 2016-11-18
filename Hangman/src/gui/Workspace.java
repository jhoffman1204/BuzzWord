package gui;

import apptemplate.AppTemplate;
import components.AppWorkspaceComponent;
import controller.HangmanController;
import controller.CreateUserHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import propertymanager.PropertyManager;
import ui.AppGUI;

import java.io.IOException;
import java.util.Random;

import static hangman.HangmanProperties.*;

/**
 * This class serves as the GUI component for the Hangman game.
 *
 * @author Ritwik Banerjee
 */
public class Workspace extends AppWorkspaceComponent {

    AppTemplate app; // the actual application
    AppGUI      gui; // the GUI inside which the application sits

    VBox        menuPane;
    VBox        bodyPane;
    VBox        titlePane;
    GridPane    gameBoard;
    VBox        statsBoard;
    HBox        gamePlayPane;
    GridPane    createAccount;
    GridPane    loginAccount;
    GridPane    levelSelectBoard;
    //
    Button      createButton;
    Button      loginButton;
    Button      viewHelpButton;
    Button      changeColorButton;
    Button      homeButton;
    Button      logoutButton;
    Button      playerProfileButton;
    ChoiceBox   selectLevel;
    Button      exitButton;
    Label       gameModeLabel;
    Button      startGame;

    String currentUserName;
    String currentPassword;


    String userName;
    WorkspaceNodeInitialization node = new WorkspaceNodeInitialization();
    HangmanController controller;
    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     * @throws IOException Thrown should there be an error loading application
     *                     data for setting up the user interface.
     */
    public Workspace(AppTemplate initApp) throws IOException {
        app = initApp;
        gui = app.getGUI();
        layoutGUI();     // initialize all the workspace (GUI) components including the containers and their layout
        setupHandlers(); // ... and set up event handling
    }

    public HBox getGamePlayPane() {
        return gamePlayPane;
    }

    public void setGamePlayPane(HBox gamePlayPane) {
        this.gamePlayPane = gamePlayPane;
    }

    public VBox getMenuPane() {
        return menuPane;
    }

    public String getCurrentUserName()
    {
        return this.currentUserName;
    }
    public String getCurrentPassword()
    {
        return this.currentPassword;
    }
    public void setMenuPane(VBox menuPane) {
        this.menuPane = menuPane;
    }


    private void layoutGUI() {
        try {
            bodyPane = new VBox();
            menuPane = new VBox();
            gamePlayPane = new HBox();
            gameBoard = this.createGameBoard();
            //spacing between the nodes that are in the Gridpane
            gameBoard.setHgap(0);
            gameBoard.setVgap(0);


            gameModeLabel = new Label("Log in or Create a Profile!");
            gameModeLabel.setStyle("-fx-font-size: 100px;");

            exitButton = node.createExitButton();
            Image image = new Image("file:Hangman/resources/images/projectTitle.png", 800, 200, true, true);
            ImageView image2 = new ImageView(image);
            titlePane = new VBox();
            titlePane.getChildren().addAll(image2, gameModeLabel);
            //moves the title 400 pixels to the right (because the margin is on the left
            titlePane.setMargin(image2,new Insets(-20,0,-50,400));

            playerProfileButton = node.createPlayerProfileButton();
            createButton = node.createProfileButton();
            loginButton = node.createLoginButton();
            changeColorButton = node.createChangeColor();
            viewHelpButton = node.createviewHelpButton();
            homeButton = node.createHomeButton();
            selectLevel =  node.createSelectLevel();
            createAccount = this.createProfileScreen();
            loginAccount = this.createLoginScreen();
            levelSelectBoard = this.createLevelSelection();
            logoutButton = node.createLogoutnButton();
            gamePlayPane.setMargin(levelSelectBoard,new Insets(30,0,0,100));
            selectLevel.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    levelSelectScreen();
                    if(newValue.intValue() == 0)
                    {
                        gameModeLabel.setText("<-- Select a Level");
                    }
                    if(newValue.intValue() == 1){
                        gameModeLabel.setText("Presidents");
                    }
                    else if(newValue.intValue() == 2)
                    {
                        gameModeLabel.setText("Science");
                    }
                    else if(newValue.intValue() == 3)
                    {
                        gameModeLabel.setText("Countries");
                    }

                }
            });
            viewHelpButton.setOnAction(event -> {
                try {
                   // controller.generateRandomWordFromFile("CountriesVocab",35);
                    controller.generateRandomWordFromFile("AnimalsVocab",135);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            changeColorButton.setOnAction(event -> randomizeColorPalette());
            logoutButton.setOnAction(event -> {
                loginScreen();
                this.userName = "";
            });
            exitButton.setOnAction(event -> System.exit(1));
            createButton.setOnAction(event -> {
                gamePlayPane.getChildren().clear();
                menuPane.getChildren().clear();
                menuPane.getChildren().addAll(exitButton,homeButton);
                gamePlayPane.getChildren().addAll(createAccount);
            });
            loginButton.setOnAction(event ->
            {
                gamePlayPane.getChildren().clear();
                menuPane.getChildren().clear();
                menuPane.getChildren().addAll(exitButton,homeButton);
                gamePlayPane.getChildren().addAll(loginAccount);
            });
            homeButton.setOnAction(event ->
            {
                homeScreen();
                gamePlayPane.getChildren().clear();
                gamePlayPane.getChildren().addAll(gameBoard);
            });
            menuPane.getChildren().addAll(selectLevel,createButton,loginButton,viewHelpButton,changeColorButton);
            this.setPaneMargins();
            changeColorButton.setAlignment(Pos.BASELINE_LEFT);
            menuPane.setPadding(new Insets(0,50,0,0));
            menuPane.setStyle("-fx-background-color: #FA7C92;-fx-border-color: black;-fx-border-width: 7px;");

            ///ScrollPane helpView = new ScrollPane();
            //menuPane.getChildren().add(helpView);

            titlePane.setMargin(   gameModeLabel, new Insets(0,0,0,200));
            gamePlayPane.setMargin(createAccount, new Insets(0,0,0,200));
            gamePlayPane.setMargin(loginAccount, new Insets(0,0,0,200));

            gamePlayPane.getChildren().add(gameBoard);
            statsBoard = new VBox();
            statsBoard.setMinWidth(500);
            statsBoard.setMinHeight(500);
            statsBoard.setStyle("-fx-background-color: white;-fx-border-color: black;-fx-border-width: 4px;");

            HBox timeRemainingPane  = node.timeRemainingPanes();
            HBox currentLettersPane = node.createCurrentLetterPane();
            HBox guessedWordsPane   = node.guessedWordsPane();
            HBox totalPointsPane    = node.totalPointsPane();
            HBox targetPoints       = node.targetPointsPane();

            Button pauseButton = new Button("Pause Game");
            pauseButton.setMinHeight(50);
            pauseButton.setMinWidth(100);
            pauseButton.setStyle("-fx-font-size: 25px;-fx-background-color: red;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");

            statsBoard.getChildren().add(timeRemainingPane);
            statsBoard.getChildren().add(currentLettersPane);
            statsBoard.getChildren().add(guessedWordsPane);
            statsBoard.getChildren().add(totalPointsPane);
            statsBoard.getChildren().add(targetPoints);
            statsBoard.getChildren().add(pauseButton);



            statsBoard.setMargin(timeRemainingPane,new Insets(40,0,40,40));
            statsBoard.setMargin(currentLettersPane,new Insets(0,0,40,40));
            statsBoard.setMargin(guessedWordsPane,new Insets(0,0,40,40));
            statsBoard.setMargin(totalPointsPane,new Insets(0,0,40,40));
            statsBoard.setMargin(targetPoints,new Insets(0,0,40,40));
            statsBoard.setMargin(pauseButton,new Insets(0,0,40,40));


            gamePlayPane.setMargin(statsBoard, new Insets(0,0,0,100));
            gamePlayPane.getChildren().add(statsBoard);
            //Inset parameters go in the order Top, Right, Bottom, Left
            //so that the gameboard does not touch the menu or the statistics and is far down from the title
            gamePlayPane.setMargin(gameBoard,new Insets(20,0,0,100));


            bodyPane.getChildren().add(titlePane);
            bodyPane.getChildren().add(gamePlayPane);


            bodyPane.setStyle("-fx-background-color: #6EC4D8;");
            bodyPane.setMinWidth(1800);
            workspace = new HBox();
            workspace.getChildren().addAll(menuPane,bodyPane);

            startGame = new Button();
            loginScreen();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void randomizeColorPalette()
    {
        Random x = new Random();
        int y = x.nextInt(3);
        if(y == 0)
        {
            bodyPane.setStyle("-fx-background-color: #B56357;");
            menuPane.setStyle("-fx-background-color: #B4DBC0;-fx-border-color: black;-fx-border-width: 7px;");
        }
        if(y == 1)
        {
            bodyPane.setStyle("-fx-background-color: #BFD8D2;");
            menuPane.setStyle("-fx-background-color: #DF744A;-fx-border-color: black;-fx-border-width: 7px;");
        }
        if(y == 2)
        {
            bodyPane.setStyle("-fx-background-color: #6EC4D8;");
            menuPane.setStyle("-fx-background-color: #FA7C92;-fx-border-color: black;-fx-border-width: 7px;");

        }
    }
    public void setPaneMargins()
    {
        menuPane.setMargin(selectLevel,      new Insets(80,0,0,50));
        menuPane.setMargin(createButton,     new Insets(80,0,0,50));
        menuPane.setMargin(loginButton,      new Insets(80,0,0,50));
        menuPane.setMargin(viewHelpButton,   new Insets(80,0,0,50));
        menuPane.setMargin(homeButton,       new Insets(80,0,0,50));
        menuPane.setMargin(changeColorButton,new Insets(80,0,0,50));
        menuPane.setMargin(logoutButton,     new Insets(80,0,0,50));
    }
    public void statBoardMargins()
    {

    }

    /**
     * These represent the states of the game, they are able to be called and have the appropriate workspace, but the
     * data for the workspace will be provided by the hangmancontroller, based on what the user chooses
     */
    public void loginScreen()
    {
        menuPane.getChildren().clear();
        menuPane.getChildren().addAll(createButton,loginButton,viewHelpButton,changeColorButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().add(gameBoard);
    }
    public void homeScreen()
    {
        menuPane.getChildren().clear();
        menuPane.getChildren().addAll(selectLevel,logoutButton,viewHelpButton,changeColorButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().add(gameBoard);
    }
    public void levelSelectScreen()
    {
        menuPane.getChildren().clear();
        menuPane.getChildren().addAll(selectLevel,logoutButton,viewHelpButton,changeColorButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().add(levelSelectBoard);
    }
    public void gamePlayScreen(String level)
    {
        menuPane.getChildren().clear();
        gameModeLabel.setText(gameModeLabel.getText() + " level " + level);
        menuPane.getChildren().addAll(homeButton,logoutButton,viewHelpButton,changeColorButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().addAll(gameBoard,statsBoard);
    }
    public void highlightGameButton(Button button) {
        button.setStyle(button.getStyle() + ";-fx-border-color: yellow;");
    }
    public GridPane createProfileScreen()
    {
        GridPane pane = new GridPane();
        pane.setMinWidth(600);
        pane.setStyle("-fx-border-color: black;-fx-background-color: white;-fx-border-width: 4px;");
        pane.setVgap(50);
        pane.setHgap(50);
        Label title = new Label("Create a Profile!");
        title.setStyle("-fx-font-size: 25px;-fx-underline:true;");
        pane.add(title,0,0);
        Label userNameLabel = new Label("Enter a Username: ");
        userNameLabel.setMinSize(200,50);
        userNameLabel.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 2px;");
        TextField userNameField = new TextField();
        userNameField.setStyle("-fx-border-color: black;-fx-border-width: 2px;");
        Label passwordLabel = new Label("Enter a Password: ");
        passwordLabel.setMinSize(200,50);
        passwordLabel.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 2px;");
        TextField passwordField = new TextField();
        passwordField.setStyle("-fx-border-color: black;-fx-border-width: 2px;");
        pane.add(userNameLabel,0,1);
        pane.add(userNameField,1,1);
        pane.add(passwordLabel,0,2);
        pane.add(passwordField,1,2);
        CreateUserHandler handler = new CreateUserHandler(app,this);
        Button submit = new Button("Create Profile");
        submit.setOnAction(event -> {
            handler.setUsername(userNameField.getText());
            handler.setPassword(passwordField.getText());
            handler.handle(new ActionEvent());
        });
        pane.setMargin(title,new Insets(40,0,-20,40));
        pane.setMargin(userNameLabel,new Insets(20,0,0,40));
        pane.setMargin(userNameField,new Insets(20,0,0,20));
        pane.setMargin(passwordLabel,new Insets(20,0,0,40));
        pane.setMargin(passwordField,new Insets(20,0,0,20));
        pane.setMargin(submit,new Insets(0,0,40,40));
        pane.add(submit,0,3);
        return pane;
    }
    public GridPane createGameBoard()
    {
        GridPane gameboard = new GridPane();
        int counter = 1;
        //j is the row and i is the column
        for(int i = 0 ; i < 7; i = i + 2)
        {
            for(int j = 0; j < 7; j = j + 2)
            {
                String letter = generateRandomLetter();
                Button button = node.createBuzzWordButton(letter);
                gameboard.add(button, i, j);
                counter++;
                button.setOnAction(event -> highlightGameButton(button));
            }

        }
        for(int i = 1 ; i < 7; i = i + 2)
        {
            for(int j = 0; j < 7; j = j + 2)
            {
                Line line = new Line(0,60,60,60);
                gameboard.add(line, i, j);
            }

        }
        for(int i = 0; i < 7; i = i + 2)
        {
            for(int j = 1; j < 7; j = j + 2)
            {
                Pane pane = new Pane();
                pane.setPrefSize(60,60);
                Line line = new Line(60,0,60,60);
                pane.getChildren().add(line);
                gameboard.add(pane, i, j);
            }

        }
        return gameboard;
    }
    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }
    public String generateRandomLetter()
    {
        String letterBank = "AAAAAAAABCDEEEEEEEFGHIKLMNOPRSTUWY";
        Random randomNumberGenerator = new Random();
        return (letterBank.charAt(randomNumberGenerator.nextInt(letterBank.length()-1))+"");
    }
    public GridPane createLoginScreen()
    {
        GridPane pane = new GridPane();
        pane.setMinWidth(600);
        pane.setStyle("-fx-border-color: black;-fx-background-color: white;-fx-border-width: 4px;");
        pane.setVgap(50);
        pane.setHgap(50);
        Label title = new Label("Login to your account!");
        title.setStyle("-fx-font-size: 25px;-fx-underline:true;");
        pane.add(title,0,0);
        Label userNameLabel = new Label("Enter Username: ");
        userNameLabel.setMinSize(200,50);
        userNameLabel.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 2px;");
        TextField userNameField = new TextField();
        userNameField.setStyle("-fx-border-color: black;-fx-border-width: 2px;");
        Label passwordLabel = new Label("Enter Password: ");
        passwordLabel.setMinSize(200,50);
        passwordLabel.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 2px;");
        TextField passwordField = new TextField();
        passwordField.setStyle("-fx-border-color: black;-fx-border-width: 2px;");
        pane.add(userNameLabel,0,1);
        pane.add(userNameField,1,1);
        pane.add(passwordLabel,0,2);
        pane.add(passwordField,1,2);
        CreateUserHandler handler = new CreateUserHandler(app,this);
        Button submit = new Button("Login Profile!");
        submit.setOnAction(event -> {
            handler.setUsername(userNameField.getText());
            handler.setPassword(passwordField.getText());
            handler.handle(new ActionEvent());
        });
        pane.setMargin(title,new Insets(40,0,-20,40));
        pane.setMargin(userNameLabel,new Insets(20,0,0,40));
        pane.setMargin(userNameField,new Insets(20,0,0,20));
        pane.setMargin(passwordLabel,new Insets(20,0,0,40));
        pane.setMargin(passwordField,new Insets(20,0,0,20));
        pane.setMargin(submit,new Insets(0,0,40,40));
        pane.add(submit,0,3);
        return pane;
    }
    public GridPane createLevelSelection() throws IOException {
        GridPane levelSelection = new GridPane();
        levelSelection.setHgap(50);
        levelSelection.setVgap(50);

        int counter = 1;
        for(int i = 0 ; i < 2; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                Button button = node.createLevelSelectionButton();
                button.setOnAction(event -> {
                    gamePlayScreen(button.getText());
                    setupHandlers();
                });
                button.setText(counter + "");
                levelSelection.add(button, j, i);
                counter++;
            }
        }

        return levelSelection;
    }


    private void setupHandlers() {
        controller = new HangmanController(app, startGame);
    }

    /**
     * This function specifies the CSS for all the UI components known at the time the workspace is initially
     * constructed. Components added and/or removed dynamically as the application runs need to be set up separately.
     */
    @Override
    public void initStyle() {
        PropertyManager propertyManager = PropertyManager.getManager();

        gui.getAppPane().setId(propertyManager.getPropertyValue(ROOT_BORDERPANE_ID));
        gui.getToolbarPane().getStyleClass().setAll(propertyManager.getPropertyValue(SEGMENTED_BUTTON_BAR));
        gui.getToolbarPane().setId(propertyManager.getPropertyValue(TOP_TOOLBAR_ID));

        ObservableList<Node> toolbarChildren = gui.getToolbarPane().getChildren();
        toolbarChildren.get(0).getStyleClass().add(propertyManager.getPropertyValue(FIRST_TOOLBAR_BUTTON));
        toolbarChildren.get(toolbarChildren.size() - 1).getStyleClass().add(propertyManager.getPropertyValue(LAST_TOOLBAR_BUTTON));

        workspace.getStyleClass().add(CLASS_BORDERED_PANE);
       // guiHeadingLabel.getStyleClass().setAll(propertyManager.getPropertyValue(HEADING_LABEL));

    }

    /** This function reloads the entire workspace */
    @Override
    public void reloadWorkspace() {

    }


    public Button getStartGame() {
        return startGame;
    }

    public void reinitialize() {
//        guessedLetters = new HBox();
//        guessedLetters.setStyle("-fx-background-color: transparent;");
//        remainingGuessBox = new HBox();
//        gameTextsPane = new VBox();
//        gameTextsPane.getChildren().setAll(remainingGuessBox, guessedLetters);
//        bodyPane.getChildren().setAll(figurePane, gameTextsPane);
    }
}
