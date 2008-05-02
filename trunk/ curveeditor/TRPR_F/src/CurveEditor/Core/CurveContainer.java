package CurveEditor.Core;

import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class CurveContainer {

	private static short RANGE = 3;
	// houdt bij of een x,y-punt op een curve ligt
	private Curve[][] curves;
	public int maxX, maxY;

	public CurveContainer(int Maxx, int Maxy) {
		this.maxX = Maxx;
		this.maxY = Maxy;

		curves = new Curve[Maxx][Maxy];

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
	}

	public void deleteCurve(Curve c) {
		for (int i = 0; i < c.getOutput().size(); ++i)
			if (isValidPoint(c.getOutput().elementAt(i)))
				curves[c.getOutput().elementAt(i).X()][c.getOutput().elementAt(
						i).Y()] = null;
	}

	private boolean isValidPoint(Point p) {
		return p.X() < maxX && p.Y() < maxY && p.Y() >= 0 && p.X() >= 0;
	}

	public Curve searchCurve(Point p) {
		if (isValidPoint(p)) {
			int max_x = p.X() + RANGE < maxX ? p.X() + RANGE : maxX - 1;
			int min_x = p.X() - RANGE >= 0 ? p.X() - RANGE : 0;
			int max_y = p.Y() + RANGE < maxY ? p.Y() + RANGE : maxY - 1;
			int min_y = p.Y() - RANGE >= 0 ? p.Y() - RANGE : 0;

			for (int i = min_x; i <= max_x; ++i)
				for (int j = min_y; j <= max_y; ++j)
					if (curves[i][j] != null)
						return curves[i][j];
		}

		return null;
	}

	private final class ControlPointNode {
		ControlPointNode next;
		Point p;
		Vector<Curve> curves;

		ControlPointNode(Point p, Curve c) {
			curves.clear();
			curves.add(c);
			this.p = p;
			next = null;
		}

	}
}
