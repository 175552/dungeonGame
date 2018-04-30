import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.*;

abstract class Entity implements ActionListener {
	int xPos, yPos, defaultMovespeed, acceleration, xVelocity, yVelocity, xOffset, yOffset;			//Position and movement information

	int HP = 1000, HPMax = 1000, baseATK, baseATKSPD, baseDEF;								//Stat info

	boolean attacksUp, attacksDown, attacksLeft, attacksRight, xMove, yMove;

	boolean hit, stunned, attacking; //Effect booleans

	Animation sprite;

	AnimationCollection library = new AnimationCollection();

	Area attackHitbox = new Area(new Rectangle2D.Double(0, 0, 0, 0));

	Weapons activeWeapon;
	Timer stunDelay = new Timer(500, this);

//////////////////////////////////////////////////////////Setter Methods

	abstract void setAnimations();

	void setAnimation(Animation a){
		sprite = a;
	}

	void setIdle(){
		setAnimation(library.get("idle"));
	}

	void setupEffectTimers(){
		stunDelay.setActionCommand("stun");
	}

	void effectHandler(){
		if(activeWeapon.attackReady() && attacking){
			if(attacksUp)
				setAnimation(library.get("cmUp"));
			else if(attacksDown)
				setAnimation(library.get("cmDown"));
			else if(attacksLeft)
				setAnimation(library.get("cmLeft"));
			else setAnimation(library.get("cmRight"));
		}
	}

//////////////////////////////////////////////////////////Getter Methods
	int getX(){
		return xPos;
	}
	int getY(){
		return yPos;
	}
	int getXOffset(){
		return xOffset;
	}
	int getYOffset(){
		return yOffset;
	}
	int getXVelocity(){
		return xVelocity;
	}
	int getYVelocity(){
		return yVelocity;
	}
	int[] getBounds(){
		return new int[]{xPos, yPos, xOffset, yOffset};
	}
	int[] getCoords(Entity e){
		return new int[]{xPos, yPos};
	}
	int getHP(){
		return HP;
	}
	int getHPMax(){
		return HPMax;
	}
	boolean checkIfHit(){
		return hit;
	}
	boolean getXMove(){
		return xMove;
	}
	boolean getYMove(){
		return yMove;
	}
	Animation getAnimation(){
		return sprite;
	}
	Area getAttackHitbox(){
		return attackHitbox;
	}
	Weapons getWeapon(){
		return activeWeapon;
	}
	abstract Timer getAttackTimer();
//////////////////////////////////////////////////////////////////////////Movement methods
	void moveUp(){
		if(yVelocity > -1 * defaultMovespeed && !stunned){
			if(Math.abs(yVelocity - acceleration) < defaultMovespeed)
				yVelocity -= acceleration;
			else if(yVelocity != defaultMovespeed)
				yVelocity -= defaultMovespeed - acceleration;
			yMove = true;
		}
	}
	void moveUpDis(int dis){
		yPos -= dis;
	}
	void moveDown(){
		if(yVelocity < defaultMovespeed && !stunned){
			if(Math.abs(yVelocity + acceleration) < defaultMovespeed)
				yVelocity += acceleration;
			else if(yVelocity != defaultMovespeed)
				yVelocity += defaultMovespeed - acceleration;
			yMove = true;
		}
	}
	void moveDownDis(int dis){
		yPos += dis;
	}
	void moveLeft(){
		if(xVelocity > -1 * defaultMovespeed && !stunned){
			if(Math.abs(xVelocity - acceleration) < defaultMovespeed)
				xVelocity -= acceleration;
			else if(xVelocity != defaultMovespeed)
				xVelocity -= defaultMovespeed - acceleration;
			xMove = true;
		}
	}
	void moveLeftDis(int dis){
		xPos -= dis;
	}
	void moveRight(){
		if(xVelocity < defaultMovespeed && !stunned){
			if(Math.abs(xVelocity + acceleration) < defaultMovespeed)
				xVelocity += acceleration;
			else if(xVelocity != defaultMovespeed)
				xVelocity += defaultMovespeed - acceleration;
			xMove = true;
		}
	}
	void moveRightDis(int dis){
		xPos += dis;
	}
//////////////////////////////////////////////////////////////////////////////Velocity movement methods
	void move(){
		if((getYVelocity() < 0 && !checkUp()) || (getYVelocity() > 0 && !checkDown()))
			stopY();

		if((getXVelocity() < 0 && !checkLeft()) || (getXVelocity() > 0 && !checkRight()))
			stopX();

		if(!stunned){
			inertiaX();		//////////Checks if entity is not actively moving in a direction, and if so, slows the player.
			inertiaY();
		}

		if(attacking){
			xVelocity = (int)(xVelocity * activeWeapon.getSpeedMod());
			yVelocity = (int)(yVelocity * activeWeapon.getSpeedMod());
		}

		xPos += xVelocity;
		yPos += yVelocity;
	}

