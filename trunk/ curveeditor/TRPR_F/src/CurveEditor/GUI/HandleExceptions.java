// auteur Sibren Polders
package CurveEditor.GUI;

/*
 * Deze klasse zal ervoor zorgen dat in de GUI de verschillende errors
 * in een messagebox worden getoond
 */
import javax.swing.JOptionPane;

import CurveEditor.Exceptions.DivisionByZeroException;
import CurveEditor.Exceptions.InvalidArgumentException;

public class HandleExceptions {
	public HandleExceptions( ) {

	}

	static public void print( NullPointerException npe ) {
		JOptionPane.showMessageDialog( null, npe
				.getMessage( ), "Curve Editor: ERROR", JOptionPane.ERROR_MESSAGE );
	}

	static public void print( DivisionByZeroException dbze ) {
		JOptionPane.showMessageDialog( null, dbze
				.getMessage( ), "Curve Editor: ERROR", JOptionPane.ERROR_MESSAGE );
	}

	static public void print( InvalidArgumentException iae ) {
		JOptionPane.showMessageDialog( null, iae
				.getMessage(), "Curve Editor: ERROR", JOptionPane.ERROR_MESSAGE );
	}
	
	static public void print( Exception e ) {
		JOptionPane.showMessageDialog( null, e
				.getMessage(), "Curve Editor: ERROR", JOptionPane.ERROR_MESSAGE );
	}
}
