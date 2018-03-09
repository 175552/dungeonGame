import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class DungeonGamePanel extends GamePanels implements ActionListener{

	Timer updater = new Timer(World.framerate, this);

	BufferedImage worldImage, playerImage;

	Player p1 = new Player();

	Action upStart, upEnd, downStart, downEnd, leftStart, leftEnd, rightStart, rightEnd;
	Action upAS, upAE, downAS, downAE, leftAS, leftAE, rightAS, rightAE;

	Set<String> keysList = new HashSet<String>();

	Map<Integer, BufferedImage> idToTexture = new HashMap<Integer, BufferedImage>();

	ArrayList<Enemies> enemyList = new ArrayList<Enemies>();

	DungeonGamePanel(){
		createPanel();
		idToHashMap();
		drawWorld();

		updater.start();

		enemyList.add(new Enemies());

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
		if(p1.movesUp && p1.checkUp()){
			p1.moveUp();
		}
		else if(p1.movesDown && p1.checkDown()){
			p1.moveDown();
		}
		if(p1.movesLeft && p1.checkLeft()){
			p1.moveLeft();
		}
		else if(p1.movesRight && p1.checkRight()){
			p1.moveRight();
		}
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
/////////////////////////////////////////////////////////////////////////////////////////Makes each enemy chase player
		for(int i = 0; i < enemyList.size(); i++){
			enemyList.get(i).checkCurrentPos(p1);
			enemyList.get(i).chasePlayer(p1);
		}
/////////////////////////////////////////////////////////////////////////////////////////For handling attacks
		repaint();
	}
	
	
	
	
	
	
	
	
	
//////////////////////////////////////////////////////////////////////////////////////////////////Draw player and world image
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(worldImage, 0, 0, World.cellSize * World.worldLength, World.cellSize * World.worldHeight, this);
		g.drawImage(p1.getSprite(), p1.getX() - p1.getXOffset(), p1.getY() - p1.getYOffset(), 50, 50, this);
		//drawEnemies(g);
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setColor(Color.white);
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
		if(p1.attacksUp || p1.attacksLeft || p1.attacksRight || p1.attacksDown){
			g2.fill(p1.getAttackHitbox());
		}
	}
	
	
	

	private void drawWorld(){
		worldImage = new BufferedImage(World.cellSize * World.worldLength,
		World.cellSize * World.worldHeight, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = worldImage.createGraphics();
		int store = World.cellSize;
		try{
			Scanner input = new Scanner(new File("../maps/testMap.txt"));
			for(int a = 0; a < World.worldHeight; a++){					//Draw images from the map buttons into a larger image.
				for(int i = 0; i < World.worldLength; i++){
					try{
						int temp = input.nextInt();
						g2.drawImage(idToTexture.get(temp), i*store, a*store, null);
						World.worldIDs[i][a] = new Cell(temp);
					}catch(Exception e){System.out.println("World draw error: " + e);}
				}
			}

		}catch(Exception e){System.out.println("World file read error.");}
	}
	
	

	private void drawEnemies(Graphics g){								//Encapsulated method for drawing the enemise
		for(int i = 0; i < enemyList.size(); i++){
			Enemies temp = enemyList.get(i);
			g.drawImage(temp.getSprite(), temp.getX() - temp.getXOffset(), temp.getY() - temp.getYOffset(), 50, 50, this);
		}
	}


/////////////////////////////////////////////////////////////////////////////Associates IDs with Buffered Images for drawing world.

	private void idToHashMap(){
		try{
			Scanner temp = new Scanner(new File("../resources/Texture Links.txt"));
			int i = 0;
			while(temp.hasNextLine()){
				idToTexture.put(i, ImageIO.read(new File(temp.nextLine())).getSubimage(0, 0, World.cellSize, World.cellSize));
				i++;
			}
		}catch(Exception e){System.out.println("ID to HashMap file read error");}
	}

}
