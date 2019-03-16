package world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryUtil;

import util.VBO;
import util.text.Text;

public class Stars {
	public static boolean renderBuffer = false;
	
	private static Star[] stars;
	
	private static VBO starvbo;
	public static void generateNew(int amount)
	{
		stars = new Star[amount];
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Star();
		}
		DoubleBuffer buf = MemoryUtil.memAllocDouble(stars.length * 3);
		for (Star star : stars) {
			buf.put(star.x);
			buf.put(star.y);
			buf.put(star.z);
		}
		buf.flip();
		starvbo = new VBO(buf, 3, GL_STATIC_DRAW, GL_POINTS);
	}
	
	public static void render()
	{
		glColor3f((1f / 255f) * 213f, (1f / 255f) * 213, (1f / 255f) * 154);
		starvbo.draw();
	}
	
	
	private static class Star
	{
		public final double x, y, z;
		private final byte r, g, b;
		private float size;
		public Star() {
			Random rnd = new Random();
			x = (rnd.nextDouble() - 0.5) * 100;
			y = (rnd.nextDouble() - 0.5) * 100;
			z = rnd.nextInt(200) - 200d;
			
			byte[] rgb = new byte[3]; rnd.nextBytes(rgb);
			
			r = rgb[0];
			g = rgb[1];
			b = rgb[2];
			
			size = 1f + (5f - 1f) * rnd.nextFloat();
		}
		public void applyColor()
		{
			glColor3b(r, g, b);
		}
		public void applySize()
		{
			glPointSize(size);
		}
	}
}
