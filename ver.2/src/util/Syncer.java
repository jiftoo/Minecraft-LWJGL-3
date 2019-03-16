package util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Syncer {
	public static float DELTA = 0;
	public static float ACT_DELTA = 0;
	public static float AVERAGE_DELTA = 0;
	
	public static int desiredFps;
	
	public static long sleepTime;
	
	public static long variableYieldTime;
	public static long lastTime;
	
	public static void syncer(int desiredFps) {
		Syncer.desiredFps = desiredFps;
		measuredfps = 0;
		sleepTime = 1000000000 / Syncer.desiredFps;
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
	
	
	
	private static int fpsframes = 0;
	private static double measuredfps; // First time
	
	private static int precision = 1;
	
	public static void setPrecision(int precision) {
		Syncer.precision = precision;
	}
	
	public static double measureFPS(double deltaa) {
		if(fpsframes < precision) {
			AVERAGE_DELTA += deltaa;
			fpsframes++;
		} else {	
			measuredfps = 1d / (AVERAGE_DELTA / (double)precision);
			fpsframes = 0;
			AVERAGE_DELTA = 0;
		}
		return measuredfps;
	}
	
	
	public static double getFps() {
		return measuredfps;
	}
	public static float getDeltaCeil() {
		return DELTA;
	}
	public static float getDelta() {
		return ACT_DELTA;
	}
	public static float getDeltaAvg() {
		return AVERAGE_DELTA;
	}
}
