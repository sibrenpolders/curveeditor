package CurveEditor.Core;

import java.util.HashMap;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class CurveMap2 {

	public static int DEFAULT_SIZE = 50;

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

		for (int x = 0; x <= Maxx; ++x)
			for (int y = 0; y <= Maxy; ++y)
				regions.put(new Point(x, y), null);

		curves.put(null, null);
	}

	private void addRegion(Curve c, Region r) {
		for (int x = r.minx; x <= r.maxx; ++x)
			for (int y = r.miny; y <= r.maxy; ++y)
				regions.put(new Point(x, y), r);

		curves.put(r, c);
	}

	public void addCurve(Curve c) {
		Point TempMax = new Point(-1,-1), 
			TempMin = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		for (int i = 0; i < c.getOutput().size(); ++i)
			if(c.getOutput().get(i).X)
			
	}

	public Curve searchCurve(Point p) {
		return curves.get(regions.get(p));
	}
}