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

	boolean arrUp, arrDown, arrLeft, arrRight;

	Action upStart, upEnd, downStart, downEnd, leftStart, leftEnd, rightStart, rightEnd;
	Action upAS, upAE, downAS, downAE, leftAS, leftAE, rightAS, rightAE;

	Set<String> keysList = new HashSet<String>();

	DungeonGamePanel(){
		createPanel();
		Cell.idToAnimationMap();												//Creates hashmap assigning animations to cells
		World.createRoomList();													//Creates a hashmap that handles which rooms have which doors
		World.createWorld(new File("../maps/worlds/worldTest.txt"));			//Creates the world of rooms from a file
		World.setup();															//Sets player in room in middle of world
		setupCurrentRoom();														//Creates the array of cells
		generateEnemies();														//Creates enemies at positions on map

		updater.start();												//Starts the actual timer for redrawing and taking in inputs

////////////////////////////////////////////////////////////////////////////////////////////Create actions for movement
		upStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				getPlayer().movesUp = true;
			}
		};
		upEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				getPlayer().movesUp = false;
			}
		};
		downStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				getPlayer().movesDown = true;
			}
		};
		downEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				getPlayer().movesDown = false;
			}
		};
		leftStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				getPlayer().movesLeft = true;
			}
		};
		leftEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				getPlayer().movesLeft = false;
			}
		};
		rightStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				getPlayer().movesRight = true;
			}
		};
		rightEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				getPlayer().movesRight = false;
			}
		};
/////////////////////////////////////////////////////////////////////////////////////////////////////////////Actions for attacking
		upAS = new AbstractAction(){						//When the button is held...
			public void actionPerformed(ActionEvent e){
				if(!arrUp){
					getPlayer().setAnimation(getPlayer().library.get("cUp"));
				}
				arrUp = true;								//Set the flag for the up arrow being pressed to true
				if(!getPlayer().attacking)					//Set player attacking boolean to true
					getPlayer().attacking = true;
			}
		};
		upAE = new AbstractAction(){						//When the button is released...
			public void actionPerformed(ActionEvent e){
				if(getPlayer().getWeapon().attackReady() && (!arrLeft && !arrRight && !arrDown)){	//Is the player's attack ready?
					getPlayer().attacksUp = true;			//If so, set the attack boolean for that direction to true, then attack
					getPlayer().startAttack();
				}
				if(!arrLeft && !arrRight && !arrDown){		//Is the only arrow key being pressed the selected arrow?
					getPlayer().attacking = false;			//If so, set the attacking boolean to false and cancel the attack
					getPlayer().getWeapon().cancelAttack(getPlayer());
					getPlayer().setIdle();
				}
				arrUp = false;								//Remove flag
			}
		};
		downAS = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(!arrDown){
					getPlayer().setAnimation(getPlayer().library.get("cDown"));
				}
				arrDown = true;
				if(!getPlayer().attacking)
					getPlayer().attacking = true;
			}
		};
		downAE = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(getPlayer().getWeapon().attackReady() && (!arrLeft && !arrRight && !arrUp)){
					getPlayer().attacksDown = true;
					getPlayer().startAttack();
				}
				if(!arrLeft && !arrRight && !arrUp){
					getPlayer().attacking = false;
					getPlayer().getWeapon().cancelAttack(getPlayer());
					getPlayer().setIdle();
				}
				arrDown = false;
			}
		};
		leftAS = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(!arrLeft){
					getPlayer().setAnimation(getPlayer().library.get("cLeft"));
				}
				arrLeft = true;
				if(!getPlayer().attacking)
					getPlayer().attacking = true;
			}
		};
		leftAE = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(getPlayer().getWeapon().attackReady() && (!arrUp && !arrRight && !arrDown)){
					getPlayer().attacksLeft = true;
					getPlayer().startAttack();
				}
				if(!arrUp && !arrRight && !arrDown){
					getPlayer().attacking = false;
					getPlayer().getWeapon().cancelAttack(getPlayer());
					getPlayer().setIdle();
				}
				arrLeft = false;
			}
		};
		rightAS = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(!arrRight){
					getPlayer().setAnimation(getPlayer().library.get("cRight"));
				}
				arrRight = true;
				if(!getPlayer().attacking)
					getPlayer().attacking = true;
			}
		};
		rightAE = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				if(getPlayer().getWeapon().attackReady() && (!arrLeft && !arrUp && !arrDown)){
					getPlayer().attacksRight = true;
					getPlayer().startAttack();
				}
				if(!arrLeft && !arrUp && !arrDown){
					getPlayer().attacking = false;
					getPlayer().getWeapon().cancelAttack(getPlayer());
					getPlayer().setIdle();
				}
				arrRight = false;
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

		getPlayer().inertiaSetup(); ///////////Sets player's booleans for moving in the x and y to false.

		if(getPlayer().movesUp){			////////////Handles taking in input for movement
			getPlayer().moveUp();
		}
		else if(getPlayer().movesDown){
			getPlayer().moveDown();
		}
		if(getPlayer().movesLeft){
			getPlayer().moveLeft();
		}
		else if(getPlayer().movesRight){
			getPlayer().moveRight();
		}

		getPlayer().move();			//Handles movement and stops player if it hits a wall


