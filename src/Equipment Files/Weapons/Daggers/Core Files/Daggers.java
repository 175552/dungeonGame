
class Daggers extends Weapons {

	Daggers(){
		attackTime = 250;
		attackDuration = 100;
		maxChargeTime = (attackTime*World.framerate)/1000;
		spdMod = 1;
		hb = new SlashHitboxes(7, 100, 60);
		knockback = 2;
		canAttackWhileMoving = true;
	}

}
