package CurveEditor.Core;

import java.util.HashMap;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class CurveMap {
	private static int DEFAULT_RANGE = 2;

	private HashMap<Point, Curve> hashOutput;
	private HashMap<Point, Curve> hashControlPoint;
	private int range;// de straal die bekeken wordt rond een gegeven punt

	public CurveMap(int range) {
		hashOutput = new HashMap<Point, Curve>();
		hashControlPoint = new HashMap<Point, Curve>();
		this.range = range;
	}

	public CurveMap() {
		hashOutput = new HashMap<Point, Curve>();
		hashControlPoint = new HashMap<Point, Curve>();
		this.range = DEFAULT_RANGE;
	}

	public void addCurve(Curve c) {
		for (int i = 0; i < c.getNbInputPoints(); ++i)
			// oude waarde wordt vervangen
			hashControlPoint.put(c.getInput().elementAt(i), c);

		for (int i = 0; i < c.getOutput().size(); ++i)
			// oude waarde wordt vervangen
			hashOutput.put(c.getOutput().elementAt(i), c);
	}

	public class CurveAndPointContainer {
		public Curve c;
		public Point p;

		public CurveAndPointContainer(Curve c, Point p) {
			this.c = c;
			this.p = p;
		}
	}

	public CurveAndPointContainer searchCurveByControlPoint(Point p) {
		Curve result = null;
		Point resultP = null;

		for (int x = p.X() - range; x <= p.X() + range && result != null; ++x)
			for (int y = p.Y() - range; y <= p.Y() + range && result != null; ++y)
				result = hashControlPoint.get(resultP = new Point(x, y));

		return new CurveAndPointContainer(result, resultP); // null indien niet
															// gevonden
	}

	public CurveAndPointContainer searchCurveByCurvePoint(Point p) {
		Curve result = null;

		for (int x = p.X() - range; x <= p.X() + range && result != null; ++x)
			for (int y = p.Y() - range; y <= p.Y() + range && result != null; ++y)
				result = hashOutput.get(new Point(x, y));

		return new CurveAndPointContainer(result, null); // null indien niet gevonden
	}
}
