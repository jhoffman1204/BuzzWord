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
import ui.YesNoCancelDialogSingleton;

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
    Button      backButton;

    String currentUserName;
    String currentPassword;
    String currentGameMode;

    String targetPointsLabel;

    String userName;
    WorkspaceNodeInitialization node = new WorkspaceNodeInitialization();
    HangmanController controller;

    CountDownTimer ct = new CountDownTimer();
    HBox timerPane = ct.createCountdownPane();
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
            gameBoard.setHgap(50);
            gameBoard.setVgap(50);


            gameModeLabel = new Label("Log in or Create a Profile!");
            gameModeLabel.setStyle("-fx-font-size: 100px;");

            exitButton = node.createExitButton();
            Image image = new Image("file:Hangman/resources/images/projectTitle.png", 800, 200, true, true);
            ImageView image2 = new ImageView(image);
            titlePane = new VBox();
            titlePane.getChildren().addAll(image2, gameModeLabel);
            //moves the title 400 pixels to the right (because the margin is on the left
            titlePane.setMargin(image2,new Insets(-20,0,-50,400));

            backButton = node.createBackButton();
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
                    if(newValue.intValue() == 0)
                    {
                        gameModeLabel.setText("<-- Select a Level");
                    }
                    if(newValue.intValue() == 1){

                        levelSelectScreen();
                        gameModeLabel.setText("Presidents");
                        currentGameMode = "presidents";
                        controller.setCurrentGameModeString("presidents");
                        controller.updateLevelCurrentlyOn();
                    }
                    else if(newValue.intValue() == 2)
                    {
                        levelSelectScreen();
                        gameModeLabel.setText("Science");
                        currentGameMode = "science";
                        controller.setCurrentGameModeString("science");
                        controller.updateLevelCurrentlyOn();
                    }
                    else if(newValue.intValue() == 3)
                    {
                        levelSelectScreen();
                        gameModeLabel.setText("Countries");
                        currentGameMode = "countries";
                        controller.setCurrentGameModeString("countries");
                        controller.updateLevelCurrentlyOn();
                    }

                }
            });
            viewHelpButton.setOnAction(event -> {
                try {
                   // controller.start();
                   // controller.generateRandomWordFromFile("CountriesVocab",35);
                   // controller.generateRandomWordFromFile("AnimalsVocab",135);
                   // controller.generateRandomWordFromFile("generalVocab",300000,4);
                    clearboard();
                    randomInsert();
                    randomInsert();
                    randomInsert();
                    fillInBoard();
                    //insertWordsIntoGameBoard("AnimalsVocab");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            changeColorButton.setOnAction(e ->
            {
                randomizeColorPalette();
                fillInBoard();
            });
            logoutButton.setOnAction(event -> {
                loginScreen();
                this.userName = "";
            });
            exitButton.setOnAction(event ->
            {
                YesNoCancelDialogSingleton.getSingleton().show("Exit Game","Do you really want to exit the game?");
                if(YesNoCancelDialogSingleton.getSingleton().getSelection().equalsIgnoreCase("yes"))
                {
                    controller.saveUserInformation();
                    System.exit(1);
                }
                else
                {
                    return;
                }

            });
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
                menuPane.getChildren().addAll(exitButton,backButton);
                gamePlayPane.getChildren().addAll(loginAccount);
            });
            homeButton.setOnAction(event -> {
                homeScreen();
            });
            backButton.setOnAction(event -> loginScreen());
            menuPane.getChildren().addAll(selectLevel,createButton,loginButton,viewHelpButton,changeColorButton);
            this.setPaneMargins();
            changeColorButton.setAlignment(Pos.BASELINE_LEFT);
            menuPane.setPadding(new Insets(0,50,0,0));
            menuPane.setStyle("-fx-background-color: #FA7C92;-fx-border-color: black;-fx-border-width: 7px;");


            titlePane.setMargin(   gameModeLabel, new Insets(0,0,0,200));
            gamePlayPane.setMargin(createAccount, new Insets(100,0,0,400));
            gamePlayPane.setMargin(loginAccount, new Insets(100,0,0,400));

            gamePlayPane.getChildren().add(gameBoard);
            statsBoard = new VBox();
            statsBoard.setMinWidth(500);
            statsBoard.setMinHeight(500);
            statsBoard.setStyle("-fx-background-color: white;-fx-border-color: black;-fx-border-width: 4px;");

            HBox timeRemainingPane  = node.timeRemainingPanes();
            HBox currentLettersPane = node.createCurrentLetterPane();
            VBox guessedWordsPane   = node.guessedWordsPane();
            HBox totalPointsPane    = node.totalPointsPane();
            HBox targetPoints       = this.targetPointsPane();

            Button pauseButton = new Button("Pause Game");
            pauseButton.setMinHeight(50);
            pauseButton.setMinWidth(100);
            pauseButton.setStyle("-fx-font-size: 25px;-fx-background-color: red;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");


            statsBoard.getChildren().add(timerPane);
            statsBoard.getChildren().add(currentLettersPane);
            statsBoard.getChildren().add(guessedWordsPane);
            statsBoard.getChildren().add(totalPointsPane);
            statsBoard.getChildren().add(targetPoints);
            statsBoard.getChildren().add(pauseButton);



            statsBoard.setMargin(timerPane ,new Insets(40,0,20,40));
            statsBoard.setMargin(currentLettersPane,new Insets(0,0,20,40));
            statsBoard.setMargin(guessedWordsPane,new Insets(0,0,40,40));
            statsBoard.setMargin(totalPointsPane,new Insets(0,0,20,40));
            statsBoard.setMargin(targetPoints,new Insets(0,0,20,40));
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

            //this is what will be used in the actual game
            loginScreen();

            //gamePlayScreen("1");

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
        menuPane.setMargin(backButton,       new Insets(80,0,0,50));
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
    public void countDownTimer()
    {

    }
    /**
     * These represent the states of the game, they are able to be called and have the appropriate workspace, but the
     * data for the workspace will be provided by the hangmancontroller, based on what the user chooses
     */
    public void loginScreen()
    {
        menuPane.getChildren().clear();
        gameModeLabel.setText("Login or Create a New Profile");
        //this code is for testing purposes
        menuPane.getChildren().addAll(exitButton,createButton,loginButton,viewHelpButton,changeColorButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().add(gameBoard);
    }
    public void homeScreen()
    {
        menuPane.getChildren().clear();
        gameModeLabel.setText("Select a Level");
        menuPane.getChildren().addAll(exitButton,selectLevel,logoutButton,viewHelpButton,changeColorButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().add(gameBoard);
        selectLevel.getSelectionModel().selectFirst();
    }
    public void levelSelectScreen()
    {
        menuPane.getChildren().clear();
        gameModeLabel.setText("Choose what level you want to play");
        menuPane.getChildren().addAll(exitButton,selectLevel,logoutButton,viewHelpButton,changeColorButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().add(levelSelectBoard);
    }
    public void gamePlayScreen(String level)
    {
        menuPane.getChildren().clear();
        gameModeLabel.setText(currentGameMode + " level " + level);
        menuPane.getChildren().addAll(exitButton,homeButton,logoutButton,viewHelpButton,changeColorButton);
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
            this.loadUser(userNameField.getText(),passwordField.getText());
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
        for(int i = 0 ; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                String letter = generateRandomLetter();
                Button button = node.createBuzzWordButton(letter);
                gameboard.add(button, i, j);
                counter++;
                button.setOnAction(event -> highlightGameButton(button));
            }

        }
        return gameboard;
    }

    /**
     * placed four randomized words into the grid, where they will be lateral
     * this is not a good way of doing it but whatever.
     */
    public void clearboard()
    {
            for(int i = 0; i < 4; i++)
            {
                for(int j = 0; j < 4; j++) {
                   Button button = (Button)getNodeByRowColumnIndex(i, j, gameBoard);
                    button.setText("");
                }
            }
    }
    public void fillInBoard()
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j= 0; j < 4; j++)
            {
                Button button = (Button)getNodeByRowColumnIndex(i, j, gameBoard);
                if(button.getText().equalsIgnoreCase(""))
                {
                    button.setText(generateRandomLetter().toLowerCase());
                }
            }
        }
    }

    public void randomInsert()
    {
        int counter = 0;
        String word = null;
        Random random = new Random();
        int xCoor = random.nextInt(4);
        int yCoor = random.nextInt(4);

        try {
            word = controller.generateRandomWordFromFile("AnimalsVocab",100,2);
            boolean a = checkIfNullOnGameBoard(gameBoard,xCoor,yCoor);
            int infiniteCounter = 0;
            while(a == false)
            {
                infiniteCounter++;
                if(infiniteCounter > 20)
                {
                    return;
                }
                xCoor = random.nextInt(4);
                yCoor = random.nextInt(4);
                System.out.println("test");
                a = checkIfNullOnGameBoard(gameBoard,xCoor,yCoor);
            }
            System.out.println(word);
            for(int i = 0; i < word.length(); i++)
            {
                if( i == 0) {
                    Button button = (Button) getNodeByRowColumnIndex(xCoor, yCoor, gameBoard);
                    button.setText(word.charAt(i) + "");
                }
                if( i > 0) {
                    counter++;
                    if(counter > 20)
                    {
                        counter = 0;
                        clearboard();
                        randomInsert();
                        randomInsert();
                        randomInsert();
                    }
                    int nextCoor = random.nextInt(4);
                    if (nextCoor == 0) {
                        a = checkIfNullOnGameBoard(gameBoard,xCoor + 1,yCoor);
                        if(a == true)
                        {
                            Button button = (Button) getNodeByRowColumnIndex(xCoor + 1, yCoor, gameBoard);
                            xCoor++;
                            button.setText(word.charAt(i) + "");
                        }
                        else{
                            i--;
                        }
                    } if (nextCoor == 1) {
                        a = checkIfNullOnGameBoard(gameBoard,xCoor - 1,yCoor);
                        if(a == true) {
                            Button button = (Button) getNodeByRowColumnIndex(xCoor - 1, yCoor, gameBoard);
                            xCoor--;
                            button.setText(word.charAt(i) + "");
                        }
                        else{
                            i--;
                        }
                    } if (nextCoor == 2) {
                        a = checkIfNullOnGameBoard(gameBoard,xCoor,yCoor + 1);
                        if(a == true){
                            Button button = (Button) getNodeByRowColumnIndex(xCoor, yCoor + 1, gameBoard);
                            yCoor++;
                            button.setText(word.charAt(i) + "");
                        }
                        else{
                            i--;
                        }
                    } if (nextCoor == 3) {
                        a = checkIfNullOnGameBoard(gameBoard,xCoor,yCoor-1);
                        if(a == true) {
                            Button button = (Button) getNodeByRowColumnIndex(xCoor, yCoor - 1, gameBoard);
                            yCoor--;
                            button.setText(word.charAt(i) + "");
                        }
                        else{
                            i--;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean checkIfNullOnGameBoard(GridPane gameBoard, int xCoor, int yCoor)
    {
        try
        {
            Button button = (Button) getNodeByRowColumnIndex(xCoor, yCoor, gameBoard);
            if(!button.getText().equalsIgnoreCase(""))
            {
                return false;
            }
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    public void insertWordsIntoGameBoard(String currentGameMode)
    {
        String word = null;
        try {
            for(int i = 0; i < 4; i++)
            {
                word = controller.generateRandomWordFromFile(currentGameMode,100,2);
                for(int j = 0; j < word.length(); j++) {
                    Button button = (Button) getNodeByRowColumnIndex(i, j, gameBoard);
                    button.setText(word.charAt(j) + "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void enableLevelSelectionNode(int row, int column)
    {
        this.getNodeByRowColumnIndex(row,column,this.levelSelectBoard).setDisable(false);
    }
    public void disableLevelSelectionNode(int row, int column)
    {
        this.getNodeByRowColumnIndex(row,column,this.levelSelectBoard).setDisable(true);
    }
    public String generateRandomLetter()
    {
        String letterBank = "AAAAABCDEEEEFGHIKLMNOPRSTUWY";
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
            if(controller.loadUserInformation(userNameField.getText(),passwordField.getText()) == true)
            {
                homeScreen();
            }
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
    public void loadUser(String username, String password)
    {
        controller.loadUserInformation(username, password);
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
                button.setDisable(true);
                button.setOnAction(event -> {
                    ct.beginTimer();
                    gamePlayScreen(button.getText());
                    controller.completedLevel(currentGameMode,Integer.parseInt(button.getText()) + 1,5);
                    controller.updateLevelCurrentlyOn();
                });
                button.setText(counter + "");
                levelSelection.add(button, j, i);
                counter++;
            }
        }
        return levelSelection;
    }
    public HBox targetPointsPane()
    {
        HBox pane = new HBox();
        pane.setMinSize(400,80);
        pane.setMaxSize(400,80);
        Label label = new Label("Target:" + "                                       " + targetPointsLabel);
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 25px;");
        pane.getChildren().add(label);
        return pane;
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
