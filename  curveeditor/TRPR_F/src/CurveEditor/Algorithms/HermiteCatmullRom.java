package CurveEditor.Algorithms;
import CurveEditor.Curves.Curve;

public class HermiteCatmullRom extends HermiteCardinal {
	private final double d = 0.5;

	public HermiteCatmullRom(char type, short degree) {
		super(type, degree);
		// TODO Auto-generated constructor stub
	}

	public void calculate(Curve cv) {
		cardinal(cv, (float) .5);
	}
}
