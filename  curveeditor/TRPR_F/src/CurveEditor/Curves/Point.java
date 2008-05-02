package CurveEditor.Curves;

public class Point {
	protected int X;
	protected int Y;

	public Point() {
		X = -1;
		Y = -1;
	}

	public Point(int X, int Y) {
		this.X = X;
		this.Y = Y;
	}

	public int length() {
		return (int) Math.sqrt((double) X * X + Y * Y);
	}

	public static int distance(Point a, Point b) {
		return (int) Math.sqrt(Math.pow((double) Math.abs(a.X() - b.X()), 2.0)
				+ Math.pow((double) Math.abs(a.Y() - b.Y()), 2.0));
	}

	public int X() {
		return X;
	}

	public void setX(int X) {
		this.X = X;
	}

	public void increaseX(int x) {
		this.X += x;
	}

	public int Y() {
		return Y;
	}

	public void setY(int Y) {
		this.Y = Y;
	}

	public void increaseY(int y) {
		this.Y += y;
	}

	public String toString() {
		return "X: " + X + ", Y: " + Y;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point p = (Point) obj;
			return (this.X == p.X && this.Y == p.Y);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public Point minus(Point other) {
		return new Point(X - other.X, Y - other.Y);
	}

	public Point plus(Point other) {
		return new Point(X + other.X, Y + other.Y);
	}

	public Point times(double d) {
		return new Point((int) Math.floor(X * d + 0.5), (int) Math.floor(Y * d
				+ 0.5));
	}
}
