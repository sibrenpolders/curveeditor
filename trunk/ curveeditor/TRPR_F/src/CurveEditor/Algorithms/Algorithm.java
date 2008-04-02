package CurveEditor.Algorithms;

import CurveEditor.Curves.Curve;

public abstract class Algorithm {
	protected final char type;

	public Algorithm(char type) {
		this.type = type;
	}

	public char getType() {
		return type;
	}

	public String toString() {
		return null;
	}

	public abstract void calculateCurve(Curve c);
}
