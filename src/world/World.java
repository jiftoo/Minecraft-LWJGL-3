package world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.system.MemoryUtil;

import engine.Syncer;
import util.Text;
import vertexBuffer.VBO;

public class World {
	private static int vbo = -1;
	
	static {
		glPointSize(2.5f);
		glLineWidth(5f);
		
		Stars.generateNew(1000);
		Camera.create();
		
		vbo = glGenBuffers();
		double[] arr =
			{
				 0.5,0,-0.5,
				-0.5,0,-0.5,
				 0.5,0, 0.5,
				 0.5,0, 0.5,
				-0.5,0,-0.5,
				-0.5,0, 0.5, 
					
				-0.5,0,-0.5,
				 0.5,0,-0.5,
					
				 0.5,0,-0.5,
				 0.5,0, 0.5,
					
				 0.5,0, 0.5,
				-0.5,0, 0.5,
					
				-0.5,0, 0.5,
				-0.5,0,-0.5,
			};
		DoubleBuffer buffer = MemoryUtil.memAllocDouble(arr.length);
		buffer.put(arr);
		buffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	private static void renderPlane()
	{
		glColor3f(1f, 0.3f, 0.1f);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexPointer(3, GL_DOUBLE, 0, 0);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		glColor3f(0f, 1f, 0.5f);
		glDrawArrays(GL_LINES, 6, 8);
		glBindBuffer(GL_ARRAY_BUFFER,0);
	}
	
	private static void renderXZacrossYdown(int x, int y, int z) {
		glPushMatrix();
		glScaled(5, 5, 5);
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < z; j++) {
				glPushMatrix();
				glTranslated(i, y, j);
				renderPlane();
				glPopMatrix();
			}
		}
		glPopMatrix();
	}
	
	public static void render()
	{
		for (int i = -1; i > -16; i--) {
			renderXZacrossYdown(16, i, 16);
		}
		
		
		
		Camera.acceptInput(Syncer.DELTA);
		Camera.apply();
	}
}
