import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.ActionEvent;

public class Enemies extends Entity{

	boolean playerUp, playerDown, playerLeft, playerRight, aggroed = true, hesitate;
	boolean[] lastPlayerPos = new boolean[]{false, false, false, false};

	int aggroRange = 250, holdAggroRange = 400;

	Timer hesitationDelay = new Timer(1000, this);

	Enemies(int x, int y){

		try{
			BufferedImage[] tempImages = new BufferedImage[]{ImageIO.read(new File("../resources/textures/yellowSquare.png"))};

			int[] tempTimes = new int[]{1};

			sprite = new Animation(tempImages, tempTimes);
		}catch(IOException e){System.out.println("Enemy sprite not found.");}
		defaultMovespeed = 3;
		acceleration = 2;
		xPos = x;
		yPos = y;
		xOffset = 25;
		yOffset = 25;
		activeWeapon = new BrokenDagger();
		setupEffectTimers();
	}

	void chasePlayer(Player p){

		inertiaSetup();


		if(aggroed && getEntityDis(p) > activeWeapon.getRange()/2){
			if(playerUp && Math.abs(p.getY() - yPos) < acceleration){
				moveUpDis(p.getY() - yPos);
				stopY();
			}
			else if(playerUp)
				moveUp();
			if(playerDown && Math.abs(p.getY() - yPos) < acceleration){
				moveDownDis(p.getY() - yPos);
				stopY();
			}
			else if(playerDown)
				moveDown();
			if(playerLeft)
				moveLeft();
			else if(playerRight)
				moveRight();
		}
		else{
			stopX();
			stopY();
		}
		move();
	}

	void checkCurrentPos(Player p){
		if(!hesitate){				//If the hesitation effect isn't active, have the enemy check it's position relative to the player
			if(p.getY() < getY()){
				playerUp = true;
				playerDown = false;
			}
			else if(p.getY() > getY()){
				playerDown = true;
				playerUp = false;
			}
			else{
				playerUp = false;
				playerDown = false;
			}
			if(p.getX() < getX()){
				playerLeft = true;
				playerRight = false;
			}
			else if(p.getX() > getX()){
				playerRight = true;
				playerLeft = false;
			}
			else{
				playerLeft = false;
				playerRight = false;
			}
			/////////////////////Check if last player position is not the same as the current player position. If so, activate hesitation
			if(playerUp != lastPlayerPos[0]){
				hesitationDelay.start();	//Activates timer for removing hesitation effect.
				hesitate = true;			//Activates hesitation effect
			}
			else if(playerDown != lastPlayerPos[1]){
				hesitationDelay.start();
				hesitate = true;
			}
			else if(playerLeft != lastPlayerPos[2]){
				hesitationDelay.start();
				hesitate = true;
			}
			else if(playerRight != lastPlayerPos[3]){
				hesitationDelay.start();
				hesitate = true;
			}
			lastPlayerPos = new boolean[]{playerUp, playerDown, playerLeft, playerRight};
			////////////////////////////////Aggro code
			if(aggroRange > getEntityDis(p))			//If player is in enemy's aggro range, enemy is aggroed
				aggroed = true;
			else if(aggroed && holdAggroRange < getEntityDis(p))//If enemy is aggroed and player moves out of a certain range, disable aggro
				aggroed = false;
		}
	}

	void aggroOff(){
		aggroed = false;
	}

	void attackPlayer(Player p){									//Attacks the player in the direction where the player is closest
		if(getVerticalDis(p) >= getHorizontalDis(p) && playerUp)
			attackUp();
		else if(getVerticalDis(p) >= getHorizontalDis(p) && playerDown)
			attackDown();
		else if(getHorizontalDis(p) >= getVerticalDis(p) && playerLeft)
			attackLeft();
		else if(getHorizontalDis(p) >= getVerticalDis(p) && playerRight)
			attackRight();
	}

	void setupEffectTimers(){
		super.setupEffectTimers();
		hesitationDelay.setActionCommand("hesitate");
	}

	public void actionPerformed(ActionEvent e){
		super.actionPerformed(e);
		if(e.getActionCommand().equals("hesitate"))
			hesitate = false;
	}
}
