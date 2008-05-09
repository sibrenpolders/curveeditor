package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class Linear extends Algorithm {

	public Linear(char type, short degree) {
		super(type, (short) degree);
	}

	public Linear(short degree) {
		super('L', (short) degree);
	}

	public Linear() {
		super('L', (short) 1);
	}

	private int findYForX(int x, int x0, int y0, int x1, int y1) {
		return (int) ((double) y0 + (x - (double) x0)
				* ((y1 - y0) / ((double) x1 - x0)));
	}

	private int findXForY(int y, int x0, int y0, int x1, int y1) {
		return (int) ((double) x0 + (y - (double) y0)
				* ((x1 - x0) / ((double) y1 - y0)));
	}

	public void calculateCurve(Curve c) {

		calculate( c.getInput(), c.getOutput() );
//		new BezierSmoothing3(150).smoothCurve(c, 0.5, BezierSmoothing3.MODE.INPUT);
	}

	public void calculate(Vector<Point> input, Vector<Point> output) {
		output.clear();

		for (int i = 0; i < input.size() - 1; ++i) {
			int diff1 = (input.get(i).X() < input.get(i + 1).X()) ? input.get(
					i + 1).X()
					- input.get(i).X() : input.get(i).X()
					- input.get(i + 1).X();
			int diff2 = (input.get(i).Y() < input.get(i + 1).Y()) ? input.get(
					i + 1).Y()
					- input.get(i).Y() : input.get(i).Y()
					- input.get(i + 1).Y();

			if (diff1 > diff2) {
				// eerste controlepunt ligt links van het tweede
				if (input.get(i).X() < input.get(i + 1).X()) {
					for (int x = input.get(i).X(); x <= input.get(i + 1).X(); ++x) {
						output.add(new Point(x, findYForX(x, input.get(i).X(),
								input.get(i).Y(), input.get(i + 1).X(), input
										.get(i + 1).Y())));
					}
				}
				// eerste controlepunt ligt rechts van het tweede
				else {
					for (int x = input.get(i).X(); x >= input.get(i + 1).X(); --x) {
						output.add(new Point(x, findYForX(x, input.get(i).X(),
								input.get(i).Y(), input.get(i + 1).X(), input
										.get(i + 1).Y())));
					}
				}
			} else {
				if (input.get(i).Y() < input.get(i + 1).Y()) {
					for (int y = input.get(i).Y(); y <= input.get(i + 1).Y(); ++y) {
						output.add(new Point(findXForY(y, input.get(i).X(),
								input.get(i).Y(), input.get(i + 1).X(), input
										.get(i + 1).Y()), y));
					}
				} else {
					for (int y = input.get(i).Y(); y >= input.get(i + 1).Y(); --y) {
						output.add(new Point(findXForY(y, input.get(i).X(),
								input.get(i).Y(), input.get(i + 1).X(), input
										.get(i + 1).Y()), y));
					}
				}
			}
		}
		
	}

	@Override
	public void calculateComplete(Curve c) {
		calculate(c);
	}
}
//package CurveEditor.Algorithms;
//
//import java.util.Vector;
//
//import CurveEditor.Curves.Curve;
//import CurveEditor.Curves.Point;
//
////Lineair --> tussen elk paar punten een lijnstuk tekenen
//public final class Linear extends Algorithm {
//
//	public Linear(char type, short degree) {
//		super(type, (short) degree);
//	}
//
//	public Linear(short degree) {
//		super('L', (short) degree);
//	}
//
//	public Linear() {
//		super('L', (short) 1);
//	}
//
//	public void calculate(Vector<Point> input, Vector<Point> output) {
//		output.clear();
//
//		for (int i = 0; i < input.size() - 1; ++i) {
//			interpolate(input.get(i), input.get(i + 1), output);
//		}
//	}
//
//	// output wordt gevuld met interpolatiepunten tussen start en end
//	public void interpolate(Point start, Point end, Vector<Point> output) {
//		int x0 = start.X();
//		int y0 = start.Y();
//		int x1 = end.X();
//		int y1 = end.Y();
////		boolean vertical = false;
////
////		double dy = 0.0, dx = 0.0, m = 0.0;
////
////		if (x0 < x1) {
////			dy = y1 - y0;
////			dx = x1 - x0;
////			m = dy / dx;
////		} else if (x0 > x1) {
////			dy = y0 - y1;
////			dx = x0 - x1;
////			m = dy / dx;
////		} else
////			vertical = true;
////
////		// voor elke x-waarde, de y-waarde bepalen
////		if (m >= -1 && m <= 1 && !vertical) {
////			double y;
////
////			if (x0 <= x1) {
////				y = y0;
////				for (int x = x0; x <= x1; ++x) {
////					output.add(new Point(x, (int) Math.floor(y + 0.5)));
////					y += m;
////				}
////			} else {
////				y = y1;
////				for (int x = x1; x <= x0; ++x) {
////					output.add(new Point(x, (int) Math.floor(y + 0.5)));
////					y += m;
////				}
////			}
////		}
////
////		// voor elke y-waarde, de x-waarde bepalen
////		else if (!vertical) {
////			double x, m2 = dx / dy;
////
////			if (y0 <= y1) {
////				x = x0;
////				for (int y = y0; y <= y1; ++y) {
////					output.add((new Point((int) Math.floor(x + 0.5), y)));
////					x += m2;
////				}
////			} else {
////				x = x1;
////				for (int y = y1; y <= y0; ++y) {
////					output.add(new Point((int) Math.floor(x + 0.5), y));
////					x += m2;
////				}
////
////			}
////		}
////
////		// verticale (m kan dan niet berekend worden)
////		else {
////			if (y0 <= y1) {
////				for (int y = y0; y <= y1; ++y) {
////					output.add(new Point(x0, y));
////				}
////			} else {
////				for (int y = y1; y <= y0; ++y) {
////					output.add(new Point(x0, y));
////				}
////
////			}
////		}
//		
//		boolean steep = false;
//		if ( Math.abs( x1 - x0 ) < Math.abs( y1 - y0 ))
//		{
//			int hulp = x0;
//			x0 = y0;
//			y0 = hulp;
//			hulp = x1;
//			x1 = y1;
//			y1 = hulp;
//			steep = true;
//		}
//
//		if ( x1 < x0 )
//		{
//			int hulp = x0;
//			x0 = x1;
//			x1 = hulp;
//			hulp = y0;
//			y0 = y1;
//			y1 = hulp;
//		}
//
//		int dx = x1 - x0;
//		int dy = y1 - y0;
//
//		float m;
//
//		if ( dx == 0 )
//			m = 0;
//		else
//			m = (float)dy / dx;
//
//		float y = y0;
//		int x = x0;
//
//		for (; x <= x1; ++x )
//		{
//			if ( !steep )
//				output.add((new Point(x, (int) Math.floor(y + 0.5))));
//			else
//				output.add((new Point((int) Math.floor(y + 0.5), x)));
//			y += m;
//		}
//	}	
////	}
//
//	public void calculateComplete(Curve c) {
//		calculate(c);
//	}
//}