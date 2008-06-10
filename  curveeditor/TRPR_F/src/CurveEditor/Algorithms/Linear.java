package CurveEditor.Algorithms;

import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;
import CurveEditor.Exceptions.DivisionByZeroException;
import CurveEditor.Exceptions.InvalidArgumentException;

/*
 * Deze van Algorithm afgeleide klasse implementeert gewoon lineaire interpolatie.
 * M.a.w., per twee punten wordt gewoon een tussenliggend lijnstuk getekend.
 */
public class Linear extends Algorithm {

	public Linear(char type, short degree) {
		super(type, (short) degree);
	}

	public Linear(short degree) {
		super('L', (short) degree);
	}

	public Linear() {
		super('L', (short) 1);
	}

	// Gegeven de coördinaten van het begin- en eindpunt én een X-waarde,
	// zoek de bijhorende Y-waarde van het punt met die X-waarde dat op
	// het tussenliggend lijnstuk ligt.
	private final int findYForX(int x, int x0, int y0, int x1, int y1)
			throws DivisionByZeroException {
		if (x1 - x0 == 0)
			throw new DivisionByZeroException();

		else
			return (int) ((double) y0 + (x - (double) x0)
					* ((y1 - y0) / ((double) x1 - x0)));
	}

	// Gegeven de coördinaten van het begin- en eindpunt én een Y-waarde,
	// zoek de bijhorende X-waarde van het punt met die Y-waarde dat op
	// het tussenliggend lijnstuk ligt.
	private final int findXForY(int y, int x0, int y0, int x1, int y1)
			throws DivisionByZeroException {
		if (y1 - y0 == 0)
			throw new DivisionByZeroException();

		return (int) ((double) x0 + (y - (double) y0)
				* ((x1 - x0) / ((double) y1 - y0)));
	}

	// Gegeven een Vector van inputpunten, hervul de meegegeven Vector van
	// outpunten m.b.v. het geïmplementeerde interpolatiealgoritme.
	public final void calculate( Vector<Point> input, Vector<Point> output )
			throws InvalidArgumentException {
		if ( input == null || output == null )
			throw new InvalidArgumentException(
					"Linear.java - calculate(Vector, Vector): Invalid Argument.");
		else {
			output.clear( );

			// Voor elk paar punten de interpolatiepunten berekenen.
			for ( int i = 0; i < input.size( ) - 1; ++i ) {
				if ( input.get( i ).X( ) != input.get( i + 1 ).X( )
						|| input.get( i ).Y( ) != input.get( i + 1 ).Y( )) {
					// Positief verschil in x-waarden.
					int diff1 = ( input.get(i).X() < input.get(i + 1).X()) ? 
							input.get( i + 1).X() - input.get( i).X() : input.get( i ).X( ) - input.get( i + 1 ).X( );

					// Positief verschil in y-waarden.
					int diff2 = ( input.get( i ).Y( ) < input.get( i + 1 ).Y( )) ? 
							input.get( i + 1 ).Y( )	- input.get( i ).Y() : input.get( i ).Y( ) - input.get( i + 1 ).Y( );

					try {
						// Lijnstuk stijgt sneller volgens X dan volgens Y
						// --> voor elke X de Y zoeken.
						if ( diff1 > diff2 ) {
							// Eerste controlepunt ligt links van het tweede.
							if ( input.get( i ).X( ) < input.get( i + 1 ).X( )) {
								for ( int x = input.get( i ).X(); x <= input.get( i + 1 ).X( ); ++x ) {
									output.add( new Point( x, findYForX( x, input.get( i ).X( ), 
											input.get( i ).Y( ), input.get( i + 1 ).X( ), 
											input.get( i + 1 ).Y( ))));
								}
							}
							// Eerste controlepunt ligt rechts van het tweede.
							else {
								for ( int x = input.get( i ).X( ); x >= input.get( i + 1 ).X( ); --x ) {
									output.add( new Point( x, findYForX( x, input.get( i ).X( ), input.get( i ).Y( ),
											input.get( i + 1 ).X( ), input.get(	i + 1 ).Y( ))));
								}
							}
						}
						// Lijnstuk stijgt sneller volgens Y dan volgens X
						// --> voor elke Y de X zoeken.
						else {
							// Eerste controlepunt ligt onder het tweede.
							if ( input.get( i ).Y( ) < input.get( i + 1 ).Y( )) {
								for (int y = input.get( i ).Y( ); y <= input.get( i + 1 ).Y(); ++y ) {
									output.add( new Point( findXForY( y, input.get( i ).X( ), input.get( i ).Y( ),
											input.get( i + 1 ).X( ), input.get( i + 1 ).Y( )), y ));
								}
							}
							// Eerste controlepunt ligt boven het tweede.
							else {
								for ( int y = input.get( i ).Y( ); y >= input.get( i + 1 ).Y( ); --y ) {
									output.add(  new Point( findXForY( y, input.get( i ).X( ), input.get( i ).Y( ),
											input.get( i + 1 ).X( ), input.get( i + 1 ).Y( )), y ));
								}
							}
						}
					} catch ( DivisionByZeroException e ) {
						output.clear( );
						throw new InvalidArgumentException( "Linear.java - calculate(Vector, Vector): Division by zero." );
					}
				}
			}
		}
	}

	// Gegeven een Curve c, hervul zijn Vector van outputpunten vollédig met de
	// geïnterpoleerde punten van de inputpunten.
	public final void calculateComplete( Curve c )
			throws InvalidArgumentException {
		try {
			calculate(c);
		} catch (InvalidArgumentException e) {
			if (c != null)
				c.clearOutput();

			throw e;
		}
	}

	@Override
	public void calculate(Curve c) throws InvalidArgumentException {
		if (c == null) // ongeldig argument --> exception gooien
			throw new InvalidArgumentException(
					"Algorithm.java - calculate(Curve): Invalid Argument.");
		else {
			c.clearOutput();
			calculate(c.getInput(), c.getOutput());
		}
	}
}