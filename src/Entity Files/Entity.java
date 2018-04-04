import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

abstract class Entity {
	int xPos, yPos, movespeed, defaultMovespeed, xOffset, yOffset;			//Position and movement information

	int HP = 1000, HPMax = 1000, baseATK, baseATKSPD, baseDEF;

	boolean attacksUp, attacksDown, attacksLeft, attacksRight, hit;

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
		yPos -= movespeed;
	}
	void moveUpDis(int dis){
		yPos -= dis;
	}
	void moveDown(){
		yPos += movespeed;
	}
	void moveDownDis(int dis){
		yPos += dis;
	}
	void moveLeft(){
		xPos -= movespeed;
	}
	void moveLeftDis(int dis){
		xPos -= dis;
	}
	void moveRight(){
		xPos += movespeed;
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
//////////////////////////////////////////////////////////////////////////////Direction checking methods
	boolean checkUp(){
		try{
			if(getY() - getYOffset() - movespeed < 0)
				return false;

			if(World.roomIDs[(xPos - xOffset + 1)/World.cellSize][(yPos - yOffset - movespeed)/World.cellSize].moveCheck() &&
				World.roomIDs[(xPos + xOffset - 1)/World.cellSize][(yPos - yOffset - movespeed)/World.cellSize].moveCheck())
					return true;

			else return false;
		}catch(Exception e){return false;}
	}
	boolean checkDown(){
		try{
			if(yPos + yOffset + movespeed > World.cellSize * World.worldHeight)
				return false;

			if(World.roomIDs[(xPos - xOffset + 1)/World.cellSize][(yPos + yOffset + movespeed)/World.cellSize].moveCheck() &&
				World.roomIDs[(xPos + xOffset - 1)/World.cellSize][(yPos + yOffset + movespeed )/World.cellSize].moveCheck())
					return true;

			else return false;
		}catch(Exception e){return false;}
	}
	boolean checkLeft(){
		try{
			if(xPos - xOffset - movespeed < 0)
				return false;

			if(World.roomIDs[(xPos - xOffset - movespeed)/World.cellSize][(yPos - yOffset + 1)/World.cellSize].moveCheck() &&
				World.roomIDs[(xPos - xOffset - movespeed)/World.cellSize][(yPos + yOffset - 1)/World.cellSize].moveCheck())
				return true;

			else return false;
		}catch(Exception e){return false;}
	}
	boolean checkRight(){
		try{
			if(xPos + xOffset + movespeed > World.cellSize * World.worldLength)
				return false;

			if(World.roomIDs[(xPos + xOffset + movespeed)/World.cellSize][(yPos - yOffset + 1)/World.cellSize].moveCheck() &&
				World.roomIDs[(xPos + xOffset + movespeed)/World.cellSize][(yPos + yOffset - 1)/World.cellSize].moveCheck())
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
//////////////////////////////////////////////////////////////////////////Check distance between this entity and specified entity
	int getEntityDis(Entity e){
		int x = Math.abs(e.getX() - getX()), y = Math.abs(e.getY() - getY());
		return (int)Math.sqrt((x*x) + (y*y));
	}
}
