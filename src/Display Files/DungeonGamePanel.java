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

	Timer updater = new Timer(15, this);

	BufferedImage worldImage, playerImage;

	Player p1 = new Player();
	boolean movesUp, movesDown, movesLeft, movesRight;

	Action upStart, upEnd, downStart, downEnd, leftStart, leftEnd, rightStart, rightEnd;

	Set<String> keysList = new HashSet<String>();

	Map<Integer, BufferedImage> idToTexture = new HashMap<Integer, BufferedImage>();

	ArrayList<Enemies> enemyList = new ArrayList<Enemies>();

	DungeonGamePanel(){
		createPanel();
		idToHashMap();
		drawWorld();

		updater.start();

		enemyList.add(new Enemies());

////////////////////////////////////////////////////////////////////////////////////////////Create Actions for keybindings.
		upStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				movesUp = true;
			}
		};
		upEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				movesUp = false;
			}
		};
		downStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				movesDown = true;
			}
		};
		downEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				movesDown = false;
			}
		};
		leftStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				movesLeft = true;
			}
		};
		leftEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				movesLeft = false;
			}
		};
		rightStart = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				movesRight = true;
			}
		};
		rightEnd = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				movesRight = false;
			}
		};
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
	}
////////////////////////////////////////////////////////////////////////////////////////////////Action Listener for updating stuff.
	public void actionPerformed(ActionEvent e){
////////////////////////////////////////////////////////////////////////////////////////////////For normal movement
		if(movesUp && p1.checkUp()){
			p1.moveUp();
		}
		else if(movesDown && p1.checkDown()){
			p1.moveDown();
		}
		if(movesLeft && p1.checkLeft()){
			p1.moveLeft();
		}
		else if(movesRight && p1.checkRight()){
			p1.moveRight();
		}
////////////////////////////////////////////////////////////////////////////////////////////For exact movement to line up with obstacles
		if(movesUp && !p1.checkUp()){
			p1.moveUpDis((p1.getY() - p1.getYOffset()) % World.cellSize);
		}
		else if(movesDown && !p1.checkDown()){
			if((p1.getY() + p1.getYOffset()) % World.cellSize != 0)
				p1.moveDownDis(World.cellSize - ((p1.getY() + p1.getYOffset()) % World.cellSize));
		}
		if(movesLeft && !p1.checkLeft()){
			p1.moveLeftDis((p1.getX() - p1.getXOffset()) % World.cellSize);
		}
		else if(movesRight && !p1.checkRight()){
			if((p1.getX() + p1.getXOffset()) % World.cellSize != 0)
				p1.moveRightDis(World.cellSize - ((p1.getX() + p1.getXOffset()) % World.cellSize));
		}
/////////////////////////////////////////////////////////////////////////////////////////Makes each enemy chase player
		for(int i = 0; i < enemyList.size(); i++){
			enemyList.get(i).checkCurrentPos(p1);
			enemyList.get(i).chasePlayer(p1);
		}
		repaint();
	}
//////////////////////////////////////////////////////////////////////////////////////////////////Draw player and world image
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(worldImage, 0, 0, World.cellSize * World.worldLength, World.cellSize * World.worldHeight, this);
		g.drawImage(p1.getSprite(), p1.getX() - p1.getXOffset(), p1.getY() - p1.getYOffset(), 50, 50, this);
		drawEnemies(g);
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
