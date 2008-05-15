package CurveEditor.Curves;

import java.util.Vector;
import CurveEditor.Exceptions.*;

/*
 * Deze klasse omvat de voorstelling van een curve.
 * Een curve heeft een type en een orde, en aan de hand van die eigenschappen kan er een Algorithm aan gelinkt worden. 
 * Dit gebeurt niet hier, maar in Editor, omdat moesten we voor elke Curve ook een Algorithm-object bijhouden, we wel 
 * van geheugenverspilling kunnen spreken. Algorithms zijn immers Curve-onafhankelijk en doen niet meer dan m.b.v. een Vector
 * van Points een andere Vector van Points vullen. Daarom: Curve en Algorithm onafhankelijk, maar wel met gemeenschappelijke
 * ID's om ze aan elkaar te kunnen linken.
 * Zoals gezegd: een Curve bestaat naast de kenmerkende eigenschappen, ook nog uit twee Vectoren, een voor de input- en een
 * voor de outpunten.
 */
public final class Curve {
	private static short SEARCH_RANGE = 3;

	// De Vector van inputpunten, volgorde is belangrijk !!!
	protected Vector<Point> input;

	// De Vector van outputpunten, die kan berekend worden uit de Vector van
	// inputpunten.
	protected Vector<Point> output;

	// Identifiers
	protected char type;
	protected short degree;

	// De default constructor is bvb. nodig voor File I/O, alwaar we stuk voor
	// stuk de curve kunnen herconstrueren: eerst Curve aanmaken, dan ID's
	// zetten en dan de input punten inladen.
	public Curve() {
		this.type = 0;
		this.degree = 0;

		input = new Vector<Point>();
		output = new Vector<Point>();
	}

	public Curve(char type, short degree) {
		this.type = type;
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

	// Verwijder al de berekende outputpunten.
	public void clearOutput() {
		this.output.clear();
	}

	// Verwijder al de inputpunten.
	public void clearInput() {
		this.input.clear();
	}

	public int getNbInputPoints() {
		return input.size();
	}

	public int getNbOutputPoints() {
		return output.size();
	}

	// Voeg een outputpunt toe aan de Curve.
	public void addOutput(Point o) {
		this.output.add(o);
	}

	// Verwijder een inputpunt.
	public void removeInput(Point o) {
		if (o != null) {
			// Voor elk inputpunt: controleer of het op dezelfde plaats ligt als
			// het
			// gegeven punt of niet.
			for (int i = 0; i < getNbInputPoints(); ++i)
				if (input.get(i).X() == o.X() && input.get(i).Y() == o.Y())
					input.remove(i--);
		}
	}

	// i is de index in de Vector.
	public void removeInput(int i) throws InvalidArgumentException {
		if (i < 0 || i > input.size() - 1)
			throw new InvalidArgumentException(
					"Curve.java - removeInput(int): Invalid Argument.");
		input.remove(i);
	}

	public void addInput(Point o) throws InvalidArgumentException {
		if (o == null)
			throw new InvalidArgumentException(
					"Curve.java - addInput(Point): Invalid Argument.");

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

	// Lineair zoeken of een punt als inputpunt aanwezig is -->
	// bij normaal gebruik: niet al teveel inputpunten, dus niet
	// zó van belang.
	public Point containsInputPoint(Point p) {
		if (p != null)
			for (int i = 0; i < input.size(); ++i)
				if (Math.abs(input.elementAt(i).X() - p.X()) <= SEARCH_RANGE
						&& Math.abs(input.elementAt(i).Y() - p.Y()) <= SEARCH_RANGE)
					return input.elementAt(i);
		return null;
	}

	// Geeft de index terug als het punt een controlepunt is.
	// Zelfde algoritme als containsInputPoint, maar een andere returnwaarde.
	public int containsInputPointi(Point p) {
		if (p != null)
			for (int i = 0; i < input.size(); ++i)
				if (Math.abs(input.elementAt(i).X() - p.X()) <= SEARCH_RANGE
						&& Math.abs(input.elementAt(i).Y() - p.Y()) <= SEARCH_RANGE)
					return i;
		return -1;
	}

	// Verschuiving van de Curve over een afstand x,y
	// --> alle controlepunten verschuiven (en elders herberekenen).
	public void translate(int x, int y) {
		output.clear();
		for (int i = 0; i < input.size(); ++i) {
			input.elementAt(i).increaseX(x);
			input.elementAt(i).increaseY(y);
		}
	}

	// De aangemaakte curve krijgt de eigenschappen van de eerste curve en AL de
	// inputpunten van de twee curves. Het eerste inputpunt van c2 volgt dus op
	// het laatste inputpunt van c1. De inputpunten behouden hun oorspronkelijke
	// positie.
	// De Curve wordt hier niet herberekend, uiteraard.
	public static Curve connectNoExtraPoint(Curve c1, Curve c2)
			throws InvalidArgumentException {
		if (c1 == null || c2 == null)
			throw new InvalidArgumentException(
					"Curve.java - connectNoExtraPoint(Curve, Curve): Invalid Argument.");

		Curve result = new Curve(c1.getType(), c1.getDegree());
		result.input.addAll(c1.input);
		result.input.addAll(c2.input);

		return result;
	}

	// De aangemaakte curve krijgt de eigenschappen van de eerste curve en de
	// inputpunten van de twee curves. Het eerste inputpunt van c2 valt samen
	// met het laatste inputpunt van c1. De overige inputpunten van c2 worden
	// over een afstand verschoven zodanig dat díe twee inputpunten samenvallen.
	public static Curve connectC0(Curve c1, Curve c2)
			throws InvalidArgumentException {
		if (c1 == null || c2 == null)
			throw new InvalidArgumentException(
					"Curve.java - connectC0(Curve, Curve): Invalid Argument.");

		Curve result = new Curve(c1.getType(), c1.getDegree());
		result.getInput().addAll(c1.getInput());

		if (c2.input.size() > 0) {
			// laatste input van c1
			Point prevCtrl = result.input.lastElement();
			Point first = c2.input.elementAt(0); // eerste input van c2

			// Verschuivingswaarden berekenen.
			int diffX = prevCtrl.X() - first.X();
			int diffY = prevCtrl.Y() - first.Y();

			// Alle inputpunten van c2 verschuiven en aan result toevoegen.
			for (int i = 1; i < c2.getNbInputPoints(); ++i) {
				result.addInput(new Point(c2.input.elementAt(i).X() + diffX,
						c2.input.elementAt(i).Y() + diffY));
			}
		}

		return result;
	}
}
