package Game.Entities.Dynamic;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import Game.GameStates.State;
import Main.Handler;

/**
 * Created by AlexVR on 7/2/2018.
 */
public class Player {

	public int lenght;
	public boolean justAte;
	public boolean justAteBad;
	public boolean starPower;
	private Handler handler;

	public int xCoord;
	public int yCoord;

	public double speed; 
	public int steps; 
	public int score;
	public int move;
	public Color snakeColor;

	public int moveCounter;

	public String direction;

	public Player(Handler handler){
		this.handler = handler;
		xCoord = 0;
		yCoord = 0;
		moveCounter = 0;
		direction= "Right";
		justAte = false;
		justAteBad = false; 
		lenght= 1;
		speed = 6;
		score=0;
		steps = 0;
		move = 0; 
		snakeColor = new Color(64,200,97);
		starPower = false; // Power up that grants invincibility
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

		if (steps>240) {
			handler.getWorld().apple.Good = false; // Apple becomes rotten
		}
		if (steps>60) {
			starPower = false; // starPower in effect for 60 steps
		}
		if (move<speed) {
			move++;
		}

		if(move >= speed) {
			if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_UP) && direction!="Down"){
				direction="Up";
				move = 0;
			}else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_DOWN) && direction!="Up"){
				direction="Down";
				move = 0;
			}else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_LEFT) && direction!= "Right"){
				direction="Left";
				move = 0;
			}else if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_RIGHT) && direction !="Left"){
				direction="Right";
				move = 0;
			}
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_N)){
			handler.getWorld().body.addFirst(new Tail(x, y,handler)); // Adds a tail
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ADD) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_EQUALS)){
			speed-=0.1; // Increases speed
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_SUBTRACT) || handler.getKeyManager().keyJustPressed(KeyEvent.VK_MINUS)){
			speed+=0.1; // Decreases speed
		}if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)) {
			State.setState(handler.getGame().pauseState); // Command to pause
		}
	}

	public void checkCollisionAndMove(){
		handler.getWorld().playerLocation[xCoord][yCoord]=false;
		int x = xCoord;
		int y = yCoord;
		switch (direction){
		case "Left":
			if(xCoord==0){
				handler.getWorld().player.xCoord = handler.getWorld().GridWidthHeightPixelCount; // Snake teleports to other side of grid
			}if (handler.getWorld().playerLocation[xCoord-1][yCoord] && !starPower) {
				kill(); // Game Over if snake collides with itself and starPower is false
			}else{
				xCoord--;
			}
			break;
		case "Right":
			if(xCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				handler.getWorld().player.xCoord = -1;
			}if (handler.getWorld().playerLocation[xCoord+1][yCoord] && !starPower) {
				kill();
			}else{
				xCoord++;
			}
			break;
		case "Up":
			if(yCoord==0){
				handler.getWorld().player.yCoord = handler.getWorld().GridWidthHeightPixelCount;
			}if (handler.getWorld().playerLocation[xCoord][yCoord-1] && !starPower) {
				kill();
			}else{
				yCoord--;
			}
			break;
		case "Down":
			if(yCoord==handler.getWorld().GridWidthHeightPixelCount-1){
				handler.getWorld().player.yCoord = -1;
			}if (handler.getWorld().playerLocation[xCoord][yCoord+1]  && !starPower) {
				kill();
			}else{
				yCoord++;
			}
			break;
		}
		handler.getWorld().playerLocation[xCoord][yCoord]=true;


		if(handler.getWorld().appleLocation[xCoord][yCoord] || handler.getWorld().starLocation[xCoord][yCoord]) {
			Eat();
		}
		if (steps>5) {
			handler.getWorld().player.justAte = false; // justAte is only true for 5 steps
			handler.getWorld().player.justAteBad = false;
		}

		if(!handler.getWorld().body.isEmpty()) {
			handler.getWorld().playerLocation[handler.getWorld().body.getLast().x][handler.getWorld().body.getLast().y] = false;
			handler.getWorld().body.removeLast();
			handler.getWorld().body.addFirst(new Tail(x, y,handler));
		}
	}

	public void render(Graphics g, Boolean[][] playeLocation){
		for (int i = 0; i < handler.getWorld().GridWidthHeightPixelCount; i++) {
			for (int j = 0; j < handler.getWorld().GridWidthHeightPixelCount; j++) {
				int R = ThreadLocalRandom.current().nextInt(0, 256);
				int G = ThreadLocalRandom.current().nextInt(0, 256);
				int B = ThreadLocalRandom.current().nextInt(0, 256);
				
				

				Color pinku = new Color(255,110,199);
				g.setColor(Color.white);
				g.setFont(new Font("Courier New", 1,20)); // Score Font
				g.drawString("Score: "+this.score,340,30); // Score Display

				if(playeLocation[i][j]){
					if (handler.getWorld().player.justAteBad) {
						g.setColor(new Color(G,G,G)); // Snake will look like static noise after eating rotten apple
					}else if (handler.getWorld().player.justAte) {
						g.setColor(new Color(99,255,32)); // Snake will turn green after eating apple
					}else if (handler.getWorld().player.starPower) {
						g.setColor(new Color(R,G,B));
					}else {
						g.setColor(handler.getWorld().player.snakeColor);
					}
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize-2,
							handler.getWorld().GridPixelsize-2);
				}
				if(handler.getWorld().appleLocation[i][j]) {
					if(handler.getWorld().apple.isGood()) {
						g.setColor(pinku);
					}else{
						g.setColor(Color.yellow); // Change apple color when apple is bad
					}
					g.fillRect((i*handler.getWorld().GridPixelsize),
							(j*handler.getWorld().GridPixelsize),
							handler.getWorld().GridPixelsize,
							handler.getWorld().GridPixelsize);
				}
				if(handler.getWorld().starLocation[i][j]) {
					g.setColor(new Color(R,G,B));
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
		if (handler.getWorld().starLocation[xCoord][yCoord]) { // Gives player starPower
			handler.getWorld().player.starPower = true; 
			handler.getWorld().starLocation[xCoord][yCoord]=false;
			handler.getWorld().starOnBoard=false;
		}
		if (handler.getWorld().appleLocation[xCoord][yCoord]) {
			handler.getWorld().appleLocation[xCoord][yCoord]=false;
			handler.getWorld().appleOnBoard=false;
			handler.getWorld().player.justAte = true;
			if (!handler.getWorld().apple.isGood()) {
				shed(); // Snake will lose segment if apple is bad
				speed+= 0.8; // Decreases speed relative to (student number + 1)/10
				handler.getWorld().player.justAteBad = true;
				score = (int)(score - Math.sqrt(2*score+1)); // Will return floored score

			}else {
				if (speed>2) {
					speed-= 0.8; // Increases speed relative to (student number + 1)/10
					System.out.println(speed);
				}
				score = (int)(score + Math.sqrt(2*score+1)); // Will return floored score
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
