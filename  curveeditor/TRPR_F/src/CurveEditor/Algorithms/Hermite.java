package CurveEditor
.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Hermite extends Algorithm {
	int steps;
	
	public Hermite(char type, short degree) {
		super(type, degree);
		steps = 1000;
	}

	protected Point hermite(Point p1, Point r1, Point p2, Point r2, double t ) {		
		double h00 = 2*Math.pow(t, 3) - 3*Math.pow(t, 2) + 1; // calculate basis function 1
		double h10 = Math.pow(t, 3) - 2*Math.pow(t, 2) + t; // calculate basis function 3
		double h01 = -2*Math.pow(t, 3) + 3*Math.pow(t, 2); // calculate basis function 2
		double h11 = Math.pow(t, 3) - Math.pow(t, 2); // calculate basis function 4 
			
		double y = h00*p1.Y() + h10*r1.Y() + h01*p2.Y() + h11*r2.Y();
		double x = h00*p1.X() + h10*r1.X() + h01*p2.X() + h11*r2.X();
		// return new Point( (int) Math.floor(a.X() + (c.X() - a.X()) * t + .5 ), (int) Math.floor( y + .5 ) );
		return new Point( (int) Math.floor( x + .5 ), (int) Math.floor( y + .5 ) );
	}	
	
	public void calculateCurve(Curve cv) {
		Vector<Point> vip = cv.getInput();
		Vector<Point> vop = cv.getOutput();
		double m0, m1, t;
		
		for ( int i = 0; i < vip.size() - 2; i += 2 ) {
			Point a = vip.get( i );
			Point b = vip.get( i+1 );
			Point c = vip.get( i+2 );
			Point d = vip.get( i+3 );
			Point r1 = new Point( b.X() - a.X(), b.Y() - a.Y() );
			Point r2 = new Point( d.X() - c.X(), d.Y() - c.Y() );
			for (int j = 0; j < steps; ++j ) {				
				t = (double) (j / (steps - 1.0));
				vop.add(hermite(a, r1, c, r2, t));
			}	
		}
	}
}
