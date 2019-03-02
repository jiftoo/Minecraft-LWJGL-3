package engine;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Syncer {
	public static float DELTA = 0;
	
	public static int fps;
	
	public static long sleepTime;
	
	public static long variableYieldTime;
	public static long lastTime;
	
	public static void syncer(int desiredFps) {
		fps = desiredFps;
		sleepTime = 1000000000 / fps;
	}
	public static void sync() {
		final long yieldTime = Math.min(sleepTime, variableYieldTime + sleepTime % (1000*1000));
		long overSleep = 0;
		
		try {
			while (true) {
				
				long t = getTime() - lastTime;
				
				if (t < sleepTime - yieldTime) {
					Thread.sleep(1);
				}
				else if (t < sleepTime) {
					Thread.yield();
				}
				else {
					overSleep = t - sleepTime;
					break;
				}
			}
		} catch (InterruptedException e) {}
		
		lastTime = getTime() - Math.min(overSleep, sleepTime);
		
		if (overSleep > variableYieldTime) {
			variableYieldTime = Math.min(variableYieldTime + 200*1000, sleepTime);
		}
		else if (overSleep < variableYieldTime - 200*1000) {
			variableYieldTime = Math.max(variableYieldTime - 2*1000, 0);
		}
	}
	public static long getTime() {
		   return (long)(glfwGetTime() * 1e9);
	}
}
