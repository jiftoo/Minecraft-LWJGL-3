package world.chunk;

import static org.lwjgl.opengl.GL11.GL_COMPILE;
import static org.lwjgl.opengl.GL11.GL_COMPILE_AND_EXECUTE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glCallList;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEndList;
import static org.lwjgl.opengl.GL11.glGenLists;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glNewList;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslated;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.nio.IntBuffer;

import engine.Tesselator;
import physics.AABB;
import util.Frustum;
import world.Blocks;
import world.Camera;
import world.World;

public class Chunk {
	public static final int CHUNK_DIM = 16, CHUNK_HEIGHT = 16;

	private Block[] blocks;
	private boolean dirty = true;
	private final int posX, posZ;

	private AABB aabb;

	private int glList;

	public Chunk(int pX, int pZ) {
		Block[][][] _blocks = new Block[CHUNK_HEIGHT][CHUNK_DIM][CHUNK_DIM];
		
		for (int y = 0; y < CHUNK_HEIGHT; y++) {
			for (int x = 0; x < CHUNK_DIM; x++) {
				for (int z = 0; z < CHUNK_DIM; z++) {

					_blocks[y][x][z] = Block.GRASS;

				}
			}
		}
		blocks = new Block[CHUNK_DIM * CHUNK_DIM * CHUNK_HEIGHT];
		
		for (int x = 0; x < CHUNK_HEIGHT; x++) {
			for (int y = 0; y < CHUNK_DIM; y++) {
				for (int z = 0; z < CHUNK_DIM; z++) {
					blocks[z + (x * z) + (x * y * z)] = _blocks[y][x][z];
				}
			}
		}
		posX = pX;
		posZ = pZ;

		aabb = new AABB(pX * 16, 0, pZ * 16, pX * 16 + 16, CHUNK_HEIGHT, pZ * 16 + 16);

		glList = glGenLists(1);
	}

	public void render(Frustum f) {
		if(f.cubeInFrustum(aabb) || isPlayerOn()) {
			glCallList(glList);
		}
	}
	
	public Block getBlock(int x, int y, int z) {
		if ((x >= CHUNK_DIM || x < 0) || (y >= CHUNK_HEIGHT || y < 0) || (z >= CHUNK_DIM || z < 0))
			return Block.AIR;
		else
			return blocks[z + (x * z) + (x * y * z)];
	}
	
	public Chunk setBlock(int x, int y, int z, Block block) {
		if ((x >= CHUNK_DIM || x < 0) || (y >= CHUNK_HEIGHT || y < 0) || (z >= CHUNK_DIM || z < 0))
			return this;
		else {
			blocks[z + (x * z) + (x * y * z)] = block;
			return this;
		}
	}
	
	public void rebuild() {
		World.chunkUpdates++;
		
		glNewList(glList, GL_COMPILE);
		
		glPushMatrix();
		
		glTranslated(posX * CHUNK_DIM, 0, posZ * CHUNK_DIM);
		
		Tesselator.get.init();
		
		for (int y = 0; y < CHUNK_HEIGHT; y++) {
			for (int x = 0; x < CHUNK_DIM; x++) {
				for (int z = 0; z < CHUNK_DIM; z++) {
					
					boolean up = !World.getBlockWorld(posX * 16 + x, y + 1, posZ * 16 + z).opaque;
					boolean down = !World.getBlockWorld(posX * 16 + x, y - 1, posZ * 16 + z).opaque;
					boolean left = !World.getBlockWorld(posX * 16 + x + 1, y, posZ * 16 + z).opaque;
					boolean right = !World.getBlockWorld(posX * 16 + x - 1, y, posZ * 16 + z).opaque;
					boolean fw = !World.getBlockWorld(posX * 16 + x, y, posZ * 16 + z - 1).opaque;
					boolean bw = !World.getBlockWorld(posX * 16 + x, y, posZ * 16 + z + 1).opaque;
					
					Blocks.render(getBlock(x, y, z), up, down, left, right, fw, bw, x, y, z);
//					Blocks.render(getBlock(x, y, z), true, true, true, true, true, true, x, y, z);
				}
			}
		}
		
		Tesselator.get.flush();

		glPopMatrix();

		glEndList();

		dirty = false;
	}
	
	public boolean isPlayerOn() {
		/*double x = Camera.getX();
		double z = Camera.getZ();
		if(x >= posX*16 && x <= posX*16+16 && z >= posZ*16 && z <= posZ*16+16)
			return true;
		return false;*/
		return false;
	}
	
	public void setDirty() {
		this.dirty = true;
	}
	public boolean isDirty() {
		return dirty;
	}

	public void setGlList(int glList) {
		this.glList = glList;
	}

	public int getGlList() {
		return glList;
	}

	public AABB getAabb() {
		return aabb;
	}

}
