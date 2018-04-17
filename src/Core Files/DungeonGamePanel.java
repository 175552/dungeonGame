import javax.swing.Timer;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class DungeonGamePanel extends GamePanels implements ActionListener{

	Timer updater = new Timer(1000/World.framerate, this);

	BufferedImage worldImage, playerImage;

	Player p1 = new Player();

	Action upStart, upEnd, downStart, downEnd, leftStart, leftEnd, rightStart, rightEnd;
	Action upAS, upAE, downAS, downAE, leftAS, leftAE, rightAS, rightAE;

	Set<String> keysList = new HashSet<String>();

	ArrayList<Enemies> enemyList = new ArrayList<Enemies>();

	DungeonGamePanel(){
		createPanel();
		Cell.idToHashMap();							//Sets up the hashmap for the world Cells
		Cell.idToAnimationMap();
		World.setup();								//Sets player in room in middle of world
		drawWorld();								//Draws the world image once, to prevent lag
		generateEnemies();							//Creates enemies at positions on map
		p1.setupEffectTimers();						//Adds action commands to the internal timers for the player

		updater.start();							//Starts the actual timer for redrawing and taking in inputs

////////////////////////////////////////////////////////////////////////////////////////////Create actions for movement
		upStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.movesUp = true;
			}
		};
		upEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.movesUp = false;
			}
		};
		downStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.movesDown = true;
			}
		};
		downEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.movesDown = false;
			}
		};
		leftStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.movesLeft = true;
			}
		};
		leftEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.movesLeft = false;
			}
		};
		rightStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.movesRight = true;
			}
		};
		rightEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.movesRight = false;
			}
		};
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////Actions for attacking
		upAS = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.attacksUp = true;
			}
		};
		upAE = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.attacksUp = false;
			}
		};
		downAS = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.attacksDown = true;
			}
		};
		downAE = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.attacksDown = false;
			}
		};
		leftAS = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.attacksLeft = true;
			}
		};
		leftAE = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.attacksLeft = false;
			}
		};
		rightAS = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.attacksRight = true;
			}
		};
		rightAE = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				p1.attacksRight = false;
			}
		};

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////Keybinds for movement
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "Up Start");
		getActionMap().put("Up Start", upStart);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "Up End");
		getActionMap().put("Up End", upEnd);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "Down Start");
		getActionMap().put("Down Start", downStart);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "Down End");
		getActionMap().put("Down End", downEnd);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "Left Start");
		getActionMap().put("Left Start", leftStart);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "Left End");
		getActionMap().put("Left End", leftEnd);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "Right Start");
		getActionMap().put("Right Start", rightStart);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "Right End");
		getActionMap().put("Right End", rightEnd);

		////////////////////////////////////////////////////////////////////////////////////////////////Keybinds for attacking

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "Up AStart");
		getActionMap().put("Up AStart", upAS);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "Up AEnd");
		getActionMap().put("Up AEnd", upAE);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "Down AStart");
		getActionMap().put("Down AStart", downAS);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Down AEnd");
		getActionMap().put("Down AEnd", downAE);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "Left AStart");
		getActionMap().put("Left AStart", leftAS);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "Left AEnd");
		getActionMap().put("Left AEnd", leftAE);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "Right AStart");
		getActionMap().put("Right AStart", rightAS);

		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "Right AEnd");
		getActionMap().put("Right AEnd", rightAE);
	}





