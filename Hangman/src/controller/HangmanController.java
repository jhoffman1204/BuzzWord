package controller;

import apptemplate.AppTemplate;
import data.GameData;
import data.GameMode;
import data.GameModeLevel;
import data.User;
import gui.Workspace;
import javafx.scene.control.Button;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.FileChooser;
import propertymanager.PropertyManager;
import ui.AppMessageDialogSingleton;

import static java.lang.System.out;
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
    private String currentGameModeString;
    private int currentGameModeLevel;

    private String[] animalsVocab;
    private int animalsVocabLength = 133;
    private String[] countriesVocab;
    private int countriesVocabLength = 37;
    private String[] generalVocab;
    private int generalVocabLength = 151;

    Random random = new Random();
    public HangmanController(AppTemplate appTemplate, Button gameButton) {
        this(appTemplate);
    }

    public HangmanController(AppTemplate appTemplate) {
        this.appTemplate = appTemplate;
    }


    public User getCurrentUser()
    {
        return this.currentUser;
    }
    public GameModeLevel getCurrentGameMode(String gameModeTitle, int level)
    {
        return currentUser.getGamemodes().get(gameModeTitle).getSpecificGameModeLevel(level);
    }
    public void scanBoardForWords()
    {
        Workspace workspace = (Workspace)appTemplate.getWorkspaceComponent();
        //workspace.detectWords(animalsVocab,"");
        if(workspace.getCurrentGameMode().equalsIgnoreCase("animals"))
        {
            workspace.detectWords(animalsVocab,"");
        }
        if(workspace.getCurrentGameMode().equalsIgnoreCase("general"))
        {
            workspace.detectWords(generalVocab,"");
        }
        if(workspace.getCurrentGameMode().equalsIgnoreCase("countries"))
        {
            System.out.println("COUNTRIES DETECTED");
            workspace.detectWords(countriesVocab,"");
        }
        workspace.printWordsDetected();
    }
    public String encrpyPassword(String password)
    {
        String pass = password;
        StringBuffer sb = null;
        try {
            MessageDigest a = MessageDigest.getInstance("MD5");
            a.update(password.getBytes());
            byte[] b = a.digest();
            sb= new StringBuffer();
            for(byte b1 : b)
            {
                sb.append(Integer.toHexString(b1 & 0xff).toString());
            }
        }
        catch(NoSuchAlgorithmException e) {
            System.out.println("there was a problem with the Message Digest method");
        }
        return sb.toString();
    }
    public void loadWordBanks()
    {
        try {
            animalsVocab = generateWordBank("AnimalsVocab", animalsVocabLength);
            countriesVocab = generateWordBank("CountriesVocab", countriesVocabLength);
            generalVocab = generateWordBank("generalVocab", generalVocabLength);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getTargetScore(String level)
    {
        currentUser.getGamemodes().get(currentGameModeString).getSpecificGameModeLevel(Integer.parseInt(level)).setRequiredPoints(20 + Integer.parseInt(level)* 10);
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        int targetScore = currentUser.getGamemodes().get(currentGameModeString).getSpecificGameModeLevel(Integer.parseInt(level)).getRequiredPoints();
        return targetScore;

    }
    public void setCurrentGameModeString(String gameMode)
    {
        this.currentGameModeString = gameMode;
    }
    public void updateLevelCurrentlyOn()
    {
        Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
        int counter = 1;
        for(int i = 0; i < 2; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                workspace.disableLevelSelectionNode(i,j);
                if(currentUser.getGamemodes().get(this.currentGameModeString).getSpecificGameModeLevel(counter).isCompleted() == true)
                {
                    counter++;
                }
            }
            workspace.enableLevelSelectionNode(counter);
        }
//        for(int i = 0; i < 2; i++)
//        {
//            for(int j = 0; j < 4; j++)
//            {
//                if(currentUser.getGamemodes().get(this.currentGameModeString).getSpecificGameModeLevel(counter).getPersonalBest() != 0)
//                {
//                    workspace.enableLevelSelectionNode(i,j);
//                }
//                counter++;
//            }
//        }
    }
    public void createNewUser(String username, String password)
    {
        User newUser = new User(username,password);
        currentUser = newUser;
        out.println("a new user has been created with the username " + currentUser.getUserName() + " and password " + currentUser.getUserPassWord());
        saveUserInformation();
    }
    public void changeUserInformation(String username, String password)
    {
        this.currentUser.setUserName(username);
        this.currentUser.setUserPassWord(password);
        saveUserInformation();
    }
    public void saveUserInformation()
    {
        ObjectMapper jsonWriter = new ObjectMapper();
        File selectedFile = new File("./Hangman/src/data/" + currentUser.getUserName() + ".json");
        try {
            jsonWriter.writerWithDefaultPrettyPrinter().writeValue(selectedFile, currentUser);
            out.println("gave saved");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean loadUserInformation(String username, String password)
    {
        ObjectMapper jsonLoader = new ObjectMapper();
        User tempUser = null;
        try {
            currentUser = jsonLoader.readValue(new File(".\\Hangman\\src\\data\\" + username + ".json"), User.class);
            if(password.contains(currentUser.getUserPassWord())) {
                Workspace workspace = (Workspace) appTemplate.getWorkspaceComponent();
                workspace.enableLevelSelectionNode(1);
            }
            else
            {
                throw new IOException();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            AppMessageDialogSingleton.getSingleton().show("Invalid Username","That username/password combination does not exist, select another");
            return false;
        }
    }


    public void completedLevel(String gamemode, int level, int score)
    {
        currentUser.levelCompleted(gamemode, level, score);
    }
    public String generateRandomWordFromFile(String wordBankName, int wordLength) throws IOException {
        String word = "";
        int randomWordIndex = 0;
        if(wordBankName.equalsIgnoreCase("Animals"))
        {
            randomWordIndex = random.nextInt(animalsVocabLength);
            word = animalsVocab[randomWordIndex];
        }
        else if(wordBankName.equalsIgnoreCase("Countries"))
        {
            randomWordIndex = random.nextInt(countriesVocabLength);
            word = countriesVocab[randomWordIndex].toLowerCase();
        }
        else if(wordBankName.equalsIgnoreCase("general"))
        {
            randomWordIndex = random.nextInt(generalVocabLength);
            word = generalVocab[randomWordIndex];
        }
        try
        {
            if(word.length() != wordLength)
            {
                System.out.println("word lengths did not match");
                return generateRandomWordFromFile(wordBankName,wordLength);
            }
        }
        catch(Exception e)
        {
            System.out.println("There was a problem");
            System.out.println(word.length());
            System.out.println(wordLength);
        }
        return word;
    }
    public String[] generateWordBank(String fileName, int fileLength) throws IOException {
        FileInputStream fis = new FileInputStream("Hangman/resources/words/" + fileName + ".txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String[] wordBank = new String[fileLength];
        for(int i = 0; i < fileLength; i++)
        {
            wordBank[i] = br.readLine();
        }
        return wordBank;
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