////////////////////////////////////////////////////////////////////////////////////////////For exact movement to line up with obstacles
		if(getPlayer().movesUp && !getPlayer().checkUp()){
			getPlayer().moveUpDis((getPlayer().getY() - getPlayer().getYOffset()) % World.cellSize);
		}
		else if(getPlayer().movesDown && !getPlayer().checkDown()){
			if((getPlayer().getY() + getPlayer().getYOffset()) % World.cellSize != 0)
				getPlayer().moveDownDis(World.cellSize - ((getPlayer().getY() + getPlayer().getYOffset()) % World.cellSize));
		}
		if(getPlayer().movesLeft && !getPlayer().checkLeft()){
			getPlayer().moveLeftDis((getPlayer().getX() - getPlayer().getXOffset()) % World.cellSize);
		}
		else if(getPlayer().movesRight && !getPlayer().checkRight()){
			if((getPlayer().getX() + getPlayer().getXOffset()) % World.cellSize != 0)
				getPlayer().moveRightDis(World.cellSize - ((getPlayer().getX() + getPlayer().getXOffset()) % World.cellSize));
		}
///////////////////////////////////////////////////////////////////////////////////////////Room changing commands

		if(getPlayer().getYVelocity() < 0 &&
				Math.abs(getPlayer().getYVelocity()) >= getPlayer().getY() - getPlayer().getYOffset() && World.getCurrentRoom().checkUp() && World.checkUp()){
			World.exitUp();
		}

		else if(getPlayer().getYVelocity() + getPlayer().getY() + getPlayer().getYOffset() > (World.worldHeight*World.cellSize) &&
					World.getCurrentRoom().checkDown() && World.checkDown()){
			World.exitDown();
		}

		else if(getPlayer().getXVelocity() < 0 &&
					Math.abs(getPlayer().getXVelocity()) >= getPlayer().getX() - getPlayer().getXOffset() &&
						World.getCurrentRoom().checkLeft() && World.checkLeft()){
			World.exitLeft();
		}

		else if(getPlayer().getXVelocity() + getPlayer().getX() + getPlayer().getXOffset() > (World.worldLength*World.cellSize) &&
					World.getCurrentRoom().checkRight() && World.checkRight()){
			World.exitRight();
		}


/////////////////////////////////////////////////////////////////////////////////////////Enemy commands
		for(int i = 0; i < getCurrentEnemies().size(); i++){ //Iterates through every enemy on screen
			getCurrentEnemies().get(i).checkCurrentPos(getPlayer());
			int[] bounds = getCurrentEnemies().get(i).getBounds();	//Gets hitbox of enemy
			int[] pBounds = getPlayer().getBounds();					//Player hitbox

			/////////////Methods to be run when enemy is hit
			if(getPlayer().getAttackHitbox().intersects(bounds[0] - bounds[2], bounds[1] - bounds[3], bounds[2] * 2, bounds[3] * 2)){
				getCurrentEnemies().get(i).loseHP(getPlayer().getWeapon().getDamage());	//Do damage to hit enemy
				getCurrentEnemies().get(i).hit(getPlayer());
			}

			getCurrentEnemies().get(i).chasePlayer(getPlayer());	//Have enemy chase the player

			if(getCurrentEnemies().get(i).isEntityInRange(getPlayer())){
				////////////////////Methods to run when player is hit
				if(getCurrentEnemies().get(i).getAttackHitbox().intersects(pBounds[0] - pBounds[2],
					pBounds[1] - pBounds[3], pBounds[2] * 2, pBounds[3] * 2)){
					getPlayer().loseHP(getCurrentEnemies().get(i).getWeapon().getDamage());
					getPlayer().hit(getCurrentEnemies().get(i));
				}
			}
			if(getCurrentEnemies().get(i).getHP() <= 0){	//If an enemy dies, remove it from the arraylist
				getCurrentEnemies().remove(i);
			}
		}
