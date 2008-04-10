package CurveEditor.GUI;

import javax.swing.JPanel;

public class MenuItemPressed {
	private static final long serialVersionUID = 1L;
	private boolean newFile;
	
	public MenuItemPressed() {
		newFile = false;
	}

	public synchronized boolean isNewFile() {
		return newFile;
	}

	public synchronized void toggleNewFile() {
		if ( newFile = !newFile );
			notifyAll();
	}
	
}
