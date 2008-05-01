package CurveEditor.Core;

import java.util.HashMap;
import java.util.Vector;

import CurveEditor.Core.CurveHashMap.Region;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Curve2DArray {

	// houdt bij of een x,y-punt op een curve ligt
	private Curve[][] curves;
	public int maxX, maxY;

	public Curve2DArray(int Maxx, int Maxy) {
		this.maxX = Maxx;
		this.maxY = Maxy;

		curves = new Curve[Maxx][Maxy];

		for (int x = 0; x < Maxx; ++x)
			for (int y = 0; y < Maxy; ++y)
				curves[x][y] = null;
	}

	public void reset(int Maxx, int Maxy) {
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
			if (c.getOutput().elementAt(i).X() < maxX
					&& c.getOutput().elementAt(i).Y() < maxY
					&& c.getOutput().elementAt(i).Y() >= 0
					&& c.getOutput().elementAt(i).X() >= 0)
				curves[c.getOutput().elementAt(i).X()][c.getOutput().elementAt(
						i).Y()] = c;
	}

	public void deleteCurve(Curve c) {
		for (int i = 0; i < c.getOutput().size(); ++i)
			if (c.getOutput().elementAt(i).X() < maxX
					&& c.getOutput().elementAt(i).Y() < maxY
					&& c.getOutput().elementAt(i).Y() >= 0
					&& c.getOutput().elementAt(i).X() >= 0)
				curves[c.getOutput().elementAt(i).X()][c.getOutput().elementAt(
						i).Y()] = null;
	}

	public Curve searchCurve(Point p) {
		int max_x = p.X() + 3 < maxX ? p.X() + 3 : maxX;
		int min_x = p.X() - 3 >= 0 ? p.X() - 3 : 0;
		int max_y = p.Y() + 3 < maxY ? p.Y() + 3 : maxY;
		int min_y = p.Y() - 3 >= 0 ? p.Y() - 3 : 0;

		for (int i = min_x; i <= max_x; ++i)
			for (int j = min_y; j <= max_y; ++j)
				if (curves[i][j] != null)
					return curves[i][j];

		return null;
	}
}
