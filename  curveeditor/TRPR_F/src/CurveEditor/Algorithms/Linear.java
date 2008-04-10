package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Linear extends Algorithm {

	public Linear(short degree) {
		super('L', degree);
	}

	private int findYForX(int x, int x0, int y0, int x1, int y1) {
		return (int) ((double) y0 + (x - (double) x0)
				* ((y1 - y0) / ((double) x1 - x0)));
	}

	private int findXForY(int y, int x0, int y0, int x1, int y1) {
		return (int) ((double) x0 + (y - (double) y0)
				* ((x1 - x0) / ((double) y1 - y0)));
	}

	public void calculateCurve(Curve c) {

		Vector<Point> input = c.getInput();
		Vector<Point> output = c.getOutput();
		output.clear();

		for (int i = 0; i < (c.getNbInputPoints() - 1); ++i) {
			int diff1 = (input.get(i).X() < input.get(i + 1).X()) ? input.get(
					i + 1).X()
					- input.get(i).X() : input.get(i).X()
					- input.get(i + 1).X();
			int diff2 = (input.get(i).Y() < input.get(i + 1).Y()) ? input.get(
					i + 1).Y()
					- input.get(i).Y() : input.get(i).Y()
					- input.get(i + 1).Y();

			if (diff1 > diff2) {
				// eerste controlepunt ligt links van het tweede
				if (input.get(i).X() < input.get(i + 1).X()) {
					for (int x = input.get(i).X(); x <= input.get(i + 1).X(); ++x) {
						output.add(new Point(x, findYForX(x, input.get(i).X(),
								input.get(i).Y(), input.get(i + 1).X(), input
										.get(i + 1).Y())));
					}
				}
				// eerste controlepunt ligt rechts van het tweede
				else {
					for (int x = input.get(i).X(); x >= input.get(i + 1).X(); --x) {
						output.add(new Point(x, findYForX(x, input.get(i).X(),
								input.get(i).Y(), input.get(i + 1).X(), input
										.get(i + 1).Y())));
					}
				}
			} else {
				if (input.get(i).Y() < input.get(i + 1).Y()) {
					for (int y = input.get(i).Y(); y <= input.get(i + 1).Y(); ++y) {
						output.add(new Point(findXForY(y, input.get(i).X(),
								input.get(i).Y(), input.get(i + 1).X(), input
										.get(i + 1).Y()), y));
					}
				} else {
					for (int y = input.get(i).Y(); y >= input.get(i + 1).Y(); --y) {
						output.add(new Point(findXForY(y, input.get(i).X(),
								input.get(i).Y(), input.get(i + 1).X(), input
										.get(i + 1).Y()), y));
					}
				}
			}
		}
		
		new BezierSmoothing3(150).smoothCurve(c, 0.5, BezierSmoothing3.MODE.INPUT);
	}
}