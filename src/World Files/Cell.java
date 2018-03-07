import java.io.*;
import java.util.*;

public class Cell {

	boolean canMove, canSpawn;

	int id;

	Scanner reader = new Scanner(new File("../resources/IDs and Properties.txt"));

	Cell(int idInput) throws Exception{
		id = idInput;
		String properties = "";
		while(reader.hasNextLine()){
			String temp = reader.nextLine();
			if(!temp.substring(0, 2).equals("//")){
				if(temp.split(":")[0].equals(id + "")){
					properties = temp.split(":")[1];
					break;
				}
			}
		}
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
		else System.out.println("Spawn property error at " + id);
	}

	boolean moveCheck(){
		return canMove;
	}
	boolean spawnCheck(){
		return canSpawn;
	}
}
