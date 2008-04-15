package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class HermiteCardinal extends Hermite {

	public HermiteCardinal(char type, short degree) {
		super(type, degree);
	}

	protected Point hermiteCard( Point a, Point b, Point c, double d, double t ) {
		double m0 = d * (b.Y() - a.Y());
		double m1 = d * (c.Y() - b.Y());
		
		return hermite(b, c, t, m0, m1);
	}
	
	public void calculateCurve(Curve cv) {
		Vector<Point> vip = cv.getInput();
		Vector<Point> vop = cv.getOutput();
		double t;
		double d;
		do {
			d = Math.random();
		} while ( d == 0.0 );
		
		for ( int i = 0; i < vip.size() - 3; ++i ) {
			Point a = vip.get( i );
			Point b = vip.get( i+1 );
			Point c = vip.get( i+2 );
			for (int j = 0; j < steps; ++j ) {				
				t = (double) (j / (steps - 1.0));
				vop.add(hermiteCard(a, b, c, d, t));
			}	
		}
	}
}
