package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Bezier2 extends Algorithm {

	public Bezier2(short degree) {
		super('B', (short) 2);
	}

	public Bezier2() {
		super('B', (short) 2);
	}

	public void calculate(Curve c) {
		calculate(c.getInput(), c.getOutput());
	}

	public void calculate(Vector<Point> input, Vector<Point> output) {
		for (int i = 0; i <= input.size() - 3; i = i + 2) {
			int steps = (int)Math.floor(0.1 * Point.distance(input.elementAt(i), input
					.elementAt(i + 2)));
			interpolate(input.elementAt(i), input.elementAt(i + 1), input
					.elementAt(i + 2), steps, output);
		}

		// de output-vector lineair interpoleren
		Linear smoothing = new Linear();
		Vector<Point> temp = new Vector<Point>();
		for (int i = 0; i < output.size() - 1; ++i)
			smoothing.interpolate(output.elementAt(i), output.elementAt(i + 1),
					temp);

		output.clear();
		output.addAll(temp);
	}

	private void interpolate(Point a, Point b, Point c, int steps,
			Vector<Point> v) {
		double d = 1.0 / steps;
		double t = d;

		v.add(new Point(a.X(), a.Y()));

		for (int i = 0; i < steps; ++i) {
			double tmin = 1.0 - t;
			double tmin2 = tmin * tmin;
			double t2 = t * t;
			int x = (int) Math.floor(tmin2 * 1.0 * a.X() + 2.0 * tmin * t * 1.0
					* b.X() + t2 * 1.0 * c.X() + 0.5);
			int y = (int) Math.floor(tmin2 * 1.0 * a.Y() + 2.0 * tmin * t * 1.0
					* b.Y() + t2 * 1.0 * c.Y() + 0.5);

			v.add(new Point(x, y));
			t += d;
		}
	}

	public void calculateComplete(Curve c) {
		calculate(c);
	}
}