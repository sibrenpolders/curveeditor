package CurveEditor.Algorithms;

import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;
import CurveEditor.Exceptions.*;

/*
 * Deze van Algorithm afgeleide klasse implementeert het Bezier-interpolatiealgoritme.
 * Per vier punten wordt een curve berekend als volgt: de curve start in het eerste punt 
 * in de richting van het tweede, om dan in het vierde punt te eindigen in de richting 
 * van het derde.
 * Voor meer informatie over dit algoritme: 
 * 		zie cursus Computer Graphics 2e Bach UHasselt, p. 106.
 */
public class Bezier extends Algorithm {
	protected double[][] matrix; // de Bezier-
	protected double[][] controlPtsMatrix; // de matrix
	protected double[] parameterMatrix;

	// Deze constructor is voorzien opdat we subklassen van Bezier zouden kunnen
	// implementeren, die dezelfde orde zouden hebben.
	public Bezier(char type, short degree) {
		super(type, degree);
		createMatrix();
	}

	public Bezier(short degree) {
		super('B', degree);
		createMatrix();
	}

	// orde = 3 --> per 4 controlepunten berkenen we interpolatiepunten
	public Bezier() {
		super('B', (short) 3);
		createMatrix();
	}

	protected final void createMatrix() {
		matrix = new double[4][4];
		matrix[0][0] = -1.0;
		matrix[0][1] = 3.0;
		matrix[0][2] = -3.0;
		matrix[0][3] = 1.0;
		matrix[1][0] = 3.0;
		matrix[1][1] = -6.0;
		matrix[1][2] = 3.0;
		matrix[1][3] = 0.0;
		matrix[2][0] = -3.0;
		matrix[2][1] = 3.0;
		matrix[2][2] = 0.0;
		matrix[2][3] = 0.0;
		matrix[3][0] = 1.0;
		matrix[3][1] = 0.0;
		matrix[3][2] = 0.0;
		matrix[3][3] = 0.0;
	}

	protected final void fillControlPointsMatrix(Point a, Point b, Point c,
			Point d) {
		controlPtsMatrix = new double[2][4];
		controlPtsMatrix[0][0] = a.X();
		controlPtsMatrix[0][1] = b.X();
		controlPtsMatrix[0][2] = c.X();
		controlPtsMatrix[0][3] = d.X();

		controlPtsMatrix[1][0] = a.Y();
		controlPtsMatrix[1][1] = b.Y();
		controlPtsMatrix[1][2] = c.Y();
		controlPtsMatrix[1][3] = d.Y();
	}

	protected final void fillParameterMatrix(double t) {
		parameterMatrix = new double[4];

		parameterMatrix[3] = 1;
		parameterMatrix[2] = t;
		parameterMatrix[1] = t * t;
		parameterMatrix[0] = parameterMatrix[1] * t;
	}

	public final void interpolate(Point aa, Point bb, Point cc, Point dd,
			int steps, Vector<Point> out) {
		double d = 1.0 / steps;
		double d2 = d * d;
		double d3 = d2 * d;
		double x = aa.X();
		double y = aa.Y();

		fillControlPointsMatrix(aa, bb, cc, dd);

		double ax = matrix[0][0] * controlPtsMatrix[0][0] + matrix[0][1]
				* controlPtsMatrix[0][1] + matrix[0][2]
				* controlPtsMatrix[0][2] + matrix[0][3]
				* controlPtsMatrix[0][3];
		double ay = matrix[0][0] * controlPtsMatrix[1][0] + matrix[0][1]
				* controlPtsMatrix[1][1] + matrix[0][2]
				* controlPtsMatrix[1][2] + matrix[0][3]
				* controlPtsMatrix[1][3];

		double bx = matrix[1][0] * controlPtsMatrix[0][0] + matrix[1][1]
				* controlPtsMatrix[0][1] + matrix[1][2]
				* controlPtsMatrix[0][2] + matrix[1][3]
				* controlPtsMatrix[0][3];
		double by = matrix[1][0] * controlPtsMatrix[1][0] + matrix[1][1]
				* controlPtsMatrix[1][1] + matrix[1][2]
				* controlPtsMatrix[1][2] + matrix[1][3]
				* controlPtsMatrix[1][3];

		double cx = matrix[2][0] * controlPtsMatrix[0][0] + matrix[2][1]
				* controlPtsMatrix[0][1] + matrix[2][2]
				* controlPtsMatrix[0][2] + matrix[2][3]
				* controlPtsMatrix[0][3];
		double cy = matrix[2][0] * controlPtsMatrix[1][0] + matrix[2][1]
				* controlPtsMatrix[1][1] + matrix[2][2]
				* controlPtsMatrix[1][2] + matrix[2][3]
				* controlPtsMatrix[1][3];

		double Dx = d3 * ax + d2 * bx + d * cx;
		double Dy = d3 * ay + d2 * by + d * cy;
		double D2x = 6 * d3 * ax + 2 * d2 * bx;
		double D2y = 6 * d3 * ay + 2 * d2 * by;
		double D3x = 6 * d3 * ax;
		double D3y = 6 * d3 * ay;

		out
				.add(new Point((int) Math.floor(x + 0.5), (int) Math
						.floor(y + 0.5)));

		for (int i = 0; i <= steps; ++i) {
			x += Dx;
			Dx += D2x;
			D2x += D3x;
			y += Dy;
			Dy += D2y;
			D2y += D3y;

			out.add(new Point((int) Math.floor(x + 0.5), (int) Math
					.floor(y + 0.5)));
		}
	}

	public void calculate(Vector<Point> input, Vector<Point> output) {
		output.clear();

		for (int i = 0; i <= input.size() - 4; i = i + 3) {
			// aantal stappen bepalen a.h.v. de afstand tussen de eindpunten
			int steps = 2 * Point.distance(input.elementAt(i), input
					.elementAt(i + 3));

			interpolate(input.elementAt(i), input.elementAt(i + 1), input
					.elementAt(i + 2), input.elementAt(i + 3), steps, output);
		}
	}

	public final void calculateComplete(Curve c)
			throws InvalidArgumentException {
		c.clearOutput();
		calculate(c);
	}
}