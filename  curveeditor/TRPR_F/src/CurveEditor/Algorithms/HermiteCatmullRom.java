// auteur Sibren Polders
package CurveEditor.Algorithms;

import CurveEditor.Curves.Curve;

/*
 * Deze van Algorithm afgeleide klasse implementeert het Hermite CatmullRom-interpolatiealgoritme.
 * Het algorithme is identiek aan hetgeen van Hermite Cardinaal, met als enige verschil dat in de formule die 
 * gebruikt werd om de richtingsvectoren te berekenen, c nu vast is (nl. 0.5 )
 */
public final class HermiteCatmullRom extends HermiteCardinal {
	private final double d = 0.5;

	public HermiteCatmullRom( char type, short degree ) {
		super( type, degree );
	}

	public HermiteCatmullRom( short degree ) {
		this( 'R', degree );
	}

	public HermiteCatmullRom( ) {
		this( 'R', (short) 0 );
	}

	public void calculate( Curve cv ) {
		cardinal( cv, (float) d );
	}

	public void calculateComplete( Curve cv ) {
		cardinalComplete( cv, (float) d );
	}
}
