package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Linear extends Algorithm {

	public Linear() {
		super('L');
	}

	public void calculateCurve(Curve c) {

		Vector<Point> input = c.getInput();
		Vector<Point> output = c.getOutput();

		for (int i = 0; i < (c.getNbInputPoints() - 1); ++i) {
			if (input.get(i).X() < input.get(i + 1).X()) {
				for (int x = input.get(i).X(); x <= input.get(i + 1).X(); ++x) {
					output
							.add(new Point(x,
									(int) (input.get(i).Y() + (x - input.get(i)
											.X())
											* (double) ((double) input.get(
													i + 1).Y() / (double) input
													.get(i + 1).X()))));
				}
			} else if (input.get(i + 1).X() > 0) {
				for (int x = input.get(i).X(); x >= input.get(i + 1).X(); --x) {
					output
							.add(new Point(x,
									(int) (input.get(i).Y() + (x - input.get(i)
											.X())
											* (double) ((double) input.get(
													i + 1).Y() / (double) input
													.get(i + 1).X()))));
				}
			} else {
				for (int x = input.get(i).X(); x >= 0; --x)
					output.add(new Point(x, (int) (input.get(i + 1).Y() + input
							.get(i).Y()
							* (double) (x / (double) input.get(i).X()))));
			}
		}
	}
}
