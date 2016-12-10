package controller;

import apptemplate.AppTemplate;
import gui.Workspace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by James on 12/10/2016.
 */
public class ReplayGameHandler implements EventHandler<ActionEvent> {
    AppTemplate app;
    public ReplayGameHandler(AppTemplate app)
    {
        this.app = app;
    }

    @Override
    public void handle(ActionEvent event) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        workspace.replayLevel();
    }
}
