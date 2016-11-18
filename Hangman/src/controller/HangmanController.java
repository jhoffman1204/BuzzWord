package controller;

import apptemplate.AppTemplate;
import data.GameData;
import data.GameMode;
import data.User;
import gui.Workspace;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import propertymanager.PropertyManager;
import ui.AppMessageDialogSingleton;

import java.io.IOException;
import java.nio.file.Path;

import static settings.AppPropertyType.*;

/**
 * @author Ritwik Banerjee
 */
public class HangmanController implements FileController {

    private AppTemplate appTemplate; // shared reference to the application
    private User currentUser;
    private GameMode currentGameMode;
    private int currentGameModeLevel;


    public HangmanController(AppTemplate appTemplate, Button gameButton) {
        this(appTemplate);
    }

    public HangmanController(AppTemplate appTemplate) {
        this.appTemplate = appTemplate;
    }


    public void start() {
        currentUser.levelCompleted("presidents",1);
        System.out.println(currentUser.isLevelCompleted("presidents",1));


    }
    public void createNewUser(String username, String password)
    {
        User newUser = new User(username,password);
        currentUser = newUser;
    }
    public void completedLevel(String gamemode, int level)
    {
        currentUser.levelCompleted(gamemode, level);
    }
    @Override
    public void handleNewRequest() {
            appTemplate.getDataComponent().reset();
            appTemplate.getWorkspaceComponent().reloadWorkspace();
            ensureActivatedWorkspace();
    }
    
    @Override
    public void handleSaveRequest() throws IOException {

    }

    @Override
    public void handleLoadRequest() {

    }
    
    @Override
    public void handleExitRequest() {

    }

    private void ensureActivatedWorkspace() {
        appTemplate.getWorkspaceComponent().activateWorkspace(appTemplate.getGUI().getAppPane());
    }
}