	void inertiaX(){
		if(!xMove && xVelocity != 0){
			if(xVelocity < 0)
				xVelocity += acceleration/4;
			else
				xVelocity -= acceleration/4;
		}
	}
	void inertiaY(){
		if(!yMove && yVelocity != 0){
			if(yVelocity < 0)
				yVelocity += acceleration/4;
			else
				yVelocity -= acceleration/4;
		}
	}
	void stopX(){
		xVelocity = 0;
	}
	void stopY(){
		yVelocity = 0;
	}
	void inertiaSetup(){
		xMove = false;
		yMove = false;
	}
//////////////////////////////////////////////////////////////////////////////Direction checking methods
	boolean checkUp(){
		try{
			if(yPos - yOffset + yVelocity < 0)
				return false;

			if(World.roomIDs[(xPos - xOffset + 1)/World.cellSize][(yPos - yOffset + yVelocity)/World.cellSize].moveCheck() &&
				World.roomIDs[(xPos + xOffset - 1)/World.cellSize][(yPos - yOffset + yVelocity)/World.cellSize].moveCheck())
					return true;

			else return false;
		}catch(Exception e){return false;}
	}
	boolean checkDown(){
		try{
			if(yPos + yOffset + yVelocity > World.cellSize * World.worldHeight)
				return false;

			if(World.roomIDs[(xPos - xOffset + 1)/World.cellSize][(yPos + yOffset + yVelocity)/World.cellSize].moveCheck() &&
				World.roomIDs[(xPos + xOffset - 1)/World.cellSize][(yPos + yOffset + yVelocity)/World.cellSize].moveCheck())
					return true;

			else return false;
		}catch(Exception e){return false;}
	}
	boolean checkLeft(){
		try{
			if(xPos - xOffset + xVelocity < 0)
				return false;

			if(World.roomIDs[(xPos - xOffset + xVelocity)/World.cellSize][(yPos - yOffset + 1)/World.cellSize].moveCheck() &&
				World.roomIDs[(xPos - xOffset + xVelocity)/World.cellSize][(yPos + yOffset - 1)/World.cellSize].moveCheck())
				return true;

			else return false;
		}catch(Exception e){return false;}
	}
	boolean checkRight(){
		try{
			if(xPos + xOffset  + xVelocity > World.cellSize * World.worldLength)
				return false;

			if(World.roomIDs[(xPos + xOffset  + xVelocity)/World.cellSize][(yPos - yOffset + 1)/World.cellSize].moveCheck() &&
				World.roomIDs[(xPos + xOffset  + xVelocity)/World.cellSize][(yPos + yOffset - 1)/World.cellSize].moveCheck())
				return true;

			else return false;
		}catch(Exception e){return false;}
	}
///////////////////////////////////////////////////////////////////////////////Combat methods
	void attackUp(){
		attackHitbox = activeWeapon.getHitboxes().get(0);
	}
	void attackDown(){
		attackHitbox = activeWeapon.getHitboxes().get(1);
	}
	void attackLeft(){
		attackHitbox = activeWeapon.getHitboxes().get(2);
	}
	void attackRight(){
		attackHitbox = activeWeapon.getHitboxes().get(3);
	}

	void loseHP(int lostHP){
		HP -= lostHP;
	}

	void hit(Entity attacker){				//Handles what happens when an entity is hit
		hit = true;
		stunned = true;						//Entity is stunned
		int knockback = activeWeapon.getKnockback(), xRes, yRes;
		double angle = 0;
		/////////////////////////////////Handles knockback
		if(attacker.getX() == xPos){
			xRes = knockback;
			yRes = 0;
		}
		else if(attacker.getY() == yPos){
			xRes = 0;
			yRes = knockback;
		}
		try{				//Prevents divide by zero exceptions
			angle = Math.atan((attacker.getY() - yPos)/(attacker.getX() - xPos));
		}catch(Exception e){}

		if(attacker.getX() > xPos){		//Makes residual velocities negative if attacked entity is to the left of the attacker
			xRes = -(int)(Math.cos(angle) * knockback);
			yRes = -(int)(Math.sin(angle) * knockback);
		}
		else{
			xRes = (int)(Math.cos(angle) * knockback);
			yRes = (int)(Math.sin(angle) * knockback);
		}
		xVelocity = xRes;
		yVelocity = yRes;
		stunDelay.start();		//Starts timer to delay the stun going away.
	}

	void showHPBar(Graphics g){
		g.setColor(Color.red);
		g.fillRect(getX() - getXOffset(), getY() + getYOffset() + 10, 2 * getXOffset(), 6);
		g.setColor(Color.green);
		double percent = (double)(getHP())/(double)(getHPMax());
		g.fillRect(getX() - getXOffset(), getY() + getYOffset() + 10, (int)((2 * getXOffset()) * percent), 6);
	}

	void drawAttack(Graphics g){
		g.setColor(Color.white);
		Graphics2D g2 = (Graphics2D)g.create();
		g2.fill(attackHitbox);
	}
	boolean isEntityInRange(Entity e){
		return getEntityDis(e) < activeWeapon.getRange();
	}
//////////////////////////////////////////////////////////////////////////Check distance between this entity and specified entity
	int getEntityDis(Entity e){
		int x = Math.abs(e.getX() - getX()), y = Math.abs(e.getY() - getY());
		return (int)Math.sqrt((x*x) + (y*y));
	}

	int getVerticalDis(Entity e){
		return Math.abs(e.getY() - getY());
	}

	int getHorizontalDis(Entity e){
		return Math.abs(e.getX() - getX());
	}
////////////////////////////////////////////////////////////////////////////ActionListener for Timer
	public void actionPerformed(ActionEvent e){
		if(e.getActionCommand().equals("stun")){
			stunned = false;
			stunDelay.stop();
		}
	}

}
