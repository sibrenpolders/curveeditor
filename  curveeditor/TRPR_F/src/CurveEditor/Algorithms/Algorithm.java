package CurveEditor.Algorithms;

import CurveEditor.Curves.Curve;

public abstract class Algorithm {
	protected final char type;
	protected final short degree;

	public Algorithm(char type, short degree) {
		this.type = type;
		this.degree = degree;
	}

	public char getType() {
		return type;
	}

	public short getDegree() {
		return degree;
	}

	public String toString() {
		return null;
	}

	public abstract void calculateCurve(Curve c);
}
