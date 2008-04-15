package CurveEditor
.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Hermite extends Algorithm {
	int steps;
	
	public Hermite(short degree) {
		super('H', degree);
		steps = 1000;
	}

	private Point hermite(Point a, Point b, Point c, Point d, double t, double m0, double m1 ) {
			double h00 = 2*Math.pow(t, 3) - 3*Math.pow(t, 2) + 1; // calculate basis function 1
			double h10 = Math.pow(t, 3) - 2*Math.pow(t, 2) + t; // calculate basis function 3
			double h01 = -2*Math.pow(t, 3) + 3*Math.pow(t, 2); // calculate basis function 2
			double h11 = Math.pow(t, 3) - Math.pow(t, 2); // calculate basis function 4 
		
			double y = h00*a.Y() + h10*m0 + h01*c.Y() + h11*m1;
		
		return new Point( (int) Math.floor(a.X() + (c.X() - a.X()) * t + 0.5), (int) Math.floor( y + .5 ) );
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
			m0 = ((double)b.Y() - a.Y()) / (b.X()- a.X());
			m1 = ((double)d.Y() - c.Y()) / (d.X()- c.X());
			for (int j = 0; j < steps; ++j ) {				
				t = (double) (j / (steps - 1.0));
				vop.add(hermite(a, b, c, d, t, m0, m1));
			}	
		}
	}
}
