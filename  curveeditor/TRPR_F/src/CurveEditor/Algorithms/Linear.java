package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Linear extends Algorithm {

	public Linear(short degree) {
		super('L', degree);
	}

	private int findYForX(int x, int x0, int y0, int x1, int y1) {
		return (int) (y0 + (x - x0) * ((y1 - y0) / ((double) x1 - x0)));
	}

	public void calculateCurve(Curve c) {

		Vector<Point> input = c.getInput();
		Vector<Point> output = c.getOutput();
		output.clear();

		for (int i = 0; i < (c.getNbInputPoints() - 1); ++i) {
			// eerste controlepunt ligt links van het tweede
			if (input.get(i).X() < input.get(i + 1).X()) {
				for (int x = input.get(i).X(); x <= input.get(i + 1).X(); ++x) {
					output.add(new Point(x, findYForX(x, input.get(i).X(),
							input.get(i).Y(), input.get(i + 1).X(), input.get(
									i + 1).Y())));
				}
			}
			// eerste controlepunt ligt rechts van het tweede
			else if (input.get(i).X() > input.get(i + 1).X()) {
				for (int x = input.get(i).X(); x >= input.get(i + 1).X(); --x) {
					output.add(new Point(x, findYForX(x, input.get(i).X(),
							input.get(i).Y(), input.get(i + 1).X(), input.get(
									i + 1).Y())));
				}
			}
			// controlepunten liggen op een verticale
			else {
				if (input.get(i).Y() < input.get(i + 1).Y()) {
					for (int y = input.get(i).Y(); y <= input.get(i + 1).Y(); ++y) {
						output.add(new Point(input.get(i).X(), y));
					}
				} else {
					for (int y = input.get(i).Y(); y >= input.get(i + 1).Y(); --y) {
						output.add(new Point(input.get(i).X(), y));
					}
				}
			}
		}
	}
}