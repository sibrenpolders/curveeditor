package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class HermiteCatmulRom extends HermiteCardinal {
	private final double d = 0.5;
	
	public HermiteCatmulRom(char type, short degree) {
		super(type, degree);
		// TODO Auto-generated constructor stub
	}
	
	public void calculateCurve(Curve cv) {
		Vector<Point> vip = cv.getInput();
		Vector<Point> vop = cv.getOutput();
		double t;

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
