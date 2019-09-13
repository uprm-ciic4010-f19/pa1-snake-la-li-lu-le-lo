package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class PauseState extends State {

    private int count = 0;
    private UIManager uiManager;

    public PauseState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(171, 702, 120, 56, Images.Options, () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().optionState);
        }));

        uiManager.addObjects(new UIImageButton(484, 702, 120, 56, Images.BTitle, () -> {
        	handler.getMouseManager().setUimanager(null);
            System.exit(0);
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
        g.drawImage(Images.Pause,0,0,handler.getWidth(),handler.getHeight(),null);
        g.setColor(new Color(198,240,41));
        g.setFont(new Font("Courier New",1,60));
        g.drawString("" + handler.getWorld().player.score, 426, 612);
        uiManager.Render(g);

    }
}
