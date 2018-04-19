
abstract class Weapons extends Equipment {
	WeaponHitboxes hb;											//Weapon hitboxes for the four directions. Contains an array of hitboxes, use
																//the get(index) method to get the hitbox associated with that direction.
																//Direction indexes are [Up, Down, Left, Right]
	int damage, knockback, attackTime, attackDuration;

	int chargeTime, maxChargeTime = attackTime/World.framerate;

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

	int getAttackDuration(){
		return attackDuration;
	}

	void chargeAttack(Entity e){
		if(chargeTime != 0 && chargeTime != maxChargeTime && !e.attacking){
			chargeTime = 0;
		}
		else if(chargeTime == maxChargeTime){
			if(!e.attacking){
				doAttack(e);
				chargeTime = 0;
			}
		}
		else chargeTime++;
	}

	void doAttack(Entity e){
		if(e.attacksUp)
			e.attackUp();
		else if(e.attacksDown)
			e.attackDown();
		else if(e.attacksLeft)
			e.attackLeft();
		else if(e.attacksRight)
			e.attackRight();
		else System.out.println("Attacking error: " + e);
	}
}
