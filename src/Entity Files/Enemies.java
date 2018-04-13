import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Enemies extends Entity{

	boolean playerUp, playerDown, playerLeft, playerRight, aggroed = true;

	int aggroRange = 250, holdAggroRange = 400;

	Enemies(int x, int y){
		try{
			sprite = ImageIO.read(new File("../resources/textures/yellowSquare.png"));
		}catch(IOException e){System.out.println("Enemy sprite not found.");}
		defaultMovespeed = 3;
		acceleration = 2;
		xPos = x;
		yPos = y;
		xOffset = 25;
		yOffset = 25;
		activeWeapon = new BrokenDagger();
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
		if(aggroRange > getEntityDis(p))			//If player is in enemy's aggro range, enemy is aggroed
			aggroed = true;
		else if(aggroed && holdAggroRange < getEntityDis(p))//If enemy is aggroed and player moves out of a certain range, disable aggro
			aggroed = false;
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
