package Game.Entities.Dynamic;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;

import Game.GameStates.State;
import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

	public int lenght;
	public boolean justAte;
	private Handler handler;

	public int xCoord;
	public int yCoord;
	public int speed; 
	public int steps; 
	public int score;

	public int moveCounter;

	public String direction;

	public Player(Handler handler){
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		moveCounter = 0;
		direction= "Right";
		justAte = false;
		lenght= 1;
		speed = 4;
		score=0;
		steps = 0;
	}

	public void tick(){
		int x = xCoord;
		int y = yCoord;
		moveCounter++;

		if(moveCounter>=speed) {
			checkCollisionAndMove();
			moveCounter=0;
			steps++;
		}
		if (steps>100) {
			handler.getWorld().apple.Good = false; // Apple becomes rotten
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) && direction!="Down"){
			direction="Up";
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) && direction!="Up"){
			direction="Down";
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) && direction!= "Right"){
			direction="Left";
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) && direction !="Left"){
			direction="Right";
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
			handler.getWorld().body.addFirst(new Tail(x, y,handler));
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ADD)){
			speed--; // Supposed to make fast
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_SUBTRACT)){
			speed++; // Supposed to make slow
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
			// Command to pause
			State.setState(handler.getGame().pauseState);	
		}


	}

	public void checkCollisionAndMove(){
		handler.getWorld().playerLocation[xCoord][yCoord]=false;
		int x = xCoord;
		int y = yCoord;
		// Snake will die if it collides with itself ***
		switch (direction){
		case "Left":
			if(xCoord==0){
				handler.getWorld().player.xCoord = 59;
			}if (handler.getWorld().playerLocation[xCoord-1][yCoord]) {
				kill();
			}else{
				xCoord--;
			}
			break;
		case "Right":
			if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				handler.getWorld().player.xCoord = 0;
			}if (handler.getWorld().playerLocation[xCoord+1][yCoord]) {
				kill();
			}else{
				xCoord++;
			}
			break;
		case "Up":
			if(yCoord==0){
				handler.getWorld().player.yCoord = 59;
			}if (handler.getWorld().playerLocation[xCoord][yCoord-1]) {
				kill();
			}else{
				yCoord--;
			}
			break;
		case "Down":
			if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				handler.getWorld().player.yCoord = 0;
			}if (handler.getWorld().playerLocation[xCoord][yCoord+1]) {
				kill();
			}else{
				yCoord++;
			}
			break;
		}
		handler.getWorld().playerLocation[xCoord][yCoord]=true;


		if(handler.getWorld().appleLocation[xCoord][yCoord]){
			Eat();
		}

		if(!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y,handler));
		}

	}

	public void render(Graphics g, Boolean[][] playeLocation){
		Random r = new Random();
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

				Color dgreen = new Color(64,200,97);
				Color pinku = new Color(255,110,199);
				g.setColor(Color.white);
				g.setFont(new Font("Courier New", 1,20));// font of game
				g.drawString("Score: "+this.score,340,30);// message of score

				if(playeLocation[i][j]){
					g.setColor(dgreen);
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize-2,
							handler.getWorld().GridPixelsize-2);
				}
				if(handler.getWorld().appleLocation[i][j]) {
					if(handler.getWorld().apple.isGood()){
						g.setColor(pinku);
					}else{
						g.setColor(Color.yellow); // Change apple color when apple is bad
					}
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}
			}
		}
	}

	public void Eat(){
		steps = 0; 
		lenght++;
		Tail tail= null;
		handler.getWorld().appleLocation[xCoord][yCoord]=false;
		handler.getWorld().appleOnBoard=false;
		if (!handler.getWorld().apple.isGood()) {
			shed(); // Snake will lose segment if apple is bad
			this.score= (int)( this.score - Math.sqrt(2*this.score+1));
		}else {
			this.score= (int)( this.score + Math.sqrt(2*this.score+1));
			switch (direction){
			case "Left":
				if( handler.getWorld().body.isEmpty()){
					if(this.xCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
						tail = new Tail(this.xCoord+1,this.yCoord,handler);
					}else{
						if(this.yCoord!=0){
							tail = new Tail(this.xCoord,this.yCoord-1,handler);
						}else{
							tail =new Tail(this.xCoord,this.yCoord+1,handler);
						}
					}
				}else{
					if(handler.getWorld().body.getLast().x!=handler.getWorld().GridWidthHeightPixelCount-1){
						tail=new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler);
					}else{
						if(handler.getWorld().body.getLast().y!=0){
							tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler);
						}else{
							tail=new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler);

						}
					}

				}
				break;
			case "Right":
				if( handler.getWorld().body.isEmpty()){
					if(this.xCoord!=0){
						tail=new Tail(this.xCoord-1,this.yCoord,handler);
					}else{
						if(this.yCoord!=0){
							tail=new Tail(this.xCoord,this.yCoord-1,handler);
						}else{
							tail=new Tail(this.xCoord,this.yCoord+1,handler);
						}
					}
				}else{
					if(handler.getWorld().body.getLast().x!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
					}else{
						if(handler.getWorld().body.getLast().y!=0){
							tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
						}else{
							tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
						}
					}

				}
				break;
			case "Up":
				if( handler.getWorld().body.isEmpty()){
					if(this.yCoord!=handler.getWorld().GridWidthHeightPixelCount-1){
						tail=(new Tail(this.xCoord,this.yCoord+1,handler));
					}else{
						if(this.xCoord!=0){
							tail=(new Tail(this.xCoord-1,this.yCoord,handler));
						}else{
							tail=(new Tail(this.xCoord+1,this.yCoord,handler));
						}
					}
				}else{
					if(handler.getWorld().body.getLast().y!=handler.getWorld().GridWidthHeightPixelCount-1){
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord+1,handler));
					}else{
						if(handler.getWorld().body.getLast().x!=0){
							tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
						}else{
							tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
						}
					}

				}
				break;
			case "Down":
				if( handler.getWorld().body.isEmpty()){
					if(this.yCoord!=0){
						tail=(new Tail(this.xCoord,this.yCoord-1,handler));
					}else{
						if(this.xCoord!=0){
							tail=(new Tail(this.xCoord-1,this.yCoord,handler));
						}else{
							tail=(new Tail(this.xCoord+1,this.yCoord,handler));
						} System.out.println("Tu biscochito");
					}
				}else{
					if(handler.getWorld().body.getLast().y!=0){
						tail=(new Tail(handler.getWorld().body.getLast().x,this.yCoord-1,handler));
					}else{
						if(handler.getWorld().body.getLast().x!=0){
							tail=(new Tail(handler.getWorld().body.getLast().x-1,this.yCoord,handler));
						}else{
							tail=(new Tail(handler.getWorld().body.getLast().x+1,this.yCoord,handler));
						}
					}

				}
				break;
			}
			handler.getWorld().body.addLast(tail);
			handler.getWorld().playerLocation[tail.x][tail.y] = true;
		}
	}

	public void shed() { // Snake loses tail segment
		if (handler.getWorld().body.size()>0) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y]=false;
			handler.getWorld().body.removeLast();
		}
	}

	public void kill(){
		lenght = 0;
		//display game over screen
		State.setState(handler.getGame().loseState);
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {

				handler.getWorld().playerLocation[i][j]=false;

			}
		}
	}

	public boolean isJustAte() {
		return justAte;
	}

	public void setJustAte(boolean justAte) {
		this.justAte = justAte;
	}
}
