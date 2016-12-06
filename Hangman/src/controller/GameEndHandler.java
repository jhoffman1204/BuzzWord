package controller;

import apptemplate.AppTemplate;
import gui.Workspace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by James on 12/3/2016.
 */
public class GameEndHandler implements EventHandler<ActionEvent>
{
    AppTemplate app;
    Workspace workspace;
    HangmanController controller;
    String username;
    String password;

    public GameEndHandler(AppTemplate app)
    {
        this.app = app;
    }
    @Override
    public void handle(ActionEvent event)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.gameEnd();
    }
}
