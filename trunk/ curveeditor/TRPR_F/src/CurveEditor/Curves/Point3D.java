package CurveEditor.Curves;

public class Point3D extends Point {

	protected int Z;

	public Point3D() {
		Z = -1;
	}
	
	public Point3D(int x, int y, int z) {
		super(x, y);
		this.Z = z;
	}

	public int Z() {
		return Z;
	}

	public void setZ(int Z) {
		this.Z = Z;
	}

	public String toString() {
		return super.toString() + ", Z: " + Z;
	}
}
