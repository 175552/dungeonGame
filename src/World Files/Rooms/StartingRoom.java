
public class StartingRoom extends Room {

	static Animation roomDrawing;

	StartingRoom(){
		filePath = "../maps/map.txt";
		doorCount = 4;
		for(int i = 0; i < 4; i++){
			doorLocations[i] = true;
		}
	}
}
