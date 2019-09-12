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

    public OptionState(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUimanager(uiManager);

        uiManager.addObjects(new UIImageButton(350, 720, 120, 56, Images.Resume, () -> {
            handler.getMouseManager().setUimanager(null);
            State.setState(handler.getGame().pauseState);
        }));
        uiManager.addObjects(new UIImageButton(265, 322, 40, 40, Images.OptionsB, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getWorld().player.snakeColor = new Color(64,200,97);
        }));
        uiManager.addObjects(new UIImageButton(114, 372, 40, 40, Images.OptionsB, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getWorld().player.snakeColor = new Color(82,219,255);
        }));
        uiManager.addObjects(new UIImageButton(265, 422, 40, 40, Images.OptionsB, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getWorld().player.snakeColor = Color.white;
        }));
        uiManager.addObjects(new UIImageButton(114, 474, 40, 40, Images.OptionsB, () -> {
            handler.getMouseManager().setUimanager(null);
            handler.getWorld().player.snakeColor = Color.red;
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
        g.drawImage(Images.Option,0,0,800,800,null);
        
        uiManager.Render(g);

    }
}
