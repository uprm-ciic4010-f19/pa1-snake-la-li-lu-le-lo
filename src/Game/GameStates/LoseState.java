package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class LoseState extends State {

    private int count = 0;
    private UIManager uiManager;

    public LoseState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(296, 700, 170, 50, Images.Restart, () -> {
        	handler.getMouseManager().setUimanager(null);
            handler.getGame().reStart();
            State.setState(handler.getGame().gameState);
        }));

    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        count++;
        if( count>=30){
            count=30;
        }
        if(handler.getKeyManager().pbutt && count>=30){
            count=0;

            State.setState(handler.getGame().gameState);
        }


    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.GameOver,0,0,handler.getWidth(),handler.getHeight(),null);
        g.setColor(new Color(198,255,41));
        g.setFont(new Font("Courier New",1,60));
        g.drawString("" + handler.getWorld().player.score, 426, 612);
        uiManager.Render(g);

    }
}
