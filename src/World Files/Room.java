import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.image.*;
import java.awt.Graphics;
import javax.swing.JPanel;

abstract class Room{
	String filePath;
	int doorCount;
	boolean[] doorLocations = new boolean[4];				//[Up, Down, Left, Right], if true, a door generates there. Else no door.
	Animation roomDrawing;

	String getFilePath(){
		return filePath;
	}

	void drawWorld(Graphics g, JPanel panel){
		int store = World.cellSize;
		try{
			Scanner input = new Scanner(new File(World.rooms[World.currentX][World.currentY].getFilePath()));
			for(int a = 0; a < World.worldHeight; a++){					//Get int values and convert them into images from the hashmap
				for(int i = 0; i < World.worldLength; i++){
					try{
						int temp = input.nextInt();
						World.roomIDs[i][a] = new Cell(temp);
						World.roomIDs[i][a].getCellAnimation().drawAnimation(g, i*store, a*store, store, store, panel);
					}catch(Exception e){System.out.println("World draw error: " + e);}
				}
			}

		}catch(Exception e){System.out.println("World file read error.");}
	}
}
