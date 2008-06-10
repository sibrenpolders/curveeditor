package CurveEditor.GUI;

import javax.swing.JOptionPane;

import CurveEditor.Exceptions.DivisionByZeroException;
import CurveEditor.Exceptions.InvalidArgumentException;

public class HandleExceptions {
	public HandleExceptions( ) {

	}

	void print( NullPointerException npe ) {
		JOptionPane.showMessageDialog( null, "Curve Editor: ERROR", npe
				.getMessage( ), JOptionPane.ERROR_MESSAGE );
	}

	void print( DivisionByZeroException dbze ) {
		JOptionPane.showMessageDialog( null, "Curve Editor: ERROR", dbze
				.getMessage( ), JOptionPane.ERROR_MESSAGE );
	}

	void print( InvalidArgumentException iae ) {
		JOptionPane.showMessageDialog( null, "Curve Editor: ERROR", iae
				.getMessage(), JOptionPane.ERROR_MESSAGE );
	}
}
