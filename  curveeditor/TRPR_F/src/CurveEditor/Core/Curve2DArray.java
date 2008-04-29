package CurveEditor.Core;

import java.util.HashMap;
import java.util.Vector;

import CurveEditor.Core.CurveHashMap.Region;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Curve2DArray {

	// houdt bij of een x,y-punt op een curve ligt
	private Curve[][] curves;
	private int maxX, maxY;

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

		for (int x = 0; x <= Maxx; ++x)
			for (int y = 0; y <= Maxy; ++y)
				curves[x][y] = prevCurves[x][y];
	}

	public void addCurve(Curve c) {
		for (int i = 0; i < c.getOutput().size(); ++i)
			if (c.getOutput().elementAt(i).X() < maxX
					&& c.getOutput().elementAt(i).Y() < maxY)
				curves[c.getOutput().elementAt(i).X()][c.getOutput().elementAt(
						i).Y()] = c;
	}

	public void deleteCurve(Curve c) {
		for (int i = 0; i < c.getOutput().size(); ++i)
			if (c.getOutput().elementAt(i).X() < maxX
					&& c.getOutput().elementAt(i).Y() < maxY)
				curves[c.getOutput().elementAt(i).X()][c.getOutput().elementAt(
						i).Y()] = null;
	}

	public Curve searchCurve(Point p) {
		for (int i = 0; i <= 3; ++i)
			for (int j = 0; j <= 3; ++j)
				if (curves[p.X() + i][p.Y() + j] != null)
					return curves[p.X() + i][p.Y() + j];
				else if (curves[p.X() - i][p.Y() + j] != null)
					return curves[p.X() - i][p.Y() + j];
				else if (curves[p.X() - i][p.Y() - j] != null)
					return curves[p.X() - i][p.Y() - j];
				else if (curves[p.X() - i][p.Y() + j] != null)
					return curves[p.X() - i][p.Y() + j];

		return null;
	}
}
