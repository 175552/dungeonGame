import java.util.*;
import java.io.*;

public class World{
	static int worldLength = 17, worldHeight = 11, cellSize = 75, framerate = 50, xRoomCount, yRoomCount;
	static int currentX = 0, currentY = 0;
	static Cell[][] roomIDs = new Cell[worldLength][worldHeight];
	static Room[][] rooms = new Room[xRoomCount][yRoomCount];
	static Player p1 = new Player();
	static HashMap<String, boolean[]> roomList = new HashMap<String, boolean[]>();

	static void setup(){
		//rooms[xRoomCount/2][yRoomCount/2] = new Room("../maps/rooms/map.txt");
		currentX = xRoomCount/2;
		currentY = yRoomCount/2;
	}

	static void createWorld(File f){										//File f is meant to be a world file, in the worlds folder
		try{
			Scanner input = new Scanner(f);									//Scanner for reading files
			HashMap<String, String> urlStorage = new HashMap<String, String>();	//Hashmap for storing shorthand for file names in file

			String[] nums = input.nextLine().split(";");						//Gets the amount of rooms in the x and y direction
			int x = Integer.parseInt(nums[0]), y = Integer.parseInt(nums[1]);	//And parses them into ints.
			rooms = new Room[x][y];												//Creates a new room array
			xRoomCount = x;
			yRoomCount = y;

			int urlCount = input.nextInt();										//Gets the number of URLs that have shorthands
			input.nextLine();													//Resets the scanner

			for(int i = 0; i < urlCount; i++){									//Gets all of the URL values
				String[] urlMapValues = input.nextLine().split("=");
				urlStorage.put(urlMapValues[0], urlMapValues[1]);				//And puts them into a hashmap
			}

			for(int i = 0; i < y; i++){
				String[] roomNames = input.nextLine().split(" ");				//Gets the shorthand for each room
				for(int a = 0; a < x; a++){
					if(!urlStorage.get(roomNames[a]).equals("n")){				//Checks if the room name is n, if it is, that space is empty
						rooms[a][i] = new Room(urlStorage.get(roomNames[a]));	//If not, create a new room with that file path
					}
				}
			}
		}catch(Exception e){System.out.println("World creation error");}

	}

	static boolean checkUp(){			/////////////////////////////////////Checks if the room the player is going to is null
		try{
			if(rooms[currentX][currentY-1] == null)
				return false;
			else return true;
		}catch(Exception e){return false;}
	}
	static boolean checkDown(){
		try{
			if(rooms[currentX][currentY+1] == null)
				return false;
			else return true;
		}catch(Exception e){return false;}
	}
	static boolean checkLeft(){
		try{
			if(rooms[currentX-1][currentY] == null)
				return false;
			else return true;
		}catch(Exception e){return false;}
	}
	static boolean checkRight(){
		try{
			if(rooms[currentX+1][currentY] == null)
				return false;
			else return true;
		}catch(Exception e){return false;}
	}

	static void exitUp(){		/////////////////////////////////////////////Moves the character to a different room
		if(currentY != 0){
			currentY--;
			p1.setX((worldLength*cellSize)/2);
			p1.setY((worldHeight*cellSize) - p1.getYOffset());
		}
	}

	static void exitDown(){
		if(currentY != roomIDs[currentX].length){
			currentY++;
			p1.setX((worldHeight*cellSize)/2);
			p1.setY(p1.getYOffset());
		}
	}

	static void exitLeft(){
		if(currentX != 0){
			currentX--;
			p1.setX((worldLength*cellSize) - p1.getXOffset());
			p1.setY((worldHeight*cellSize)/2);
		}
	}

	static void exitRight(){
		if(currentX != roomIDs.length){
			currentX++;
			p1.setX(p1.getXOffset());
			p1.setY((worldHeight*cellSize)/2);
		}
	}

	static void createRoomList(){			//////////////////Creates a hashmap that contains which doors are usable for each room
		try{
			Scanner input = new Scanner(new File("../maps/rooms/doorList.txt"));
			while(input.hasNextLine()){
				String[] temp = input.nextLine().split(";"), temp2 = temp[1].split(",");
				boolean[] doors = new boolean[4];
				for(int i = 0; i < 4; i++){
					if(temp2[i].equals("t")){
						doors[i] = true;
					}
					else if(temp2[i].equals("f")){
						doors[i] = false;
					}
				}
				roomList.put(temp[0], doors);
			}
		}catch(Exception e){System.out.println("Room list creation error");}
	}

	static Room getCurrentRoom(){			//////////////////Returns the room the player is currently in
		return rooms[currentX][currentY];
	}
}
