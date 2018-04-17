
public class StartingRoom extends Room {

	StartingRoom(){
		filePath = "../maps/map.txt";
		doorCount = 4;
		for(int i = 0; i < 4; i++){
			doorLocations[i] = true;
		}
	}
}
