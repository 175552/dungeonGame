
abstract class Weapons extends Equipment {
	WeaponHitboxes hb;											//Weapon hitboxes for the four directions. Contains an array of hitboxes, use
																//the get(index) method to get the hitbox associated with that direction.
																//Direction indexes are [Up, Down, Left, Right]

	WeaponHitboxes getHitboxes(){
		return hb;
	}
}
