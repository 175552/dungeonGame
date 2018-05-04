import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.Timer;
import java.io.*;
import java.util.Scanner;

public class Player extends Entity{

	boolean movesUp, movesDown, movesLeft, movesRight;

	Timer attackActive;

	Player(){
		xPos = (World.worldLength*World.cellSize)/2;
		yPos = (World.worldHeight*World.cellSize)/2;
		xOffset = 25;
		yOffset = 25;
		defaultMovespeed = 6;
		acceleration = 4;

		activeWeapon = new BasicSword();

		attackActive = new Timer(activeWeapon.getAttackDuration(), this);

		setAnimations();
		sprite = library.get("idle");
		setupEffectTimers();
	}

	Timer getAttackTimer(){
		return attackActive;
	}

	void die(Graphics g){
		int fontSize = 100;
		g.setColor(Color.black);
		g.setFont(new Font("Serif", Font.BOLD, fontSize));
		g.drawString("YOU DIED", (World.worldLength * World.cellSize)/2 - fontSize, (World.worldHeight * World.cellSize)/2 - fontSize);
	}

	void move(){
		if(!attackActive.isRunning() && !activeWeapon.canAttackWhileMoving)
			super.move();
	}

	void setAnimations(){
		try{;
			Scanner input = new Scanner(new File("../resources/lib/player/player.txt"));
			while(input.hasNextLine()){
				String[] temp = input.nextLine().split(";");
				library.assignAnim(temp[0], new Animation(new File(temp[1])));
			}
		}catch(Exception e){System.out.println("Player image file error");}
	}

	void setupEffectTimers(){
		super.setupEffectTimers();
		attackActive.setActionCommand("attack");
	}

	void startAttack(){
		attackActive.start();
	}

	public void actionPerformed(ActionEvent e){
		super.actionPerformed(e);
		if(e.getActionCommand().equals("attack")){
			attacksUp = false;
			attacksDown = false;
			attacksLeft = false;
			attacksRight = false;
			sprite = library.get("idle");
			attackActive.stop();
		}
	}
}
