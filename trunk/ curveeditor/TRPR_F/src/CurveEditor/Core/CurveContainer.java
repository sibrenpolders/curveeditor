package CurveEditor.Core;

import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class CurveContainer {

	public static short SEARCH_RANGE = 3;
	// houdt bij of een x,y-punt op een curve ligt
	private Curve[][] curves;
	private Vector<ControlPointNode> controlPoints;
	public int maxX, maxY;

	public CurveContainer(int Maxx, int Maxy) {
		this.maxX = Maxx;
		this.maxY = Maxy;

		curves = new Curve[Maxx][Maxy];
		controlPoints = new Vector<ControlPointNode>();

		for (int x = 0; x < Maxx; ++x)
			for (int y = 0; y < Maxy; ++y)
				curves[x][y] = null;
	}

	public final void reset(int Maxx, int Maxy) {
		this.maxX = Maxx;
		this.maxY = Maxy;

		Curve[][] prevCurves = curves;
		curves = new Curve[Maxx][Maxy];

		for (int x = 0; x < Maxx; ++x)
			for (int y = 0; y < Maxy; ++y)
				curves[x][y] = prevCurves[x][y];
	}

	public void addCurve(Curve c) {
		for (int i = 0; i < c.getOutput().size(); ++i)
			if (isValidPoint(c.getOutput().elementAt(i)))
				curves[c.getOutput().elementAt(i).X()][c.getOutput().elementAt(
						i).Y()] = c;

		addControlPoints(c);
	}

	public void deleteCurve(Curve c) {
		for (int i = 0; i < c.getOutput().size(); ++i)
			if (isValidPoint(c.getOutput().elementAt(i)))
				curves[c.getOutput().elementAt(i).X()][c.getOutput().elementAt(
						i).Y()] = null;

		deleteControlPoints(c);
	}

	private boolean isValidPoint(Point p) {
		return p.X() < maxX && p.Y() < maxY && p.Y() >= 0 && p.X() >= 0;
	}

	public Curve searchCurve(Point p) {
		if (isValidPoint(p)) {
			int max_x = p.X() + SEARCH_RANGE < maxX ? p.X() + SEARCH_RANGE
					: maxX - 1;
			int min_x = p.X() - SEARCH_RANGE >= 0 ? p.X() - SEARCH_RANGE : 0;
			int max_y = p.Y() + SEARCH_RANGE < maxY ? p.Y() + SEARCH_RANGE
					: maxY - 1;
			int min_y = p.Y() - SEARCH_RANGE >= 0 ? p.Y() - SEARCH_RANGE : 0;

			for (int i = min_x; i <= max_x; ++i)
				for (int j = min_y; j <= max_y; ++j)
					if (curves[i][j] != null)
						return curves[i][j];
		}

		return null;
	}

	private final class ControlPointNode {
		ControlPointNode next;
		int x, y;
		Vector<Curve> curves;

		ControlPointNode(Point p, Curve c) {
			curves = new Vector<Curve>();
			curves.add(c);
			x = p.X();
			y = p.Y();
			next = null;
		}

		public boolean equals(Object obj) {
			if (obj instanceof Point) {
				return (Math.abs(x - ((Point) obj).X()) <= SEARCH_RANGE && Math
						.abs(y - ((Point) obj).Y()) <= SEARCH_RANGE);
			} else if (obj instanceof ControlPointNode) {
				return (Math.abs(x - ((ControlPointNode) obj).x) <= SEARCH_RANGE && Math
						.abs(y - ((ControlPointNode) obj).y) <= SEARCH_RANGE);
			} else
				return false;
		}

		public boolean containsCurve(Curve c) {
			for (int i = 0; i < curves.size(); ++i)
				if (curves.elementAt(i).equals(c))
					return true;
			return false;
		}

		public int getNbCurves() {
			return curves.size();
		}

		public void deleteCurve(Curve c) {
			for (int i = 0; i < curves.size(); ++i)
				if (curves.elementAt(i).equals(c))
					curves.remove(i--);
		}

		public void addCurve(Curve c) {
			curves.add(c);
		}
	}

	private void addControlPoints(Curve c) {
		Point temp;

		for (int i = 0; i < c.getNbInputPoints(); ++i) {
			temp = c.getInput().elementAt(i);
			addControlPoint(temp, c);
		}
	}

	private void addControlPoint(Point p, Curve c) {
		boolean added = false;

		for (int i = 0; i < controlPoints.size() && !added; ++i) {
			if (controlPoints.elementAt(i).equals(p)) {
				controlPoints.elementAt(i).addCurve(c);
				added = true;
			}
		}

		if (!added)
			controlPoints.add(new ControlPointNode(p, c));
	}

	private void deleteControlPoints(Curve c) {
		Point temp;

		for (int i = 0; i < c.getNbInputPoints(); ++i) {
			temp = c.getInput().elementAt(i);
			deleteControlPoint(temp, c);
		}
	}

	private void deleteControlPoint(Point p, Curve c) {
		for (int i = 0; i < controlPoints.size(); ++i) {
			if (controlPoints.elementAt(i).equals(p)) {
				controlPoints.elementAt(i).deleteCurve(c);
				if (controlPoints.elementAt(i).getNbCurves() == 0)
					controlPoints.remove(i--);
			}
		}
	}

	public Vector<Curve> findCurvesForControlPoint(int x, int y,
			short searchRange) {
		Vector<Curve> result = null;

		if (isValidPoint(new Point(x, y))) {

			result = new Vector<Curve>();
			ControlPointNode temp;

			for (int i = 0; i < controlPoints.size(); ++i) {
				temp = controlPoints.elementAt(i);
				if (Math.abs(temp.x - x) <= searchRange
						&& Math.abs(temp.y - y) <= searchRange) {
					for (int j = 0; j < temp.curves.size(); ++j)
						result.add(temp.curves.elementAt(j));
				}
			}
		}

		return result;
	}

	public Vector<Point> findPointsForControlPoint(int x, int y,
			short searchRange) {
		Vector<Point> result = null;

		if (isValidPoint(new Point(x, y))) {

			result = new Vector<Point>();
			ControlPointNode temp;

			for (int i = 0; i < controlPoints.size(); ++i) {
				temp = controlPoints.elementAt(i);
				if (Math.abs(temp.x - x) <= searchRange
						&& Math.abs(temp.y - y) <= searchRange)
					result.add(new Point(temp.x, temp.y));

			}
		}

		return result;
	}
}
