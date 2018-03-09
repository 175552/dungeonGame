import java.awt.geom.*;
import java.awt.*;

public class DaggerHitboxes extends WeaponHitboxes {

	double size = 100, offset = 7, cx = 10, cy = 10;

	DaggerHitboxes(){
		Area base = new Area(new Ellipse2D.Double(cx, cy, size, size));
		for(int i = 0; i < 4; i++){
			hitboxes[i] = new Area(base);
			switch(i){
				case 0:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(cx, cy + offset, size, size)));
					break;
				case 1:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(cx, cy - offset, size, size)));
					break;
				case 2:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(cx + offset, cy, size, size)));
					break;
				case 3:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(cx - offset, cy, size, size)));
					break;
			}
		}
	}
	void moveHitbox(Entities e, int x, int y){
		x -= ((size/2) - e.getXOffset());
		y -= ((size/2) - e.getYOffset());
		Area base = new Area(new Ellipse2D.Double(x, y, size, size));
		for(int i = 0; i < 4; i++){
			hitboxes[i] = new Area(base);
			switch(i){
				case 0:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(x, y + offset, size, size)));
					break;
				case 1:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(x, y - offset, size, size)));
					break;
				case 2:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(x + offset, y, size, size)));
					break;
				case 3:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(x - offset, y, size, size)));
					break;
			}
		}
	}
}
