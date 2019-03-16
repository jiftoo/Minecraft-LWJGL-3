package world.chunk;

public class Block {
	
	public static Block AIR;
	public static Block GRASS;
	public static Block DIRT;
	public static Block ROCK;
	
	public final int id;
	public final boolean opaque;
	
	public Block(int id, boolean opaque) {
		this.id = id;
		this.opaque = opaque;
	}
	
	public static void createBlocks() {
		AIR = new Block(0, false);
		GRASS = new Block(1, true);
		DIRT = new Block(2, true);
		ROCK = new Block(3, true);
	}
	
	@Override
	public String toString() {
		return "Block:id="+id;
	}

}
