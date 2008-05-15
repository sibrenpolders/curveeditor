package CurveEditor.Curves;

/*
 * Deze klasse stelt een 2D-punt voor.
 */
public class Point {
	protected int X;
	protected int Y;

	// Default constructor.
	public Point() {
		X = -1;
		Y = -1;
	}

	public Point(int X, int Y) {
		this.X = X;
		this.Y = Y;
	}

	public final int X() {
		return X;
	}

	public final void setX(int X) {
		this.X = X;
	}

	// Verhoog X met de gegeven waarde.
	public final void increaseX(int x) {
		this.X += x;
	}

	public final int Y() {
		return Y;
	}

	public final void setY(int Y) {
		this.Y = Y;
	}

	// Verhoog Y met de gegeven waarde.
	public final void increaseY(int y) {
		this.Y += y;
	}

	public String toString() {
		return "X: " + X + ", Y: " + Y;
	}

	// Indien punten in een Vector en dergelijke opgeslaan worden,
	// dan wordt m.b.v. deze functie bepaald of het punt er al in zit.
	public boolean equals(Object obj) {
		if (obj instanceof Point) {
			Point p = (Point) obj;
			return (this.X == p.X && this.Y == p.Y);
		} else {
			return false;
		}
	}

	public final int hashCode() {
		return toString().hashCode();
	}

	// Geef een Point terug dat gelijk is aan het verschil tussen dit Point en
	// het gegeven Point.
	public Point minus(Point other) {
		return new Point(X - other.X, Y - other.Y);
	}

	// Geef een Point terug dat gelijk is aan de som van dit Point en
	// het gegeven Point.
	public Point plus(Point other) {
		return new Point(X + other.X, Y + other.Y);
	}

	// Vermenigvuldig/scaleer dit Point met de waarde d en geef terug.
	public Point times(double d) {
		return new Point((int) Math.floor(X * d + 0.5), (int) Math.floor(Y * d
				+ 0.5));
	}

	// Bereken de lengte, of de afstand tot de oorsprong.
	public int length() {
		return (int) Math.sqrt((double) X * X + Y * Y);
	}

	// Bereken de afstand tussen twee Points.
	public static int distance(Point a, Point b) {
		return (int) Math.sqrt(Math.pow((double) Math.abs(a.X() - b.X()), 2.0)
				+ Math.pow((double) Math.abs(a.Y() - b.Y()), 2.0));
	}
}
