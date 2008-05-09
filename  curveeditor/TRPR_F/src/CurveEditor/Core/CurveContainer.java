package CurveEditor.Core;

import java.util.Vector;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class CurveContainer {

	private static short SEARCH_RANGE = 3;
	// houdt bij of een x,y-punt op een curve ligt
	private Curve[][] curves;
	private Curve[][] controlPoints;
	public int maxX, maxY;

	public CurveContainer(int Maxx, int Maxy) {
		this.maxX = Maxx;
		this.maxY = Maxy;

		curves = new Curve[Maxx][Maxy];
		controlPoints = new Curve[Maxx][Maxy];

		for (int x = 0; x < Maxx; ++x)
			for (int y = 0; y < Maxy; ++y) {
				curves[x][y] = null;
				controlPoints[x][y] = null;
			}
	}

	public void resize(int MaxX, int MaxY) {
		Curve[][] tmp = curves;
		Curve[][] tmp2 = controlPoints;

		curves = new Curve[MaxX][MaxY];
		controlPoints = new Curve[MaxX][MaxY];

		int minX = (this.maxX < MaxX) ? this.maxX : MaxX;
		int minY = (this.maxY < MaxY) ? this.maxY : MaxY;

		int x = 0, y = 0;
		for (; x < minX; ++x)
			for (y = 0; y < minY; ++y) {
				curves[x][y] = tmp[x][y];
				controlPoints[x][y] = tmp2[x][y];
			}
		for (x = 0; x < MaxX; ++x)
			for (y = 0; y < MaxY; ++y) {
				if (y >= minY || x >= minX) {
					curves[x][y] = null;
					controlPoints[x][y] = null;
				}
			}

		this.maxX = MaxX;
		this.maxY = MaxY;
	}

	public final void reset(int Maxx, int Maxy) {
		Curve[][] prevCurves = curves;
		Curve[][] prevControlPoints = controlPoints;

		curves = new Curve[Maxx][Maxy];
		controlPoints = new Curve[Maxx][Maxy];

		int max_x = (Maxx > maxX) ? maxX : Maxx;
		int max_y = (Maxy > maxY) ? maxY : Maxy;

		for (int x = 0; x < Maxx; ++x)
			for (int y = 0; y < Maxy; ++y) {
				curves[x][y] = null;
				controlPoints[x][y] = null;
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
		controlPoints = new Curve[maxX][maxY];

		for (int x = 0; x < maxX; ++x)
			for (int y = 0; y < maxY; ++y) {
				curves[x][y] = null;
				controlPoints[x][y] = null;
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
			if (isValidPoint(c.getInput().elementAt(i))) {
				if (controlPoints[c.getInput().elementAt(i).X()][c.getInput()
						.elementAt(i).Y()] == null)
					controlPoints[c.getInput().elementAt(i).X()][c.getInput()
							.elementAt(i).Y()] = new Curve();

				controlPoints[c.getInput().elementAt(i).X()][c.getInput()
						.elementAt(i).Y()] = c;
			}

		}
	}

	private void deleteControlPoints(Curve c) {
		for (int i = 0; i < c.getNbInputPoints(); ++i) {
			if (isValidPoint(c.getInput().elementAt(i)))
				if (controlPoints[c.getInput().elementAt(i).X()][c.getInput()
						.elementAt(i).Y()] != null) {
					controlPoints[c.getInput().elementAt(i).X()][c.getInput().elementAt(i).Y()] = null;
				}
		}
	}

	public Curve searchCurvesByControlPoint(Point p, short range) {
		return searchCurvesByControlPoint_(p, range);

	}

	public Curve searchCurvesByControlPoint(Point p) {
		return searchCurvesByControlPoint_(p, SEARCH_RANGE);
	}

	private Curve searchCurvesByControlPoint_(Point p, short range) {
		if (isValidPoint(p)) {
			int max_x = p.X() + range < maxX ? p.X() + range : maxX - 1;
			int min_x = p.X() - range >= 0 ? p.X() - range : 0;
			int max_y = p.Y() + range < maxY ? p.Y() + range : maxY - 1;
			int min_y = p.Y() - range >= 0 ? p.Y() - range : 0;

			for (int i = min_x; i <= max_x; ++i)
				for (int j = min_y; j <= max_y; ++j)
					if (controlPoints[i][j] != null)
						return controlPoints[i][j];
		}
		
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
					if (controlPoints[i][j] != null) {
						int k;
						if ( ( k = controlPoints[i][j].containsInputPointi( new Point( i, j )) ) != -1 )						
							result.add( controlPoints[i][j].getInput().get(k));
					}
			if (result.size() > 0)
				return result;
			else
				return null;
		} else
			return null;
	}

	public Point isControlPoint(Point p) {
		if (isValidPoint(p)) {
			if ( controlPoints[p.X()][p.Y()] != null ) {
				Vector<Point> input = controlPoints[p.X()][p.Y()].getInput();
				for ( int i = 0; i < input.size(); ++i )
					if ( p.equals( input.get(i)) )
						return input.get(i);
			}		
		}
		return null;
	}

	public void deleteControlPoint(int x, int y) {
		controlPoints[x][y] = null;
	}
}
