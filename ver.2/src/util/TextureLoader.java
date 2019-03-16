package util;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL11.glTexParameteri;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.lwjgl.opengl.ARBInternalformatQuery2;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GL41;
import org.lwjgl.opengl.GL43;
import org.lwjgl.system.MemoryUtil;

public class TextureLoader {

	private static final int DIMENSIONS = 16;

	public static int grasstop = -1;
	public static int grassside = -1;
	public static int dirt = -1;
	
	private static BufferedImage sheet;
	static {
		try {
			sheet = ImageIO.read(TextureLoader.class.getResourceAsStream("/tex/tx.jpg"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int loadTexture(int col, int row) {
		try {
			BufferedImage image = sheet.getSubimage(row * DIMENSIONS, col * DIMENSIONS, DIMENSIONS, DIMENSIONS);
			
			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, DIMENSIONS, DIMENSIONS, pixels, 0, DIMENSIONS);

			ByteBuffer buffer = MemoryUtil.memAlloc(DIMENSIONS * DIMENSIONS * 4); // 4 for RGBA, 3 for RGB

			for (int i = 0; i < pixels.length; ++i) {
				int a = pixels[i] >> 24 & 255;
				int r = pixels[i] >> 16 & 255;
				int g = pixels[i] >> 8 & 255;
				int b = pixels[i] & 255;
				pixels[i] = a << 24 | r << 16 | g << 8 | b;
			}
			
			buffer.asIntBuffer().put(pixels);
			buffer.flip();
			
			int texture = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texture);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, DIMENSIONS, DIMENSIONS, 0, GL_BGRA, GL_UNSIGNED_BYTE, pixels);
			
			GL30.glGenerateMipmap(GL_TEXTURE_2D);
			
			glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 4);
			
			glBindTexture(GL_TEXTURE_2D, 0);
			
			return texture;
		}
		catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static int loadTexture(BufferedImage image, int format, boolean mipmap) {
		try {
			int[] pixels = new int[image.getWidth() * image.getHeight()];
			image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
			
			ByteBuffer buffer = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4); // 4 for RGBA, 3 for RGB

			for (int i = 0; i < pixels.length; ++i) {
				int a = pixels[i] >> 24 & 255;
				int r = pixels[i] >> 16 & 255;
				int g = pixels[i] >> 8 & 255;
				int b = pixels[i] & 255;
				pixels[i] = a << 24 | b << 16 | g << 8 | r;
			}
			
			buffer.asIntBuffer().put(pixels);
			buffer.flip();
			
			int texture = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texture);
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, mipmap ? GL_NEAREST_MIPMAP_NEAREST : GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
			glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
			
			glTexImage2D(GL_TEXTURE_2D, 0, format, image.getWidth(), image.getHeight(), 0, format, GL_UNSIGNED_BYTE, pixels);
			
			if(mipmap) {
				GL30.glGenerateMipmap(GL_TEXTURE_2D);
			}
			
			glBindTexture(GL_TEXTURE_2D, 0);
			
			return texture;
		}
		catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static void loadGrassTop() {
		grasstop = loadTexture(0, 0);
	}
	public static void loadGrassSide() {
		grassside = loadTexture(0, 3);
	}
	public static void loadDirt() {
		dirt = loadTexture(0, 2);
	}

}
