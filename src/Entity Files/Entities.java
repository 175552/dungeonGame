import java.util.*;
import java.awt.image.*;

abstract class Entities {
	int xPos, yPos, movespeed, xOffset, yOffset;			//Position and movement information

	int HP, baseATK, baseATKSPD, baseDEF;

	BufferedImage sprite;
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
//////////////////////////////////////////////////////////////////////////////Direction checking methods
	boolean checkUp(){
		try{
			if(getY() - getYOffset() - movespeed < 0)
				return false;

			if(World.worldIDs[(xPos - xOffset + 1)/World.cellSize][(yPos - yOffset - movespeed)/World.cellSize].moveCheck() &&
				World.worldIDs[(xPos + xOffset - 1)/World.cellSize][(yPos - yOffset - movespeed)/World.cellSize].moveCheck())
					return true;

			else return false;
		}catch(Exception e){return false;}
	}
	boolean checkDown(){
		try{
			if(yPos + yOffset + movespeed > World.cellSize * World.worldHeight)
				return false;

			if(World.worldIDs[(xPos - xOffset + 1)/World.cellSize][(yPos + yOffset + movespeed)/World.cellSize].moveCheck() &&
				World.worldIDs[(xPos + xOffset - 1)/World.cellSize][(yPos + yOffset + movespeed )/World.cellSize].moveCheck())
					return true;

			else return false;
		}catch(Exception e){return false;}
	}
	boolean checkLeft(){
		try{
			if(xPos - xOffset - movespeed < 0)
				return false;

			if(World.worldIDs[(xPos - xOffset - movespeed)/World.cellSize][(yPos - yOffset + 1)/World.cellSize].moveCheck() &&
				World.worldIDs[(xPos - xOffset - movespeed)/World.cellSize][(yPos + yOffset - 1)/World.cellSize].moveCheck())
				return true;

			else return false;
		}catch(Exception e){return false;}
	}
	boolean checkRight(){
		try{
			if(xPos + xOffset + movespeed > World.cellSize * World.worldLength)
				return false;

			if(World.worldIDs[(xPos + xOffset + movespeed)/World.cellSize][(yPos - yOffset + 1)/World.cellSize].moveCheck() &&
				World.worldIDs[(xPos + xOffset + movespeed)/World.cellSize][(yPos + yOffset - 1)/World.cellSize].moveCheck())
				return true;

			else return false;
		}catch(Exception e){return false;}
	}
///////////////////////////////////////////////////////////////////////////////Combat methods
	void attack(ArrayList<Entities> hitEntities){

	}
	ArrayList<Entities> checkHitEntities(){
		ArrayList<Entities> hitEntities = new ArrayList<Entities>();
		return null;
	}
	BufferedImage getSprite(){
		return sprite;
	}
}
