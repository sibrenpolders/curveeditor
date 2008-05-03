package CurveEditor.Algorithms;

import CurveEditor.Curves.Curve;

public final class HermiteCatmullRom extends HermiteCardinal {
	private final double d = 0.5;

	public HermiteCatmullRom(char type, short degree) {
		super(type, degree);
	}

	public HermiteCatmullRom(short degree) {
		this('R', degree);
	}

	public HermiteCatmullRom() {
		this('R', (short) 0);
	}

	public void calculate(Curve cv) {
		cardinal(cv, (float) d);
	}

	public void calculateComplete(Curve cv) {
		cardinalComplet(cv, (float) d);
	}
}
