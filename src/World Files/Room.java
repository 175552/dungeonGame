
abstract class Room{
	String filePath;
	int doorCount;
	boolean[] doorLocations = new boolean[4];				//[Up, Down, Left, Right], if true, a door generates there. Else no door.

	String getFilePath(){
		return filePath;
	}
}
