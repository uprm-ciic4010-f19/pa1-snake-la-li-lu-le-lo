package Game.Entities.Static;

import Main.Handler;


public class Star {

    private Handler handler;

    public int xCoord;
    public int yCoord;
    
    public Star(Handler handler,int x, int y){
        this.handler=handler;
        this.xCoord=x;
        this.yCoord=y;
    }

}