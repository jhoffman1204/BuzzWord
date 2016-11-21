package data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by James on 11/15/2016.
 */
public class User
{
    private String userName;
    private String userPassWord;
    //private String levelsCompletedBinary = "test";
    //private boolean[] levelsCompleted;
    private GameModes gamemodes;


    public User()
    {

    }
    public User(@JsonProperty("userName")String userName,@JsonProperty("userPassWord")String userPassWord,@JsonProperty("levelsCompletedBinary")String levelsCompletedBinary
            ,@JsonProperty("gamemodes")GameModes gameModes)
    {
        this.userName = userName;
        this.userPassWord = userPassWord;
        this.gamemodes = new GameModes();
        //this.levelsCompleted = initializeLevelsCompleted();
    }
    public User(String userName, String userPassWord)
    {
        this.userName = userName;
        this.userPassWord = userPassWord;
        this.gamemodes = new GameModes();

    }
//    public User(@JsonProperty("userName")String userName,@JsonProperty("userPassWord")String userPassWord,@JsonProperty("levelsCompleted")boolean[] levelsCompleted)
//    {
//        this.userName = userName;
//        this.userPassWord = userPassWord;
//        this.gamemodes = new GameModes();
//        //this.levelsCompleted = initializeLevelsCompleted();
//    }
//    public boolean[] initializeLevelsCompleted()
//    {
//        levelsCompleted = new boolean[8];
//        for(int i=0; i < levelsCompleted.length ; i++)
//        {
//            levelsCompleted[i] = false;
//        }
//        levelsCompleted[0] = true;
//        return levelsCompleted;
//    }
//    public boolean[] getLevelsCompleted()
//    {
//        return this.levelsCompleted;
//    }
    public String getUserName() {
        return userName;
    }
//
//    public String getLevelsCompletedBinary() {
//        return levelsCompletedBinary;
//    }

//    public void setLevelsCompletedBinary(String levelsCompletedBinary) {
//        this.levelsCompletedBinary = levelsCompletedBinary;
//    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassWord() {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord) {
        this.userPassWord = userPassWord;
    }

    public GameModes getGamemodes() {
        return gamemodes;
    }

    public void setGamemodes(GameModes gamemodes) {
        this.gamemodes = gamemodes;
    }

    public boolean isLevelCompleted(String gameMode, int level)
    {
        return gamemodes.get(gameMode).getSpecificGameModeLevel(level).isCompleted();
    }
    public void levelCompleted(String gameMode, int level, int score)
    {
        gamemodes.get(gameMode).getSpecificGameModeLevel(level).setCompleted(true);
        gamemodes.get(gameMode).getSpecificGameModeLevel(level).setPersonalBest(score);

    }
}
