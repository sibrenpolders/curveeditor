package CurveEditor.Curves;

import java.util.Vector;

//interpolatiepunten worden berekend vanuit Editor
public class Curve {
	// de controlepunten, volgorde is belangrijk !!!
	protected Vector<Point> input;

	// de berekende tussenpunten
	protected Vector<Point> output;

	// identifier
	protected char type;

	protected short degree;

	public Curve(char Type, short degree) {
		type = Type;
		this.degree = degree;

		input = new Vector<Point>();
		output = new Vector<Point>();
	}

	public Vector<Point> getInput() {
		return input;
	}

	public Vector<Point> getOutput() {
		return output;
	}

	// verwijder de interpolatiepunten
	public void clearOutput() {
		this.output.clear();
	}

	public void clearInput() {
		this.input.clear();
	}

	public int getNbInputPoints() {
		return input.size();
	}

	public void addOutput(Point o) {
		this.output.add(o);
	}

	public void removeInput(Point o) {
		for (int i = 0; i < getNbInputPoints(); ++i)
			if (input.get(i).X() == o.X() && input.get(i).Y() == o.Y())
				input.remove(i--);
	}

	public void addInput(Point o) {
		this.input.add(o);
	}

	public char getType() {
		return type;
	}

	public short getDegree() {
		return degree;
	}

	public void setDegree(short d) {
		degree = d;
	}

	public void setType(char t) {
		this.type = t;
	}

	public String toString() {
		return null;
	}
}
