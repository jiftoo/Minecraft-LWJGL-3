package input;

import org.lwjgl.glfw.GLFW;

import engine.Engine;

public class Keyboard {
	
	public static boolean isKeyDown(int key)
	{
		return GLFW.GLFW_PRESS == GLFW.glfwGetKey(Engine.window, key);
	}
	
	public static boolean isKeyUp(int key)
	{
		return GLFW.GLFW_RELEASE == GLFW.glfwGetKey(Engine.window, key);
	}

}
