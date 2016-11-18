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
        GameMode presidents = new GameMode("Presidents",1);
        this.put("presidents",presidents);

        GameMode science = new GameMode("Science",1);
        this.put("science",science);

        GameMode countries = new GameMode("Countries", 1);
        this.put("countries",countries);
    }
}
