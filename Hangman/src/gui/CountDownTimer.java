package gui;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by James on 11/23/2016.
 */
public class CountDownTimer
{
    private static final Integer STARTTIME = 15;
    private Timeline timeline;
    private Label timerLabel = new Label();
    private Integer timeSeconds = STARTTIME;

    public HBox createCountdownPane() {

        // Configure the Label
        timerLabel.setText(timeSeconds.toString());
        timerLabel.setStyle("-fx-font-size: 40px;");


        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds = STARTTIME;

        // update timerLabel
        timerLabel.setText(timeSeconds.toString());
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        event1 ->  {
                            timeSeconds--;
                            // update timerLabel
                            timerLabel.setText(
                                    timeSeconds.toString());
                            if (timeSeconds <= 0) {
                                timeline.stop();
                            }
                        }));

        HBox pane = new HBox();
        pane.setMinSize(400,80);
        pane.setMaxSize(400,80);
        pane.setStyle("-fx-background-color: beige;-fx-border-color: black;-fx-border-width: 4px;");


        Label label = new Label("Time Remaining: ");
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 30px;");

        pane.setMargin(label, new Insets(15,0,0,15));
        pane.setMargin(timerLabel, new Insets(10,0,0,0));
        pane.getChildren().addAll(label, timerLabel);
        return pane;
    }
    public void beginTimer()
    {
        timeline.playFromStart();
    }
}
