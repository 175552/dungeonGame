import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.image.*;
import java.awt.Graphics2D;

public class Cell {

	boolean canMove, canSpawn;

	int id;

	Scanner reader = new Scanner(new File("../resources/IDs and Properties.txt"));

	static Map<Integer, BufferedImage> idToTexture = new HashMap<Integer, BufferedImage>();
	static Map<Integer, Animation> idToAnim = new HashMap<Integer, Animation>();

	Animation cellAnimation;

	Cell(int idInput) throws Exception{
		id = idInput;										//Gets the int id to be used for HashMaps
		String properties = "";								//Stores what properties the cell has
		while(reader.hasNextLine()){
			String temp = reader.nextLine();				//Grabs a string from a file for reading the properties.
			if(!temp.substring(0, 2).equals("//")){
				if(temp.split(":")[0].equals(id + "")){
					properties = temp.split(":")[1];
					break;
				}
			}
		}													//Assigns boolean values for the properties
		String move = properties.split(";")[0], spawn = properties.split(";")[1];
		if(move.equals("True")){
			canMove = true;
		}
		else if(move.equals("False")){
			canMove = false;
		}
		else System.out.println("Move property error at " + id);

		if(spawn.equals("True")){
			canSpawn = true;
		}
		else if(spawn.equals("False")){
			canSpawn = false;
		}
		else System.out.println("Spawn property error at tile type " + id);

		cellAnimation = idToAnim.get(id);					//Assigns this cell the animation assigned to that ID from the HashMap
	}

	boolean moveCheck(){
		return canMove;
	}
	boolean spawnCheck(){
		return canSpawn;
	}
	static void idToHashMap(){
		try{
			Scanner temp = new Scanner(new File("../resources/Texture Links.txt"));
			int i = 0;
			while(temp.hasNextLine()){
				idToTexture.put(i, ImageIO.read(new File(temp.nextLine())).getSubimage(0, 0, World.cellSize, World.cellSize));
				i++;
			}
		}catch(Exception e){System.out.println("ID to HashMap file read error");}
	}

	static void idToAnimationMap(){							//Creates a HashMap for sorting animations for each cell
		try{
			String temp = "";
			int num = 0;

			List<String> links = new ArrayList<String>();
			List<Integer> times = new ArrayList<Integer>();

			Scanner input = new Scanner(new File("../resources/Animation Texture Links.txt"));
			while(input.hasNextLine()){
				temp = input.nextLine();
				if(temp.equals("END")){
					idToAnim.put(num, new Animation(listToBIArray(links), listToIntArray(times)));
					num++;
				}
				else{
					String[] tempArray = temp.split(";");
					links.add(tempArray[0]);
					times.add(Integer.parseInt(tempArray[1]));
				}
			}
		}catch(Exception e){System.out.println("Animation ID to HashMap file read error");}
	}

	private static BufferedImage[] listToBIArray(List<String> links){
		BufferedImage[] images = new BufferedImage[links.size()];
		for(int i = 0; i < images.length; i++){
			try{
			images[i] = ImageIO.read(new File(links.get(i))).getSubimage(0, 0, World.cellSize, World.cellSize);
			}catch(Exception e){System.out.println("List to Buffered Image Array file read error.");}
		}
		return images;
	}

	private static int[] listToIntArray(List<Integer> nums){
		int[] numArray = new int[nums.size()];
		for(int i = 0; i < numArray.length; i++){
			numArray[i] = nums.get(i);
		}
		return numArray;
	}

	Animation getCellAnimation(){
		return cellAnimation;
	}
}
