import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Enemies extends Entity{

	boolean moveUp, moveDown, moveLeft, moveRight, aggroed = true;

	Enemies(){
		try{
			sprite = ImageIO.read(new File("../resources/textures/yellowSquare.png"));
		}catch(IOException e){System.out.println("Enemy sprite not found.");}
		movespeed = 2;
		xOffset = 25;
		yOffset = 25;
	}

	void chasePlayer(Player p){
		if(aggroed){
			if(moveUp && getY() - p.getY() >= movespeed)
				moveUp();
			else if(moveUp)
				moveUpDis(getY() - p.getY());

			if(moveDown && p.getY() - getY() >= movespeed)
				moveDown();
			else if(moveDown)
				moveDownDis(p.getY() - getY());

			if(moveLeft && getX() - p.getX() >= movespeed)
				moveLeft();
			else if(moveLeft)
				moveLeftDis(getX() - p.getX());

			if(moveRight && p.getX() - getX() >= movespeed)
				moveRight();
			else if(moveRight)
				moveRightDis(p.getX() - getX());
		}
	}

	void checkCurrentPos(Player p){
		boolean upI = moveUp, downI = moveDown, leftI = moveLeft, rightI = moveRight;
		if(p.getY() < getY()){
			upI = true;
			downI = false;
		}
		else if(p.getY() > getY()){
			downI = true;
			upI = false;
		}
		else{
			upI = false;
			downI = false;
		}
		if(p.getX() < getX()){
			leftI = true;
			rightI = false;
		}
		else if(p.getX() > getX()){
			rightI = true;
			leftI = false;
		}
		else{
			leftI = false;
			rightI = false;
		}
		boolean hesitate =  moveUp =! upI ? true :
					moveDown != downI ? true :
					moveLeft != leftI ? true :
					moveRight != rightI ? true :
					false;


		moveUp = upI;
		moveDown = downI;
		moveLeft = leftI;
		moveRight = rightI;
	}
}
