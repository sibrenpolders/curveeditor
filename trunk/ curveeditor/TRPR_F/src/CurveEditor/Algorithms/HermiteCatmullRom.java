package CurveEditor.Algorithms;

import CurveEditor.Curves.Curve;

public final class HermiteCatmullRom extends HermiteCardinal {
	private final double d = 0.5;

	public HermiteCatmullRom(char type, short degree) {
		super(type, degree);
		// TODO Auto-generated constructor stub
	}

	public void calculate(Curve cv) {
		cardinal(cv, (float) d);
	}

	public void calculateComplete(Curve cv) {
		cardinalComplet(cv, (float) d);
	}
}
