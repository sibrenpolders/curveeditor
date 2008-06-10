// auteur Sibren Polders
package CurveEditor.Tools;

/*
 * Deze klasse zal voor een klein bolletje zorgen die de getekende curve zal evalueren
 * De klasse start in een aparte thread zodat de gebruiker nog rustig verder kan werken.
 */
import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;
import CurveEditor.GUI.DrawArea;

public class PathSimulation implements Runnable {
	private Vector<Curve> curves;
	private DrawArea draw;

	public PathSimulation( DrawArea draw, Vector<Curve> curves ) {
		this.draw = draw;
		this.curves = curves;
	}

	public void run( ) {
		for ( int i = 0; i < curves.size( ); ++i ) {
			Vector<Point> out = curves.get( i ).getOutput( );

			for ( int j = 0; j < out.size( ); j += 10 ) {
				// teken het bolletje op positie out.get( j )
				draw.drawRunner( out.get( j ));
				// doe het eigenlijke tekenwerk
				draw.repaint( );
				try {
					Thread.sleep( 100 );
				} catch ( InterruptedException e ) {
					e.printStackTrace( );
				}
			}
			draw.repaint( );
		}
	}
}
