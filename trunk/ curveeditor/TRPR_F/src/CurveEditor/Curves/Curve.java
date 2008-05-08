package CurveEditor.Curves;

import java.util.Vector;
import CurveEditor.Algorithms.Tangent;

//interpolatiepunten worden berekend vanuit Editor
public final class Curve {
	private static short SEARCH_RANGE = 3;

	// de controlepunten, volgorde is belangrijk !!!
	protected Vector<Point> input;

	// de berekende tussenpunten
	protected Vector<Point> output;

	// identifier
	protected char type;
	protected short degree;

	public Curve() {
		type = 0;
		degree = 0;
		input = new Vector<Point>();
		output = new Vector<Point>();
	}

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

	public int getNbOutputPoints() {
		return output.size();
	}

	public void addOutput(Point o) {
		this.output.add(o);
	}

	public void removeInput(Point o) {
		for (int i = 0; i < getNbInputPoints(); ++i)
			if (input.get(i).X() == o.X() && input.get(i).Y() == o.Y())
				input.remove(i--);
	}

	// i is de index in de vector
	public void removeInput(int i) {
		input.remove(i);
	}

	public void addInput(Point o) {
		this.input.add(new Point(o.X(), o.Y()));
	}

	public char getType() {
		return type;
	}

	public String getTypeAsString() {
		return Character.toString(type);
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

	// lineair zoeken --> normaal gebruik: niet al teveel inputpunten, dus niet
	// zó van belang
	public Point containsInputPoint(Point p) {
		for (int i = 0; i < input.size(); ++i)
			if (Math.abs(input.elementAt(i).X() - p.X()) <= SEARCH_RANGE
					&& Math.abs(input.elementAt(i).Y() - p.Y()) <= SEARCH_RANGE)
				return input.elementAt(i);
		return null;
	}

	// geeft de index terug als het punt een controlepunt is
	public int containsInputPointi(Point p) {
		for (int i = 0; i < input.size(); ++i)
			if (Math.abs(input.elementAt(i).X() - p.X()) <= SEARCH_RANGE
					&& Math.abs(input.elementAt(i).Y() - p.Y()) <= SEARCH_RANGE)
				return i;
		return -1;
	}

	// verschuiving over een afstand x,y --> alle controlepunten verschuiven
	public void translate(int x, int y) {
		output.clear();
		for (int i = 0; i < input.size(); ++i) {
			input.elementAt(i).increaseX(x);
			input.elementAt(i).increaseY(y);
		}
	}

	// de aangemaakte curve krijgt de eigenschappen van de eerste curve en AL de
	// inputpunten van de twee curves; zeer basic en dus niet echt goed
	public static Curve connectNoExtraPoint(Curve c1, Curve c2) {
		Curve result = new Curve(c1.getType(), c1.getDegree());
		result.input.addAll(c1.input);
		result.input.addAll(c2.input);

		return result;
	}

	// de aangemaakte curve krijgt de eigenschappen van de eerste curve en de
	// inputpunten van de twee curves, laatste van c1 en eerste van c2 vallen
	// samen, c2 wordt richting c1 verschoven
	public static Curve connectC0(Curve c1, Curve c2) {
		Curve result = new Curve(c1.getType(), c1.getDegree());

		for (int i = 0; i < c1.input.size(); ++i)
			result.addInput(c1.input.elementAt(i));

		if (c2.input.size() > 0) {
			Point prevCtrl = result.input.lastElement();
			Point first = c2.input.elementAt(0);
			int diffX = prevCtrl.X() - first.X();
			int diffY = prevCtrl.Y() - first.Y();

			for (int i = 1; i < c2.getNbInputPoints(); ++i) {
				result.addInput(new Point(c2.input.elementAt(i).X() + diffX,
						c2.input.elementAt(i).Y() + diffY));
			}
		}

		return result;
	}
}
