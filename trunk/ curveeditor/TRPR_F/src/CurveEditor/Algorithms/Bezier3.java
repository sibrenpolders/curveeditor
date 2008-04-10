package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Bezier3 extends Algorithm {

	private int steps;

	// orde = 3 --> per 4 controlepunten de dingen berekenen dus
	public Bezier3(short degree) {
		super('B', degree);
		steps = 1000;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	private Point linIntPol(Point a, Point b, float t) {
		return new Point((int) Math.floor(a.X() + (b.X() - a.X()) * t + 0.5),
				(int) Math.floor(a.Y() + (b.Y() - a.Y()) * t + 0.5));
	}

	private Point bezier(Point a, Point b, Point c, Point d, float t) {
		Point ab = linIntPol(a, b, t);
		Point bc = linIntPol(b, c, t);
		Point cd = linIntPol(c, d, t);
		Point abbc = linIntPol(ab, bc, t);
		Point bccd = linIntPol(bc, cd, t);

		return linIntPol(abbc, bccd, t);
	}

	public void calculateCurve(Curve c) {
		Vector<Point> input = c.getInput();
		Vector<Point> output = c.getOutput();
		output.clear();

		for (int i = 0; i < input.size() - 3; i += 3) {
			for (int j = 0; j < steps; ++j) {
				float t = (float) (j / (steps - 1.0));
				output.add(bezier(input.elementAt(i), input.elementAt(i + 1),
						input.elementAt(i + 2), input.elementAt(i + 3), t));
			}
		}
	}
}