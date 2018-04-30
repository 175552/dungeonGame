
class Daggers extends Weapons {

	Daggers(){
		attackTime = 250;
		attackDuration = 100;
		maxChargeTime = (attackTime*World.framerate)/1000;
		spdMod = 0.7;
	}

}
