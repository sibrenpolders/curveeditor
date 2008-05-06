package CurveEditor.Core;

import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class CurveContainer {

	private static short SEARCH_RANGE = 3;
	// houdt bij of een x,y-punt op een curve ligt
	private Curve[][] curves;
	private Vector<Curve>[][] controlPoints;
	public int maxX, maxY;

	public CurveContainer(int Maxx, int Maxy) {
		this.maxX = Maxx;
		this.maxY = Maxy;

		curves = new Curve[Maxx][Maxy];
		controlPoints = new Vector[Maxx][Maxy];

		for (int x = 0; x < Maxx; ++x)
			for (int y = 0; y < Maxy; ++y) {
				curves[x][y] = null;
				controlPoints[x][y] = new Vector<Curve>();
			}
	}

	public void resize( int MaxX, int MaxY ) {
		Curve[][] tmp = curves;
		Vector<Curve>[][] tmp2 = controlPoints;
		
		curves = new Curve[MaxX][MaxY];
		controlPoints = new Vector[MaxX][MaxY];
		
		int minX = ( this.maxX < MaxX )? this.maxX : MaxX;
		int minY = ( this.maxY < MaxY )? this.maxY : MaxY;
		
		int x = 0, y = 0;
		for ( ; x < minX; ++x )
			for ( y = 0; y < minY; ++y) {
				curves[x][y] = tmp[x][y];
				controlPoints[x][y] = tmp2[x][y];
			}
		for ( ; x < MaxX; ++x )
			for ( y = 0; y < MaxY; ++y ) {
				curves[x][y] = null;
				controlPoints[x][y] = new Vector<Curve>();
			}
		
		this.maxX = MaxX;
		this.maxY = MaxY;
	}
	
	public final void reset(int Maxx, int Maxy) {
		Curve[][] prevCurves = curves;
		Vector<Curve>[][] prevControlPoints = controlPoints;

		curves = new Curve[Maxx][Maxy];
		controlPoints = new Vector[Maxx][Maxy];

		int max_x = (Maxx > maxX) ? maxX : Maxx;
		int max_y = (Maxy > maxY) ? maxY : Maxy;

		for (int x = 0; x < Maxx; ++x)
			for (int y = 0; y < Maxy; ++y) {
				curves[x][y] = null;
				controlPoints[x][y] = new Vector<Curve>();
			}

		for (int x = 0; x < max_x; ++x)
			for (int y = 0; y < max_y; ++y) {
				curves[x][y] = prevCurves[x][y];
				controlPoints[x][y] = prevControlPoints[x][y];
			}

		this.maxX = Maxx;
		this.maxY = Maxy;
	}

	public final void clear() {
		curves = new Curve[maxX][maxY];
		controlPoints = new Vector[maxX][maxY];

		for (int x = 0; x < maxX; ++x)
			for (int y = 0; y < maxY; ++y) {
				curves[x][y] = null;
				controlPoints[x][y] = new Vector<Curve>();
			}
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
		return searchCurve_(p, SEARCH_RANGE);
	}

	public Curve searchCurve(Point p, short range) {
		return searchCurve_(p, range);
	}

	private Curve searchCurve_(Point p, short range) {
		if (isValidPoint(p)) {
			int max_x = p.X() + range < maxX ? p.X() + range : maxX - 1;
			int min_x = p.X() - range >= 0 ? p.X() - range : 0;
			int max_y = p.Y() + range < maxY ? p.Y() + range : maxY - 1;
			int min_y = p.Y() - range >= 0 ? p.Y() - range : 0;

			for (int i = min_x; i <= max_x; ++i)
				for (int j = min_y; j <= max_y; ++j)
					if (curves[i][j] != null)
						return curves[i][j];
		}

		return null;
	}

	private void addControlPoints(Curve c) {
		for (int i = 0; i < c.getNbInputPoints(); ++i) {
			if (isValidPoint(c.getInput().elementAt(i)))
				controlPoints[c.getInput().elementAt(i).X()][c.getInput()
						.elementAt(i).Y()].add(c);

		}
	}

	private void deleteControlPoints(Curve c) {
		for (int i = 0; i < c.getNbInputPoints(); ++i) {
			if (isValidPoint(c.getInput().elementAt(i)))
				while (controlPoints[c.getInput().elementAt(i).X()][c
						.getInput().elementAt(i).Y()].removeElement(c))
					;

		}
	}

	public Vector<Curve> searchCurvesByControlPoint(Point p, short range) {
		return searchCurvesByControlPoint_(p, range);

	}

	public Vector<Curve> searchCurvesByControlPoint(Point p) {
		return searchCurvesByControlPoint_(p, SEARCH_RANGE);
	}

	private Vector<Curve> searchCurvesByControlPoint_(Point p, short range) {
		if (isValidPoint(p)) {
			Vector<Curve> result = new Vector<Curve>();

			int max_x = p.X() + range < maxX ? p.X() + range : maxX - 1;
			int min_x = p.X() - range >= 0 ? p.X() - range : 0;
			int max_y = p.Y() + range < maxY ? p.Y() + range : maxY - 1;
			int min_y = p.Y() - range >= 0 ? p.Y() - range : 0;

			for (int i = min_x; i <= max_x; ++i)
				for (int j = min_y; j <= max_y; ++j)
					if (controlPoints[i][j].size() > 0)
						result.addAll(controlPoints[i][j]);
			if (result.size() > 0)
				return result;
			else
				return null;
		} else
			return null;
	}

	public Vector<Point> searchControlPoint(Point p, short range) {
		return searchControlPoint_(p, range);

	}

	public Vector<Point> searchControlPoint(Point p) {
		return searchControlPoint_(p, SEARCH_RANGE);
	}

	private Vector<Point> searchControlPoint_(Point p, short range) {
		if (isValidPoint(p)) {
			Vector<Point> result = new Vector<Point>();

			int max_x = p.X() + range < maxX ? p.X() + range : maxX - 1;
			int min_x = p.X() - range >= 0 ? p.X() - range : 0;
			int max_y = p.Y() + range < maxY ? p.Y() + range : maxY - 1;
			int min_y = p.Y() - range >= 0 ? p.Y() - range : 0;

			for (int i = min_x; i <= max_x; ++i)
				for (int j = min_y; j <= max_y; ++j)
					if (controlPoints[i][j].size() > 0)
						result.add(new Point(i, j));
			if (result.size() > 0)
				return result;
			else
				return null;
		} else
			return null;
	}

	public boolean isControlPoint(Point p) {
		if (isValidPoint(p))
			return controlPoints[p.X()][p.Y()].size() > 0;
		else
			return false;
	}
}
