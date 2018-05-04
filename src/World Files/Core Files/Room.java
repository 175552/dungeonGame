import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.image.*;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Room{
	String filePath;
	int doorCount;
	boolean[] doorLocations;				//[Up, Down, Left, Right], if true, a door generates there. Else no door.

	Cell[][] roomIDs = new Cell[World.worldLength][World.worldHeight];

	ArrayList<Enemies> enemyList = new ArrayList<Enemies>();		//The list of enemies for this room

	Room(String url){
		filePath = url;												//Sets the filepath
		try{
			Scanner input = new Scanner(new File(filePath));		//Creates a scanner for the file with the room
			for(int y = 0; y < World.worldHeight; y++){
				for(int x = 0; x < World.worldLength; x++){
					roomIDs[x][y] = new Cell(input.nextInt());		//Creates the cells in the room
				}
			}
		}catch(Exception e){System.out.println("Room creation error");}
		doorLocations = World.roomList.get(filePath);
		generateEnemies();
	}

	String getFilePath(){
		return filePath;
	}

	void generateEnemies(){											//Creates the enemies in this room at the assigned positions
		for(int a = 0; a < World.worldHeight; a++){
			for(int i = 0; i < World.worldLength; i++){
				if(roomIDs[i][a].spawnCheck()){
					enemyList.add(new Enemies(i*World.cellSize + (World.cellSize/2), a*World.cellSize + (World.cellSize/2)));
				}
			}
		}
	}

	boolean checkUp(){
		return doorLocations[0];
	}
	boolean checkDown(){
		return doorLocations[1];
	}
	boolean checkLeft(){
		return doorLocations[2];
	}
	boolean checkRight(){
		return doorLocations[3];
	}

	void setDoors(boolean[] input){
		doorLocations = input;
	}

	void drawRoom(Graphics g, JPanel panel){
		int store = World.cellSize;
		try{
			for(int a = 0; a < World.worldHeight; a++){					//Get int values and convert them into images from the hashmap
				for(int i = 0; i < World.worldLength; i++){
					roomIDs[i][a].getCellAnimation().drawAnimation(g, i*store, a*store, store, store, panel);
				}
			}

		}catch(Exception e){System.out.println("World file read error.");}
	}
}
