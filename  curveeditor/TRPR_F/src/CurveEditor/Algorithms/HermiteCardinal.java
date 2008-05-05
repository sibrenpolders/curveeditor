package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class HermiteCardinal extends Hermite {

	public HermiteCardinal(char type, short degree) {
		super(type, degree);
	}

	public HermiteCardinal(short degree) {
		this('A', degree);
	}

	public HermiteCardinal() {
		this('A', (short) 0);
	}

	protected void cardinal(Curve cv, float c) {
		Vector<Point> vip = cv.getInput();
		Vector<Point> vop = cv.getOutput();
		float t;

		int size = vip.size();

		// Er zijn minstens 4 punten nodig om deze bewerking uit te kunnen
		// voeren
		// Pi-1, Pi, Pi+1, Pi+2
		// hierbij zijn Pi en Pi+1 de punten waartussen we interpolleren
		// de andere punten worden berekend om de tangens te berekenen
		// volgens volgende formule Ti = c*(Pi+1 - Pi-1)
		if (size - 4 >= 0) {
			Point p1 = vip.get(size - 4);
			Point p2 = vip.get(size - 3);
			Point p3 = vip.get(size - 2);
			Point p4 = vip.get(size - 1);

			int x, y;
			// we interpolleren tussen p2 en p3 eerst t2 vinden
			x = (int) (c * (p3.X() - p1.X()));
			y = (int) (c * (p3.Y() - p1.Y()));
			Point t2 = new Point(x, y);

			// nu t3 vinden
			x = (int) (c * (p4.X() - p2.X()));
			y = (int) (c * (p4.Y() - p2.Y()));
			Point t3 = new Point(x, y);

			for (int j = 0; j < steps; ++j) {
				t = (float) (j / (steps - 1.0));
				vop.add(hermite(p2, t2, p3, t3, t));
			}
		}
	}

	public void calculate(Curve cv) {
		float c;
		// zoek een random d tussen 0 en 1 om als tangens te gebruiken
		do {
			c = (float) Math.random();
		} while (c == 0.0);

		cardinal(cv, c);
	}

	public void calculateComplete(Curve cv) {
		float c;
		// zoek een random d tussen 0 en 1 om als tangens te gebruiken
		do {
			c = (float) Math.random();
		} while (c == 0.0);

		cardinalComplete(cv, c);
	}

	protected void cardinalComplete(Curve cv, float c) {
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
		for (int i = 0; i < size - 3; ++i) {
			// enkel de interpolatie tussen het laatste en het voorlaatste punt
			// moet berekend worden
			Point p1 = vip.get(i);
			Point p2 = vip.get(i + 1);
			Point p3 = vip.get(i + 2);
			Point p4 = vip.get(i + 3);
			int x, y;
			// we interpolleren tussen p2 en p3 eerst t2 vinden
			x = (int) (c * (p3.X() - p1.X()));
			y = (int) (c * (p3.Y() - p1.Y()));
			Point t2 = new Point(x, y);

			// nu t3 vinden
			x = (int) (c * (p4.X() - p2.X()));
			y = (int) (c * (p4.Y() - p2.Y()));
			Point t3 = new Point(x, y);

			for (int j = 0; j < steps; ++j) {
				t = (float) (j / (steps - 1.0));
				vop.add(hermite(p2, t2, p3, t3, t));
			}
		}
	}
}
