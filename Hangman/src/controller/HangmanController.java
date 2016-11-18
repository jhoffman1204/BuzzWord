package controller;

import apptemplate.AppTemplate;
import data.GameMode;
import data.User;
import javafx.scene.control.Button;

import java.io.*;
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.FileChooser;
import propertymanager.PropertyManager;

import static settings.AppPropertyType.SAVE_WORK_TITLE;
import static settings.AppPropertyType.WORK_FILE_EXT;
import static settings.AppPropertyType.WORK_FILE_EXT_DESC;

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
        System.out.println("a new user has been created with the username " + currentUser.getUserName() + " and password " + currentUser.getUserPassWord());
        saveUserInformation();
    }
    public void saveUserInformation()
    {
        ObjectMapper jsonWriter = new ObjectMapper();
        File selectedFile = new File("./Hangman/src/data/" + currentUser.getUserName() + ".json");
        try {
            jsonWriter.writeValue(selectedFile, currentUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void completedLevel(String gamemode, int level)
    {
        currentUser.levelCompleted(gamemode, level);
    }
    public String generateRandomWordFromFile(String wordBankName, int wordBankSize) throws IOException {
        FileInputStream fis = new FileInputStream("Hangman/resources/words/" + wordBankName + ".txt");

        //Construct BufferedReader from InputStreamReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        Random random = new Random();
        String line = null;
        int search = random.nextInt(wordBankSize);
        for(int i = 0; i < search; i++)
        {
            line = br.readLine();
        }
        System.out.println(line);


        br.close();
        return line;
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
