package CurveEditor.Algorithms;

import CurveEditor.Curves.Point;

public class Tangent {
	public static enum CONTINUITY {
		G1, C1
	};

	public Tangent() {
	}

	// co = C1 of G1
	// nr -> 1= links, 2 = rechts
	public Point calculate(CONTINUITY co, short nr, Point a, Point b, Point c) {
		switch (co) {
		case G1:
			if (nr == 1)
				return tangent1G1(a, b, c);
			else if (nr == 2)
				return tangent2G1(a, b, c);
		case C1:
			if (nr == 1)
				return tangent1C1(a, b, c);
			else if (nr == 2)
				return tangent2C1(a, b, c);
		default:
			if (nr == 1)
				return a;
			else if (nr == 2)
				return c;
		}

		if (nr == 1)
			return a;
		else if (nr == 2)
			return c;
		else
			return null;
	}

	// ab = raakvector 1, b = controlepunt, bc = raakvector 2
	// result = punt voor geldige raakvector 1 voor G1
	private Point tangent1G1(Point a, Point b, Point c) {
		if (a.X() < b.X() && c.X() > b.X()) {
			Point temp = c.minus(a);
			double rico = temp.Y() / temp.X();
			double d1 = b.X() - a.X();
			temp = new Point((int) Math.floor(-d1), (int) Math.floor(-rico * d1
					+ 0.5)).plus(b);
			return temp;
		} else if (a.X() > b.X() && c.X() < b.X()) {
			Point temp = a.minus(c);
			double rico = temp.Y() / temp.X();
			double d1 = a.X() - b.X();
			temp = new Point((int) Math.floor(d1), (int) Math.floor(rico * d1
					+ 0.5)).plus(b);
			return temp;
		} else if (a.Y() > b.Y() && c.Y() < b.Y()) {
			Point temp = a.minus(c);
			double ricoInv = temp.X() / temp.Y();
			double d1 = a.Y() - b.Y();
			temp = new Point((int) Math.floor(ricoInv * d1 + 0.5), (int) Math
					.floor(d1)).plus(b);
			return temp;
		} else if (a.Y() < b.Y() && c.Y() > b.Y()) {
			Point temp = c.minus(a);
			double ricoInv = temp.X() / temp.Y();
			double d1 = b.Y() - a.Y();
			temp = new Point((int) Math.floor(-ricoInv * d1 + 0.5), (int) Math
					.floor(-d1)).plus(b);
			return temp;
		} else {
			if (a.Y() > c.Y() && a.Y() <= b.Y()) {
				Point temp = a.minus(c);
				double factor = (b.Y() - a.Y()) / (b.Y() - c.Y());
				temp = new Point((int) Math.floor(temp.X() * factor + 0.5),
						(int) Math.floor(temp.Y() * factor + 0.5)).plus(b);
				return temp;
			} else if (a.Y() > c.Y() && c.Y() >= b.Y()) {
				Point temp = a.minus(c);
				double factor = 1.0 - (c.Y() - b.Y()) / (a.Y() - b.Y());
				temp = new Point((int) Math.floor(temp.X() * factor + 0.5),
						(int) Math.floor(temp.Y() * factor + 0.5)).plus(b);
				return temp;
			} else if (c.Y() > a.Y() && a.Y() >= b.Y()) {
				Point temp = c.minus(a);
				double factor = (a.Y() - b.Y()) / (c.Y() - b.Y());
				temp = new Point((int) Math.floor(-temp.X() * factor + 0.5),
						(int) Math.floor(-temp.Y() * factor + 0.5)).plus(b);
				return temp;
			} else if (c.Y() > a.Y() && c.Y() <= b.Y()) {
				Point temp = c.minus(a);
				double factor = 1.0 - (b.Y() - c.Y()) / (b.Y() - c.Y());
				temp = new Point((int) Math.floor(-temp.X() * factor + 0.5),
						(int) Math.floor(-temp.Y() * factor + 0.5)).plus(b);
				return temp;
			}
		}
		return a;
	}

