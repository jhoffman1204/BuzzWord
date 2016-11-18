package controller;

import apptemplate.AppTemplate;
import gui.Workspace;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Created by James on 11/16/2016.
 */

public class CreateUserHandler implements EventHandler<ActionEvent>
{
    AppTemplate app;
    Workspace workspace;
    HangmanController controller;
    String username;
    String password;

    public CreateUserHandler(AppTemplate app, Workspace workspace)
    {
        this.workspace = workspace;
        this.app = app;
        this.controller = new HangmanController(app);
    }
    public void setUsername(String username)
    {
        this.username = username;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }
    @Override
    public void handle(ActionEvent event)
    {
        workspace.homeScreen();
        controller.createNewUser(this.username,this.password);
    }
}
