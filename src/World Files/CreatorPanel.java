import javax.swing.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.*;


public class CreatorPanel extends GamePanels implements ActionListener, MouseListener {

	int iconCount = 5, pageCount = 2, pageNum = 0, selectedID = 0;

	JButton[][] exampleIcons = new JButton[iconCount][pageCount];

	JButton[][] mapIcons = new JButton[World.worldLength][World.worldHeight];
	int[][] mapIds = new int[World.worldLength][World.worldHeight];

	JLabel selectedIcon = new JLabel("Label");

	JButton pageLeft = new JButton("<"), pageRight = new JButton(">"), saveButton = new JButton("Save");

	BufferedImage sprite;

	ImageIcon activeTexture;

	BufferedImage spawnIcon, moveIcon;

	Scanner textureReader;

	CreatorPanel(){

																//For getting texture image paths for the ImageIO file read.
		try{
		textureReader = new Scanner(new File("../resources/Texture Links.txt"));
		textureReader.nextLine();
		}catch(IOException e){System.out.println("File read error.");}

		try{
			spawnIcon = ImageIO.read(new File("../resources/icons/spawnIcon.png"));
			moveIcon = ImageIO.read(new File("../resources/icons/moveIcon.png"));
		}catch(IOException e){}
		createPanel();

		setBackground(Color.gray);

		for(int a = 0; a < pageCount; a++){										//Creates and fills the selection pane.
			for(int i = 0; i < iconCount; i++){
				exampleIcons[i][a] = new JButton();
				exampleIcons[i][a].setBorderPainted(false);
				exampleIcons[i][a].setActionCommand("Selection " + (i + 1));
				exampleIcons[i][a].setBounds(50, 50 + (120 * i), 100, 100);
				exampleIcons[i][a].addActionListener(this);

				try{
					sprite = ImageIO.read(new File(textureReader.nextLine()));
					exampleIcons[i][a].setIcon(new ImageIcon(sprite));
				}catch(Exception e){System.out.println("Image not found: Index " + (i + 1 + (5*a)));}

				sprite = null;
				add(exampleIcons[i][a]);
				if(a != 0){
					exampleIcons[i][a].setVisible(false);
				}
			}
		}

		for(int i = 0; i < World.worldLength; i++){									//Sets up the map and the buttons in the map.
			for(int a = 0; a < World.worldHeight; a++){
				mapIcons[i][a] = new JButton();
				mapIcons[i][a].setBounds(275 + (75*i), 50 + (75*a), 75, 75);
				mapIcons[i][a].setOpaque(false);							//This stuff makes the buttons look good in the map.
				mapIcons[i][a].setContentAreaFilled(false);
				mapIcons[i][a].setBorderPainted(true);
				mapIcons[i][a].setActionCommand("Map " + i + "," + a);
				mapIcons[i][a].addActionListener(this);
				add(mapIcons[i][a]);
			}
		}

		selectedIcon.setBounds(50, 750, 100, 100);						//Icon for which texture is currently active.
		selectedIcon.setVisible(false);
		selectedIcon.addMouseListener(this);
		add(selectedIcon);

		pageLeft.setBounds(20, 20, 50, 25);								//Left and right buttons for selection pane.
		pageLeft.addActionListener(this);
		pageLeft.setVisible(false);
		add(pageLeft);

		pageRight.setBounds(120, 20, 50, 25);
		pageRight.addActionListener(this);
		add(pageRight);

		saveButton.setBounds(800, 900, 100, 50);						//Save button is self-explanatory.
		saveButton.addActionListener(this);
		add(saveButton);
	}




	/////////////////////////////////////////////////////MouseListener methods (Unnecessary, can be done with actionlistener but I'm lazy)
	public void mouseEntered(MouseEvent e){

	}
	public void mouseExited(MouseEvent e){

	}
	public void mouseClicked(MouseEvent e){
		selectedIcon.setVisible(false);
		selectedIcon.setIcon(null);

		for(int i = 0; i < iconCount; i++){
			exampleIcons[i][pageNum].setBorderPainted(false);
		}
		activeTexture = null;
	}
	public void mousePressed(MouseEvent e){

	}
	public void mouseReleased(MouseEvent e){

	}

	//////////////////////////////////////////////////////////////////////////ActionListener

