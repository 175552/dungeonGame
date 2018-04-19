import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.*;

class Animation {

	private BufferedImage[] images;				//Meant to contain all images used in the animation

	private int[] times;						//Contains the last time in frames that the image associated with said index is used.
												//Ex: If times[0] = 10, then the first image will be used from frames 0-10.

	private int currentTime = 0, maxTime;

	Animation(BufferedImage[] imagesIn, int[] timesIn){					//Creates the arrays to be used and fills them
		if(imagesIn.length == timesIn.length){
			int length = imagesIn.length;
			images = new BufferedImage[length];
			times = new int[length];
			for(int i = 0; i < imagesIn.length; i++){
				images[i] = imagesIn[i];
				times[i] = timesIn[i];
				if(i > 0){
					if(times[i] <= times[i-1]){
						System.out.println("Animation time array mismatch error: Index- " + i);
					}
				}
			}
			maxTime = times[times.length - 1];
		}
		else System.out.println("Input arrays are not equal in length: Images- " + imagesIn.length + "; Times- " + timesIn.length);
	}

	Animation(File file){
		Scanner input = null;
		try{
			input = new Scanner(file);
		}
		catch(Exception e){
			System.out.println("Animation File Read error");
		}
		List<String> links = new ArrayList<String>();
		List<Integer> timesList = new ArrayList<Integer>();
		String temp = "";
		while(input.hasNextLine()){
			temp = input.nextLine();
			String[] tempArray = temp.split(";");
			links.add(tempArray[0]);
			timesList.add(Integer.parseInt(tempArray[1]));
		}
		images = Cell.listToBIArray(links);
		times = Cell.listToIntArray(timesList);
	}

	Animation(Animation test){
		images = test.getBIArray();
		times = test.getTimesArray();
		maxTime = times[times.length - 1];
	}

	void drawAnimation(Graphics g, int xPos, int yPos, int xSize, int ySize, JPanel panel){ //Draws the current frame in the animation and iterates to the next frame
		g.drawImage(selectImage(), xPos, yPos, xSize, ySize, panel);
		if(currentTime == maxTime)
			currentTime = 0;
		else currentTime++;
	}
	private BufferedImage selectImage(){			//Gets the current active frame from the animation and sends out an error message if there was an issue
		for(int i = 0; i < times.length; i++){
			if(currentTime <= times[i])
				return images[i];
		}
		System.out.println("Image selection error");
		return null;
	}

	private BufferedImage[] getBIArray(){
		return images;
	}

	private int[] getTimesArray(){
		return times;
	}

	int getMaxTime(){
		return maxTime;
	}
}