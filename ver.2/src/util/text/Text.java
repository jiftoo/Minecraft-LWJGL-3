package util.text;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

import engine.Engine;
import gui.GUI;
import util.TextureLoader;

public class Text {

	private static int[] widthmap = new int[256];
	private static int fontTexture = -1;

	public static void load(String path) {
		BufferedImage img;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException var13) {
			throw new RuntimeException(var13);
		}

		int imgW = img.getWidth();
		int imgH = img.getHeight();
		int[] rgbarr = new int[imgW * imgH];
		img.getRGB(0, 0, imgW, imgH, rgbarr, 0, imgW);

		for (int i = 0; i < 128; ++i) {
			imgH = i % 16;
			int var7 = i / 16;
			int var8 = 0;

			for (boolean bool = false; var8 < 8 && !bool; ++var8) {
				int var10 = (imgH << 3) + var8;
				bool = true;

				for (int var11 = 0; var11 < 8 && bool; ++var11) {
					int var12 = ((var7 << 3) + var11) * imgW;
					if ((rgbarr[var10 + var12] & 255) > 128) {
						bool = false;
					}
				}
			}

			if (i == 32) {
				var8 = 4;
			}

			widthmap[i] = var8;
		}

		fontTexture = TextureLoader.loadTexture(img, GL_RGBA, false);
	}
	
	public static void render(int x, int y, double size, String text) {
		render(text, x + 1, y + 1, 0xffffff, size, true);
		renderNoShadow(x, y, size, 0xffffff, text);
	}

	public static void render(int x, int y, double size, int rgb, String text) {
		render(text, x + 1, y + 1, rgb, size, true);
		renderNoShadow(x, y, size, rgb, text);
	}

	public static void renderNoShadow(int x, int y, double size, int rgb, String text) {
		render(text, x, y, rgb, size, false);
	}

	private static void render(String text, int x, int y, int rgb, double size, boolean shadow) {
		if (text != null) {
			char[] characters = text.toCharArray();
			if (shadow) {
				rgb = (rgb & 16579836) >> 2;
			}
			
			glPushMatrix();

			glScaled(size, size, 0);

			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

			GL11.glBindTexture(GL_TEXTURE_2D, fontTexture);
			ShapeRenderer var6 = ShapeRenderer.instance;
			ShapeRenderer.instance.begin();
			var6.color(rgb);
			int var7 = 0;
			
			for (int i = 0; i < characters.length; ++i) {
				int var9;
				
				
				if (characters[i] == 38 && characters.length > i + 1) {
					if ((rgb = "0123456789abcde".indexOf(characters[i + 1])) < 0) {
						rgb = 16;
					}
					
					var9 = (rgb & 8) << 3;
					int var10 = (rgb & 1) * 191 + var9;
					int var11 = ((rgb & 2) >> 1) * 191 + var9;
					rgb = ((rgb & 4) >> 2) * 191 + var9;

					rgb = rgb << 16 | var11 << 8 | var10;
					i += 2;
					if (shadow) {
						rgb = (rgb & 16579836) >> 2;
					}

					var6.color(rgb);
				}

				
				rgb = characters[i] % 16 << 3;
				var9 = characters[i] / 16 << 3;
				float var13 = 7.99F;
				var6.vertexUV((float) (x + var7), (float) y + var13, 0.0F, (float) rgb / 128.0F,
						((float) var9 + var13) / 128.0F);
				var6.vertexUV((float) (x + var7) + var13, (float) y + var13, 0.0F, ((float) rgb + var13) / 128.0F,
						((float) var9 + var13) / 128.0F);
				var6.vertexUV((float) (x + var7) + var13, (float) y, 0.0F, ((float) rgb + var13) / 128.0F,
						(float) var9 / 128.0F);
				var6.vertexUV((float) (x + var7), (float) y, 0.0F, (float) rgb / 128.0F, (float) var9 / 128.0F);
				var7 += widthmap[characters[i]];
			}

			var6.end();

			glDisable(GL_BLEND);
			
			glPopMatrix();
		}
	}
	
	public final int getWidth(String var1) {
		if (var1 == null) {
			return 0;
		} else {
			char[] var4 = var1.toCharArray();
			int var2 = 0;

			for (int var3 = 0; var3 < var4.length; ++var3) {
				if (var4[var3] == 38) {
					++var3;
				} else {
					var2 += this.widthmap[var4[var3]];
				}
			}

			return var2;
		}
	}

	public static String stripColor(String var0) {
		char[] var3 = var0.toCharArray();
		String var1 = "";

		for (int var2 = 0; var2 < var3.length; ++var2) {
			if (var3[var2] == 38) {
				++var2;
			} else {
				var1 = var1 + var3[var2];
			}
		}

		return var1;
	}

}