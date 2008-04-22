package CurveEditor.Core;

import java.util.Currency;
import java.util.HashMap;
import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class CurveMap2 {

	public static int DEFAULT_SIZE = 50;
	private boolean[][] onCurve;

	class Region {
		private int minx, maxx, miny, maxy;

		public Region(int minx, int maxx, int miny, int maxy) {
			this.minx = minx;
			this.maxx = maxx;
			this.miny = miny;
			this.maxy = maxy;
		}

		public Region(Point centrum) {
			this.minx = centrum.X() - DEFAULT_SIZE / 2;
			this.maxx = centrum.X() + DEFAULT_SIZE / 2;
			this.miny = centrum.Y() - DEFAULT_SIZE / 2;
			this.maxy = centrum.Y() + DEFAULT_SIZE / 2;
		}

		public boolean contains(Point p) {
			return p.X() >= minx && p.X() <= maxx && p.Y() >= miny
					&& p.Y() <= maxy;
		}

		public String toString() {
			return "MinX: " + minx + ", MinY: " + miny + ", MaxX: " + maxx
					+ ", MaxY: " + maxy;
		}

		public boolean equals(Object obj) {
			if (obj instanceof Region) {
				Region p = (Region) obj;
				return (this.minx == p.minx && this.miny == p.miny
						&& this.maxx == p.maxx && this.maxy == p.maxy);
			} else {
				return false;
			}
		}

		public int hashCode() {
			return toString().hashCode();
		}
	}

	private HashMap<Point, Region> regions;
	private HashMap<Region, Curve> curves;

	public CurveMap2(int Maxx, int Maxy) {
		regions = new HashMap<Point, Region>();
		curves = new HashMap<Region, Curve>();
		onCurve = new boolean[Maxx][Maxy];

		for (int x = 0; x <= Maxx; ++x)
			for (int y = 0; y <= Maxy; ++y) {
				regions.put(new Point(x, y), null);
				onCurve[x][y] = false;
			}

		curves.put(null, null);
	}

	private void addRegion(Curve c, Region r) {
		for (int x = r.minx; x <= r.maxx; ++x)
			for (int y = r.miny; y <= r.maxy; ++y)
				regions.put(new Point(x, y), r);

		curves.put(r, c);
	}

	public void addCurve(Curve c) {
		Region last = null;
		Point currentPoint = null;

		for (int i = 0; i < c.getOutput().size(); ++i) {
			currentPoint = c.getOutput().get(i);
			if (last != null && !last.contains(currentPoint)) {
				last = findRegion(last, c.getOutput(), i, currentPoint);
				addRegion(c, last);
			}
			onCurve[currentPoint.X()][currentPoint.Y()] = true;
		}
	}

	private Region findRegion(Region r, Vector<Point> v, int index, Point p) {

	}

	private int[] findNearestLeftPoint(int xMin, Point p) {
		int[] coords = new int[2];
		coords[0] = -1;
		coords[1] = -1;
		boolean found = false;

		for (int i = p.X() - 1; i >= xMin && !found; --i) {
			for (int crement = 1; crement <= 2 * DEFAULT_SIZE && !found; ++crement)
				if (onCurve[i][p.Y() + crement]) {
					coords[0] = i;
					coords[1] = p.Y() + crement;
					found = true;
				} else if (onCurve[i][p.Y() - crement]) {
					coords[0] = i;
					coords[1] = p.Y() - crement;
					found = true;
				}
		}
		return coords;
	}

	private int[] findNearestRightPoint(int xMax, Point p) {
		int[] coords = new int[2];
		coords[0] = -1;
		coords[1] = -1;
		boolean found = false;

		for (int i = p.X() + 1; i <= xMax && !found; ++i) {
			for (int crement = 1; crement <= 2 * DEFAULT_SIZE && !found; ++crement)
				if (onCurve[i][p.Y() + crement]) {
					coords[0] = i;
					coords[1] = p.Y() + crement;
					found = true;
				} else if (onCurve[i][p.Y() - crement]) {
					coords[0] = i;
					coords[1] = p.Y() - crement;
					found = true;
				}
		}
		return coords;
	}

	private int[] findNearestTopPoint(int yMax, Point p) {
		int[] coords = new int[2];
		coords[0] = -1;
		coords[1] = -1;
		boolean found = false;

		for (int i = p.Y() + 1; i <= yMax && !found; ++i) {
			for (int crement = 1; crement <= 2 * DEFAULT_SIZE && !found; ++crement)
				if (onCurve[p.X() + crement][i]) {
					coords[0] = p.X() + crement;
					coords[1] = i;
					found = true;
				} else if (onCurve[p.X() - crement][i]) {
					coords[0] = p.X() - crement;
					coords[1] = i;
					found = true;
				}
		}
		return coords;
	}

	private int[] findNearestBottomPoint(int yMin, Point p) {
		int[] coords = new int[2];
		coords[0] = -1;
		coords[1] = -1;
		boolean found = false;

		for (int i = p.Y() - 1; i >= yMin && !found; --i) {
			for (int crement = 1; crement <= 2 * DEFAULT_SIZE && !found; ++crement)
				if (onCurve[p.X() + crement][i]) {
					coords[0] = p.X() + crement;
					coords[1] = i;
					found = true;
				} else if (onCurve[p.X() - crement][i]) {
					coords[0] = p.X() - crement;
					coords[1] = i;
					found = true;
				}
		}
		return coords;
	}

	public Curve searchCurve(Point p) {
		return curves.get(regions.get(p));
	}
}