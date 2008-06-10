package CurveEditor.Tools;

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
				draw.drawRunner( out.get( j ));
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
