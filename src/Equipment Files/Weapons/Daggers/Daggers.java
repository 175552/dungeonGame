
class Daggers extends Weapons {

	Daggers(){
		attackTime = 500;
		attackDuration = 250;
		maxChargeTime = (attackTime*World.framerate)/1000;
	}

}
