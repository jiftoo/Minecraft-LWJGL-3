package engine;

import world.World;

public class Ticker {
	
	public static void load() {
		
	}
	
	public static void tick() {
		World.update();
	}

}
