package gui;


import apptemplate.AppTemplate;
import components.AppWorkspaceComponent;
import controller.HangmanController;
import controller.CreateUserHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import propertymanager.PropertyManager;
import ui.AppGUI;
import ui.AppMessageDialogSingleton;
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
    GridPane    changeUserInformation;
    GridPane    levelSelectBoard;
    //
    Button      createButton;
    Button      loginButton;
    Button      viewHelpButton;
    Button      changeUserInfoButton;
    Button      homeButton;
    Button      logoutButton;
    Button      playerProfileButton;
    ChoiceBox   selectLevel;
    Button      exitButton;
    Label       gameModeLabel;
    Button      startGame;
    Button      backButton;
    Button      replayButton;
    Button      pauseButton;
    Button      resumeButton;

    String currentUserName;
    String currentPassword;


    String currentGameMode;
    int    currentLevel;

    String runningWord = "";

    Label targetPointsLabel;
    String[] wordsInGrid = new String[10];
    String[] wordDuplicateChecker = new String[10];
    boolean[] wordsFound  = new boolean[10];

    public String[] wordsDetected = new String[10];

    int currentVocabLength;

    boolean gameWon = false;
    boolean draggable = false;

    String userName;
    WorkspaceNodeInitialization node = new WorkspaceNodeInitialization();
    HangmanController controller;

    StatsBoardSingleton statsBoardSingleton;

    int currentTargetScore = 0;
    public boolean gameStarted = false;

    Button recentButton = null;
    String apexLetter = "";
    String recentLetter = "";

    boolean firstButtonPressed = false;


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
        controller.loadWordBanks();
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

    public String getCurrentGameMode() {
        return currentGameMode;
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

            statsBoardSingleton = new StatsBoardSingleton(this.app);
            this.statsBoard = statsBoardSingleton.getStatsBoard();
            statsBoard.setOnMouseEntered(event -> {
                runningWord = "";
                recentLetter = "";
                apexLetter = "";
                unHighlightAllGameButtons();
                statsBoardSingleton.clearLetters();
            });
            gamePlayPane.setMargin(statsBoardSingleton.getStatsBoard(), new Insets(0,0,0,100));

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
            changeUserInfoButton = node.createChangeColor();
            viewHelpButton = node.createviewHelpButton();
            homeButton = node.createHomeButton();
            selectLevel =  node.createSelectLevel();
            createAccount = this.createProfileScreen();
            loginAccount = this.createLoginScreen();
            changeUserInformation = this.createChangeUserInfo();
            levelSelectBoard = this.createLevelSelection("AnimalsVocab");
            logoutButton = node.createLogoutnButton();

            handleShortCuts();

            gamePlayPane.setMargin(levelSelectBoard,new Insets(30,0,0,100));
            selectLevel.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if(newValue.intValue() == 0)
                    {
                        gameModeLabel.setText("<-- Select a Level");
                    }
                    if(newValue.intValue() == 1){
                        try {
                            levelSelectScreen("general");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gameModeLabel.setText("General");
                        currentGameMode = "general";
                        controller.setCurrentGameModeString("general");
                        controller.updateLevelCurrentlyOn();
                    }
                    else if(newValue.intValue() == 2)
                    {
                        currentVocabLength = 134;
                        try {
                            levelSelectScreen("Animals");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gameModeLabel.setText("Animals");
                        currentGameMode = "animals";
                        controller.setCurrentGameModeString("animals");
                        controller.updateLevelCurrentlyOn();
                    }
                    else if(newValue.intValue() == 3)
                    {
                        currentVocabLength = 36;
                        try {
                            levelSelectScreen("Countries");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        gameModeLabel.setText("Countries");
                        currentGameMode = "countries";
                        controller.setCurrentGameModeString("countries");
                        controller.updateLevelCurrentlyOn();
                    }

                }
            });
            viewHelpButton.setOnAction(event -> {
                try {
                    ScrollPane pane = new ScrollPane();
                    pane.setMaxSize(700,300);
                    pane.setHmax(100);
                    pane.setVmax(100);
                    pane.setContent(new Label(
                            "Hello and Welcome to BuzzWord, created by James Hoffman" +
                            "\n\nWhat do you need to know?" +
                            "\n1) First you want to either create an account or log in, dont worry, your password is encrypted" +
                                    "\n\n then you want to select a game mode, hitting the game level will start the game" +
                                    "\nHOW TO PLAY:" +
                                    "\n1) Type in words that you see on the grid and press enter" +
                                    "\n2) Alternatively, you can drag the mouse over words you wnat, and it will detect words for you" +
                                    "\n3) Play until the time runs out, and beat all the scores for the levels in each of the three game modes" +
                                    "\n\nHAVE FUN!!!" +
                                    "\n\nINFO ABOUT THE CREATOR" +
                                    "\nI created this game in my 219 class for Richard Mckenna" +
                                    "\nIt uses the JFXFramework that he provided us with" +
                                    "\nUsers passwords are encrypted so that no one can steal your information"+
                                    "\n\nWhat do you need to know?" +
                                    "\n1) First you want to either create an account or log in, dont worry, your password is encrypted" +
                                    "\n\n then you want to select a game mode, hitting the game level will start the game" +
                                    "\nHOW TO PLAY:" +
                                    "\n1) Type in words that you see on the grid and press enter" +
                                    "\n2) Alternatively, you can drag the mouse over words you wnat, and it will detect words for you" +
                                    "\n3) Play until the time runs out, and beat all the scores for the levels in each of the three game modes" +
                                    "\n\nHAVE FUN!!!" +
                                    "\n\nINFO ABOUT THE CREATOR" +
                                    "\nI created this game in my 219 class for Richard Mckenna" +
                                    "\nIt uses the JFXFramework that he provided us with" +
                                    "\nUsers passwords are encrypted so that no one can steal your information"));
                    //pane.setMinSize(500,500);
                    gamePlayPane.setMargin(pane, new Insets(0,0,0,50));


                    gamePlayPane.getChildren().clear();
                    menuPane.getChildren().clear();
                    menuPane.getChildren().addAll(exitButton,backButton);
                    gamePlayPane.getChildren().addAll(pane);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            changeUserInfoButton.setOnAction(e ->
            {

                gamePlayPane.getChildren().clear();
                menuPane.getChildren().clear();
                menuPane.getChildren().addAll(exitButton,backButton);
                gamePlayPane.getChildren().addAll(changeUserInformation);

            });
            logoutButton.setOnAction(event -> {
                controller.saveUserInformation();
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
                unHighlightAllGameButtons();
                statsBoardSingleton.stopTimer();
            });
            backButton.setOnAction(event -> loginScreen());

            menuPane.getChildren().addAll(selectLevel,createButton,loginButton,viewHelpButton, changeUserInfoButton);
            this.setPaneMargins();
            changeUserInfoButton.setAlignment(Pos.BASELINE_LEFT);
            menuPane.setPadding(new Insets(0,50,0,0));
            menuPane.setStyle("-fx-background-color: #FA7C92;-fx-border-color: black;-fx-border-width: 7px;");


            titlePane.setMargin(   gameModeLabel, new Insets(0,0,0,200));
            gamePlayPane.setMargin(createAccount, new Insets(100,0,0,400));
            gamePlayPane.setMargin(loginAccount, new Insets(100,0,0,400));

            gamePlayPane.getChildren().add(gameBoard);

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
    public void replayLevel()
    {
        statsBoardSingleton.removeReplayButton();
        statsBoardSingleton.resetStatBar();
        gameModeLabel.setText(currentGameMode +  "level " + currentLevel);
        generateNewGameBoard(currentGameMode);
    }
    public void iterateLevel()
    {
        statsBoardSingleton.removeReplayButton();
        statsBoardSingleton.resetStatBar();
        statsBoardSingleton.updateTargetPoints(30 + currentLevel*10);
        this.currentLevel = currentLevel + 1;
        gameModeLabel.setText(currentGameMode + " level " + currentLevel);
    }
    public void generateNewGameBoard(String vocab)
    {
        resetWordsFound();
        handleKeyPressedEvents();
        int wordLength = 0;
        if(vocab.equalsIgnoreCase("animals") || vocab.equalsIgnoreCase("general"))
        {
            wordLength = 3;
        }
        else
        {
                wordLength = 4;
        }
        clearboard();
        statsBoardSingleton.resetStatBar();
        try
        {
            statsBoardSingleton.updatePreviousBestPane(controller.getCurrentUser().getGamemodes().get(vocab).getSpecificGameModeLevel(currentLevel).getPersonalBest() + "");
        }
        catch (Exception e)
        {
            System.out.println("there was no previous best");
        }
        //The amount of time the user will have to complete the level
        statsBoardSingleton.startTimer(30);
        clearWordsInGrid();
        recentButton = null;
        if(randomInsert(vocab,wordLength) == false) {
            return;
        }
        if(randomInsert(vocab,wordLength) == false) {
            return;
        }
        wordLength++;
        if(randomInsert(vocab,wordLength) == false) {
            return;
        }
        fillInBoard();
        handleKeyPressedEvents();
        gameWon = false;
        gameBoard.setDisable(false);
        controller.scanBoardForWords();
        if(currentGameMode != "countries")
        {
            updateWordBank();
        }
        printWordsInGrid();
    }
    public void updateWordBank()
    {
        for(int i = 0 ; i < wordsInGrid.length; i++)
        {
            wordsInGrid[i] = wordsDetected[i];
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
        menuPane.setMargin(changeUserInfoButton,new Insets(80,0,0,50));
        menuPane.setMargin(logoutButton,     new Insets(80,0,0,50));
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
        menuPane.getChildren().addAll(exitButton,createButton,loginButton,viewHelpButton, changeUserInfoButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().add(gameBoard);
    }
    public void homeScreen()
    {
        menuPane.getChildren().clear();
        gameBoard.setDisable(false);
        gameModeLabel.setText("Select a Level");
        menuPane.getChildren().addAll(exitButton,selectLevel,logoutButton,viewHelpButton, changeUserInfoButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().add(gameBoard);
        selectLevel.getSelectionModel().selectFirst();
    }
    public void levelSelectScreen(String vocab) throws IOException {
        levelSelectBoard = this.createLevelSelection(vocab);
        gamePlayPane.setMargin(levelSelectBoard,new Insets(30,0,0,100));
        menuPane.getChildren().clear();
        gameModeLabel.setText("Choose what level you want to play");
        menuPane.getChildren().addAll(exitButton,selectLevel,logoutButton,viewHelpButton, changeUserInfoButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().add(levelSelectBoard);
    }
    public void gamePlayScreen(String level)
    {
        this.currentTargetScore = controller.getTargetScore(level);
        menuPane.getChildren().clear();
        gameModeLabel.setText(currentGameMode + " level " + level);
        menuPane.getChildren().addAll(exitButton,homeButton,logoutButton,viewHelpButton, changeUserInfoButton);
        gamePlayPane.getChildren().clear();
        gamePlayPane.getChildren().addAll(gameBoard,statsBoard);
        gameStarted = true;
    }
    public void highlightGameButton(Button button) {
        button.setStyle(button.getStyle() + ";-fx-border-color: yellow;");
    }
    public boolean isHighlighted(Button button)
    {
        if(button.getStyle().contains("yellow"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public void unHighLightGameButton(String letter)
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j= 0; j < 4; j++)
            {
                Button button = (Button)getNodeByRowColumnIndex(i, j, gameBoard);
                if(button.getText().equalsIgnoreCase(letter))
                {
                    unHighlightGameButton(button);
                }
            }
        }
    }
    public void unHighlightGameButton(Button button)
    {
        button.setStyle("-fx-background-radius: 30px;-fx-font-weight: bold; -fx-font-size: 30px;-fx-background-color: #FFF7C0;-fx-border-radius:30px;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
    }
    public void unHighlightAllGameButtons()
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j= 0; j < 4; j++)
            {
                Button button = (Button)getNodeByRowColumnIndex(i, j, gameBoard);
                unHighlightGameButton(button);
            }
        }
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
            this.loadUser(userNameField.getText(),controller.encrpyPassword(passwordField.getText()));
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
        for(int i = 0 ; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                String letter = generateRandomLetter();
                Button button = node.createBuzzWordButton(letter);
                gameboard.add(button, i, j);


                button.setOnMouseEntered(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (gameStarted == true) {
                            if (draggable == true) {
                                runningWord += button.getText();
                                highlightGameButton(button);
                                statsBoardSingleton.updateCurrentLetters(button.getText());
                            }
                        }
                    }
                });
                button.setOnMouseExited(event -> {
                    checkIfWordFound(runningWord);
                });
                gameboard.setOnMouseExited(event -> {
                    unHighlightAllGameButtons();
                    recentLetter = "";
                    apexLetter = "";
                    checkIfWordFound(runningWord);
                    statsBoardSingleton.clearLetters();
                    draggable = false;
                    return;

                });
                button.setOnAction(event -> {
                    if(draggable == false)
                    {
                        runningWord += button.getText();
                        highlightGameButton(button);
                        statsBoardSingleton.updateCurrentLetters(button.getText());
                        checkIfWordFound(runningWord);
                        draggable = true;
                        System.out.println("draggable is now true");
                    }
                    else if(draggable == true)
                    {
                        draggable = false;
                        System.out.println("draggable is now false");
                    }
                });
            }

        }
        return gameboard;
    }
    public void checkIfWordFound(String runningWord)
    {
        for(int i = 0; i < wordsInGrid.length; i++)
        {
            if(wordsInGrid[i] != null) {
                if (wordsInGrid[i].equalsIgnoreCase(runningWord)) {
                    statsBoardSingleton.addWordToStatsBoard(runningWord);
                    statsBoardSingleton.clearLetters();
                    this.runningWord = "";
                    for(int j = 0; j < wordsInGrid.length; j++)
                    {
                        if(runningWord.equalsIgnoreCase(wordsInGrid[i]))
                        {
                            wordsFound[i] = true;
                            draggable = false;
                        }
                    }
                    if(statsBoardSingleton.getTotalPoints() >= currentTargetScore)
                    {
                        gameWon = true;
                        System.out.println("you have achieved the required amount of points for this game");
                    }
                    unHighlightAllGameButtons();
                }
            }
        }
    }
    public void resetWordsFound()
    {
        for(int i = 0 ; i < 10; i++)
        {
            wordsFound[i] = false;
        }
    }
    public void printFound()
    {
        String[] wordsNotFound = new String[10];
        for(int i = 0 ;i < wordsFound.length; i++)
        {
            if(wordsFound[i] == false && wordsInGrid[i] != null)
            {
                wordsNotFound[i] = wordsInGrid[i];
            }
        }
        statsBoardSingleton.addNotFoundWordsToList(wordsNotFound);

    }
    public void handleShortCuts()
    {
        app.getGUI().getPrimaryScene().setOnKeyTyped((KeyEvent event) -> {
                if(event.getCharacter().equals("L"))
                {
                    System.out.println(event.getCharacter());
                    gamePlayPane.getChildren().clear();
                    menuPane.getChildren().clear();
                    menuPane.getChildren().addAll(exitButton,backButton);
                    gamePlayPane.getChildren().addAll(loginAccount);
                }
                if(event.getCharacter().equals("C"))
                {
                    gamePlayPane.getChildren().clear();
                    menuPane.getChildren().clear();
                    menuPane.getChildren().addAll(exitButton,backButton);
                    gamePlayPane.getChildren().addAll(createAccount);
                }
                if(event.getCharacter().equals("H"))
                {
                    viewHelpButton.fire();
                }
            });
    }
    public void removeKeyPressedEvents()
    {
        app.getGUI().getPrimaryScene().setOnKeyTyped((KeyEvent event) ->
        {
            System.out.println("does nothing");
        });
//        statsBoard.setOnMouseEntered(event ->
//        {
//            System.out.println("do nothing");
//        });
    }
    public void handleKeyPressedEvents()
    {
        app.getGUI().getPrimaryScene().setOnKeyTyped((KeyEvent event) -> {
            if(event.getCharacter().equalsIgnoreCase("\r")){
                System.out.println("ACTIVATED");
                unHighlightAllGameButtons();
                recentLetter = "";
                apexLetter = "";
                checkIfWordFound(runningWord);
                statsBoardSingleton.clearLetters();
                draggable = false;
                runningWord = "";
                return;
            }
            if(event.getCharacter().equals("\b")){
                statsBoardSingleton.backspace();
                unHighLightGameButton(runningWord.charAt(runningWord.length()-1) +"");
                runningWord = runningWord.substring(0,runningWord.length()-1);
                return;
            }
            String a = event.getCharacter().charAt(0)+"";
            SelectLettersByLetter(a.toUpperCase(),recentButton);
            runningWord += a;
            statsBoardSingleton.updateCurrentLetters(a);
        });
    }
    public void pauseGame()
    {
        gameBoard.setVisible(false);
        app.getGUI().getPrimaryScene().setOnKeyTyped((KeyEvent event) -> {

        });
    }
    public void resumeGame()
    {
        gameBoard.setVisible(true);
        handleKeyPressedEvents();
    }
    public void gameEnd()
    {
        removeKeyPressedEvents();
        int previousScore = controller.getCurrentGameMode(currentGameMode,currentLevel).getPersonalBest();
        gameBoard.setDisable(true);

        if(gameWon == true)
        {
            statsBoardSingleton.enableNextLevel();
            gameModeLabel.setText("You have defeated level " + currentLevel);
            System.out.println(statsBoardSingleton.getTotalPoints());
            System.out.println(currentGameMode + " " + currentLevel);
            controller.completedLevel(currentGameMode,currentLevel,statsBoardSingleton.getTotalPoints());
            controller.updateLevelCurrentlyOn();
            if(controller.getCurrentGameMode(currentGameMode,currentLevel).getPersonalBest() > previousScore)
            {
                gameModeLabel.setText("Lv." + currentLevel+ " New high score:" +
                        controller.getCurrentGameMode(currentGameMode,currentLevel).getPersonalBest());
            }
            else
                {
                    gameModeLabel.setText("Lv." + currentLevel+ " current high score:" +
                            controller.getCurrentGameMode(currentGameMode,currentLevel).getPersonalBest());
            }
        }
        else {
            gameModeLabel.setText("You lost level " + currentLevel);
            statsBoardSingleton.disableNextLevel();
        }
        printFound();
        String[] notGuessedWords = new String[10];
        //statsBoardSingleton.showMissingWords(wordsInGrid);
        statsBoardSingleton.displayReplayButton();
    }

    public void printAdjacentLetters(String recentLetters, String newLetter)
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j= 0; j < 4; j++)
            {
                Button button = (Button)getNodeByRowColumnIndex(i, j, gameBoard);
                if(button.getText().equalsIgnoreCase(recentLetters))
                {
                    Button up    = (Button)getNodeByRowColumnIndex(i+1, j, gameBoard);
                    if(checkIfExists(gameBoard,i+1,j))
                    {
                        if(up.getText().equalsIgnoreCase(newLetter) && isHighlighted(button))
                        {
                            highlightGameButton(up);
                        }
                    }
                    Button down  = (Button)getNodeByRowColumnIndex(i-1, j, gameBoard);
                    if(checkIfExists(gameBoard,i-1,j))
                    {
                        if(down.getText().equalsIgnoreCase(newLetter) && isHighlighted(button))
                        {
                            highlightGameButton(down);
                        }
                    }
                    Button left  = (Button)getNodeByRowColumnIndex(i, j+1, gameBoard);
                    if(checkIfExists(gameBoard,i,j+1))
                    {
                        if(left.getText().equalsIgnoreCase(newLetter) && isHighlighted(button)) {
                            highlightGameButton(left);
                        }
                    }
                    Button right = (Button)getNodeByRowColumnIndex(i, j-1, gameBoard);
                    if(checkIfExists(gameBoard,i+1,j-1))
                    {
                        if(right.getText().equalsIgnoreCase(newLetter) && isHighlighted(button)) {
                            highlightGameButton(right);
                        }
                    }

                }
            }
        }
    }
    public void SelectLettersByLetter(String newLetter, Button recentButton)
    {
        newLetter = newLetter.toLowerCase();
        for(int i = 0; i < 4; i++)
        {
            for(int j= 0; j < 4; j++)
            {
                Button button = (Button)getNodeByRowColumnIndex(i, j, gameBoard);
                if(button.getText().contains(newLetter))
                {
                    if(apexLetter.equalsIgnoreCase(""))
                    {
                        highlightGameButton(button);
                        recentLetter = button.getText();
                        System.out.println("FIRST recent letter is now " + recentLetter);
                    }
                    else {
                        printAdjacentLetters(recentLetter, newLetter);
                        recentLetter = button.getText();
                        System.out.println("recent letter is now " + recentLetter);
                        return;
                    }

                }
            }
        }
        apexLetter = newLetter;
    }
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
    public void clearWordsInGrid()
    {
        for(int i = 0 ;i < wordsInGrid.length; i++)
        {
            wordsInGrid[i] = null;
            wordsDetected[i] = null;
        }
    }
    public void checkIfCompleteWord()
    {

    }
    public void detectWords(String[] wordBank, String currentDetectedWord)
    {
        for(int i = 0; i < 4; i++)
        {
            for(int j= 0; j < 4; j++)
            {
                Button button = (Button)getNodeByRowColumnIndex(i, j, gameBoard);
                detectHelper(button, wordBank,button.getText(),i,j);
            }
        }
    }
    public void detectHelper(Button button, String[] wordBank, String currentDetectedWord, int xCoor, int yCoor)
    {
        checkIfCompleteWord(currentDetectedWord, wordBank);
        //button = (Button)getNodeByRowColumnIndex(xCoor, yCoor, gameBoard);
        if(checkIfExists(gameBoard,xCoor + 1,yCoor))
        {
            Button b = (Button) getNodeByRowColumnIndex(xCoor + 1, yCoor, gameBoard);
            if(containInArray(wordBank,currentDetectedWord + b.getText()))
            {
                detectHelper(b, wordBank,currentDetectedWord + b.getText() , xCoor + 1, yCoor);
                //System.out.println(currentDetectedWord);
            }
            else{

            }
        }
        if(checkIfExists(gameBoard,xCoor - 1,yCoor))
        {
            Button b = (Button) getNodeByRowColumnIndex(xCoor - 1, yCoor, gameBoard);
            if(containInArray(wordBank,currentDetectedWord + b.getText()))
            {
                detectHelper(b, wordBank,currentDetectedWord + b.getText(), xCoor - 1, yCoor);
                //System.out.println(currentDetectedWord);
            }
            else{

            }
        }
        if(checkIfExists(gameBoard,xCoor,yCoor + 1))
        {
            Button b = (Button) getNodeByRowColumnIndex(xCoor , yCoor + 1, gameBoard);
            if(containInArray(wordBank,currentDetectedWord+ b.getText()))
            {
                detectHelper(b, wordBank,currentDetectedWord+ b.getText(), xCoor, yCoor + 1);
                //System.out.println(currentDetectedWord);
            }
            else{

            }
        }
        if(checkIfExists(gameBoard,xCoor,yCoor-1))
        {
            Button b = (Button) getNodeByRowColumnIndex(xCoor, yCoor - 1, gameBoard);
            if(containInArray(wordBank,currentDetectedWord+ b.getText()))
            {
                detectHelper(b, wordBank,currentDetectedWord + b.getText(), xCoor , yCoor - 1);
                //System.out.println(currentDetectedWord);
            }
            else{

            }
        }
        return;
    }
    public void checkIfCompleteWord(String word, String[] wordBank)
    {
        for(int i = 0 ; i < wordBank.length; i++)
        {
            if(wordBank[i].equalsIgnoreCase(word))
            {
                for(int j = 0; j < wordsDetected.length; j++)
                {
                    if(wordsDetected[j] == null)
                    {
                        if(alreadyInArray(wordsDetected,word)) {
                            wordsDetected[j] = word;
                        }
                    }
                }
            }
        }
    }
    public boolean alreadyInArray(String[] array, String word)
    {
        for(int i = 0; i < array.length; i++)
        {
            if(array[i] != null) {
                if (array[i].equalsIgnoreCase(word)) {
                    return false;
                }
            }
        }
        return true;
    }
    public void printWordsDetected()
    {
        for(int i = 0 ; i < wordsDetected.length; i++)
        {
            if(wordsDetected[i] != null)
            {
                System.out.println(wordsDetected[i]);
            }
        }
    }
    public boolean containInArray(String[] array, String word)
    {
        for(int i = 0 ; i < array.length; i++)
        {
             if(array[i].contains(word)){
                 return true;
             }
        }
        return false;
    }
    public boolean randomInsert(String vocabList, int wordLength)
    {
        int counter = 0;
        String word = null;
        Random random = new Random();
        int xCoor = random.nextInt(4);
        int yCoor = random.nextInt(4);

        try {
            word = controller.generateRandomWordFromFile(vocabList,wordLength);
            boolean a = checkIfNullOnGameBoard(gameBoard,xCoor,yCoor);
            int infiniteCounter = 0;
            int wordCounter = 0;
            while(a == false)
            {
                infiniteCounter++;
                if(infiniteCounter > 100)
                {
                    System.out.println("a word was trapped in another word");
                    randomInsert(vocabList,wordLength);
                    return false;
                }
                xCoor = random.nextInt(4);
                yCoor = random.nextInt(4);
                a = checkIfNullOnGameBoard(gameBoard,xCoor,yCoor);
            }
            for(int i = 0; i < word.length(); i++)
            {
                if( i == 0) {
                    Button button = (Button) getNodeByRowColumnIndex(xCoor, yCoor, gameBoard);
                    button.setText(word.charAt(i) + "");
                }
                if( i > 0) {
                    counter++;
                    if(counter > 50)
                    {
                        counter = 0;
                        System.out.println("a word was trapped");
                        clearboard();
                        clearWordsInGrid();
                        generateNewGameBoard(vocabList);
                        //this made the board not always have all the solutions
                        return false;
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
            for(int i = 0; i < 10; i++)
            {
                if(wordsInGrid[i] == null)
                {
                    System.out.println("a word has been added");
                    System.out.println(i);
                    wordsInGrid[i] = word;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("THE ERROR IS HERE");
            e.printStackTrace();
        }
        return true;
    }
    public void printWordsInGrid()
    {
        for(int i = 0; i < wordsInGrid.length; i++)
        {
            if(wordsInGrid[i] != null)
            {
                System.out.println((1+i) + ") " + wordsInGrid[i]);
            }
        }
    }
    public boolean checkIfExists(GridPane gameBaord, int xCoor, int yCoor)
    {
        try
        {
            Button button = (Button) getNodeByRowColumnIndex(xCoor, yCoor, gameBoard);
            if(button.getText().equalsIgnoreCase(""))
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
    public void enableLevelSelectionNode(int levelAvailable)
    {
        int counter = 0;
        for(int i = 0; i < 2; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                this.getNodeByRowColumnIndex(i,j,this.levelSelectBoard).setDisable(false);
                counter++;
                if(counter == levelAvailable)
                {
                    return;
                }
            }
        }
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
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-border-color: black;-fx-border-width: 2px;");
        pane.add(userNameLabel,0,1);
        pane.add(userNameField,1,1);
        pane.add(passwordLabel,0,2);
        pane.add(passwordField,1,2);
        CreateUserHandler handler = new CreateUserHandler(app,this);
        Button submit = new Button("Login Profile!");
        submit.setOnAction(event -> {
            if(controller.loadUserInformation(userNameField.getText(),controller.encrpyPassword(passwordField.getText())) == true)
            {
                homeScreen();
                userNameField.clear();
                passwordField.clear();
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
    public GridPane createChangeUserInfo()
    {
        GridPane pane = new GridPane();
        pane.setMinWidth(600);
        pane.setStyle("-fx-border-color: black;-fx-background-color: white;-fx-border-width: 4px;");
        pane.setVgap(50);
        pane.setHgap(50);
        Label title = new Label("Change your account!");
        title.setStyle("-fx-font-size: 25px;-fx-underline:true;");
        pane.add(title,0,0);
        Label userNameLabel = new Label("Enter New Username: ");
        userNameLabel.setMinSize(200,50);
        userNameLabel.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 2px;");
        TextField userNameField = new TextField();
        userNameField.setStyle("-fx-border-color: black;-fx-border-width: 2px;");
        Label passwordLabel = new Label("Enter New Password: ");
        passwordLabel.setMinSize(200,50);
        passwordLabel.setStyle("-fx-font-size: 25px;-fx-background-color: #FFF7C0;-fx-border-color: black;-fx-border-width: 2px;");
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-border-color: black;-fx-border-width: 2px;");
        pane.add(userNameLabel,0,1);
        pane.add(userNameField,1,1);
        pane.add(passwordLabel,0,2);
        pane.add(passwordField,1,2);
        CreateUserHandler handler = new CreateUserHandler(app,this);
        Button submit = new Button("Login Profile!");
        submit.setOnAction(event -> {
            controller.changeUserInformation(userNameField.getText(),controller.encrpyPassword(passwordField.getText()));
            homeScreen();

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
    public GridPane createLevelSelection(String vocab) throws IOException {
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
                    gamePlayScreen(button.getText());
                    this.currentLevel = Integer.parseInt(button.getText());
                    statsBoardSingleton.updateTargetPoints(currentTargetScore);
                    generateNewGameBoard(vocab);
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