/////////////////////////////////////////////////////////////////////////////////////////Combat

		if(getPlayer().attacking)
			getPlayer().getWeapon().chargeAttack(getPlayer());
		if(!getPlayer().attacking)
			getPlayer().getWeapon().doAttack(getPlayer());

		if(!getPlayer().attacksUp && !getPlayer().attacksDown && !getPlayer().attacksLeft && !getPlayer().attacksRight){
			getPlayer().attackHitbox = new Area(new Rectangle2D.Double(0, 0, 0, 0));
		}

		getPlayer().effectHandler();					//Handles any passive effects to the player that aren't caused by direct inputs.

		repaint();
		if(getPlayer().getHP() < 0){
			updater.stop();
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////Enemy Generation


	void generateEnemies(){
		for(int a = 0; a < World.worldHeight; a++){
			for(int i = 0; i < World.worldLength; i++){
				if(World.roomIDs[i][a].spawnCheck()){
					getCurrentEnemies().add(new Enemies(i*World.cellSize + (World.cellSize/2), a*World.cellSize + (World.cellSize/2)));
				}
			}
		}
	}


//////////////////////////////////////////////////////////////////////////////////////////////////Draw player and world image
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		if(!(getPlayer().getHP() < 0)){
			World.getCurrentRoom().drawRoom(g, this);
			getPlayer().getAnimation().drawAnimation(g, getPlayer().getX() - getPlayer().getXOffset(),
															getPlayer().getY() - getPlayer().getYOffset(), 50, 50, this);
			drawEnemies(g);
			Graphics2D g2 = (Graphics2D)g.create();
			g2.setColor(Color.white);
			if(getPlayer().attacksUp || getPlayer().attacksLeft || getPlayer().attacksRight || getPlayer().attacksDown){
				getPlayer().drawAttack(g);
			}
			if(getPlayer().checkIfHit()){
				getPlayer().showHPBar(g);
			}
			for(int i = 0; i < getCurrentEnemies().size(); i++){
				Enemies e = getCurrentEnemies().get(i);
				if(getCurrentEnemies().get(i).checkIfHit()){
					e.showHPBar(g);
				}
				if(e.getAttackTimer().isRunning()){
					e.drawAttack(g);
				}
			}
		}
		else{
			getPlayer().die(g);
		}
	}


	private ArrayList<Enemies> getCurrentEnemies(){
		return World.getCurrentRoom().enemyList;
	}


	private void setupCurrentRoom(){
		System.out.println(World.xRoomCount/2 + ", " + World.currentY);
		try{
			Scanner input = new Scanner(new File(World.rooms[World.currentX][World.currentY].getFilePath()));
			for(int a = 0; a < World.worldHeight; a++){
				for(int i = 0; i < World.worldLength; i++){
					try{
						int temp = input.nextInt();
						World.roomIDs[i][a] = new Cell(temp);
					}catch(Exception e){System.out.println("World draw error: " + e);}
				}
			}

		}catch(Exception e){System.out.println("World file read error.");}
	}

	Player getPlayer(){
		return World.p1;
	}



	private void drawEnemies(Graphics g){								//Encapsulated method for drawing the enemise
		for(int i = 0; i < getCurrentEnemies().size(); i++){
			Enemies temp = getCurrentEnemies().get(i);
			temp.getAnimation().drawAnimation(g, temp.getX() - temp.getXOffset(), temp.getY() - temp.getYOffset(), 50, 50, this);
		}
	}

}