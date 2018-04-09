import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Enemies extends Entity{

	boolean playerUp, playerDown, playerLeft, playerRight, aggroed = true;

	int aggroRange = 250;

	Enemies(int x, int y){
		try{
			sprite = ImageIO.read(new File("../resources/textures/yellowSquare.png"));
		}catch(IOException e){System.out.println("Enemy sprite not found.");}
		movespeed = 5;
		defaultMovespeed = 3;
		xPos = x;
		yPos = y;
		xOffset = 25;
		yOffset = 25;
		activeWeapon = new BrokenDagger();
	}

	void chasePlayer(Player p){
		if(aggroed && getEntityDis(p) > activeWeapon.getRange()/2){
			if(playerUp && getY() - p.getY() >= movespeed)
				moveUp();
			else if(playerUp)
				moveUpDis(getY() - p.getY());

			if(playerDown && p.getY() - getY() >= movespeed)
				moveDown();
			else if(playerDown)
				moveDownDis(p.getY() - getY());

			if(playerLeft && getX() - p.getX() >= movespeed)
				moveLeft();
			else if(playerLeft)
				moveLeftDis(getX() - p.getX());

			if(playerRight && p.getX() - getX() >= movespeed)
				moveRight();
			else if(playerRight)
				moveRightDis(p.getX() - getX());
		}
	}

	void checkCurrentPos(Player p){
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
		////////////////////////////////Aggro code
		if(aggroRange > getEntityDis(p)){
			aggroed = true;
		}
		else aggroed = false;
	}

	void aggroOff(){
		aggroed = false;
	}

	void attackPlayer(Player p){
		if(getVerticalDis(p) >= getHorizontalDis(p) && playerUp)
			attackUp();
		else if(getVerticalDis(p) >= getHorizontalDis(p) && playerDown)
			attackDown();
		else if(getHorizontalDis(p) >= getVerticalDis(p) && playerLeft)
			attackLeft();
		else if(getHorizontalDis(p) >= getVerticalDis(p) && playerRight)
			attackRight();
	}
}
