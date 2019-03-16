package world;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.*;
import static world.chunk.Block.*;

import engine.Tesselator;
import util.TextureLoader;
import world.chunk.Block;

public class Blocks {

	private static final int[][] lists = new int[3][6]; // 1 - ID, 2 - sides: t0 b1 l2 r3 fw4 bw5

	public static void loadBlocks() {
		Block.createBlocks();

		//genlist6tx(GRASS.id, TextureLoader.grasstop, TextureLoader.grasstop, TextureLoader.grassside,
		//		TextureLoader.grassside, TextureLoader.grassside, TextureLoader.grassside);
		glBindTexture(GL_TEXTURE_2D, TextureLoader.grasstop);
	}

	public static void render(Block block, boolean upfree, boolean downfree, boolean leftfree, boolean rightfree,
			boolean frontfree, boolean backfree, float x, float y, float z) {
		
		if (upfree) {
			up(TextureLoader.grasstop, x, y, z);
		}
		if (downfree) {
			down(TextureLoader.grasstop, x, y, z);
		}
		if (leftfree) {
			left(TextureLoader.grasstop, x, y, z);
		}
		if (rightfree) {
			right(TextureLoader.grasstop, x, y, z);
		}
		if (frontfree) {
			front(TextureLoader.grasstop, x, y, z);
		}
		if (backfree) {
			back(TextureLoader.grasstop, x, y, z);
		}
	}

	private static void up(int tx, float x, float y, float z) {
		glBindTexture(GL_TEXTURE_2D, tx);
		
		Tesselator.get.color(0xff, 0xff, 0xff);
		
		Tesselator.get.tex(0, 1);
		Tesselator.get.vertex(1+x, 1+y, 0+z);
		Tesselator.get.tex(1, 1);
		Tesselator.get.vertex(0+x, 1+y, 0+z);
		Tesselator.get.tex(1, 0);
		Tesselator.get.vertex(0+x, 1+y, 1+z);
		Tesselator.get.tex(0, 0);
		Tesselator.get.vertex(1+x, 1+y, 1+z);
		
		/*glBegin(GL_QUADS);
		{
			// top face
			glTexCoord2f(0f, 1f);
			glVertex3f(1f, 1f, 0f); // 1
			glTexCoord2f(1f, 1f);
			glVertex3f(0f, 1f, 0f); // 2
			glTexCoord2f(1f, 0f);
			glVertex3f(0f, 1f, 1f); // 3
			glTexCoord2f(0f, 0f);
			glVertex3f(1f, 1f, 1f); // 4
		}
		glEnd();*/
	}

	private static void down(int tx, float x, float y, float z) {
		//glBindTexture(GL_TEXTURE_2D, tx);
		
		Tesselator.get.color(0xff, 0xff, 0xff);
		
		Tesselator.get.tex(0, 1);
		Tesselator.get.vertex(1+x, 0+y, 1+z);
		Tesselator.get.tex(1, 1);
		Tesselator.get.vertex(0+x, 0+y, 1+z);
		Tesselator.get.tex(1, 0);
		Tesselator.get.vertex(0+x, 0+y, 0+z);
		Tesselator.get.tex(0, 0);
		Tesselator.get.vertex(1+x, 0+y, 0+z);
		
		/*glBegin(GL_QUADS);
		{
			// bottom face
			glTexCoord2f(0f, 1f);
			glVertex3f(1f, 0f, 1f); // 1
			glTexCoord2f(1f, 1f);
			glVertex3f(0f, 0f, 1f); // 2
			glTexCoord2f(1f, 0f);
			glVertex3f(0f, 0f, 0f); // 3
			glTexCoord2f(0f, 0f);
			glVertex3f(1f, 0f, 0f); // 4
		}
		glEnd();*/
	}

	private static void left(int tx, float x, float y, float z) {
		//glBindTexture(GL_TEXTURE_2D, tx);
		
		Tesselator.get.color(0xff, 0xff, 0xff);
		
		Tesselator.get.tex(0, 1);
		Tesselator.get.vertex(1+x, 0+y, 1+z);
		Tesselator.get.tex(1, 1);
		Tesselator.get.vertex(1+x, 0+y, 0+z);
		Tesselator.get.tex(1, 0);
		Tesselator.get.vertex(1+x, 1+y, 0+z);
		Tesselator.get.tex(0, 0);
		Tesselator.get.vertex(1+x, 1+y, 1+z);
		
		/*glBegin(GL_QUADS);
		{
			// left face
			glTexCoord2f(0f, 1f);
			glVertex3f(1f, 0f, 1f); // 1
			glTexCoord2f(1f, 1f);
			glVertex3f(1f, 0f, 0f); // 2
			glTexCoord2f(1f, 0f);
			glVertex3f(1f, 1f, 0f); // 3
			glTexCoord2f(0f, 0f);
			glVertex3f(1f, 1f, 1f); // 4
		}
		glEnd();*/
	}

