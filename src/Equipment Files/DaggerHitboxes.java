import java.awt.geom.*;
import java.awt.*;

public class DaggerHitboxes extends WeaponHitboxes {

	double center = 10, size = 70, offset = 10;

	DaggerHitboxes(){
		Area base = new Area(new Ellipse2D.Double(center, center, size, size));
		for(int i = 0; i < 4; i++){
			hitboxes[i] = new Area(base);
			switch(i){
				case 0:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(center, center + offset, size, size)));
					break;
				case 1:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(center, center - offset, size, size)));
					break;
				case 2:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(center + offset, center, size, size)));
					break;
				case 3:
					hitboxes[i].subtract(new Area(new Ellipse2D.Double(center - offset, center, size, size)));
					break;
			}
		}
	}
}
