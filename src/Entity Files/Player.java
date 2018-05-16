import javax.imageio.ImageIO;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.Timer;
import java.io.*;
import java.util.Scanner;

public class Player extends Entity{

	boolean movesUp, movesDown, movesLeft, movesRight;
	BufferedImage head, lArm, rArm, chest, lLeg, rLeg, rSlot1, rSlot2;

	Timer attackActive;

	Player(){
		xPos = (World.worldLength*World.cellSize)/2;
		yPos = (World.worldHeight*World.cellSize)/2;
		xOffset = 25;
		yOffset = 25;
		defaultMovespeed = 6;
		acceleration = 4;

		activeWeapon = new BasicSword();
		inventory.add(activeWeapon);

		attackActive = new Timer(activeWeapon.getAttackDuration(), this);

		setAnimations();
		setEquipmentImages();
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

	void setEquipmentImages(){
		try{
			head = ImageIO.read(new File("../resources/lib/items/default/defPlayHead.png"));
			lArm = ImageIO.read(new File("../resources/lib/items/default/defPlayLeftArm.png"));
			rArm = ImageIO.read(new File("../resources/lib/items/default/defPlayRightArm.png"));
			chest = ImageIO.read(new File("../resources/lib/items/default/defPlayChest.png"));
			lLeg = ImageIO.read(new File("../resources/lib/items/default/defPlayLeftLeg.png"));
			rLeg = ImageIO.read(new File("../resources/lib/items/default/defPlayRightLeg.png"));
			rSlot1 = ImageIO.read(new File("../resources/lib/items/default/defPlayRing1.png"));
			rSlot2 = ImageIO.read(new File("../resources/lib/items/default/defPlayRing2.png"));
		}catch(Exception e){}
	}

	void drawPlayerItems(Graphics g, DungeonGamePanel p){
		g.drawImage(head, 1400, 450, 50, 50, p);//Head
		g.drawImage(lArm, 1345, 505, 40, 80, p);//Left Arm
		g.drawImage(rArm, 1465, 505, 40, 80, p);//Right Arm
		g.drawImage(chest, 1390, 503, 70, 70, p);//Chest
		g.drawImage(lLeg, 1380, 590, 40, 80, p);//Left Leg
		g.drawImage(rLeg, 1430, 590, 40, 80, p);//Right Leg
		g.drawImage(rSlot1, 1300, 750, 25, 25, p);//Ring Slot 1
		g.drawImage(rSlot2, 1330, 750, 25, 25, p);//Ring Slot 2
		g.drawImage(activeWeapon.getSprite(), 1500, 330, 75, 50, p);//Weapon
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