	private static void right(int tx, float x, float y, float z) {
		//glBindTexture(GL_TEXTURE_2D, tx);
		
		Tesselator.get.color(0xff, 0xff, 0xff);
		
		Tesselator.get.tex(0, 1);
		Tesselator.get.vertex(0+x, 0+y, 0+z);
		Tesselator.get.tex(1, 1);
		Tesselator.get.vertex(0+x, 0+y, 1+z);
		Tesselator.get.tex(1, 0);
		Tesselator.get.vertex(0+x, 1+y, 1+z);
		Tesselator.get.tex(0, 0);
		Tesselator.get.vertex(0+x, 1+y, 0+z);
		
		/*glBegin(GL_QUADS);
		{
			// right face
			glTexCoord2f(0f, 1f);
			glVertex3f(0f, 0f, 0f); // 1
			glTexCoord2f(1f, 1f);
			glVertex3f(0f, 0f, 1f); // 2
			glTexCoord2f(1f, 0f);
			glVertex3f(0f, 1f, 1f); // 3
			glTexCoord2f(0f, 0f);
			glVertex3f(0f, 1f, 0f); // 4
		}
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);*/
	}

	private static void front(int tx, float x, float y, float z) {
		//glBindTexture(GL_TEXTURE_2D, tx);
		
		Tesselator.get.color(0xff, 0xff, 0xff);
		
		Tesselator.get.tex(0, 1);
		Tesselator.get.vertex(1+x, 0+y, 0+z);
		Tesselator.get.tex(1, 1);
		Tesselator.get.vertex(0+x, 0+y, 0+z);
		Tesselator.get.tex(1, 0);
		Tesselator.get.vertex(0+x, 1+y, 0+z);
		Tesselator.get.tex(0, 0);
		Tesselator.get.vertex(1+x, 1+y, 0+z);
		
		/*glBegin(GL_QUADS);
		{
			// front face
			glTexCoord2f(0f, 1f);
			glVertex3f(1f, 0f, 0f); // 1
			glTexCoord2f(1f, 1f);
			glVertex3f(0f, 0f, 0f); // 2
			glTexCoord2f(1f, 0f);
			glVertex3f(0f, 1f, 0f); // 3
			glTexCoord2f(0f, 0f);
			glVertex3f(1f, 1f, 0f); // 4
		}
		glEnd();*/
	}

	private static void back(int tx, float x, float y, float z) {
		
		Tesselator.get.color(0xff, 0xff, 0xff);
		
		Tesselator.get.tex(0, 1);
		Tesselator.get.vertex(0+x, 0+y, 1+z);
		Tesselator.get.tex(1, 1);
		Tesselator.get.vertex(1+x, 0+y, 1+z);
		Tesselator.get.tex(1, 0);
		Tesselator.get.vertex(1+x, 1+y, 1+z);
		Tesselator.get.tex(0, 0);
		Tesselator.get.vertex(0+x, 1+y, 1+z);
		
		/*glBegin(GL_QUADS);
		{
			// back face
			glTexCoord2f(0f, 1f);
			glVertex3f(0f, 0f, 1f); // 1
			glTexCoord2f(1f, 1f);
			glVertex3f(1f, 0f, 1f); // 2
			glTexCoord2f(1f, 0f);
			glVertex3f(1f, 1f, 1f); // 3
			glTexCoord2f(0f, 0f);
			glVertex3f(0f, 1f, 1f); // 4
		}
		glEnd();*/
	}

	private static void genlist6tx(int arrindex, int txup, int txdown, int txright, int txleft, int txfw, int txbw) {
		/*int li = glGenLists(6);

		glNewList(li, GL_COMPILE);
		{
			front(txfw);
		}
		glEndList();
		lists[arrindex][4] = li;

		glNewList(li + 1, GL_COMPILE);
		{
			back(txbw);
		}
		glEndList();
		lists[arrindex][5] = li + 1;

		glNewList(li + 2, GL_COMPILE);
		{
			right(txright);
		}
		glEndList();
		lists[arrindex][3] = li + 2;

		glNewList(li + 3, GL_COMPILE);
		{
			left(txleft);
		}
		glEndList();
		lists[arrindex][2] = li + 3;

		glNewList(li + 4, GL_COMPILE);
		{
			up(txup);
		}
		glEndList();
		lists[arrindex][0] = li + 4;

		glNewList(li + 5, GL_COMPILE);
		{
			down(txdown);
		}
		glEndList();
		lists[arrindex][1] = li + 5;*/
	}

	private static void genlist1tx(int arrindex, int texture) {
		genlist6tx(arrindex, texture, texture, texture, texture, texture, texture);
	}
}
