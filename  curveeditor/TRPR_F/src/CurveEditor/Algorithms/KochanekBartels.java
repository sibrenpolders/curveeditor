package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

/*
 * Deze van Algorithm afgeleide klasse implementeert het KochanekBartels.
 * Per vier punten wordt een curve berekend als volgt: de curve start in het tweede punt,
 * en eindigd in het derde. De tangens van het tweede punt wordt berekend m.b.v. het eerste zichzelf en het derde .
 * de tangens van het derde punt wordt berekend m.b.v. het tweede, zichzelf en het derde.
 * De formules worden hieronder weergegeven, merk op dat hoewel de formules uiterlijk op elkaar lijken ze
 * toch verschillen van min en plus tekens. In de formule vind je ook 3 random variabelen
 * t = tension ( bepaalt de lengte van de tangens vector )
 * c = continuiteit ( bepaalt de scherpte van overgan van de tangens ti en tangen ti+1 )
 * b = bias  ( bepaalt de richting van de tangens vector )
 * Dze waardens worden random gekozen tussen 0 en 1
 * 
 * input = Pi-1, Pi, Pi+1, Pi+2 
 *
 *           (1-t)*(1+c)*(1+b)
 * Ti    =   -----------------  * ( Pi   -  Pi-1    )
 *                 2               
 *
 *           (1-t)*(1-c)*(1-b)
 *       +   -----------------  * ( Pi+1   -  Pi    )
 *                 2               
 *            (1-t)*(1-c)*(1+b)
 * Ti+1    =   -----------------  * ( Pi+1   -  Pi    )
 * 
 *           (1-t)*(1+c)*(1-b)
 *       +   -----------------  * ( Pi+2   -  Pi+1    )
 *                  2                
 *                                
 */

public class KochanekBartels extends Hermite {
	public KochanekBartels( char type, short degree ) {
		super( type, degree );
	}	
     
	public void calculate(Curve cv) {
		float b, c, t;
		
		// zoek een random b en c tussen 0 en 1 om als tangens te gebruiken		
		do {
			b = (float) Math.random();
		} while (b == 0.0);
		
		do {
			c = (float) Math.random();
		} while (c == 0.0);
		
		do {
			t = (float) Math.random();
		} while (t == 0.0);
		
		kochanekBartels(cv, b, c, t);
	}
	
	public void calculateComplete(Curve cv) {
		float b, c, t;
		// zoek een random b en c tussen 0 en 1 om als tangens te gebruiken
		
		do {
			b = (float) Math.random();
		} while (b == 0.0);
		
		do {
			c = (float) Math.random();
		} while (c == 0.0);
		
		do {
			t = (float) Math.random();
		} while (t == 0.0);
		
		kochanekBartelsComplete(cv, b, c, t);
	}
	


//  The "outgoing" Tangent equation:

	private void kochanekBartels( Curve cv, float c, float b, float t ) {
		Vector<Point> vip = cv.getInput();
		Vector<Point> vop = cv.getOutput();

		int size = vip.size();

		// Er zijn minstens 4 punten nodig om deze bewerking uit te kunnen
		// voeren
		// Pi-1, Pi, Pi+1, Pi+2
		// hierbij zijn Pi en Pi+1 de punten waartussen we interpoleren
		// de andere punten worden berekend om de tangens te berekenen
		// volgens volgende formule Ti = c*(Pi+1 - Pi-1)
		if (size - 4 >= 0) {
			Point p1 = vip.get( size - 4 );
			Point p2 = vip.get( size - 3 );
			Point p3 = vip.get( size - 2 );
			Point p4 = vip.get( size - 1 );
			
			int x, y;
			// we interpoleren tussen p2 en p3 eerst t2 vinden
			x = (int) (((1-t)*(1+c)*(1+b)*( p3.X() - p2.X())) / 2 + ((1-t)*(1-c)*(1-b)*( p4.X() - p3.X())) / 2);
			y = (int) (((1-t)*(1+c)*(1+b)*( p3.Y() - p2.Y())) / 2 + ((1-t)*(1-c)*(1-b)*( p4.Y() - p3.Y())) / 2);
			Point t2 = new Point(x, y);

			// nu t3 vinden
			x = (int) (((1-t)*(1-c)*(1+b)*( p2.X() - p1.X())) / 2 + ((1-t)*(1+c)*(1-b)*( p3.X() - p2.X())) / 2);
			y = (int) (((1-t)*(1-c)*(1+b)*( p2.Y() - p1.Y())) / 2 + ((1-t)*(1+c)*(1-b)*( p3.Y() - p2.Y())) / 2);			
			Point t3 = new Point(x, y);

			hermite(p2, t2, p3, t3, vop );			
		}
	}
	
	private void kochanekBartelsComplete( Curve cv, float c, float b, float t ) {
		Vector<Point> vip = cv.getInput();
		Vector<Point> vop = cv.getOutput();
		cv.clearOutput();

		int size = vip.size();
		// Er zijn minstens 4 punten nodige om deze hermiet berekening te kunnen
		// uitvoeren
		// nl. Pi Ri Pj Rj waarbij Pi, Pj de punten zijn waartussen we
		// interpoleren
		// en Ri, Rj de zijn tangens van de kromme in respectievelijk Pi, Pj
		for (int i = 0; i < size - 3; ++i) {
			// enkel de interpolatie tussen het laatste en het voorlaatste punt
			// moet berekend worden
			Point p1 = vip.get(i);
			Point p2 = vip.get(i + 1);
			Point p3 = vip.get(i + 2);
			Point p4 = vip.get(i + 3);
			int x, y;
			// we interpoleren tussen p2 en p3 eerst t2 vinden
			x = (int) (((1-t)*(1-c)*(1+b)*( p2.X() - p1.X())) / 2 + ((1-t)*(1+c)*(1-b)*( p3.X() - p2.X())) / 2);
			y = (int) (((1-t)*(1-c)*(1+b)*( p2.Y() - p1.Y())) / 2 + ((1-t)*(1+c)*(1-b)*( p3.Y() - p2.Y())) / 2);
			Point t2 = new Point(x, y);

			// nu t3 vinden
			x = (int) (((1-t)*(1+c)*(1+b)*( p3.X() - p2.X())) / 2 + ((1-t)*(1-c)*(1-b)*( p4.X() - p3.X())) / 2);
			y = (int) (((1-t)*(1+c)*(1+b)*( p3.Y() - p2.Y())) / 2 + ((1-t)*(1-c)*(1-b)*( p4.Y() - p3.Y())) / 2);
			Point t3 = new Point(x, y);

			hermite(p2, t2, p3, t3, vop );
		}
	}
}
