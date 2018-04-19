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

		setAnimations();
		sprite = library.get("idle");
		setupEffectTimers();
	}

	void die(Graphics g){
		int fontSize = 100;
		g.setColor(Color.black);
		g.setFont(new Font("Serif", Font.BOLD, fontSize));
		g.drawString("YOU DIED", (World.worldLength * World.cellSize)/2 - fontSize, (World.worldHeight * World.cellSize)/2 - fontSize);
	}

	void setAnimations(){
		try{;
			library.assignAnim("idle", new Animation(new File("../resources/lib/player/idle.txt")));
			library.assignAnim("aRight", new Animation(new File("../resources/lib/player/aR.txt")));
		}catch(Exception e){System.out.println("Player image file error");}
	}
}
