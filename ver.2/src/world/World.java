package world;

import static org.lwjgl.opengl.GL11.GL_EXP2;
import static org.lwjgl.opengl.GL11.GL_FOG;
import static org.lwjgl.opengl.GL11.GL_FOG_COLOR;
import static org.lwjgl.opengl.GL11.GL_FOG_DENSITY;
import static org.lwjgl.opengl.GL11.GL_FOG_END;
import static org.lwjgl.opengl.GL11.GL_FOG_MODE;
import static org.lwjgl.opengl.GL11.GL_FOG_START;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFogf;
import static org.lwjgl.opengl.GL11.glFogfv;
import static org.lwjgl.opengl.GL11.glFogi;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

import input.Keyboard;
import util.Frustum;
import util.text.Text;
import world.chunk.Block;
import world.chunk.Chunk;

public class World {
	private static FloatBuffer fogColor;
	
	private static Chunk[][] chunks;
	
	public static int chunkUpdates = 0;
	
	private static final int WORLD_X_SIZE = 16, WORLD_Z_SIZE = 16;
	
	public static void load() {
		glPointSize(2.5f);
		glLineWidth(5f);
		
		fogColor = BufferUtils.createFloatBuffer(4);
		
		int col = 920330;
	    fogColor.put(new float[]{(float)(col >> 16 & 255) / 255.0F, (float)(col >> 8 & 255) / 255.0F, (float)(col & 255) / 255.0F, 1.0F});
		fogColor.flip();
		
		chunks = new Chunk[WORLD_X_SIZE][WORLD_Z_SIZE];
		for (int i = 0; i < WORLD_X_SIZE; i++) {
			for (int j = 0; j < WORLD_Z_SIZE; j++) {
				chunks[i][j] = new Chunk(i, j);
			}
		}
	}
	
	public static Block getBlockWorld(int x, int y, int z) {
		int xChunk = Math.floorDiv(x , 16);
		int zChunk = Math.floorDiv(z , 16);
		
		if(xChunk < 0 || xChunk >= WORLD_X_SIZE || y < 0 || zChunk < 0 || zChunk >= WORLD_Z_SIZE) return Block.AIR;
		
		return chunks[xChunk][zChunk].getBlock(x - 16 * xChunk, y, z - 16 * zChunk);
	}
	public static void setBlockWorld(int x, int y, int z, Block block) {
		int xChunk = Math.floorDiv(x , 16);
		int zChunk = Math.floorDiv(z , 16);
		
		if(xChunk < 0 || xChunk >= WORLD_X_SIZE || y < 0 || zChunk < 0 || zChunk >= WORLD_Z_SIZE) return;
		
		chunks[xChunk][zChunk].setBlock(x - 16 * xChunk, y, z - 16 * zChunk, block).setDirty();
		
	}
	
	public static void render()
	{
		Frustum f = Frustum.getFrustum();
		
		for (int i = 0; i < WORLD_X_SIZE; i++) {
			for (int j = 0; j < WORLD_Z_SIZE; j++) {
				chunks[i][j].render(f);
			}
		}
	}

	public static void update() {
		for (int i = 0; i < WORLD_X_SIZE; i++) {
			for (int j = 0; j < WORLD_Z_SIZE; j++) {
				if(chunks[i][j].isDirty()) {
					chunks[i][j].rebuild();
				}
			}
		}
		if(Keyboard.isKeyDown(GLFW.GLFW_KEY_0)) {
			setBlockWorld(1, 16, 1, Block.AIR);
			setBlockWorld(1, 15, 1, Block.AIR);
		}
	}
	
	public static int getChunkUpdates() {
		return chunkUpdates;
	}

	public static void applyFog() {
		glEnable(GL_FOG);
		glFogi(GL_FOG_MODE, GL_EXP2);
		glFogf(GL_FOG_START, 1f);
        glFogf(GL_FOG_END, 100f);
	    glFogf(GL_FOG_DENSITY, 0.001F);
	    glFogfv(GL_FOG_COLOR, fogColor);
	}
	
	public static void removeFog() {
		glDisable(GL_FOG);
	}
}
