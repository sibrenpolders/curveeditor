package CurveEditor.Algorithms;

import java.util.LinkedList;
import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;
import CurveEditor.Exceptions.*;

/*
 * Deze klasse is de klasse waar ALLE voorziene interpolatiealgoritmen van afgeleid moeten
 * worden. Deze bevat de kenmerkende eigenschappen van een algoritme, zijnde het type en de
 * orde.
 * De belangrijkste/publieke methodes zijn de calculate-methodes, die a.h.v. een gegeven 
 * vector inputpunten een gegeven vector voor outputpunten vult.
 */
public abstract class Algorithm {
	// Het type, dat ééns gezet in principe niet meer veranderd dient te worden.
	protected final char type;
	// De orde, dat ééns gezet in principe niet meer veranderd dient te worden.
	protected final short degree;

	// De constructor; een default constructor is niet voorzien, omdat type én
	// degree gezet moéten worden.
	public Algorithm(char type, short degree) {
		this.type = type;
		this.degree = degree;
	}

	public final char getType() {
		return type;
	}

	public final short getDegree() {
		return degree;
	}

	// Het algoritme omzetten naar een String --> nodig voor oa. hashCode().
	public final String toString() {
		return "Type: " + type + ", degree: " + degree;
	}

	// De teruggegeven waarde bepaalt de bucket waarin het algoritme in een
	// HashMap terecht zal komen.
	public final int hashCode() {
		return toString().hashCode();
	}

	// Gegeven een Curve c, vul zijn Vector van outputpunten zodanig dat deze
	// de geïnterpoleerde punten van de inputpunten voorstellen.
	public void calculate(Curve c) throws InvalidArgumentException {
		if (c == null) // ongeldig argument --> exception gooien
			throw new InvalidArgumentException(
					"Algorithm.java - calculate(Curve): Invalid Argument.");
		else
			calculate(c.getInput(), c.getOutput());
	}

	// Gegeven een Curve c, hervul zijn Vector van outputpunten vollédig met de
	// geïnterpoleerde punten van de inputpunten.
	// 't Verschil met calculate ligt 'm in het feit dat die laatste ook enkel
	// de Vector van outpunten kan bijvullen, i.p.v. volledig te (her-)vullen.
	public abstract void calculateComplete(Curve c)
			throws InvalidArgumentException;

	// Gegeven een Vector van inputpunten, hervul de meegegeven Vector van
	// outpunten m.b.v. het geïmplementeerde interpolatiealgoritme.
	public abstract void calculate(Vector<Point> input, LinkedList<Point> output)
			throws InvalidArgumentException;
}
