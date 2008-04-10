package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class BezierUnlimited extends Algorithm {

	private int steps;

	// orde = onbepaald --> alle controlepunten worden tegelijkertijd gebruikt
	public BezierUnlimited(short degree) {
		super('B', (short) 0);
		steps = 1000;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	private Point linIntPol(Point a, Point b, float t) {
		return new Point((int) Math.floor(a.X() + (b.X() - a.X()) * t + 0.5),
				(int) Math.floor(a.Y() + (b.Y() - a.Y()) * t + 0.5));
	}

	private Point bezier(Vector<Point> input, float t) {
		Vector<Point> prev = input;
		Vector<Point> temp = input;
		
		while(temp.size() > 1)
		{
			temp = new Vector<Point>();
			
			for(int i = 0; i < prev.size() - 1; ++i)
				temp.add(linIntPol(prev.elementAt(i), prev.elementAt(i + 1), t));
			
			prev = temp;
		}

		return temp.get(0);
	}

	public void calculateCurve(Curve c) {
		Vector<Point> input = c.getInput();
		Vector<Point> output = c.getOutput();
		output.clear();

		for (int j = 0; j < steps; ++j) {
			float t = (float) (j / (steps - 1.0));
			output.add(bezier(input, t));
		}
	}
}