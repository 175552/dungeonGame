import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;

public class Player extends Entities {

	Player(){
		xPos = 25;
		yPos = 400;
		movespeed = 5;
		xOffset = 25;
		yOffset = 25;

		try{
			sprite = ImageIO.read(new File("../resources/textures/blueSquare.png"));
		}catch(IOException e){System.out.println("Player image not found.");}
	}

}
