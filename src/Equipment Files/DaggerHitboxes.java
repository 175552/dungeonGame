import java.awt.geom.*;
import java.awt.*;

public class DaggerHitboxes extends WeaponHitboxes {

	double offset = 7, par = 120, perp = 70;		//offset = offset of circles to create arc.
													//par = diameter of ellipse along the parallel to the direction the player is aiming
													//perp = diameter of ellipse along the perpendicular to the direction the player is aiming


	void moveHitbox(Entity e, int x, int y){			//Recreates Area at player location for each attack
		for(int i = 0; i < 4; i++){
			switch(i){
				case 0:
					x -= ((perp/2) - e.getXOffset());		//Sets the bounds for the arc
					y -= ((par/2) - e.getYOffset());

					hitboxes[i] = new Area(new Ellipse2D.Double(x, y, perp, par));		//Adds an ellipse area to the hitboxes array

					hitboxes[i].subtract(new Area(new Ellipse2D.Double(x, y + offset, perp, par)));		//Subtracts an ellipse of the same size but offset

					x += ((perp/2) - e.getXOffset());
					y += ((par/2) - e.getYOffset());		//Resets the bounds for the next hitbox
					break;

				case 1:
					x -= ((perp/2) - e.getXOffset());
					y -= ((par/2) - e.getYOffset());

					hitboxes[i] = new Area(new Ellipse2D.Double(x, y, perp, par));

					hitboxes[i].subtract(new Area(new Ellipse2D.Double(x, y - offset, perp, par)));

					x += ((perp/2) - e.getXOffset());
					y += ((par/2) - e.getYOffset());
					break;

				case 2:
					x -= ((par/2) - e.getXOffset());
					y -= ((perp/2) - e.getYOffset());

					hitboxes[i] = new Area(new Ellipse2D.Double(x, y, par, perp));

					hitboxes[i].subtract(new Area(new Ellipse2D.Double(x + offset, y, par, perp)));

					x += ((par/2) - e.getXOffset());
					y += ((perp/2) - e.getYOffset());
					break;

				case 3:
					x -= ((par/2) - e.getXOffset());
					y -= ((perp/2) - e.getYOffset());

					hitboxes[i] = new Area(new Ellipse2D.Double(x, y, par, perp));

					hitboxes[i].subtract(new Area(new Ellipse2D.Double(x - offset, y, par, perp)));

					x += ((par/2) - e.getXOffset());
					y += ((perp/2) - e.getYOffset());
					break;
			}
		}
	}
}