////////////////////////////////////////////////////////////////////////////////////////////////Action Listener for updating stuff.
	public void actionPerformed(ActionEvent e){
////////////////////////////////////////////////////////////////////////////////////////////////For normal movement

		p1.inertiaSetup(); ///////////Sets player's booleans for moving in the x and y to false.

		if(p1.movesUp){			////////////Handles taking in input for movement
			p1.moveUp();
		}
		else if(p1.movesDown){
			p1.moveDown();
		}
		if(p1.movesLeft){
			p1.moveLeft();
		}
		else if(p1.movesRight){
			p1.moveRight();
		}

		p1.move();			//Handles movement and stops player if it hits a wall


////////////////////////////////////////////////////////////////////////////////////////////For exact movement to line up with obstacles
		if(p1.movesUp && !p1.checkUp()){
			p1.moveUpDis((p1.getY() - p1.getYOffset()) % World.cellSize);
		}
		else if(p1.movesDown && !p1.checkDown()){
			if((p1.getY() + p1.getYOffset()) % World.cellSize != 0)
				p1.moveDownDis(World.cellSize - ((p1.getY() + p1.getYOffset()) % World.cellSize));
		}
		if(p1.movesLeft && !p1.checkLeft()){
			p1.moveLeftDis((p1.getX() - p1.getXOffset()) % World.cellSize);
		}
		else if(p1.movesRight && !p1.checkRight()){
			if((p1.getX() + p1.getXOffset()) % World.cellSize != 0)
				p1.moveRightDis(World.cellSize - ((p1.getX() + p1.getXOffset()) % World.cellSize));
		}
/////////////////////////////////////////////////////////////////////////////////////////Enemy commands
		for(int i = 0; i < enemyList.size(); i++){ //Iterates through every enemy on screen
			enemyList.get(i).checkCurrentPos(p1);
			int[] bounds = enemyList.get(i).getBounds();	//Gets hitbox of enemy
			int[] pBounds = p1.getBounds();					//Player hitbox

			/////////////Methods to be run when enemy is hit
			if(p1.getAttackHitbox().intersects(bounds[0] - bounds[2], bounds[1] - bounds[3], bounds[2] * 2, bounds[3] * 2)){
				enemyList.get(i).loseHP(p1.getWeapon().getDamage());	//Do damage to hit enemy
				enemyList.get(i).hit(p1);
			}

			enemyList.get(i).chasePlayer(p1);	//Have enemy chase the player

			if(enemyList.get(i).isEntityInRange(p1)){
				enemyList.get(i).attackPlayer(p1);
				////////////////////Methods to run when player is hit
				if(enemyList.get(i).getAttackHitbox().intersects(pBounds[0] - pBounds[2],
					pBounds[1] - pBounds[3], pBounds[2] * 2, pBounds[3] * 2)){
					p1.loseHP(enemyList.get(i).getWeapon().getDamage());
					p1.hit(enemyList.get(i));
				}
			}
			if(enemyList.get(i).getHP() <= 0){	//If an enemy dies, remove it from the arraylist
				enemyList.remove(i);
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////Combat
		if(p1.attacksUp){
			p1.attackUp();
		}
		else if(p1.attacksDown){
			p1.attackDown();
		}
		else if(p1.attacksLeft){
			p1.attackLeft();
		}
		else if(p1.attacksRight){
			p1.attackRight();
		}
		if(!p1.attacksUp && !p1.attacksDown && !p1.attacksLeft && !p1.attacksRight){
			p1.attackHitbox = new Area(new Rectangle2D.Double(0, 0, 0, 0));
		}
		repaint();
		if(p1.getHP() < 0){
			updater.stop();
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////Enemy Generation


	void generateEnemies(){
		for(int a = 0; a < World.worldHeight; a++){
			for(int i = 0; i < World.worldLength; i++){
				if(World.roomIDs[i][a].spawnCheck()){
					enemyList.add(new Enemies(i*World.cellSize + (World.cellSize/2), a*World.cellSize + (World.cellSize/2)));
				}
			}
		}
		for(int i = 0; i < enemyList.size(); i++){
			enemyList.get(i).setupEffectTimers();
		}
	}


//////////////////////////////////////////////////////////////////////////////////////////////////Draw player and world image
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if(!(p1.getHP() < 0)){
			g.drawImage(worldImage, 0, 0, World.cellSize * World.worldLength, World.cellSize * World.worldHeight, this);
			//World.getCurrentRoom().drawWorld(g, this);
			p1.getAnimation().drawAnimation(g, p1.getX() - p1.getXOffset(), p1.getY() - p1.getYOffset(), 50, 50, this);
			drawEnemies(g);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setColor(Color.white);
			if(p1.attacksUp || p1.attacksLeft || p1.attacksRight || p1.attacksDown){
				p1.drawAttack(g);
			}
			if(p1.checkIfHit()){
				p1.showHPBar(g);
			}
			for(int i = 0; i < enemyList.size(); i++){
				Entity e = enemyList.get(i);
				if(enemyList.get(i).checkIfHit()){
					e.showHPBar(g);
				}
				if(enemyList.get(i).isEntityInRange(p1)){
					e.drawAttack(g);
				}
			}
		}
		else{
			p1.die(g);
		}
	}




	private void drawWorld(){
		worldImage = new BufferedImage(World.cellSize * World.worldLength,
		World.cellSize * World.worldHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = worldImage.createGraphics();
		int store = World.cellSize;
		System.out.println(World.xRoomCount/2 + ", " + World.currentY);
		try{
			Scanner input = new Scanner(new File(World.rooms[World.currentX][World.currentY].getFilePath()));
			for(int a = 0; a < World.worldHeight; a++){					//Draw images from the map buttons into a larger image.
				for(int i = 0; i < World.worldLength; i++){
					try{
						int temp = input.nextInt();
						g2.drawImage(Cell.idToTexture.get(temp), i*store, a*store, null);
						World.roomIDs[i][a] = new Cell(temp);
					}catch(Exception e){System.out.println("World draw error: " + e);}
				}
			}

		}catch(Exception e){System.out.println("World file read error.");}
	}



	private void drawEnemies(Graphics g){								//Encapsulated method for drawing the enemise
		for(int i = 0; i < enemyList.size(); i++){
			Enemies temp = enemyList.get(i);
			temp.getAnimation().drawAnimation(g, temp.getX() - temp.getXOffset(), temp.getY() - temp.getYOffset(), 50, 50, this);
		}
	}

}
