package CurveEditor.Algorithms;

import CurveEditor.Curves.Point;
import CurveEditor.Exceptions.*;

/*
 * Deze klasse is de klasse die de methodes voorziet om uit een gegeven drietal input-
 * punten een ander drietal inputpunten te berekenen, die voldoen aan ofwel C1-
 * ofwel G1-continuïteit.
 */
public final class Tangent {

	// Enum-container die ervoor zorgt dat één publieke methode volstaat
	// om alle mogelijke inputpunten te berekenen. Het berekende
	// inputpunt hangt dan af van de meegegeven enum-waarde als parameter.
	public static enum CONTINUITY {
		G1, C1
	};

	public Tangent() {
	}

	// Mogelijke parametercombinaties, a = linkertangent, b = controlepunt, c =
	// rechtertangent:
	//
	// C1, 1 --> dit geeft de linkertangent zodanig dat linker- en
	// rechtertangent op één lijn liggen en op eenzelfde afstand van punt b
	// liggen
	// 
	// C1, 2 --> dit geeft de rechtertangent zodanig dat linker- en
	// rechtertangent op één lijn liggen en op eenzelfde afstand van punt b
	// liggen
	// 
	// G1, 1 --> dit geeft de linkertangent zodanig dat linker- en
	// rechtertangent op één lijn liggen en de verhouding van de afstanden tot
	// punt b overeenkomt met de oorspronkelijke verhouding
	// 
	// G1, 2 --> dit geeft de rechtertangent zodanig dat linker- en
	// rechtertangent op één lijn liggen en de verhouding van de afstanden tot
	// punt b overeenkomt met de oorspronkelijke verhouding
	public final Point calculate(CONTINUITY co, short nr, Point a, Point b,
			Point c) throws InvalidArgumentException {
		if (nr > 2 || nr < 1 || a == null || b == null || c == null)
			throw new InvalidArgumentException(
					"Tangent.java - calculate(CONTINUITY, short, Point, Point, Point): Invalid Argument.");
		else {
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
	}

	// ab = raakvector 1, b = controlepunt, bc = raakvector 2
	// result = punt voor geldige raakvector 1 voor G1
	//
	// Het resultaat wordt bekomen door de vector ac te verschuiven zodanig dat
	// het punt b op die vector ligt. Het nieuwe eerste uiteinde van die
	// vector is dan het gevraagde punt.
	private final Point tangent1G1(Point a, Point b, Point c) {
		// horizontale volgorde: a ... b ... c
		if (a.X() < b.X() && c.X() > b.X()) {
			Point temp = c.minus(a);
			double rico = temp.Y() / temp.X(); // kan nooit nul zijn
			double d1 = b.X() - a.X();
			temp = new Point((int) Math.floor(-d1), (int) Math.floor(-rico * d1
					+ 0.5)).plus(b);
			return temp;
		}
		// horizontale volgorde: c ... b ... a
		else if (a.X() > b.X() && c.X() < b.X()) {
			Point temp = a.minus(c);
			double rico = temp.Y() / temp.X(); // kan nooit nul zijn
			double d1 = a.X() - b.X();
			temp = new Point((int) Math.floor(d1), (int) Math.floor(rico * d1
					+ 0.5)).plus(b);
			return temp;
		}
		// verticale volgorde: a ... b ... c
		else if (a.Y() > b.Y() && c.Y() < b.Y()) {
			Point temp = a.minus(c);
			double ricoInv = temp.X() / temp.Y(); // kan nooit nul zijn
			double d1 = a.Y() - b.Y();
			temp = new Point((int) Math.floor(ricoInv * d1 + 0.5), (int) Math
					.floor(d1)).plus(b);
			return temp;
		}
		// verticale volgorde: c ... b ... a
		else if (a.Y() < b.Y() && c.Y() > b.Y()) {
			Point temp = c.minus(a);
			double ricoInv = temp.X() / temp.Y(); // kan nooit nul zijn
			double d1 = b.Y() - a.Y();
			temp = new Point((int) Math.floor(-ricoInv * d1 + 0.5), (int) Math
					.floor(-d1)).plus(b);
			return temp;
		}
		// Beide tangentpunten liggen in eenzelfde kwadrant van het vlak t.o.v.
		// het punt b --> nu worden de eindpunten van de vector ac verplaatst,
		// overeenkomstig met de oorspronkelijke afstand tot b.
		//
		// Bvb.: a en c liggen onder b, a ligt verder van b dan c
		// --> het eerste eindpunt van de vector ac zal ook onder b
		// komen te liggen, het andere eindpunt zal boven b liggen.
		else {
			// verticale volgorde: b ... a ... c
			if (a.Y() > c.Y() && a.Y() <= b.Y()) {
				Point temp = a.minus(c);
				double factor = (b.Y() - a.Y()) / (b.Y() - c.Y());
				temp = new Point((int) Math.floor(temp.X() * factor + 0.5),
						(int) Math.floor(temp.Y() * factor + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: a ... c ... b
			else if (a.Y() > c.Y() && c.Y() >= b.Y()) {
				Point temp = a.minus(c);
				double factor = 1.0 - (c.Y() - b.Y()) / (a.Y() - b.Y());
				temp = new Point((int) Math.floor(temp.X() * factor + 0.5),
						(int) Math.floor(temp.Y() * factor + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: c ... a ... b
			else if (c.Y() > a.Y() && a.Y() >= b.Y()) {
				Point temp = c.minus(a);
				double factor = (a.Y() - b.Y()) / (c.Y() - b.Y());
				temp = new Point((int) Math.floor(-temp.X() * factor + 0.5),
						(int) Math.floor(-temp.Y() * factor + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: b ... c ... a
			else if (c.Y() > a.Y() && c.Y() <= b.Y()) {
				Point temp = c.minus(a);
				double factor = 1.0 - (b.Y() - c.Y()) / (b.Y() - a.Y());

				temp = new Point((int) Math.floor(-temp.X() * factor + 0.5),
						(int) Math.floor(-temp.Y() * factor + 0.5)).plus(b);

				return temp;
			}
		}
		return a;
	}

	// ab = raakvector 1, b = controlepunt, bc = raakvector 2
	// result = punt voor geldige raakvector 2 voor G1
	//
	// Het resultaat wordt bekomen door de vector ac te verschuiven zodanig dat
	// het punt b op die vector ligt. Het nieuwe tweede uiteinde van die
	// vector is dan het gevraagde punt.
	private final Point tangent2G1(Point a, Point b, Point c) {
		// horizontale volgorde: a ... b ... c
		if (a.X() < b.X() && c.X() > b.X()) {
			Point temp = c.minus(a);
			double rico = temp.Y() / temp.X();
			double d1 = c.X() - b.X();
			temp = new Point((int) Math.floor(d1), (int) Math.floor(rico * d1
					+ 0.5)).plus(b);
			return temp;
		}
		// horizontale volgorde: c ... b ... a
		else if (a.X() > b.X() && c.X() < b.X()) {
			Point temp = a.minus(c);
			double rico = temp.Y() / temp.X();
			double d1 = b.X() - c.X();
			temp = new Point((int) Math.floor(-d1), (int) Math.floor(-rico * d1
					+ 0.5)).plus(b);
			return temp;
		}
		// verticale volgorde: a ... b ... c
		else if (a.Y() > b.Y() && c.Y() < b.Y()) {
			Point temp = a.minus(c);
			double ricoInv = temp.X() / temp.Y();
			double d1 = b.Y() - c.Y();
			temp = new Point((int) Math.floor(-ricoInv * d1 + 0.5), (int) Math
					.floor(-d1)).plus(b);
			return temp;
		}
		// verticale volgorde: c ... b ... a
		else if (a.Y() < b.Y() && c.Y() > b.Y()) {
			Point temp = c.minus(a);
			double ricoInv = temp.X() / temp.Y();
			double d1 = c.Y() - b.Y();
			temp = new Point((int) Math.floor(ricoInv * d1 + 0.5), (int) Math
					.floor(d1)).plus(b);
			return temp;
		}
		// Beide tangentpunten liggen in eenzelfde kwadrant van het vlak t.o.v.
		// het punt b --> nu worden de eindpunten van de vector ac verplaatst,
		// overeenkomstig met de oorspronkelijke afstand tot b.
		//
		// Bvb.: a en c liggen onder b, a ligt verder van b dan c
		// --> het eerste eindpunt van de vector ac zal ook onder b
		// komen te liggen, het andere eindpunt zal boven b liggen.
		else {
			// verticale volgorde: b ... a ... c
			if (a.Y() > c.Y() && a.Y() <= b.Y()) {
				Point temp = a.minus(c);
				double factor = 1.0 - (b.Y() - a.Y()) / (b.Y() - c.Y());
				temp = new Point((int) Math.floor(-temp.X() * factor + 0.5),
						(int) Math.floor(-temp.Y() * factor + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: a ... c ... b
			else if (a.Y() > c.Y() && c.Y() >= b.Y()) {
				Point temp = a.minus(c);
				double factor = (c.Y() - b.Y()) / (a.Y() - b.Y());
				temp = new Point((int) Math.floor(-temp.X() * factor + 0.5),
						(int) Math.floor(-temp.Y() * factor + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: c ... a ... b
			else if (c.Y() > a.Y() && a.Y() >= b.Y()) {
				Point temp = c.minus(a);
				double factor = 1.0 - (a.Y() - b.Y()) / (c.Y() - b.Y());
				temp = new Point((int) Math.floor(temp.X() * factor + 0.5),
						(int) Math.floor(temp.Y() * factor + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: b ... c ... a
			else if (c.Y() > a.Y() && c.Y() <= b.Y()) {
				Point temp = c.minus(a);
				double factor = (b.Y() - c.Y()) / (b.Y() - a.Y());
				temp = new Point((int) Math.floor(temp.X() * factor + 0.5),
						(int) Math.floor(temp.Y() * factor + 0.5)).plus(b);
				return temp;
			}
		}
		return c;
	}

	// ab = raakvector 1, b = controlepunt, bc = raakvector 2
	// result = punt voor geldige raakvector 1 voor C1
	//
	// Het resultaat wordt bekomen door de vector ac te verschuiven zodanig dat
	// het punt b op die vector ligt én die vector door het punt b in twee
	// gelijke delen verdeeld wordt. Het nieuwe eerste uiteinde van die
	// vector is dan het gevraagde punt.
	private final Point tangent1C1(Point a, Point b, Point c) {
		// horizontale volgorde: a ... b ... c
		if (a.X() < b.X() && c.X() > b.X()) {
			Point temp = c.minus(a);
			temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5), (int) Math
					.floor(-temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		}
		// horizontale volgorde: c ... b ... a
		else if (a.X() > b.X() && c.X() < b.X()) {
			Point temp = a.minus(c);
			temp = new Point((int) Math.floor(temp.X() / 2 + 0.5), (int) Math
					.floor(temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		}
		// verticale volgorde: a ... b ... c
		else if (a.Y() > b.Y() && c.Y() < b.Y()) {
			Point temp = a.minus(c);
			temp = new Point((int) Math.floor(temp.X() / 2 + 0.5), (int) Math
					.floor(temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		}
		// verticale volgorde: c ... b ... a
		else if (a.Y() < b.Y() && c.Y() > b.Y()) {
			Point temp = c.minus(a);
			temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5), (int) Math
					.floor(-temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		}
		// Beide tangentpunten liggen in eenzelfde kwadrant van het vlak t.o.v.
		// het punt b --> nu worden de eindpunten van de vector ac verplaatst,
		// overeenkomstig met de oorspronkelijke afstand tot b.
		//
		// Bvb.: a en c liggen onder b, a ligt verder van b dan c
		// --> het eerste eindpunt van de vector ac zal ook onder b
		// komen te liggen, het andere eindpunt zal boven b liggen.
		else {
			// verticale volgorde: b ... a ... c
			if (a.Y() > c.Y() && a.Y() <= b.Y()) {
				Point temp = a.minus(c);

				int x = (int) Math.floor(temp.X() / 2 + 0.5);
				int y = (int) Math.floor(temp.Y() / 2 + 0.5);

				temp = new Point(x, y).plus(b);

				return temp;
			}
			// verticale volgorde: a ... c ... b
			else if (a.Y() > c.Y() && c.Y() >= b.Y()) {
				Point temp = a.minus(c);
				temp = new Point((int) Math.floor(temp.X() / 2 + 0.5),
						(int) Math.floor(temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: c ... a ... b
			else if (c.Y() > a.Y() && a.Y() >= b.Y()) {
				Point temp = c.minus(a);
				temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5),
						(int) Math.floor(-temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: b ... c ... a
			else if (c.Y() > a.Y() && c.Y() <= b.Y()) {
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
	//
	// Het resultaat wordt bekomen door de vector ac te verschuiven zodanig dat
	// het punt b op die vector ligt én die vector door het punt b in twee
	// gelijke delen verdeeld wordt. Het nieuwe tweede uiteinde van die
	// vector is dan het gevraagde punt.
	private final Point tangent2C1(Point a, Point b, Point c) {
		// horizontale volgorde: a ... b ... c
		if (a.X() < b.X() && c.X() > b.X()) {
			Point temp = c.minus(a);
			temp = new Point((int) Math.floor(temp.X() / 2 + 0.5), (int) Math
					.floor(temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		}
		// horizontale volgorde: c ... b ... a
		else if (a.X() > b.X() && c.X() < b.X()) {
			Point temp = a.minus(c);
			temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5), (int) Math
					.floor(-temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		}
		// verticale volgorde: a ... b ... c
		else if (a.Y() > b.Y() && c.Y() < b.Y()) {
			Point temp = a.minus(c);
			temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5), (int) Math
					.floor(-temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		}
		// verticale volgorde: c ... b ... a
		else if (a.Y() < b.Y() && c.Y() > b.Y()) {
			Point temp = c.minus(a);
			temp = new Point((int) Math.floor(temp.X() / 2 + 0.5), (int) Math
					.floor(temp.Y() / 2 + 0.5)).plus(b);
			return temp;
		}
		// Beide tangentpunten liggen in eenzelfde kwadrant van het vlak t.o.v.
		// het punt b --> nu worden de eindpunten van de vector ac verplaatst,
		// overeenkomstig met de oorspronkelijke afstand tot b.
		//
		// Bvb.: a en c liggen onder b, a ligt verder van b dan c
		// --> het eerste eindpunt van de vector ac zal ook onder b
		// komen te liggen, het andere eindpunt zal boven b liggen.
		else {
			// verticale volgorde: b ... a ... c
			if (a.Y() > c.Y() && a.Y() <= b.Y()) {
				Point temp = a.minus(c);
				temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5),
						(int) Math.floor(-temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: a ... c ... b
			else if (a.Y() > c.Y() && c.Y() >= b.Y()) {
				Point temp = a.minus(c);
				temp = new Point((int) Math.floor(-temp.X() / 2 + 0.5),
						(int) Math.floor(-temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: c ... a ... b
			else if (c.Y() > a.Y() && a.Y() >= b.Y()) {
				Point temp = c.minus(a);
				temp = new Point((int) Math.floor(temp.X() / 2 + 0.5),
						(int) Math.floor(temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			}
			// verticale volgorde: b ... c ... a
			else if (c.Y() > a.Y() && c.Y() <= b.Y()) {
				Point temp = c.minus(a);
				temp = new Point((int) Math.floor(temp.X() / 2 + 0.5),
						(int) Math.floor(temp.Y() / 2 + 0.5)).plus(b);
				return temp;
			}
		}
		return c;
	}
}
