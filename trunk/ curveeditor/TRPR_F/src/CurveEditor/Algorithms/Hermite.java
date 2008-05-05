package CurveEditor.Algorithms;

import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Hermite extends Algorithm {
	protected int steps;

	public Hermite(char type, short degree) {
		super(type, degree);
		steps = 1000;
	}

	public Hermite(short degree) {
		super('H', degree);
		steps = 1000;
	}

	public Hermite() {
		super('H', (short) 0);
		steps = 1000;
	}

	protected Point hermite(Point p1, Point r1, Point p2, Point r2, float t) {
		float t2 = (float) Math.pow(t, 2);
		float t3 = (float) Math.pow(t, 3);

		float h00 = 2 * t3 - 3 * t2 + 1; // basis function 1
		float h10 = t3 - 2 * t2 + t; // basis function 3
		float h01 = -2 * t3 + 3 * t2; // basis function 2
		float h11 = t3 - t2; // basis function 4

		float y = h00 * p1.Y() + h10 * r1.Y() + h01 * p2.Y() + h11 * r2.Y(); // nieuwe
		// y-coordinaat
		float x = h00 * p1.X() + h10 * r1.X() + h01 * p2.X() + h11 * r2.X(); // nieuwe
		// x-coordinaat

		return new Point((int) Math.floor(x + .5), (int) Math.floor(y + .5));
	}

	public void calculate(Curve cv) {
		calculate(cv.getInput(), cv.getOutput());
	}

	public void calculate(Vector<Point> vip, Vector<Point> vop) {
		float t;

		// for ( int i = 0; i <= vip.size() - 4; i += 2 ) {
		int size = vip.size();
		// Er zijn minstens 4 punten nodige om deze hermiet berekening te kunnen
		// uitvoeren
		// nl. Pi Ri Pj Rj waarbij Pi, Pj de punten zijn waartussen we
		// interpolleren
		// en Ri, Rj de tangens zijn van de kromme in respectievelijk Pi, Pj
		if (size - 4 >= 0) {
			// enkel de interpolatie tussen het laatste en het voorlaatste punt
			// moet berekend worden
			Point a = vip.get(size - 4);
			Point b = vip.get(size - 3);
			Point c = vip.get(size - 2);
			Point d = vip.get(size - 1);
			Point r1 = new Point(b.X() - a.X(), b.Y() - a.Y());
			Point r2 = new Point(d.X() - c.X(), d.Y() - c.Y());
			for (int j = 0; j < steps; ++j) {
				t = (float) (j / (steps - 1.0));
				vop.add(hermite(a, r1, c, r2, t));
			}
		}
	}

	public void calculateComplete(Curve cv) {
		Vector<Point> vip = cv.getInput();
		Vector<Point> vop = cv.getOutput();
		cv.clearOutput();

		float t;

		// for ( int i = 0; i <= vip.size() - 4; i += 2 ) {
		int size = vip.size();
		// Er zijn minstens 4 punten nodige om deze hermiet berekening te kunnen
		// uitvoeren
		// nl. Pi Ri Pj Rj waarbij Pi, Pj de punten zijn waartussen we
		// interpolleren
		// en Ri, Rj de tangens zijn van de kromme in respectievelijk Pi, Pj
		for (int i = 0; i < size - 3; i += 2) {
			// enkel de interpolatie tussen het laatste en het voorlaatste punt
			// moet berekend worden
			Point a = vip.get(i);
			Point b = vip.get(i + 1);
			Point c = vip.get(i + 2);
			Point d = vip.get(i + 3);
			Point r1 = new Point(b.X() - a.X(), b.Y() - a.Y());
			Point r2 = new Point(d.X() - c.X(), d.Y() - c.Y());
			for (int j = 0; j < steps; ++j) {
				t = (float) (j / (steps - 1.0));
				vop.add(hermite(a, r1, c, r2, t));
			}
		}
	}
}
