package CurveEditor.Algorithms;

import java.util.Vector;
import CurveEditor.Curves.Point;
import CurveEditor.Exceptions.InvalidArgumentException;

/*
 * Deze van Bezier afgeleide klasse implementeert het Bezier-interpolatiealgoritme,
 * maar eveneens G1-continuïteit.
 * Per vier punten wordt een curve berekend als volgt: de curve start in het eerste punt 
 * in de richting van het tweede, om dan in het vierde punt te eindigen in de richting 
 * van het derde. De G1-continuïteit wordt berekend m.b.v. methodes uit de klasse Tangent.
 * Meer info over continuïteit is dan ook daar terug te vinden.
 */
public class BezierG1 extends Bezier {

	// Deze constructor is voorzien opdat we subklassen van BezierG1 kunnen
	// implementeren, die dezelfde orde zouden hebben.
	public BezierG1(char type, short degree) {
		super(type, degree);
	}

	public BezierG1(short degree) {
		this('G', degree);
	}

	public BezierG1() {
		this('G', (short) 3);
	}

	// Gegeven een Vector van inputpunten, hervul de meegegeven Vector van
	// outpunten m.b.v. het geïmplementeerde interpolatiealgoritme.
	public void calculate(Vector<Point> input, Vector<Point> output)
			throws InvalidArgumentException {
		if (input == null || output == null)
			throw new InvalidArgumentException(
					"BezierG1.java - calculate(Vector, Vector): Invalid Argument.");
		else {
			// Voor elk viertal van inputpunten berekenen we de outputpunten.
			for (int i = 0; i <= input.size() - 4; i = i + 3) {
				// Aantal stappen bepalen a.h.v. de afstand tussen de
				// eindpunten.
				int steps = 2 * Point.distance(input.elementAt(i), input
						.elementAt(i + 3));

				Point first, second;
				// Hiermee berekenen we de inputpunten, nodig voor G1.
				Tangent c = new Tangent();

				try {
					// Bereken het tweede inputpunt uit het huidige tweede
					// inputpunt en uit het vorige derde inputpunt,
					// indien beschikbaar.
					if (i > 0)
						first = c.calculate(Tangent.CONTINUITY.G1, (short) 2,
								input.elementAt(i - 1), input.elementAt(i),
								input.elementAt(i + 1));
					else
						first = input.elementAt(i + 1);

					// Bereken het derde inputpunt uit het huidige derde
					// inputpunt en uit het volgende tweede inputpunt,
					// indien beschikbaar.
					if (i <= input.size() - 7)
						second = c.calculate(Tangent.CONTINUITY.G1, (short) 1,
								input.elementAt(i + 2), input.elementAt(i + 3),
								input.elementAt(i + 4));
					else
						second = input.elementAt(i + 2);
				} catch (InvalidArgumentException e) {
					output.clear();
					throw e;
				}

				// Bereken de interpolatiepunten.
				interpolate(input.elementAt(i), first, second, input
						.elementAt(i + 3), steps, output);
			}
		}
	}
}
