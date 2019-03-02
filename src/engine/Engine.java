package engine;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_ANY_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_LEFT;
import static org.lwjgl.opengl.GL11.GL_LESS;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_STACK_UNDERFLOW;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPointSize;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;
import java.util.Iterator;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;

import util.Access;
import world.World;

public class Engine {
	
	public static long runningTimeMillis = 0;

	public static long Window = NULL;
	
	public Engine(int width, int height, String title, boolean fullscreen) {
		GLFWErrorCallback.createPrint(System.err).set();
		
		initGLFW();
		createWindow(width, height, title, fullscreen);
	}

	private void initGLFW() {
		if (!glfwInit())
			System.err.println("Failed to init GLFW");
	}

	private void createWindow(int width, int height, String title, boolean fullscreen) {
		long window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
		Access.drop(window, "window");
		Access.drop(width, "width");
		Access.drop(height, "height");
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_ANY_PROFILE);
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		glfwShowWindow(window);
		
		GLFWVidMode vmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, vmode.width()/2 - width / 2, vmode.height()/2 - height / 2);
		
		createCapabilities();
		
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST); glDepthFunc(GL_LESS);
		//GL11.glEnable(GL11.GL_CULL_FACE); glCullFace(GL_BACK);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		
		
		glClearColor(0.1f, 0.1f, 0.1f, 1f);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(70, (float)width / (float)height, 1f, 1000);
		glMatrixMode(GL_MODELVIEW);
		
		Syncer.syncer(79);
		
		last_time = Syncer.getTime();
		
        while ( !glfwWindowShouldClose(window) ) {
        	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        	
        	
        	World.render();
        	Syncer.sync();
        	
			glfwSwapBuffers(window);
			glfwPollEvents();
			
			postEvents();
		}
	}
	
	public static void gluPerspective(float fovy, float aspect, float near, float far) {
		double fw, fh;
		fh = Math.tan(fovy / 360 * 3.14159265358979323846d) * near;
		fw = fh * aspect;
		glFrustum(-fw, fw, -fh, fh, near, far);
	}
	
	private static int errocode = GL_NO_ERROR;
	private static long last_time;
	
	private void postEvents()
	{
		checkActions();
		
		long time = Syncer.getTime();
		
		Syncer.DELTA = (float) ((time - last_time) / 1e7f);
		System.out.format("  Delta: %.2f" ,Syncer.DELTA);
		System.out.format("  FPS: %.1f\n", Syncer.fps / Syncer.DELTA);
		runningTimeMillis += Syncer.DELTA / 1e6f;
		last_time = time;
		
		if((errocode = glGetError()) != GL_NO_ERROR)
		{
			String[] errors = {"Invalid enum", "Invalid operation", "Invalid value", "Stack overflow", "Stack underflow", "Out of memory", "Unknown"};
			String errstr;
			switch (errocode) {
				case GL_INVALID_ENUM: errstr = errors[0]; break;
				case GL_INVALID_OPERATION: errstr = errors[1]; break;
				case GL_INVALID_VALUE: errstr = errors[2]; break;
				case GL_STACK_OVERFLOW: errstr = errors[3]; break;
				case GL_STACK_UNDERFLOW: errstr = errors[4]; break;
				case GL_OUT_OF_MEMORY: errstr = errors[5]; break;
				default: errstr = errors[7]; break;
			}
			System.err.println("Error detected, code: "+errstr+ " - "+ errocode);
			System.exit(errocode);
		}
	}
	
	private static ArrayList<TimeAction> time_actions = new ArrayList<>();
	
	public static void everyN_MS(int millis, Runnable action)
	{
		time_actions.add(new TimeAction(runningTimeMillis, millis, action));
	}
	private void checkActions()
	{
		Iterator<TimeAction> i = time_actions.iterator();
		while (i.hasNext()) {
			TimeAction timeAction = i.next();
			if(runningTimeMillis > timeAction.msecondsi + timeAction.msecondst)
			{
				timeAction.run.run();
				timeAction.msecondsi = runningTimeMillis;
			}
		}
	}
	private static class TimeAction {
		public long msecondsi, msecondst;
		public Runnable run;
		public TimeAction(long msecondsi, long msecondst, Runnable run) {
			this.msecondsi = msecondsi;
			this.msecondst = msecondst;
			this.run = run;
		}
	}
}