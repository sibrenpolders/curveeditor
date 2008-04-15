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

	private Point hermite(Point a, Point b, Point c, Point d, double t) {
		double x = a.X() + t;
		double h00 = 2*Math.pow(t, 3) - 3*Math.pow(t, 2) + 1; // calculate basis function 1
		double h10 = Math.pow(t, 3) - 2*Math.pow(t, 2) + t; // calculate basis function 3
		double h01 = -2*Math.pow(t, 3) + 3*Math.pow(t, 2); // calculate basis function 2
		double h11 = Math.pow(t, 3) - Math.pow(t, 2); // calculate basis function 4 
		
		double y = h00*a.Y() + h10*b.Y() + h01*c.Y() + h11*d.Y();
		
		return new Point( (int) Math.floor(a.X() + (c.X() - a.X()) * t + 0.5), (int) Math.floor( y + .5 ) );
	}
	
	public void calculateCurve(Curve c) {
		Vector<Point> vip = c.getInput();
		Vector<Point> vop = c.getOutput();
		
		System.out.println( "begin" );
		for ( int i = 0; i < vip.size() - 2; i += 2 ) {
			System.out.println( vip.get(i) + " " + vip.get(i+2) );
			for (int j = 0; j < steps; ++j ) {
				double t = (double) (j / (steps - 1.0));
				vop.add(hermite( vip.get( i ), vip.get( i+1 ), vip.get( i+2 ), vip.get( i+3 ), t ));
			}	
		}
		System.out.println( "end" );
	}
}

