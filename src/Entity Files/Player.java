import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.Timer;
import java.io.*;

public class Player extends Entity{

	boolean movesUp, movesDown, movesLeft, movesRight;

	Player(){
		xPos = 25;
		yPos = 400;
		xOffset = 25;
		yOffset = 25;
		defaultMovespeed = 8;
		acceleration = 4;

		activeWeapon = new BasicDagger();


		try{
			BufferedImage[] tempImages = new BufferedImage[]{ImageIO.read(new File("../resources/textures/blueSquare.png")),
				ImageIO.read(new File("../resources/textures/purpleSquare.png"))};

			int[] tempTimes = new int[]{15, 30};

			sprite = new Animation(tempImages, tempTimes);
		}catch(IOException e){System.out.println("Player image not found.");}
	}

	void die(Graphics g){
		int fontSize = 100;
		g.setColor(Color.black);
		g.setFont(new Font("Serif", Font.BOLD, fontSize));
		g.drawString("YOU DIED", (World.worldLength * World.cellSize)/2 - fontSize, (World.worldHeight * World.cellSize)/2 - fontSize);
	}
}
