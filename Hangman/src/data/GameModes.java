package data;

import java.util.HashMap;

/**
 * Created by James on 11/15/2016.
 */
public class GameModes extends HashMap<String,GameMode>
{
    GameModes()
    {
        this.initializeGameModes();
    }
    public void initializeGameModes()
    {
        GameMode general = new GameMode("general",1);
        this.put("general",general);

        GameMode animals = new GameMode("animals",1);
        this.put("animals",animals);

        GameMode countries = new GameMode("countries", 1);
        this.put("countries",countries);
    }
}
