import java.awt.image.*;
import java.io.*;
import java.util.Arrays;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Enemies extends Entity{

	boolean playerUp, playerDown, playerLeft, playerRight, aggroed = true, hesitate;
	boolean[] lastPlayerPos = new boolean[]{false, false, false, false};

	int aggroRange = 250, holdAggroRange = 400, currentTime = 0, maxTime, waitTime = 1000;

	Timer hesitationDelay = new Timer(500, this), eAtkTimer = new Timer(1000/World.framerate, this), wait = new Timer(waitTime, this);

	Enemies(int x, int y){
		try{
			BufferedImage[] tempImages = new BufferedImage[]{ImageIO.read(new File("../resources/textures/yellowSquare.png"))};

			int[] tempTimes = new int[]{1};

			sprite = new Animation(tempImages, tempTimes);
			library.assignAnim("idle", sprite);
			library.assignAnim("aRight", sprite);
		}catch(IOException e){System.out.println("Enemy sprite not found.");}
		defaultMovespeed = 3;
		acceleration = 2;
		xPos = x;
		yPos = y;
		xOffset = 25;
		yOffset = 25;
		activeWeapon = new BrokenDagger();
		maxTime = (int)Math.ceil((activeWeapon.getAttackDuration()/1000.0) * World.framerate);
		setupEffectTimers();
	}

	void setAnimations(){
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
			////////////////////////////////Aggro and combat code
			if(aggroRange > getEntityDis(p)){			//If player is in enemy's aggro range, enemy is aggroed
				aggroed = true;
			}
			else if(aggroed && holdAggroRange < getEntityDis(p)){	//If enemy is aggroed and player moves out
																	//of a certain range, disable aggro
				aggroed = false;
				attacking = false;
				activeWeapon.cancelAttack(this);
			}
			if(!eAtkTimer.isRunning() && !wait.isRunning()){	//If the enemy is not currently attacking and the enemy isn't waiting...
				if(activeWeapon.attackReady()){		//If it's attack is ready, begin to attack
					attackHitbox.reset();
					eAtkTimer.start();
				}
				else if(getEntityDis(p) <=
						activeWeapon.getRange() + p.getXOffset() + Math.abs(xVelocity * activeWeapon.getSpeedMod() *
							(int)(World.framerate * activeWeapon.getAttackTime()/1000.0))){
																					//If it's not ready, then continue to
																				 	//charge if enemy can reach p while chargin it
					attacking = true;
					activeWeapon.chargeAttack(this);
				}
				else if(getEntityDis(p) <=
						activeWeapon.getRange() + p.getYOffset() + Math.abs(yVelocity * activeWeapon.getSpeedMod() *
							(int)(World.framerate * activeWeapon.getAttackTime()/1000.0))){

					attacking = true;
					activeWeapon.chargeAttack(this);
				}
				else if(getEntityDis(p) >
						activeWeapon.getRange() + p.getXOffset() + Math.abs(xVelocity * activeWeapon.getSpeedMod() *
							(int)(World.framerate * activeWeapon.getAttackTime()/1000.0))){//If it's not ready and p is not in range,
																					//cancel the attack
					attacking = false;
					attackHitbox = new Area(new Rectangle2D.Double(0, 0, 0, 0));
					activeWeapon.cancelAttack(this);
				}
				else if(getEntityDis(p) >
						activeWeapon.getRange() + p.getYOffset() + Math.abs(yVelocity * activeWeapon.getSpeedMod() *
							(int)(World.framerate * activeWeapon.getAttackTime()/1000.0))){
					attacking = false;
					attackHitbox = new Area(new Rectangle2D.Double(0, 0, 0, 0));
					activeWeapon.cancelAttack(this);
				}
			}
		}
	}

	void aggroOff(){
		aggroed = false;
	}

	void targetPlayer(Player p){									//Attacks the player in the direction where the player is closest
		if(getVerticalDis(p) >= getHorizontalDis(p) && playerUp)
			attacksUp = true;
		else if(getVerticalDis(p) >= getHorizontalDis(p) && playerDown)
			attacksDown = true;
		else if(getHorizontalDis(p) >= getVerticalDis(p) && playerLeft)
			attacksLeft = true;
		else if(getHorizontalDis(p) >= getVerticalDis(p) && playerRight)
			attacksRight = true;
	}

	void setupEffectTimers(){
		super.setupEffectTimers();
		hesitationDelay.setActionCommand("hesitate");
		eAtkTimer.setActionCommand("attack");
		wait.setActionCommand("wait");
	}

	Timer getAttackTimer(){
		return eAtkTimer;
	}

	public void actionPerformed(ActionEvent e){
		super.actionPerformed(e);
		if(e.getActionCommand().equals("hesitate"))
			hesitate = false;
		if(e.getActionCommand().equals("attack")){
			targetPlayer(World.p1);
			activeWeapon.doAttack(this);
			currentTime++;
			if(currentTime == maxTime){
				activeWeapon.cancelAttack(this);
				currentTime = 0;
				attacksUp = false;
				attacksDown = false;
				attacksLeft = false;
				attacksRight = false;
				sprite = library.get("idle");
				eAtkTimer.stop();
				wait.start();
			}
		}
		if(e.getActionCommand().equals("wait")){
			wait.stop();
		}
	}
}
