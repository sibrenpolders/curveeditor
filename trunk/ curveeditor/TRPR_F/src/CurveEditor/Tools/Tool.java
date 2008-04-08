package CurveEditor.Tools;

import java.util.Vector;

import CurveEditor.Algorithms.Algorithm;
import CurveEditor.Curves.Curve;

public abstract class Tool {

	protected final char type;

	public Tool(char type) {
		this.type = type;
	}

	public char getType() {
		return type;
	}

	public void run(Vector<Algorithm> algorithms, Vector<Curve> curves) {

	}
}
