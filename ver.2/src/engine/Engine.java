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
import static org.lwjgl.opengl.GL11.GL_CCW;
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CW;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FOG;
import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_POLYGON_SMOOTH_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_STACK_UNDERFLOW;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BITS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;

import util.Syncer;
import util.TextureLoader;
import util.VBO;
import util.text.Text;
import world.Blocks;
import world.Camera;
import world.chunk.Block;

public class Engine {
	
	public static long runningTimeMillis = 0;

	public static long window = NULL;
	public static int width, height;
	
	
	public Engine(int width, int height, String title, boolean fullscreen) throws Exception {
		GLFWErrorCallback.createPrint(System.err).set();
		
		initGLFW();
		
		createWindow(width, height, title, fullscreen);
		
		cleanup();
	}

	private void initGLFW() {
		if (!glfwInit()) {
			System.err.println("Failed to init GLFW");
			JOptionPane.showMessageDialog(null, "Failed to init GLFW", "Eroor", JOptionPane.ERROR_MESSAGE);
		}
		
		glfwWindowHint(GLFW.GLFW_STENCIL_BITS, 2);
		glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
	}
	
	private void setupMatrix() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(70, (float)width / (float)height, 0.05f, 1000);
		glMatrixMode(GL_MODELVIEW);
	}
	
	private void setupOther() {
		Syncer.syncer(79);
		Syncer.setPrecision(50);
		last_time = Syncer.getTime();
		
		TextureLoader.loadGrassSide();
		TextureLoader.loadGrassTop();
		TextureLoader.loadDirt();
		
		Blocks.loadBlocks();
	}
	
	private void configureOpenGL() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST); glDepthFunc(GL_LEQUAL); glClearDepth(1f);
//		GL11.glEnable(GL11.GL_CULL_FACE); glCullFace(GL11.GL_BACK); glFrontFace(GL_CCW);
		
		glEnableClientState(GL_VERTEX_ARRAY);
		GL11.glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		GL11.glEnableClientState(GL_COLOR_ARRAY);
		
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		
		glClearColor(0.5f, 0.8f, 1f, 0f);
		glClearDepth(1.0f);
	}
	
	private void loadData() throws Exception {
		Ticker.load();
		Renderer.load();
	}

	private void createWindow(int width, int height, String title, boolean fullscreen) throws Exception {
		
		setupWindow(title, fullscreen, width, height);
		
		configureOpenGL();
		
		setupMatrix();
		
		setupOther();
		
		loadData();
		
        while ( !glfwWindowShouldClose(window) ) {
        	
        	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        	
        	for (int t = 0; t < 8; t++) {
				Ticker.tick();
			}
        	Renderer.render();
        	Renderer.applyCamera();
        	
        	//Syncer.sync();
        	
        	glfwPollEvents();
        	
        	postEvents();
        	
			glfwSwapBuffers(window);
		}
	}
	
	private void setupWindow(String title, boolean fullscreen, int width, int height) {
		Engine.window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : NULL, NULL);
		Engine.height = height;
		Engine.width = width;
		
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_ANY_PROFILE);
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(0);
		glfwShowWindow(window);
		
		GLFWVidMode vmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, vmode.width()/2 - width / 2, vmode.height()/2 - height / 2);
		
		createCapabilities();
	}
	
	private void cleanup() {
		System.out.println("\n******************SHUTDOWN CLEANUP*******************");
		
		VBO.clearBuffersOnExit(); System.out.println("- Deleted buffers");
		
		GLFW.glfwDestroyWindow(window); System.out.println("- Destroyed window");
		
		System.out.println("******************Cleanup finished!******************");
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
		//checkActions();
		
		long time = Syncer.getTime();
		
		float rawdelta = (time - last_time) / 1e9f;
		
		Syncer.DELTA = Math.max(1, rawdelta);
		Syncer.ACT_DELTA = rawdelta;
		
		Syncer.measureFPS(rawdelta);
		
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