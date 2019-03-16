package physics;

public class AABB {

	public float x0, x1;
	public float y0, y1;
	public float z0, z1;

	public AABB(float x0, float y0, float z0, float x1, float y1, float z1) {
		this.x0 = x0;
		this.y0 = y0;
		this.z0 = z0;
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
	}
	
	@Override
	public String toString() {
		return x0 + ":" + y0 + ":" + z0 + " " + x1 + ":" + y1 + ":" + z1;
	}

}
