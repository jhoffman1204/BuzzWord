package controller;

import apptemplate.AppTemplate;
import gui.Workspace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by James on 11/16/2016.
 */

public class loginHandler implements EventHandler<ActionEvent>
{
    AppTemplate app;
    Workspace workspace;
    HangmanController controller;

    public loginHandler(AppTemplate app, Workspace workspace)
    {
        this.workspace = workspace;
        this.app = app;
        this.controller = new HangmanController(app);
    }
    @Override
    public void handle(ActionEvent event)
    {


    }
}
