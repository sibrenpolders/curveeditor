package CurveEditor.Algorithms;

import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

/*
 * Deze van Algorithm afgeleide klasse implementeert het Hermite-interpolatiealgoritme.
 * Per vier punten wordt een curve berekend als volgt: de curve start in het eerste punt 
 * in de richting van het tweede, om dan in het derde punt te eindigen in de richting 
 * van het vierde. Punten 2 en 4 zijn dus de richtingsvectoren van de punten 1 en 3.
 */
public class Hermite extends Algorithm {
	private final int _2D = 2;
	private final int X_ROW = 0;
	private final int Y_ROW = 1;
	private final int F0_VALUE = 0;	
	private final int DF0_VALUE = 1;
	private final int D2F0_VALUE = 2;
	private final int D3F0_VALUE = 3;
	
	protected int steps;
	private float delta;
	private float delta2;
	private float delta3;
	private float[][] matrix;
	
	public Hermite(char type, short degree) {
		super(type, degree);
		init( );
		System.out.println( "B:::" + delta + " " + delta2 + " " + delta3 );
	}

	public Hermite(short degree) {
		super('H', degree);
		init( );
	}

	public Hermite() {
		super('H', (short) 0);
		init( );
	}

	private void init( ) {
		steps = 1000;
		delta = 1.0f/steps;
		delta2 = delta * delta;
		delta3 = delta * delta2;
		matrix = new float[_2D][4];
	}
	
	/*
	 * Als input krijg je 2 punten p1 en p2 en hun respectievelijke richtingsvectoren r1 en r2.
	 * Hieruit wordt m.b.v. de hermite basis functies de geinterpoleerde x en y gehaald.
	 * 
	 */
	protected void hermite( Point p1, Point t1, Point p2, Point t2, Vector<Point> vop ) {
//		float t2 = (float) Math.pow(t, 2);
//		float t3 = (float) Math.pow(t, 3);
//
//		float h00 = 2 * t3 - 3 * t2 + 1; // basis function 1
//		float h10 = t3 - 2 * t2 + t; // basis function 3
//		float h01 = -2 * t3 + 3 * t2; // basis function 2
//		float h11 = t3 - t2; // basis function 4
//
//		float y = h00 * p1.Y() + h10 * r1.Y() + h01 * p2.Y() + h11 * r2.Y(); // nieuwe
//		// y-coordinaat
//		float x = h00 * p1.X() + h10 * r1.X() + h01 * p2.X() + h11 * r2.X(); // nieuwe
//		// x-coordinaat
//
//		System.out.println( new Point((int) Math.floor(x + .5), (int) Math.floor(y + .5)));
//		
//		return new Point((int) Math.floor(x + .5), (int) Math.floor(y + .5));
		setMatrix( X_ROW, p1.X(), t1.X(), p2.X(), t2.X() );		
		setMatrix( Y_ROW, p1.Y(), t1.Y(), p2.Y(), t2.Y() );
		
		Point pNew;
//		Point pNew = new Point( (int) Math.floor( matrix[X_ROW][F0_VALUE] + .5 ), 
//								(int) Math.floor( matrix[Y_ROW][F0_VALUE] + .5 )); 
//		vop.add( pNew );
		
		for ( int i = 0; i < steps; ++i ) {
			pNew = new Point( (int) Math.floor( matrix[X_ROW][F0_VALUE] + .5 ), 
					(int) Math.floor( matrix[Y_ROW][F0_VALUE] + .5 ));
			
			netStep( X_ROW );
			netStep( Y_ROW );						
			
			System.out.println( pNew );			
			vop.add( pNew );
		}
	}

	private void netStep( int row ) {
		matrix[row][F0_VALUE] += matrix[row][DF0_VALUE];
		matrix[row][DF0_VALUE] += matrix[row][D2F0_VALUE];
		matrix[row][D2F0_VALUE] += matrix[row][D3F0_VALUE];
	}

	private void setMatrix( int row, int p1, int t1, int p2, int t2 ) {
		int a = 2*p1 + t1 - 2*p2 + t2;
		int b = -3*p1 - 2*t1 + 3*p2 - t2;
		int c = t1;
		int d = p1;
		
		delta = 1.0f/steps;
		delta2 = delta * delta;
		delta3 = delta * delta2;
		
		System.out.println( "A: " + a + " B: " + b + " C: " + c + " D: " + d );
		matrix[row][F0_VALUE] = d;
		matrix[row][DF0_VALUE] = a*delta3 + b*delta2 + c*delta;
		matrix[row][D2F0_VALUE] = 6*a*delta3 + 2*b*delta2;
		matrix[row][D3F0_VALUE] = 6*a*delta3;
	}

	// een overloaded functie van calculate voor het gebruiksgemak
	public void calculate(Curve cv) {
		calculate(cv.getInput(), cv.getOutput());
	}
	
	// Zal de interpolatie berekenen tussen de 4 laatste punten
	public void calculate(Vector<Point> vip, Vector<Point> vop) {
		float t;

		int size = vip.size();
		// Er zijn minstens 4 punten nodige om deze hermiet berekening te kunnen
		// uitvoeren
		// nl. Pi Ri Pj Rj waarbij Pi, Pj de punten zijn waartussen we
		// interpolleren
		// en Ri, Rj de tangens zijn van de kromme in respectievelijk Pi, Pj
		if (size - 4 >= 0) {
			// enkel de interpolatie tussen het laatste en het voorlaatste punt
			// moet berekend worden
			Point p1 = vip.get(size - 4);
			Point p2 = vip.get(size - 3);
			Point p3 = vip.get(size - 2);
			Point p4 = vip.get(size - 1);
			Point r1 = new Point(p2.X() - p1.X(), p2.Y() - p1.Y());
			Point r2 = new Point(p4.X() - p3.X(), p4.Y() - p3.Y());
//			for (int j = 0; j < steps; ++j) {
//				t = (float) (j / (steps - 1.0));
				/*vop.add(*/hermite(p1, r1, p2, r2, vop )/*)*/;
			//}
		}
	}

	// berekend de interpolatie tussen alle opgegeven punten
	public void calculateComplete(Curve cv) {
		Vector<Point> vip = cv.getInput();
		Vector<Point> vop = cv.getOutput();
		cv.clearOutput();

		float t;

		// for ( int i = 0; i <= vip.size() - 4; i += 2 ) {
		int size = vip.size();
		// Er zijn minstens 4 punten nodige om deze hermiet berekening te kunnen
		// uitvoeren
		// nl. Pi Ri Pj Rj waarbij Pi, Pj de punten zijn waartussen we
		// interpoleren
		// en Ri, Rj de tangens zijn van de kromme in respectievelijk Pi, Pj
		for (int i = 0; i < size - 3; i += 2) {
			// enkel de interpolatie tussen het laatste en het voorlaatste punt
			// moet berekend worden
			Point a = vip.get(i);
			Point b = vip.get(i + 1);
			Point c = vip.get(i + 2);
			Point d = vip.get(i + 3);
			Point r1 = new Point(b.X() - a.X(), b.Y() - a.Y());
			Point r2 = new Point(d.X() - c.X(), d.Y() - c.Y());
//			for (int j = 0; j < steps; ++j) {
//				t = (float) (j / (steps - 1.0));
				/*vop.add(*/hermite( a, r1, c, r2, vop );/*);
			}*/
		}
	}
}
