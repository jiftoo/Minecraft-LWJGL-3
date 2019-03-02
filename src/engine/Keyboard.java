package engine;

import org.lwjgl.glfw.GLFW;
import util.Access;

public class Keyboard {
	
	public static boolean isKeyDown(int key)
	{
		return GLFW.GLFW_PRESS == GLFW.glfwGetKey(Access.pull("window"), key);
	}
	
	public static boolean isKeyUp(int key)
	{
		return GLFW.GLFW_RELEASE == GLFW.glfwGetKey(Access.pull("window"), key);
	}

}
