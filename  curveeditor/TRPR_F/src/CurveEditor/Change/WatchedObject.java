package CurveEditor.Change;

public interface WatchedObject {

	public void addChangeListener(ChangeListener c);

	public void notifyListeners();
}
