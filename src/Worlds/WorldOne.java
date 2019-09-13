package Worlds;

import Game.Entities.Static.Apple;
import Game.Entities.Static.Star;
import Main.Handler;

import java.awt.*;
import java.util.Random;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class WorldOne extends WorldBase{

    public WorldOne (Handler handler) {
        super(handler);

        //has to be a number bigger than 20 and even
        GridWidthHeightPixelCount = 60;
        GridPixelsize = (780/GridWidthHeightPixelCount); // Changed to 780 for display to be exact
        playerLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
        appleLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];
        starLocation = new Boolean[GridWidthHeightPixelCount][GridWidthHeightPixelCount];

    }

    @Override
    public void tick() {
        super.tick();
        player.tick();
        if(!appleOnBoard){
            appleOnBoard=true;
            int appleX = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1);
            int appley = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1);

            // Change coordinates until one is selected in which the player isn't standing
            boolean goodCoordinates=false;
            do{
                if(!handler.getWorld().playerLocation[appleX][appley] && !handler.getWorld().starLocation[appleX][appley]){
                    goodCoordinates=true;
                }
            }while(!goodCoordinates);

            apple = new Apple(handler,appleX,appley,true,false);
            appleLocation[appleX][appley]=true; 
        }
        if (!starOnBoard && handler.getWorld().player.steps>200) {
        	 starOnBoard=true;
             int starX = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1);
             int starY = new Random().nextInt(handler.getWorld().GridWidthHeightPixelCount-1);

             // Change coordinates until one is selected in which the player isn't standing
             boolean goodCoordinates=false;
             do{
                 if(!handler.getWorld().playerLocation[starX][starY] && !handler.getWorld().starLocation[starX][starY]){
                     goodCoordinates=true;
                 }
             }while(!goodCoordinates);

             star = new Star(handler,starX,starY);
             starLocation[starX][starY]=true; 
        	
        }
    }

    @Override
    public void render(Graphics g){
        super.render(g);
        player.render(g,playerLocation);
    }

}
