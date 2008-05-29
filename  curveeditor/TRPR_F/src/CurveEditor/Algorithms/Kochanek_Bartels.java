package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Kochanek_Bartels extends Hermite {
	public Kochanek_Bartels( char type, short degree ) {
		super( type, degree );
	}	
     
	public void calculate(Curve cv) {
		float b,c;
		// zoek een random b en c tussen 0 en 1 om als tangens te gebruiken
		do {
			b = (float) Math.random();
		} while (b == 0.0);
		do {
			c = (float) Math.random();
		} while (c == 0.0);
		
		kochanekBartels(cv, b, c);
	}
	
	public void calculateComplete(Curve cv) {
		float b,c;
		// zoek een random b en c tussen 0 en 1 om als tangens te gebruiken
		do {
			b = (float) Math.random();
		} while (b == 0.0);
		do {
			c = (float) Math.random();
		} while (c == 0.0);
		
		kochanekBartelsComplete(cv, b, c);
	}
	
//            (1-t)*(1-c)*(1+b)
//  TS    =   -----------------  * ( P   -  P    )
//    i              2                i      i-1

//            (1-t)*(1+c)*(1-b)
//        +   -----------------  * ( P   -  P    )
//                   2                i+1    i

//  The "outgoing" Tangent equation:

//            (1-t)*(1+c)*(1+b)
//  TD    =   -----------------  * ( P   -  P    )
//    i              2                i      i-1

//            (1-t)*(1-c)*(1-b)
//        +   -----------------  * ( P   -  P    )
//                   2                i+1    i
	private void kochanekBartels( Curve cv, float c, float b ) {
		Vector<Point> vip = cv.getInput();
		Vector<Point> vop = cv.getOutput();
		float t;

		int size = vip.size();

		// Er zijn minstens 4 punten nodig om deze bewerking uit te kunnen
		// voeren
		// Pi-1, Pi, Pi+1, Pi+2
		// hierbij zijn Pi en Pi+1 de punten waartussen we interpoleren
		// de andere punten worden berekend om de tangens te berekenen
		// volgens volgende formule Ti = c*(Pi+1 - Pi-1)
		if (size - 3 >= 0) {
			Point p1 = vip.get(size - 3);
			Point p2 = vip.get(size - 2);
			Point p3 = vip.get(size - 1);

			int x, y;
			// we interpoleren tussen p2 en p3 eerst t2 vinden
			x = (int) (c * (p3.X() - p1.X()));
			y = (int) (c * (p3.Y() - p1.Y()));
			Point t2 = new Point(x, y);

			// nu t3 vinden
//			x = (int) (c * (p4.X() - p2.X()));
//			y = (int) (c * (p4.Y() - p2.Y()));
			Point t3 = new Point(x, y);

			for (int j = 0; j < steps; ++j) {
				t = (float) (j / (steps - 1.0));
				vop.add(hermite(p2, t2, p3, t3, t));
			}
		}
	}
	
	private void kochanekBartelsComplete( Curve cv, float c, float b ) {
		
	}
}
