package data;

/**
 * Created by James on 11/15/2016.
 */
public class GameModeLevel
{
    private int gameModeLevel;
    private int requiredPoints;
    private int timeAllowed;
    private int personalBest;
    private boolean completed;

    public GameModeLevel()
    {

    }
    public GameModeLevel(int gameModeLevel, int requiredPoints, int timeAllowed, int personalBest)
    {

    }
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public int getGameModeLevel() {
        return gameModeLevel;
    }

    public void setGameModeLevel(int gameModeLevel) {
        this.gameModeLevel = gameModeLevel;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public void setRequiredPoints(int requiredPoints) {
        this.requiredPoints = requiredPoints;
    }

    public int getTimeAllowed() {
        return timeAllowed;
    }

    public void setTimeAllowed(int timeAllowed) {
        this.timeAllowed = timeAllowed;
    }

    public int getPersonalBest() {
        return personalBest;
    }

    public void setPersonalBest(int personalBest) {
        this.personalBest = personalBest;
    }




}
