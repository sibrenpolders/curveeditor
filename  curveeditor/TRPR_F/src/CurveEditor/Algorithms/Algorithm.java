package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

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
		return "Type: " + type + ", degree: " + degree;
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public void calculate(Curve c) {
		calculate(c.getInput(), c.getOutput());
	}

	public abstract void calculate(Vector<Point> input, Vector<Point> output);
}
