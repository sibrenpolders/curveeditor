package CurveEditor;

public class Main {

	public static void main(String[] args) {
		CurveEditor.GUI.GUI g = new CurveEditor.GUI.GUI();
		(new Thread(g)).start();
	}
}