	// ab = raakvector 1, b = controlepunt, bc = raakvector 2
	// result = punt voor geldige raakvector 2 voor G1
	private Point tangent2G1(Point a, Point b, Point c) {
		if (a.X() < b.X() && c.X() > b.X()) {
			Point temp = c.minus(a);
			double rico = temp.Y() / temp.X();
			double d1 = c.X() - b.X();
			temp = new Point((int) Math.floor(d1), (int) Math.floor(rico * d1
					+ 0.5)).plus(b);
			return temp;
		} else if (a.X() > b.X() && c.X() < b.X()) {
			Point temp = a.minus(c);
			double rico = temp.Y() / temp.X();
			double d1 = b.X() - c.X();
			temp = new Point((int) Math.floor(-d1), (int) Math.floor(-rico * d1
					+ 0.5)).plus(b);
			return temp;
		} else if (a.Y() > b.Y() && c.Y() < b.Y()) {
			Point temp = a.minus(c);
			double ricoInv = temp.X() / temp.Y();
			double d1 = b.Y() - c.Y();
			temp = new Point((int) Math.floor(-ricoInv * d1 + 0.5), (int) Math
					.floor(-d1)).plus(b);
			return temp;
		} else if (a.Y() < b.Y() && c.Y() > b.Y()) {
			Point temp = c.minus(a);
			double ricoInv = temp.X() / temp.Y();
			double d1 = c.Y() - b.Y();
			temp = new Point((int) Math.floor(ricoInv * d1 + 0.5), (int) Math
					.floor(d1)).plus(b);
			return temp;
		} else {
			if (a.Y() > c.Y() && a.Y() <= b.Y()) {
				Point temp = a.minus(c);
				double factor = 1.0 - (b.Y() - a.Y()) / (b.Y() - c.Y());
				temp = new Point((int) Math.floor(-temp.X() * factor + 0.5),
						(int) Math.floor(-temp.Y() * factor + 0.5)).plus(b);
				return temp;
			} else if (a.Y() > c.Y() && c.Y() >= b.Y()) {
				Point temp = a.minus(c);
				double factor = (c.Y() - b.Y()) / (a.Y() - b.Y());
				temp = new Point((int) Math.floor(-temp.X() * factor + 0.5),
						(int) Math.floor(-temp.Y() * factor + 0.5)).plus(b);
				return temp;
			} else if (c.Y() > a.Y() && a.Y() >= b.Y()) {
				Point temp = c.minus(a);
				double factor = 1.0 - (a.Y() - b.Y()) / (c.Y() - b.Y());
				temp = new Point((int) Math.floor(temp.X() * factor + 0.5),
						(int) Math.floor(temp.Y() * factor + 0.5)).plus(b);
				return temp;
			} else if (c.Y() > a.Y() && c.Y() <= b.Y()) {
				Point temp = c.minus(a);
				double factor = (b.Y() - c.Y()) / (b.Y() - c.Y());
				temp = new Point((int) Math.floor(temp.X() * factor + 0.5),
						(int) Math.floor(temp.Y() * factor + 0.5)).plus(b);
				return temp;
			}
		}
		return c;
	}

	// ab = raakvector 1, b = controlepunt, bc = raakvector 2
	// result = punt voor geldige raakvector 1 voor C1
	private Point tangent1C1(Point a, Point b, Point c) {
		if (a.X() < b.X() && c.X() > b.X()) {
			Point temp = c.minus(a);
			temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5), (int) Math
					.floor(-temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		} else if (a.X() > b.X() && c.X() < b.X()) {
			Point temp = a.minus(c);
			temp = new Point((int) Math.floor(temp.X() / 2 + 0.5), (int) Math
					.floor(temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		} else if (a.Y() > b.Y() && c.Y() < b.Y()) {
			Point temp = a.minus(c);
			temp = new Point((int) Math.floor(temp.X() / 2 + 0.5), (int) Math
					.floor(temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		} else if (a.Y() < b.Y() && c.Y() > b.Y()) {
			Point temp = c.minus(a);
			temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5), (int) Math
					.floor(-temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		} else {
			if (a.Y() > c.Y() && a.Y() <= b.Y()) {
				Point temp = a.minus(c);

				int x = (int) Math.floor(temp.X() / 2 + 0.5);
				int y = (int) Math.floor(temp.Y() / 2 + 0.5);

				temp = new Point(x, y).plus(b);

				return temp;
			} else if (a.Y() > c.Y() && c.Y() >= b.Y()) {
				Point temp = a.minus(c);
				temp = new Point((int) Math.floor(temp.X() / 2 + 0.5),
						(int) Math.floor(temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			} else if (c.Y() > a.Y() && a.Y() >= b.Y()) {
				Point temp = c.minus(a);
				temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5),
						(int) Math.floor(-temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			} else if (c.Y() > a.Y() && c.Y() <= b.Y()) {
				Point temp = c.minus(a);
				temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5),
						(int) Math.floor(-temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			}
		}
		return c;
	}

	// ab = raakvector 1, b = controlepunt, bc = raakvector 2
	// result = punt voor geldige raakvector 2 voor C1
	private Point tangent2C1(Point a, Point b, Point c) {
		if (a.X() < b.X() && c.X() > b.X()) {
			Point temp = c.minus(a);
			temp = new Point((int) Math.floor(temp.X() / 2 + 0.5), (int) Math
					.floor(temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		} else if (a.X() > b.X() && c.X() < b.X()) {
			Point temp = a.minus(c);
			temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5), (int) Math
					.floor(-temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		} else if (a.Y() > b.Y() && c.Y() < b.Y()) {
			Point temp = a.minus(c);
			temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5), (int) Math
					.floor(-temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		} else if (a.Y() < b.Y() && c.Y() > b.Y()) {
			Point temp = c.minus(a);
			temp = new Point((int) Math.floor(temp.X() / 2 + 0.5), (int) Math
					.floor(temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		} else {
			if (a.Y() > c.Y() && a.Y() <= b.Y()) {
				Point temp = a.minus(c);
				temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5),
						(int) Math.floor(-temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			} else if (a.Y() > c.Y() && c.Y() >= b.Y()) {
				Point temp = a.minus(c);
				temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5),
						(int) Math.floor(-temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			} else if (c.Y() > a.Y() && a.Y() >= b.Y()) {
				Point temp = c.minus(a);
				temp = new Point((int) Math.floor(temp.X() / 2 + 0.5),
						(int) Math.floor(temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			} else if (c.Y() > a.Y() && c.Y() <= b.Y()) {
				Point temp = c.minus(a);
				temp = new Point((int) Math.floor(temp.X() / 2 + 0.5),
						(int) Math.floor(temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			}
		}
		return c;
	}

	// ab = raakvector 1, b = controlepunt
	// result = punt voor geldige raakvector 2 voor C1
	public Point tangent2C1_(Point a, Point b) {
		Point temp = b.times(2.0);
		temp = temp.minus(a);

		return temp;
	}
}
