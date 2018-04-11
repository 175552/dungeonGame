import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

abstract class Entity {
	int xPos, yPos, movespeed, defaultMovespeed, acceleration, xVelocity, yVelocity, xOffset, yOffset;			//Position and movement information

	int HP = 1000, HPMax = 1000, baseATK, baseATKSPD, baseDEF;

	boolean attacksUp, attacksDown, attacksLeft, attacksRight, hit, xMove, yMove;

	BufferedImage sprite;

	Area attackHitbox = new Area(new Rectangle2D.Double(0, 0, 0, 0));

	Weapons activeWeapon;
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
	BufferedImage getSprite(){
		return sprite;
	}
	Area getAttackHitbox(){
		return attackHitbox;
	}
	Weapons getWeapon(){
		return activeWeapon;
	}
//////////////////////////////////////////////////////////////////////////Movement methods
	void moveUp(){
		if(yVelocity > -1 * defaultMovespeed){
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
		if(yVelocity < defaultMovespeed){
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
		if(xVelocity > -1 * defaultMovespeed){
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
		if(xVelocity < defaultMovespeed){
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
	void slowSpeed(){
		if(movespeed == defaultMovespeed)
			movespeed /= 2;
	}
	void returnSpeed(){
		if(movespeed != defaultMovespeed)
			movespeed = defaultMovespeed;
	}
//////////////////////////////////////////////////////////////////////////////Velocity movement methods
	void move(){
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
			//System.out.println(yPos + ", " + yOffset + ", " + yVelocity);
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

			if(World.roomIDs[(xPos - xOffset + 1)/World.cellSize][(yPos + yOffset + movespeed)/World.cellSize].moveCheck() &&
				World.roomIDs[(xPos + xOffset - 1)/World.cellSize][(yPos + yOffset + movespeed )/World.cellSize].moveCheck())
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
		activeWeapon.getHitboxes().moveHitbox(this, xPos - xOffset, yPos - yOffset);
		attackHitbox = activeWeapon.getHitboxes().get(0);
		attacksDown = false;
		attacksLeft = false;
		attacksRight = false;
	}
	void attackDown(){
		activeWeapon.getHitboxes().moveHitbox(this, xPos - xOffset, yPos - yOffset);
		attackHitbox = activeWeapon.getHitboxes().get(1);
		attacksUp = false;
		attacksLeft = false;
		attacksRight = false;
	}
	void attackLeft(){
		activeWeapon.getHitboxes().moveHitbox(this, xPos - xOffset, yPos - yOffset);
		attackHitbox = activeWeapon.getHitboxes().get(2);
		attacksDown = false;
		attacksUp = false;
		attacksRight = false;
	}
	void attackRight(){
		activeWeapon.getHitboxes().moveHitbox(this, xPos - xOffset, yPos - yOffset);
		attackHitbox = activeWeapon.getHitboxes().get(3);
		attacksDown = false;
		attacksLeft = false;
		attacksUp = false;
	}

	void loseHP(int lostHP){
		HP -= lostHP;
	}

	void hit(){
		hit = true;
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
}
