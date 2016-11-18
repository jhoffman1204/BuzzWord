package data;

import java.util.HashMap;

/**
 * Created by James on 11/15/2016.
 */
public class GameMode
{
    private HashMap wordBank = new HashMap(100);
    private String gameModeName;
    private GameModeLevel[] gameModeLevels;
    private int highestLevelCompleted;

    public GameMode()
    {

    }
    public GameMode(String gameModeName, int highestLevelCompleted)
    {
        this.gameModeName = gameModeName;
        this.highestLevelCompleted = highestLevelCompleted;
        gameModeLevels = new GameModeLevel[8];
        initializeGameModeLevels();
    }
    public void initializeGameModeLevels()
    {
        for(int i = 0; i < gameModeLevels.length; i++)
        {
            this.gameModeLevels[i] = new GameModeLevel();
        }
    }

    public int getHighestLevelCompleted() {
        return highestLevelCompleted;
    }

    public void setHighestLevelCompleted(int highestLevelCompleted) {
        this.highestLevelCompleted = highestLevelCompleted;
    }

    public GameModeLevel[] getGameModeLevels() {
        return gameModeLevels;
    }

    /**
     * maker sure to user the name of the level and not the index for simplicity sake
     * @param level
     *      the level you want to access, do not use 0
     * @return
     */
    public GameModeLevel getSpecificGameModeLevel(int level)
    {
        return gameModeLevels[level-1];
    }

    public void setGameModeLevels(GameModeLevel[] gameModeLevels) {
        this.gameModeLevels = gameModeLevels;
    }

    public HashMap getWordBank() {
        return wordBank;
    }

    public void setWordBank(HashMap wordBank) {
        this.wordBank = wordBank;
    }
    public String getGameModeName() {
        return gameModeName;
    }

    public void setGameModeName(String gameModeName) {
        this.gameModeName = gameModeName;
    }


}
