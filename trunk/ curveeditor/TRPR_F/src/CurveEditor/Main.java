package CurveEditor;

import CurveEditor.Exceptions.InvalidArgumentException;

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

		if (file == null) {
			try {
				new CurveEditor.GUI.GUI();
			} catch (InvalidArgumentException e) {
				e.printStackTrace();
			}
		} else {
			try {
				new CurveEditor.GUI.GUI(file);
			} catch (InvalidArgumentException e) {
				e.printStackTrace();
			}
		}
	}
}
