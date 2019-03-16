package gui;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import engine.Engine;
import engine.Tesselator;
import util.Syncer;
import util.text.Text;
import world.Camera;
import world.World;

public class GUI {

	private static int make2dlist, make3dlist;

	public static void load() {
		make2dlist = glGenLists(2);
		make3dlist = make2dlist+1;
		
		glNewList(make2dlist, GL_COMPILE);
		{
			glMatrixMode(GL_PROJECTION);
			glPushMatrix();
			glLoadIdentity();
			
			glOrtho(0f, Engine.width, Engine.height, 0f, -1, 1);
			
			glMatrixMode(GL_MODELVIEW);
			glPushMatrix();
			glLoadIdentity();
			
			glDisable(GL_DEPTH_TEST);
		}
		glEndList();

		glNewList(make3dlist, GL_COMPILE);
		{
			glMatrixMode(GL_PROJECTION);
			glPopMatrix();
			glMatrixMode(GL_MODELVIEW);
			glPopMatrix();

			glEnable(GL_DEPTH_TEST);
		}
		glEndList();
	}

	
	public static void render() {
		make2d();

		renderDebug();

		make3d();
	}

	private static void renderDebug() {
		Text.render(4, 2, 2, String.format("x : %.1f", Camera.getX()));
		Text.render(4, 12, 2, String.format("y : %.1f", Camera.getY()));
		Text.render(4, 22, 2, String.format("z : %.1f ", Camera.getZ()));
		Text.render(4, 32, 2, String.format("rt: %s", Camera.getDir()));
		Text.render(4, 42, 2, String.format("Delta: F:%.2f, DT:%.2f", Syncer.getDeltaCeil(), Syncer.ACT_DELTA));
		Text.render(4, 52, 2, String.format("FPS: %.1f", Syncer.getFps()));
		Text.render(4, 62, 2, Tesselator.getMemoryUsage());
		Text.render(4, 72, 2, "Chunk updates: " + World.getChunkUpdates());
	}

	
	public static void make2d() {
		glCallList(make2dlist);
	}
	public static void make3d() {
		glCallList(make3dlist);
	}
}
