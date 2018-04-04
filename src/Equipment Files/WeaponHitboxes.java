import java.awt.geom.*;

abstract class WeaponHitboxes{
	Area[] hitboxes = new Area[4];

	int range;

	Area get(int index){
		return hitboxes[index];
	}
	abstract void moveHitbox(Entity e, int x, int y);
}
