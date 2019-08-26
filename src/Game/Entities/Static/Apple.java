package Game.Entities.Static;


import Game.Entities.Dynamic.Player;
import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Apple {

    private Handler handler;

    public int xCoord;
    public int yCoord;
    public boolean Good;
    
    public boolean isGood() {
    		return Good;
    	}
    public Apple(Handler handler,int x, int y, boolean good){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
        this.Good=good;
    }


}
