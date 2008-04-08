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
		c.clearOutput();

		for (int i = 0; i < (c.getNbInputPoints() - 1); ++i) {
			// eerste controlepunt ligt links van het tweede
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
			}
			// eerste controlepunt ligt rechts van het tweede, dat niet op de
			// Y-as ligt
			else if (input.get(i + 1).X() > 0) {
				for (int x = input.get(i).X(); x >= input.get(i + 1).X(); --x) {
					output
							.add(new Point(x,
									(int) (input.get(i).Y() + (x - input.get(i)
											.X())
											* (double) ((double) input.get(
													i + 1).Y() / (double) input
													.get(i + 1).X()))));
				}
			}
			// ligt wél op de Y-as
			else {
				for (int x = input.get(i).X(); x > 0; --x)
					output.add(new Point(x, (int) (input.get(i + 1).Y() + input
							.get(i).Y()
							* (double) (x / (double) input.get(i).X()))));

				output.add(new Point(0, input.get(i + 1).Y()));
			}

			// noch links, noch rechts
			if (input.get(i + 1).X() == input.get(i).X()
					&& input.get(i).Y() != input.get(i + 1).Y()) {
				int x = input.get(i + 1).X();

				if (input.get(i).Y() < input.get(i + 1).Y())
					for (int y = input.get(i).Y(); y <= input.get(i + 1).Y(); ++y)
						output.add(new Point(x, y));
				else
					for (int y = input.get(i).Y(); y >= input.get(i + 1).Y(); --y)
						output.add(new Point(x, y));
			}
		}
	}
}
