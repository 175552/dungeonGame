import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.swing.JPanel;

class AnimationCollection {

	Animation idle, moveUp, moveDown, moveLeft, moveRight, attackUp, attackDown, attackLeft, attackRight, attackAndMoveUp, attackAndMoveDown,
	attackAndMoveLeft, attackAndMoveRight, chargeUp, chargeDown, chargeLeft, chargeRight, chargeAndMoveUp, chargeAndMoveDown,
	chargeAndMoveLeft, chargeAndMoveRight;

	Animation get(String name){
		if(name.equals("idle")){
			return idle;
		}
		else if(name.equals("mUp")){
			return moveUp;
		}
		else if(name.equals("mDown")){
			return moveDown;
		}
		else if(name.equals("mLeft")){
			return moveLeft;
		}
		else if(name.equals("mRight")){
			return moveRight;
		}
		else if(name.equals("aUp")){
			return attackUp;
		}
		else if(name.equals("aDown")){
			return attackDown;
		}
		else if(name.equals("aLeft")){
			return attackLeft;
		}
		else if(name.equals("aRight")){
			return attackRight;
		}
		else if(name.equals("amUp")){
			return attackAndMoveUp;
		}
		else if(name.equals("amDown")){
			return attackAndMoveDown;
		}
		else if(name.equals("amLeft")){
			return attackAndMoveLeft;
		}
		else if(name.equals("amRight")){
			return attackAndMoveRight;
		}
		else if(name.equals("cUp")){
			return chargeUp;
		}
		else if(name.equals("cDown")){
			return chargeDown;
		}
		else if(name.equals("cLeft")){
			return chargeLeft;
		}
		else if(name.equals("cRight")){
			return chargeRight;
		}
		else if(name.equals("cmUp")){
			return chargeAndMoveUp;
		}
		else if(name.equals("cmDown")){
			return chargeAndMoveDown;
		}
		else if(name.equals("cmLeft")){
			return chargeAndMoveLeft;
		}
		else if(name.equals("cmRight")){
			return chargeAndMoveRight;
		}
		else{
			System.out.println("AnimationCollection intake string error.");
			return null;
		}
	}

	void assignAnim(String name, Animation a){
		if(name.equals("idle")){
			idle = new Animation(a);
		}
		else if(name.equals("mUp")){
			moveUp = new Animation(a);
		}
		else if(name.equals("mDown")){
			moveDown = new Animation(a);
		}
		else if(name.equals("mLeft")){
			moveLeft = new Animation(a);
		}
		else if(name.equals("mRight")){
			moveRight = new Animation(a);
		}
		else if(name.equals("aUp")){
			attackUp = new Animation(a);
		}
		else if(name.equals("aDown")){
			attackDown = new Animation(a);
		}
		else if(name.equals("aLeft")){
			attackLeft = new Animation(a);
		}
		else if(name.equals("aRight")){
			attackRight = new Animation(a);
		}
		else if(name.equals("amUp")){
			attackAndMoveUp = new Animation(a);
		}
		else if(name.equals("amDown")){
			attackAndMoveDown = new Animation(a);
		}
		else if(name.equals("amLeft")){
			attackAndMoveLeft = new Animation(a);
		}
		else if(name.equals("amRight")){
			attackAndMoveRight = new Animation(a);
		}
		else if(name.equals("cUp")){
			chargeUp = new Animation(a);
		}
		else if(name.equals("cDown")){
			chargeDown = new Animation(a);
		}
		else if(name.equals("cLeft")){
			chargeLeft = new Animation(a);
		}
		else if(name.equals("cRight")){
			chargeRight = new Animation(a);
		}
		else if(name.equals("cmUp")){
			chargeAndMoveUp = new Animation(a);
		}
		else if(name.equals("cmDown")){
			chargeAndMoveDown = new Animation(a);
		}
		else if(name.equals("cmLeft")){
			chargeAndMoveLeft = new Animation(a);
		}
		else if(name.equals("cmRight")){
			chargeAndMoveRight = new Animation(a);
		}
		else{
			System.out.println("AnimationCollection intake string error.");
		}
	}
}