	public void actionPerformed(ActionEvent e){
		System.out.println(e.getActionCommand());
		String[] stuff = e.getActionCommand().split(" ");

		if(stuff[0].equals("Selection")){							//Code for selecting the active texture.
			int num = Integer.parseInt(stuff[1]) - 1;
			for(int i = 0; i < iconCount; i++){
				exampleIcons[i][pageNum].setBorderPainted(false);
			}
			exampleIcons[num][pageNum].setBorderPainted(true);

			activeTexture = (ImageIcon)(exampleIcons[num][pageNum].getIcon());

			selectedIcon.setIcon(activeTexture);
			selectedIcon.setVisible(true);

			selectedID = num + (5*pageNum) + 1;						//Needs a +1 so that 0 can be the empty area.
		}
		else if (stuff[0].equals("Map")){							//Code for painting the active texture onto the map.
			String[] stuff2 = stuff[1].split(",");
			int y = Integer.parseInt(stuff2[0]), x = Integer.parseInt(stuff2[1]);
			if(activeTexture == null){
				mapIcons[y][x].setIcon(null);
				mapIcons[y][x].setContentAreaFilled(false);
				mapIds[y][x] = 0;
			}
			else{
				mapIcons[y][x].setIcon(activeTexture);
				mapIcons[y][x].setContentAreaFilled(true);
				mapIcons[y][x].setVisible(true);
				mapIds[y][x] = selectedID;
			}
		}
		else if (stuff[0].equals("<")){								//Left and right buttons for selection pane.
			for(int i = 0; i < 5; i++){
				exampleIcons[i][pageNum].setVisible(false);
				exampleIcons[i][pageNum-1].setVisible(true);
			}
			pageNum -= 1;
			if(pageNum == 0) pageLeft.setVisible(false);
			if(pageNum != pageCount-1) pageRight.setVisible(true);
		}
		else if(stuff[0].equals(">")){
			for(int i = 0; i < 5; i++){
				exampleIcons[i][pageNum].setVisible(false);
				exampleIcons[i][pageNum+1].setVisible(true);
			}
			pageNum += 1;
			if(pageNum == pageCount-1) pageRight.setVisible(false);
			if(pageNum != 0) pageLeft.setVisible(true);
		}
		else if(stuff[0].equals("Save")){							//Save button.
			//saveWorld();
			saveWorldAsText();
		}
	}
	///////////////////////////////////////////////////////////////////////////Draw parts of the UI
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.fillRect(0, 0, 200, GameFrame.getYSize());
		g.setColor(Color.white);
		g.fillRect(45, 745, 110, 110);
	}
	//////////////////////////////////////////////////////////////////////////Stuff for saving world in an image
	private void saveWorld(){
		BufferedImage world = new BufferedImage(World.worldLength*75, World.worldHeight*75, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = world.createGraphics();								//Create Graphics2D for drawing world image.
		g2.setPaint(Color.black);
		g2.fillRect(0, 0, World.worldLength*75, World.worldHeight*75);					//Set background to black. Can be changed later.

		for(int a = 0; a < World.worldHeight; a++){					//Draw images from the map buttons into a larger image.
			for(int i = 0; i < World.worldLength; i++){
				try{
				g2.drawImage(iconToBufferedImage((ImageIcon)mapIcons[i][a].getIcon()), null, i*75, a*75);
				}catch(Exception e){}
			}
		}

		String fileName = JOptionPane.showInputDialog("Enter a file name.");		//Gets file name from JOptionPane popup.

		File output = new File("../maps/" + fileName + ".png");
		try{
			ImageIO.write(world, "png", output);									//Saves world image as .png file.
		}catch(Exception e){}
	}

	private void saveWorldAsText(){
		String worldText = "";

		for(int i = 0; i < World.worldLength * World.worldHeight; i++){
			worldText += mapIds[i%World.worldLength][i/World.worldLength] + "\r\n";
		}

		String fileName = JOptionPane.showInputDialog("Enter a file name.");

		try{
			PrintWriter output = new PrintWriter("../maps/"+ fileName + ".txt");
			output.println(worldText);
			output.close();
		}catch(Exception e){System.out.println("Save world as text error.");}
	}

	private BufferedImage iconToBufferedImage(ImageIcon icon){						//Converts icon to buffered image and gets a
		BufferedImage temp = (BufferedImage)icon.getImage();						//subimage of it to make it fit.
		return temp.getSubimage(0, 0, 75, 75);
	}
}
