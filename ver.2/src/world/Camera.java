package world;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.awt.Font;
import java.nio.DoubleBuffer;
import org.joml.Vector3d;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.TrueTypeFont;
import engine.Engine;
import engine.Tesselator;
import input.Keyboard;
import util.Syncer;
import util.text.Text;

public class Camera {
	 public static double moveSpeed = 0.05f;

	    private static double maxLook = 85;

	    private static double mouseSensitivity = 0.04f;

	    private static Vector3d pos;
	    private static Vector3d rotation;
	    
	    public static int direction;
	    
	    public static void create() {
	        pos = new Vector3d(0, 20, 0);
	        rotation = new Vector3d(15, 138, 0);
	        
	        direction = 0;
	        
	        GLFW.glfwSetInputMode(Engine.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
	    }

	    public static void apply() {
	        if(rotation.y / 360 > 1) {
	            rotation.y -= 360;
	        } else if(rotation.y / 360 < -1) {
	            rotation.y += 360;
	        }
	        GL11.glLoadIdentity();
	        GL11.glRotated(rotation.x, 1, 0, 0);
	        GL11.glRotated(rotation.y, 0, 1, 0);
	        GL11.glRotated(rotation.z, 0, 0, 1);
	        GL11.glTranslated(-pos.x, -pos.y, -pos.z);
	        
	        double rot = rotation.y < 0 ? 360 - Math.abs(rotation.y + 45d) : rotation.y + 45d;
	        if(rot >= 0 && rot < 90d)
	        	direction = 0;
	        else if(rot >= 90d && rot < 180d)
	        	direction = 1;
	        else if(rot >= 180d && rot < 270d)
	        	direction = 2;
	        else if(rot >= 270d && rot < 360d)
	        	direction = 3;
	        
	        
	    }
	    
	    public static void printValues() {
	    	
	    }
	    
	    public static String getDir() {
	    	switch (direction) {
			case 0: return "negative z";
			case 1: return "positive x";
			case 2: return "positive z";
			case 3: return "negative x";
			default:return "unknown";
			}
	    }
	    

	    public static void acceptInput(float delta) {
	        acceptInputRotate(delta);
	        acceptInputMove(delta);
	    }
	    
	    private static double mouseX, mouseY;
	    private static double[] pollMouseDelta() {
	        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
	        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

	        GLFW.glfwGetCursorPos(Engine.window, x, y);
	        x.rewind();
	        y.rewind();
	        
	        double tempX = mouseX;
	        double tempY = mouseY;

	        mouseX = x.get();
	        mouseY = y.get();

	        return new double[] {
	               mouseX - tempX,
	               mouseY - tempY
	        };
	    }

	    public static void acceptInputRotate(float delta) {
	    	double[] mdelta = pollMouseDelta();
	        if(GLFW.glfwGetInputMode(Engine.window, GLFW.GLFW_CURSOR) == GLFW.GLFW_CURSOR_DISABLED) {
	            rotation.y += mdelta[0] * mouseSensitivity * delta;
	            rotation.x += mdelta[1] * mouseSensitivity * delta;
	            rotation.x = Math.max(-maxLook, Math.min(maxLook, rotation.x));
	        }
	    }
	    
	    private static double sspeed = 1;

	    public static void acceptInputMove(float delta) {
	        boolean keyForward = Keyboard.isKeyDown(GLFW.GLFW_KEY_W);
	        boolean keyBack = Keyboard.isKeyDown(GLFW.GLFW_KEY_S);
	        boolean keyRight = Keyboard.isKeyDown(GLFW.GLFW_KEY_D);
	        boolean keyLeft = Keyboard.isKeyDown(GLFW.GLFW_KEY_A);
	        boolean keyFast = Keyboard.isKeyDown(GLFW.GLFW_KEY_Q);
	        boolean keySlow = Keyboard.isKeyDown(GLFW.GLFW_KEY_E);
	        boolean keyFlyUp = Keyboard.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT);
	        boolean keyFlyDown = Keyboard.isKeyDown(GLFW.GLFW_KEY_SPACE);
	        
	        if(Keyboard.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) GLFW.glfwSetWindowShouldClose(Engine.window, true);
	        Stars.renderBuffer = Keyboard.isKeyDown(GLFW.GLFW_KEY_TAB);

	        double speed;

	        if(keyFast) {
	            speed = moveSpeed * 5;
	        }
	        else if(keySlow) {
	            speed = moveSpeed / 4;
	        }
	        else {
	            speed = moveSpeed;
	        }

	        speed *= delta;
	        sspeed = speed;

	        if(keyFlyUp) {
	            pos.y -= speed;
	        }
	        if(keyFlyDown) {
	            pos.y += speed;
	        }

	        if(keyBack) {
	            pos.x -= Math.sin(Math.toRadians(rotation.y)) * speed;
	            pos.z += Math.cos(Math.toRadians(rotation.y)) * speed;
	        }
	        if(keyForward) {
	            pos.x += Math.sin(Math.toRadians(rotation.y)) * speed;
	            pos.z -= Math.cos(Math.toRadians(rotation.y)) * speed;
	        }
	        if(keyLeft) {
	            pos.x += Math.sin(Math.toRadians(rotation.y - 90)) * speed;
	            pos.z -= Math.cos(Math.toRadians(rotation.y - 90)) * speed;
	        }
	        if(keyRight) {
	            pos.x += Math.sin(Math.toRadians(rotation.y + 90)) * speed;
	            pos.z -= Math.cos(Math.toRadians(rotation.y + 90)) * speed;
	        }
	    }

	    public static void setSpeed(double speed) {
	        moveSpeed = speed;
	    }

	    public static void setPos(Vector3d pos) {
	        Camera.pos = pos;
	    }

	    public static Vector3d getPos() {
	        return pos;
	    }

	    public static void setX(double x) {
	        pos.x = x;
	    }

	    public static double getX() {
	        return pos.x;
	    }

	    public static void addToX(double x) {
	        pos.x += x;
	    }

	    public static void setY(double y) {
	        pos.y = y;
	    }

	    public static double getY() {
	        return pos.y;
	    }

	    public static void addToY(double y) {
	        pos.y += y;
	    }

	    public static void setZ(double z) {
	        pos.z = z;
	    }

	    public static double getZ() {
	        return pos.z;
	    }

	    public static void addToZ(double z) {
	        pos.z += z;
	    }

	    public static void setRotation(Vector3d rotation) {
	        Camera.rotation = rotation;
	    }

	    public static Vector3d getRotation() {
	        return rotation;
	    }

	    public static void setRotationX(double x) {
	        rotation.x = x;
	    }

	    public static double getRotationX() {
	        return rotation.x;
	    }

	    public static void addToRotationX(double x) {
	        rotation.x += x;
	    }

	    public static void setRotationY(double y) {
	        rotation.y = y;
	    }

	    public static double getRotationY() {
	        return rotation.y;
	    }

	    public static void addToRotationY(double y) {
	        rotation.y += y;
	    }

	    public static void setRotationZ(double z) {
	        rotation.z = z;
	    }

	    public static double getRotationZ() {
	        return rotation.z;
	    }

	    public static void addToRotationZ(double z) {
	        rotation.z += z;
	    }

	    public static void setMaxLook(double maxLook) {
	        Camera.maxLook = maxLook;
	    }

	    public static double getMaxLook() {
	        return maxLook;
	    }

	    public static void setMouseSensitivity(double mouseSensitivity) {
	        Camera.mouseSensitivity = mouseSensitivity;
	    }

	    public static double getMouseSensitivity() {
	        return mouseSensitivity;
	    }
}
