package CurveEditor.GUI;

public class MenuItemPressed {
	private static final long serialVersionUID = 1L;
	private boolean newFileSelected;
	private boolean openSelected;
	private boolean saveSelected;
	private boolean undoSelected;
	private boolean redoSelected;
	private boolean bezierSelected;
	private boolean hermitesSelected;
	private String fileName;
	
	public MenuItemPressed() {
		newFileSelected = false;
		openSelected = false;
		saveSelected = false;
		undoSelected = false;
		redoSelected = false;
		bezierSelected = false;
		hermitesSelected = false;
		fileName = null;
	}

	public synchronized boolean isNewFileSelected() {
		return newFileSelected;
	}

	public synchronized void toggleNewFileSelected() {
		if ( newFileSelected = !newFileSelected );
			notifyAll();
	}
	
	public synchronized boolean isOpenSelected() {
		return openSelected;
	}
	
	public synchronized void toggleOpenSelected( String fileName ) {
		this.fileName = fileName;
		
		if ( openSelected = !openSelected )
			notifyAll();
	}
	
	public synchronized boolean isSaveSelected() {
		return saveSelected;
	}
	
	public synchronized void toggleSaveSelected( String fileName ) {
		this.fileName = fileName;
		
		if ( saveSelected = !saveSelected )
			notifyAll();
	}
	
	// enkel te gebruiken als fileName al gedefineert is
	public synchronized void toggleSaveSelected() {
			if ( saveSelected = !saveSelected )
			notifyAll();
	}
		
	public synchronized String getFileName( ) {
		return fileName;
	}

	public synchronized boolean isUndoSelected( ) {
		return undoSelected;
	}
	
	public synchronized void toggleUndoSelected() {
		if ( undoSelected = !undoSelected )
			notifyAll();
	}
	
	public synchronized boolean isRedoSelected( ) {
		return redoSelected;
	}
	
	public synchronized void toggleRedoSelected() {
		if ( redoSelected = !redoSelected )
			notifyAll();
	}
	
	public synchronized boolean isBezierSelected( ) {
		return bezierSelected;
	}
	
	public synchronized void toggleBezierSelected() {
		if ( bezierSelected = !bezierSelected )
			notifyAll();
	}
	
	public synchronized boolean isHermitesSelected( ) {
		return hermitesSelected;
	}
	
	public synchronized void toggleHermitesSelected() {
		if ( hermitesSelected = !hermitesSelected )
			notifyAll();
	}
}
