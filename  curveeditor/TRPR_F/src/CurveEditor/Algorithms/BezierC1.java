package CurveEditor.Algorithms;

import java.util.Vector;
import CurveEditor.Curves.Point;

public class BezierC1 extends Bezier {

	public BezierC1(char type, short degree) {
		super(type, degree);
	}

	public BezierC1(short degree) {
		this('C', degree);
	}

	public BezierC1() {
		this('C', (short) 3);
	}

	public final void calculate(Vector<Point> input, Vector<Point> output) {
		for (int i = 0; i <= input.size() - 4; i = i + 3) {
			// aantal stappen bepalen a.h.v. de afstand tussen de eindpunten
			int steps = 2 * Point.distance(input.elementAt(i), input
					.elementAt(i + 3));

			Point first, second;
			Tangent c = new Tangent();

			if (i > 0)
				first = c.calculate(Tangent.CONTINUITY.C1, (short) 2, input
						.elementAt(i - 1), input.elementAt(i), input
						.elementAt(i + 1));
			else
				first = input.elementAt(i + 1);

			if (i <= input.size() - 7)
				second = c.calculate(Tangent.CONTINUITY.C1, (short) 1, input
						.elementAt(i + 2), input.elementAt(i + 3), input
						.elementAt(i + 4));
			else
				second = input.elementAt(i + 2);

			interpolate(input.elementAt(i), first, second, input
					.elementAt(i + 3), steps, output);
		}
	}
}
