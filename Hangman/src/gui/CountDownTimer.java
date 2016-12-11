package gui;
import apptemplate.AppTemplate;
import controller.GameEndHandler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * Created by James on 11/23/2016.
 */
public class CountDownTimer
{
    private Timeline timer;
    private Label timeRemainingLabel = new Label();
    private Integer timeRemaining = 0;
    private AppTemplate app;

    public CountDownTimer(AppTemplate app)
    {
        this.app = app;
    }
    public HBox createCountdownPane() {

        // Configure the Label
        timeRemainingLabel.setText(timeRemaining.toString());
        timeRemainingLabel.setStyle("-fx-font-size: 40px;");


//        if (timer != null) {
//            timer.stop();
//        }
        timeRemaining = 0;

        // update timeRemainingLabel
        timeRemainingLabel.setText(timeRemaining.toString());
        timer = new Timeline();
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        event1 ->  {
                            timeRemaining--;
                            // update timeRemainingLabel
                            timeRemainingLabel.setText(
                                    timeRemaining.toString());
                            if (timeRemaining <= 0) {
                                timer.stop();
                                GameEndHandler handler = new GameEndHandler(this.app);
                                handler.handle(new ActionEvent());
                            }
                        }));

        HBox pane = new HBox();
        pane.setMinSize(400,80);
        pane.setMaxSize(400,80);
        //pane.setStyle("-fx-background-color: beige;-fx-border-color: black;-fx-border-width: 4px;");


        Label label = new Label("Time Remaining: ");
        label.setStyle("-fx-font-weight: bold;-fx-font-size: 30px;");

        pane.setMargin(label, new Insets(15,0,0,15));
        pane.setMargin(timeRemainingLabel, new Insets(10,0,0,0));
        pane.getChildren().addAll(label, timeRemainingLabel);
        return pane;
    }
    public void pauseTimer()
    {
        timer.pause();
    }
    public void continueTimer()
    {
        timer.play();
    }
    public void beginTimer(int time)
    {
        timeRemaining = time;
        timer.playFromStart();
    }
}
