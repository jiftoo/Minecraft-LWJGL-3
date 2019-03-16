package launcher;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import java.lang.reflect.Field;
import javax.swing.JOptionPane;
import org.lwjgl.opengl.GL11;

import engine.Engine;

public class Launcher {
	public static String[] arguments;
	public static void main(String[] args) {
		
		arguments = args;
		try {
			new Engine(1280, 720, "My Game", false);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e, "Eroor", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	public static String getGlValue(long val) {
		Class[] classes = {
			GL11.class
		};
		
		int vali = (int) val;
		
		String ret = null;
		
		for (Class cl : classes) {
			for(Field f : cl.getDeclaredFields()) {
				try {
					Object o = f.get(null);
					Long v1 = -1l; Integer v2 = -1;
					
					if(o instanceof Long) {
						v1 = (Long)o;
						if(v1 == val) {
							ret = f.getName();
							break;
						}
					} else if(o instanceof Integer) {
						v2 = (Integer)o;
						if(v2 == vali) {
							ret = f.getName();
							break;
						}
					}
					
				} catch (Exception e) { System.out.println("Lmao how?");}
			}
		}
		
		return ret;
	}
}