package engine;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

import java.nio.FloatBuffer;
import java.util.function.DoubleBinaryOperator;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class Tesselator {

	private static final int MAX_VERTICES = 524288;
	private FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(524288);
	private FloatBuffer texCoordBuffer = BufferUtils.createFloatBuffer(262144);
	private FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(524288);
	private int vertices = 0;
	
	private int usedVertices = 0;
	private int usedColors = 0;
	private int usedTxcoords = 0;
	
	private float u;
	private float v;
	private float r;
	private float g;
	private float b;
	private boolean hasColor = false;
	private boolean hasTexture = false;

	public static final Tesselator get = new Tesselator();

	public void flush() {
		this.vertexBuffer.flip();
		this.texCoordBuffer.flip();
		this.colorBuffer.flip();
		
		GL11.glVertexPointer(3, GL_FLOAT, 0, this.vertexBuffer);
		if (this.hasTexture) {
			GL11.glTexCoordPointer(2, GL_FLOAT, 0, this.texCoordBuffer);
		}

		if (this.hasColor) {
			GL11.glColorPointer(3, GL_FLOAT, 0, this.colorBuffer);
		}

		GL11.glDrawArrays(GL_QUADS, 0, this.vertices);

		this.clear();
	}

	private void clear() {
		this.vertices = 0;
		
		this.vertexBuffer.clear();
		this.texCoordBuffer.clear();
		this.colorBuffer.clear();
	}

	public void init() {
		this.clear();
		this.hasColor = false;
		this.hasTexture = false;
	}

	public void tex(float u, float v) {
		this.hasTexture = true;
		this.u = u;
		this.v = v;
	}

	public void color(float r, float g, float b) {
		this.hasColor = true;
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public void vertex(float x, float y, float z) {
		this.vertexBuffer.put(this.vertices * 3 + 0, x).put(this.vertices * 3 + 1, y).put(this.vertices * 3 + 2, z);
		if (this.hasTexture) {
			this.texCoordBuffer.put(this.vertices * 2 + 0, this.u).put(this.vertices * 2 + 1, this.v);
			usedTxcoords+=3;
		}

		if (this.hasColor) {
			this.colorBuffer.put(this.vertices * 3 + 0, this.r).put(this.vertices * 3 + 1, this.g)
					.put(this.vertices * 3 + 2, this.b);
			usedColors+=3;
		}

		++this.vertices;
		usedVertices+=3;
		
		if (this.vertices == MAX_VERTICES) {
			this.flush();
		}
	}
	
	public static String getMemoryUsage() {
		Runtime r = Runtime.getRuntime();
		return (String.format("Memory usage: %.1f Mb" ,(get.usedVertices+get.usedColors+get.usedTxcoords + r.totalMemory() - r.freeMemory()) / 1024f / 1024f));
	}
}