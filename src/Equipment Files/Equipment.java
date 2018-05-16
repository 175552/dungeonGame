import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

abstract class Equipment {
	int ATKchange, DEFchange;
	String spriteURL;

	BufferedImage getSprite(){
		try{
			return (BufferedImage)ImageIO.read(new File(spriteURL));
		}catch(Exception e){
			if(spriteURL == null)
				System.out.println("No weapon image URL");
			else System.out.println("File not found exception: " + spriteURL);
			return null;
		}
	}

	int getATK(){
		return ATKchange;
	}

	int getDEF(){
		return DEFchange;
	}
}
