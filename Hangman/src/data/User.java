package data;

/**
 * Created by James on 11/15/2016.
 */
public class User
{
    private String userName;
    private String userPassWord;
    private GameModes gamemodes;

    public User()
    {

    }
    public User(String userName, String userPassWord)
    {
        this.userName = userName;
        this.userPassWord = userPassWord;
        this.gamemodes = new GameModes();
    }
    public String getUserName() {
        return userName;
    }

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
    public void levelCompleted(String gameMode, int level)
    {
            gamemodes.get(gameMode).getSpecificGameModeLevel(level).setCompleted(true);
    }
}
