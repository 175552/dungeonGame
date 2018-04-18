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

	String getFilePath(){
		return filePath;
	}

	void drawRoom(Graphics g, JPanel panel){
		int store = World.cellSize;
		try{
			for(int a = 0; a < World.worldHeight; a++){					//Get int values and convert them into images from the hashmap
				for(int i = 0; i < World.worldLength; i++){						
					World.roomIDs[i][a].getCellAnimation().drawAnimation(g, i*store, a*store, store, store, panel);
				}
			}

		}catch(Exception e){System.out.println("World file read error.");}
	}
}
