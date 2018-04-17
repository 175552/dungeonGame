import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.swing.JPanel;

class Animation {

	BufferedImage[] images;				//Meant to contain all images used in the animation

	int[] times;						//Contains the last time in frames that the image associated with said index is used.
										//Ex: If times[0] = 10, then the first image will be used from frames 0-10.

	int currentTime = 0, maxTime;

	Animation(BufferedImage[] imagesIn, int[] timesIn){					//Creates the arrays to be used and fills them
		if(imagesIn.length == timesIn.length){
			int length = imagesIn.length;
			images = new BufferedImage[length];
			times = new int[length];
			for(int i = 0; i < imagesIn.length; i++){
				images[i] = imagesIn[i];
				times[i] = timesIn[i];
			}
			maxTime = times[times.length - 1];
		}
		else System.out.println("Input arrays are not equal in length: Images- " + imagesIn.length + "; Times- " + timesIn.length);
	}

	void drawAnimation(Graphics g, int xPos, int yPos, int xSize, int ySize, JPanel panel){ //Draws the current frame in the animation and iterates to the next frame
		g.drawImage(selectImage(), xPos, yPos, xSize, ySize, panel);
		if(currentTime == maxTime)
			currentTime = 0;
		else currentTime++;
	}
	BufferedImage selectImage(){			//Gets the current active frame from the animation and sends out an error message if there was an issue
		for(int i = 0; i < times.length; i++){
			if(currentTime <= times[i])
				return images[i];
		}
		System.out.println("Image selection error");
		return null;
	}

	int getMaxTime(){
		return maxTime;
	}
}