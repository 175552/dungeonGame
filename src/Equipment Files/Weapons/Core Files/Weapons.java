
abstract class Weapons extends Equipment {
	WeaponHitboxes hb;											//Weapon hitboxes for the four directions. Contains an array of hitboxes, use
																//the get(index) method to get the hitbox associated with that direction.
																//Direction indexes are [Up, Down, Left, Right]
	int damage, knockback, attackTime, attackDuration;

	double spdMod;												//Multiply current speed by spdMod when charging to get new speed.

	int chargeTime, maxChargeTime;

	boolean canAttackWhileMoving;

	WeaponHitboxes getHitboxes(){
		return hb;
	}

	double getSpeedMod(){
		return spdMod;
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

	int getTime(){
		return chargeTime;
	}

	void chargeAttack(Entity e){
		if(chargeTime < maxChargeTime)
			chargeTime++;
	}

	void cancelAttack(Entity e){
		chargeTime = 0;
	}

	void doAttack(Entity e){
		getHitboxes().moveHitbox(e, e.getX() - e.getXOffset(), e.getY() - e.getYOffset());
		if(e.attacksUp){
			e.attackUp();
			if(e.library.get("aUp") != null)
				e.sprite = e.library.get("aUp");
		}
		else if(e.attacksDown){
			e.attackDown();
			if(e.library.get("aDown") != null)
				e.sprite = e.library.get("aDown");
		}
		else if(e.attacksLeft){
			e.attackLeft();
			if(e.library.get("aLeft") != null)
				e.sprite = e.library.get("aLeft");
		}
		else if(e.attacksRight){
			e.attackRight();
			if(e.library.get("aRight") != null)
				e.sprite = e.library.get("aRight");
		}
		e.attacking = false;
	}

	boolean attackReady(){
		if(chargeTime == maxChargeTime)
			return true;
		else return false;
	}
}
