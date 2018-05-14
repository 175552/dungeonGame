import java.awt.image.BufferedImage;

abstract class Equipment {
	int ATKchange, DEFchange;
	BufferedImage sprite;

	BufferedImage getSprite(){
		return sprite;
	}

	int getATK(){
		return ATKchange;
	}

	int getDEF(){
		return DEFchange;
	}
}
