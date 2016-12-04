package gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by James on 12/1/2016.
 */
public class StatsBoardSingleton
{
    VBox statsBoard;

    VBox pointsPane;

    private HBox currentLettersPane;
    private VBox guessedWordsPane;
    private HBox totalPointsPane;
    private HBox targetPoints;
    private Button pauseButton;
    private Button resumeButton;

    Label targetPointsLabel = null;

    CountDownTimer ct = new CountDownTimer();
    HBox timerPane = ct.createCountdownPane();

    private int totalPoints;
    private String currentlySelectedLetters = "";
    private String[] currentlyGuessedWords = new String[10];

    public StatsBoardSingleton()
    {
        currentLettersPane = this.createCurrentLetterPane();
        guessedWordsPane = this.createGuessedWordsPane();
        totalPointsPane = this.createTotalPointsPane();
        targetPoints = this.createTargetPointsPane();
        pauseButton = this.createPauseButton();
        resumeButton = this.createResumeButton();

        statsBoard = new VBox();
        statsBoard.setMinWidth(500);
        statsBoard.setMinHeight(400);
        statsBoard.setMaxHeight(700);
        statsBoard.setStyle("-fx-background-color: white;-fx-border-color: black;-fx-border-width: 4px;");

        pointsPane = new VBox();
        pointsPane.setStyle("-fx-background-color: white;-fx-border-color: black;-fx-border-width: 2px;");
        pointsPane.setMaxWidth(250);
        pointsPane.setMaxWidth(250);
        pointsPane.getChildren().addAll(guessedWordsPane,totalPointsPane);
        pointsPane.setMargin(guessedWordsPane, new Insets(0,20,0,20));
        pointsPane.setMargin(totalPointsPane,  new Insets(0,20,0,20));

        statsBoard.getChildren().add(timerPane);
        statsBoard.getChildren().add(currentLettersPane);
        statsBoard.getChildren().add(pointsPane);
        statsBoard.getChildren().add(targetPoints);
        statsBoard.getChildren().add(pauseButton);

        statsBoard.setMargin(timerPane ,         new Insets(10,0,5,20));
        statsBoard.setMargin(currentLettersPane, new Insets(0,0,20,20));
        statsBoard.setMargin(pointsPane,         new Insets(0,0,40,25));
        statsBoard.setMargin(targetPoints,       new Insets(0,0,20,40));
        statsBoard.setMargin(pauseButton,        new Insets(0,0,40,40));
        statsBoard.setMargin(resumeButton,       new Insets(0,0,40,40));


        updateTargetPoints(100);

        startTimer(60);
    }
    public void startTimer(int time)
    {
        ct.beginTimer(time);
    }
    public void addWordToStatsBoard(String word)
    {
        int points = word.length()*10;
        for(int i = 0; i < 6-word.length(); i++)
        {
            word = word + " ";
        }
        Label label = new Label(word + "                                         " + points);
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 25px;");
        guessedWordsPane.getChildren().add(label);

        updateTotalPoints(points);

        for(int i = 0; i < currentlyGuessedWords.length; i++)
        {
            if(currentlyGuessedWords[i] == null)
            {
                currentlyGuessedWords[i] = word;
                break;
            }
        }
    }
    public void updateTargetPoints(int targetPoints)
    {
        targetPointsLabel = new Label("Target:" + "                                       " + targetPoints);
        targetPointsLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 25px;");
        this.targetPoints.getChildren().clear();
        this.targetPoints.getChildren().add(targetPointsLabel);
    }
    public void updateTotalPoints(int points)
    {
        this.totalPoints+= points;
        totalPointsPane.getChildren().clear();
        Label label = new Label("Total" + "                                          " + totalPoints);
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 25px;");
        totalPointsPane.getChildren().add(label);
    }
    public void updateCurrentLetters(String s)
    {
        currentlySelectedLetters += (s + " ");
        Label label = new Label(currentlySelectedLetters);
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 25px;");
        currentLettersPane.getChildren().clear();
        currentLettersPane.getChildren().add(label);
    }
    public void clearLetters()
    {
        currentlySelectedLetters = "";
        Label label = new Label(currentlySelectedLetters);
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 25px;");
        currentLettersPane.getChildren().clear();
        currentLettersPane.getChildren().add(label);
    }
    public HBox createCurrentLetterPane()
    {
        HBox pane = new HBox();
        pane.setMaxSize(400,80);
        return pane;
    }
    public VBox createGuessedWordsPane()
    {
        VBox pane = new VBox();
        pane.setMinSize(400,250);
        pane.setMaxSize(400,250);
        pane.setStyle("-fx-background-color: white;");
        return pane;
    }
    public HBox createTotalPointsPane()
    {
        HBox pane = new HBox();
        pane.setMinSize(400,80);
        pane.setMaxSize(400,80);
        pane.setStyle("-fx-background-color: white;");
        return pane;
    }
    public HBox createTargetPointsPane()
    {
        HBox pane = new HBox();
        pane.setMinSize(400,80);
        pane.setMaxSize(400,80);
        targetPointsLabel = new Label("Target:" + "                                       " + targetPoints);
        targetPointsLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 25px;");
        pane.getChildren().add(targetPointsLabel);
        return pane;
    }
    public static VBox getStatBoardSingleton()
    {
        StatsBoardSingleton a = new StatsBoardSingleton();
        return a.getStatsBoard();
    }
    public VBox getStatsBoard()
    {
        return this.statsBoard;
    }
    public Button createPauseButton()
    {
        pauseButton = new Button("Pause Game");
        pauseButton.setMinHeight(50);
        pauseButton.setMinWidth(100);
        pauseButton.setStyle("-fx-font-size: 25px;-fx-background-color: red;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");
        pauseButton.setOnAction(event ->
        {
            //gameBoard.setVisible(false);
            statsBoard.getChildren().remove(pauseButton);
            statsBoard.getChildren().add(resumeButton);
        });

        return pauseButton;
    }
    public Button createResumeButton()
    {
        resumeButton = new Button("Resume Game");
        resumeButton.setMinHeight(50);
        resumeButton.setMinWidth(100);
        resumeButton.setStyle("-fx-font-size: 25px;-fx-background-color: red;-fx-border-color: black;-fx-border-width: 7px;-fx-border-insets: -5px");

        resumeButton.setOnAction(event ->
        {
            //gameBoard.setVisible(true);
            statsBoard.getChildren().remove(resumeButton);
            statsBoard.getChildren().add(pauseButton);
        });

        return resumeButton;
    }


}
