
abstract class Weapons extends Equipment {
	WeaponHitboxes hb;											//Weapon hitboxes for the four directions. Contains an array of hitboxes, use
																//the get(index) method to get the hitbox associated with that direction.
																//Direction indexes are [Up, Down, Left, Right]
	int damage, knockback, attackTime;

	WeaponHitboxes getHitboxes(){
		return hb;
	}

	int getDamage(){
		return damage;
	}

	int getRange(){
		return hb.range;
	}

	int getKnockback(){
		return knockback;
	}
	
	int getAttackTime(){
		return attackTime;
	}
}
