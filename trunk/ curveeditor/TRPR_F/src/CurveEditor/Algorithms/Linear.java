package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

//Lineair --> tussen elk paar punten een lijnstuk tekenen
public final class Linear extends Algorithm {

	public Linear(short degree) {
		super('L', (short) 1);
	}

	public Linear() {
		super('L', (short) 1);
	}

	public void calculate(Vector<Point> input, Vector<Point> output) {
		output.clear();

		for (int i = 0; i < input.size() - 1; ++i)
			interpolate(input.elementAt(i), input.elementAt(i + 1), output);
	}

	// output wordt gevuld met interpolatiepunten tussen start en end
	public void interpolate(Point start, Point end, Vector<Point> output) {
		int x0 = start.X();
		int y0 = start.Y();
		int x1 = end.X();
		int y1 = end.Y();
		boolean vertical = false;

		double dy = 0.0, dx = 0.0, m = 0.0;

		if (x0 < x1) {
			dy = y1 - y0;
			dx = x1 - x0;
			m = dy / dx;
		} else if (x0 > x1) {
			dy = y0 - y1;
			dx = x0 - x1;
			m = dy / dx;
		} else
			vertical = true;

		// voor elke x-waarde, de y-waarde bepalen
		if (m >= -1 && m <= 1 && !vertical) {
			double y;

			if (x0 <= x1) {
				y = y0;
				for (int x = x0; x <= x1; ++x) {
					output.add(new Point(x, (int) Math.floor(y + 0.5)));
					y += m;
				}
			} else {
				y = y1;
				for (int x = x1; x <= x0; ++x) {
					output.add(new Point(x, (int) Math.floor(y + 0.5)));
					y += m;
				}
			}
		}

		// voor elke y-waarde, de x-waarde bepalen
		else if (!vertical) {
			double x, m2 = dx / dy;

			if (y0 <= y1) {
				x = x0;
				for (int y = y0; y <= y1; ++y) {
					output.add((new Point((int) Math.floor(x + 0.5), y)));
					x += m2;
				}
			} else {
				x = x1;
				for (int y = y1; y <= y0; ++y) {
					output.add(new Point((int) Math.floor(x + 0.5), y));
					x += m2;
				}

			}
		}

		// verticale (m kan dan niet berekend worden)
		else {
			if (y0 <= y1) {
				for (int y = y0; y <= y1; ++y) {
					output.add(new Point(x0, y));
				}
			} else {
				for (int y = y1; y <= y0; ++y) {
					output.add(new Point(x0, y));
				}

			}
		}
	}

	public void calculateComplete(Curve c) {
		calculate(c);
	}
}