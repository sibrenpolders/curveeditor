package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class HermiteCatmullRom extends HermiteCardinal {
	private final double d = 0.5;
	
	public HermiteCatmullRom(char type, short degree) {
		super(type, degree);
		// TODO Auto-generated constructor stub
	}
	
	public void calculateCurve(Curve cv) {
		cardinal(cv, (float).5);
	}
}
