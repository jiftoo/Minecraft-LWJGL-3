package launcher;

import engine.Engine;
import util.Access;

public class Launcher {
	
	public static void main(String[] args) {
		Access.drop(args, "_arguments");
		Access.drop(new Engine(1280, 720, "My Game", false), "engine");
	}
}