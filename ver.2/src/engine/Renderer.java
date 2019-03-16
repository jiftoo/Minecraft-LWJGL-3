package engine;

import gui.GUI;
import util.Syncer;
import util.text.Text;
import world.Camera;
import world.World;

public class Renderer {
	
	public static void load() throws Exception {
		Camera.create();
		
		World.load();
		
		Text.load("c:/users/user/desktop/default.png");
		
		GUI.load();
		
		last_tick = Syncer.getTime();
	}
	
	public static void render() {
		//World.applyFog();
		World.render();
		//World.removeFog();
		
		GUI.render();
	}

	static double ready = 0;
	static long now_tick;
    static long last_tick;
	
	public static void applyCamera() {
		now_tick = Syncer.getTime();
    	ready += (now_tick - last_tick) / (double)Syncer.sleepTime;
		last_tick = now_tick;
		
		if(ready >= 1) {
			Camera.acceptInput(Syncer.getDeltaCeil());
			Camera.apply();
			ready--;
		}
	}

}
