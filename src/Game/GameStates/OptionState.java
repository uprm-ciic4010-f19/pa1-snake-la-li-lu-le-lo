package Game.GameStates;

import Main.Handler;
import Resources.Images;
import UI.UIImageButton;
import UI.UIManager;

import java.awt.*;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class OptionState extends State {

    private int count = 0;
    private UIManager uiManager;
    public Color nured = new Color(230,0,18);
    public Color nugreen = new Color(64,200,97);
    public Color nublue = new Color(82,219,255);

    public OptionState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(330, 700, 120, 56, Images.Resume, () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().pauseState);
        }));
        uiManager.addObjects(new UIImageButton(260, 312, 40, 40, Images.OptionsB, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getWorld().player.snakeColor = nugreen;
        }));
        uiManager.addObjects(new UIImageButton(110, 366, 40, 40, Images.OptionsB, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getWorld().player.snakeColor = nublue;
        }));
        uiManager.addObjects(new UIImageButton(260, 416, 40, 40, Images.OptionsB, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getWorld().player.snakeColor = Color.white;
        }));
        uiManager.addObjects(new UIImageButton(110, 460, 40, 40, Images.OptionsB, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getWorld().player.snakeColor = nured;
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
        g.drawImage(Images.Option,0,0,handler.getWidth(),handler.getHeight(),null);
        
        uiManager.Render(g);

    }
}
