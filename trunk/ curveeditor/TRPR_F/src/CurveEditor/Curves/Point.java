package CurveEditor.Curves;

public class Point {

	protected int X;
	protected int Y;

	public Point(int X, int Y) {
		this.X = X;
		this.Y = Y;
	}

	public int X() {
		return X;
	}

	public void setX(int X) {
		this.X = X;
	}

	public int Y() {
		return Y;
	}

	public void setY(int Y) {
		this.Y = Y;
	}

	public String toString() {
		return "X: " + X + ", Y: " + Y;
	}
}
