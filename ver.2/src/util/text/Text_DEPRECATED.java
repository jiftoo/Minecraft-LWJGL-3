package util.text;

import static org.lwjgl.opengl.GL11.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.opengl.GL11;

import util.TextureLoader;

public class Text_DEPRECATED {
	
	private static int[] ascii_lists = new int[255];
	
	private static final int FONT_SAMPLES = 16;
	
	public static Font mono;
	
	public static String base;
	static {
		StringBuilder b = new StringBuilder(255);
		for (int i = 32; i < 127; i++) {
			b.append((char)i);
		}
		base = b.toString();
		
		Map<TextAttribute, Object> att = new HashMap<>();
		att.put(TextAttribute.SIZE, FONT_SAMPLES);
		att.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRA_LIGHT);
//		att.put(TextAttribute.WIDTH, TextAttribute.WIDTH_CONDENSED);
		try {
			mono = Font.createFont(Font.TRUETYPE_FONT, Text_DEPRECATED.class.getResourceAsStream("/font/mono/mono.ttf")).deriveFont(att);
		} catch (Exception e) {
		}
	}
	
	private static void genList(BufferedImage img, int aval) {
		int texture = TextureLoader.loadTexture(img, GL_RGBA, false);

		int list = glGenLists(1);

		glNewList(list, GL_COMPILE);
		{
			glBindTexture(GL_TEXTURE_2D, texture);
			glBegin(GL_QUADS);
			{
				glTexCoord2f(0, 0);
				glVertex2f(0, 0);

				glTexCoord2f(1, 0);
				glVertex2f(img.getWidth(), 0);

				glTexCoord2f(1, 1);
				glVertex2f(img.getWidth(), img.getHeight());

				glTexCoord2f(0, 1);
				glVertex2f(0, img.getHeight());
			}
			glEnd();
			glTranslated(img.getWidth(), 0, 0);
			glBindTexture(GL_TEXTURE_2D, 0);
		}
		glEndList();
		ascii_lists[aval] = list;
	}
	
	public static void load() throws Exception {
		char[] chars = new char[base.length()];
		base.getChars(0, base.length(), chars, 0);
		
		Graphics2D g2 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
		FontMetrics m = g2.getFontMetrics(mono);
		
		for (char c : chars) {
			int ascii_value = (int)c;
			
			String ch = Character.toString(c);
			
			BufferedImage img = new BufferedImage(m.stringWidth(ch), m.getAscent() + m.getDescent()+ 3, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = img.createGraphics();
			
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			
			g.setFont(mono);
			g.drawString(ch, img.getWidth()-m.stringWidth(ch), m.getAscent()+2);
			g.dispose();
			
			genList(img, ascii_value);
		}
	}
	
	private static final HashMap<String, Integer> string_lists = new HashMap<>(16, 0.9f);
	
	public static void drawString(float x, float y, float size, float r, float g, float b, boolean saveAsList, String what) {
		Integer thelist;
		
		if(saveAsList) {
			if((thelist = string_lists.get(what)) != null) {
				glCallList(thelist);
			} else {
				int newlist = glGenLists(1);
				
				glNewList(newlist, GL_COMPILE_AND_EXECUTE);
				{
					draw_internal(x, y, size, r, g, b, what);
				}
				glEndList();
				
				string_lists.put(what, newlist);
			}
		} else {
			draw_internal(x, y, size, r, g, b, what);
		}
	}
	
	private static void draw_internal(float x, float y, float size, float r, float g, float b, String what) {
		glEnable(GL11.GL_BLEND);
		glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		glPushMatrix();
		glTranslated(x, y, 0);
		//glScaled(size / (double)FONT_SAMPLES, size / (double)FONT_SAMPLES, 0);
		glColor3f(r, g, b);
		for (int i = 0; i < what.length(); i++) {
			glCallList(ascii_lists[what.charAt(i)]);
		}
		glPopMatrix();
		
		glDisable(GL11.GL_BLEND);
	}
	
	public static void drawString(float x, float y, float size, String what) {
		drawString(x, y, size, 1f, 1f, 1f, false, what);
	}
	
}