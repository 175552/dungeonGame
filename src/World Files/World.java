
public class World{
	static int worldLength = 17, worldHeight = 11, cellSize = 75, framerate = 50, xRoomCount = 3, yRoomCount = 3;
	static int currentX = 0, currentY = 0;
	static Cell[][] roomIDs = new Cell[worldLength][worldHeight];
	static Room[][] rooms = new Room[xRoomCount][yRoomCount];
	static Player p1 = new Player();

	static void setup(){
		rooms[xRoomCount/2][yRoomCount/2] = new StartingRoom();
		currentX = xRoomCount/2;
		currentY = yRoomCount/2;
	}

	static Room getCurrentRoom(){
		return rooms[currentX][currentY];
	}
}
