
class Sword extends Weapons{

	Sword(){
		attackTime = 400;
		attackDuration = 200;
		maxChargeTime = (attackTime*World.framerate)/1000;
		spdMod = 0.7;
		knockback = 4;
		hb = new SlashHitboxes(10, 130, 115);
	}
}