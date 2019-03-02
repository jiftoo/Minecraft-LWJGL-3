package vertexBuffer;

import static org.lwjgl.opengl.GL11.GL_BYTE;
import static org.lwjgl.opengl.GL11.GL_DOUBLE;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.util.HashSet;

import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

public class VBO {
	
	private static final HashSet<Integer> vboIndices = new HashSet<>();
	
	
	private final int vboID;
	private boolean invalid = false;
	private boolean shouldmemfree = false;
	
	private final int datatype;
	private final int usage;
	private final int mode;
	private final int vertexcount;
	private final int vertexsize;
	
	private final Buffer dataPointer;
	
	
	// Array versions
	
	public VBO(float[] arr, int vertexsize, int usage, int mode) {
		shouldmemfree = true;
		
		FloatBuffer buf = MemoryUtil.memAllocFloat(arr.length);
		buf.put(arr);
		buf.flip();
		
		this.dataPointer = buf;
		
		this.datatype = GL_FLOAT;
		this.usage = usage;
		this.mode = mode;
		
		this.vertexsize = vertexsize;
		this.vertexcount = arr.length / vertexsize;
		
		this.vboID = glGenBuffers();
		vboIndices.add(vboID);
		
		feedF(buf);
	}
	public VBO(double[] arr, int vertexsize, int usage, int mode) {
		shouldmemfree = true;
		
		DoubleBuffer buf = MemoryUtil.memAllocDouble(arr.length);
		buf.put(arr);
		buf.flip();
		
		this.dataPointer = buf;
		
		this.datatype = GL_DOUBLE;
		this.usage = usage;
		this.mode = mode;
		
		this.vertexsize = vertexsize;
		this.vertexcount = arr.length / vertexsize;
		
		this.vboID = glGenBuffers();
		vboIndices.add(vboID);
		
		feedD(buf);
	}
	public VBO(byte[] arr, int vertexsize, int usage, int mode) {
		shouldmemfree = true;
		
		ByteBuffer buf = MemoryUtil.memAlloc(arr.length);
		buf.put(arr);
		buf.flip();
		
		this.dataPointer = buf;
		
		this.datatype = GL_BYTE;
		this.usage = usage;
		this.mode = mode;
		
		this.vertexsize = vertexsize;
		this.vertexcount = arr.length / vertexsize;
		
		this.vboID = glGenBuffers();
		vboIndices.add(vboID);
		
		feedB(buf);
	}
	
	// Buffer versions
	
	public VBO(FloatBuffer buf, int vertexsize, int usage, int mode) {
		this.dataPointer = buf;
		
		this.datatype = GL_FLOAT;
		this.usage = usage;
		this.mode = mode;
		
		this.vertexsize = vertexsize;
		this.vertexcount = buf.limit() / vertexsize;
		
		this.vboID = glGenBuffers();
		vboIndices.add(vboID);
		
		feedF(buf);
	}
	public VBO(DoubleBuffer buf, int vertexsize, int usage, int mode) {
		this.dataPointer = buf;
		
		this.datatype = GL_DOUBLE;
		this.usage = usage;
		this.mode = mode;
		
		this.vertexsize = vertexsize;
		this.vertexcount = buf.limit() / vertexsize;
		
		this.vboID = glGenBuffers();
		vboIndices.add(vboID);
		
		feedD(buf);
	}
	public VBO(ByteBuffer buf, int vertexsize, int usage, int mode) {
		this.dataPointer = buf;
		
		this.datatype = GL_BYTE;
		this.usage = usage;
		this.mode = mode;
		
		this.vertexsize = vertexsize;
		this.vertexcount = buf.limit() / vertexsize;
		
		this.vboID = glGenBuffers();
		vboIndices.add(vboID);
		
		feedB(buf);
	}
	
	
	public void bind() {
		checkDeleted();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
	}
	public void unbind() {
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	
	public void draw() {
		bind();
		System.out.println(GL15.glIsBuffer(vboID));
		
		glVertexPointer(vertexsize, datatype, 0, 0l);
		glDrawArrays(mode, 0, vertexcount);
		
		unbind();
	}
	
	public void delete() {
		checkDeleted();
		glDeleteBuffers(vboID);
		
		if(shouldmemfree)
			MemoryUtil.memFree(dataPointer);
	}
	
	
	
	private void checkDeleted() {
		if(invalid) throw new IllegalStateException("This buffer is deleted.");
	}
	
	private void feedF(FloatBuffer b)
	{
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, b, usage);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	private void feedD(DoubleBuffer b)
	{
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, b, usage);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	private void feedB(ByteBuffer b)
	{
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		glBufferData(GL_ARRAY_BUFFER, b, usage);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
}
