package CurveEditor;

import CurveEditor.GUI.HandleExceptions;

public class Main {

	public static void main(String[] args) {
		// Indien een filename werd meegegeven --> inladen.
		// Anders gewoon een lege GUI aanmaken.

		String file = null;
		boolean found = false;

		for (int i = 0; i < args.length && !found; ++i)
			if (args[i].compareTo(new String("-f")) == 0)
				if (i + 1 < args.length) {
					found = true;
					file = args[i + 1];
				}

		try {
			if (file == null)
				new CurveEditor.GUI.GUI();
			else
				new CurveEditor.GUI.GUI(file);
		} catch (Exception e) {
//			System.out.println( e.getMessage() );
//			e.printStackTrace();
			HandleExceptions.print( e );
		}
	}
}
