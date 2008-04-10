package CurveEditor.Algorithms;

import java.util.Vector;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class BezierSmoothing3 {

	public static enum MODE {
		INPUT, OUTPUT
	};

	private int NUM_STEPS;

	public BezierSmoothing3(int steps) {
		this.NUM_STEPS = steps;
	}

	public void smoothCurve(Curve c, double smoothFactor, MODE m) {
		Vector<Point> prevOutput;
		Vector<Point> out = new Vector<Point>();

		if (m == MODE.OUTPUT)
			prevOutput = c.getOutput();
		else
			prevOutput = c.getInput();

		for (int i = 1; i < (prevOutput.size() - 2); ++i) {
			Point[] ControlPoints = findControlPoints(prevOutput.get(i - 1),
					prevOutput.get(i), prevOutput.get(i + 1), prevOutput
							.get(i + 2), smoothFactor);

			interpolate(out, prevOutput.get(i).X(), prevOutput.get(i).Y(),
					ControlPoints[0].X(), ControlPoints[0].Y(),
					ControlPoints[1].X(), ControlPoints[1].Y(), prevOutput.get(
							i + 1).X(), prevOutput.get(i + 1).Y());
		}

		c.getOutput().clear();
		c.getOutput().addAll(0, out);
	}

	private Point[] findControlPoints(Point a, Point b, Point c, Point d,
			double smoothFactor) {
		int x0 = a.X();
		int x1 = b.X();
		int x2 = c.X();
		int x3 = d.X();
		int y0 = a.Y();
		int y1 = b.Y();
		int y2 = c.Y();
		int y3 = d.Y();

		double xc1 = (x0 + x1) / 2.0;
		double yc1 = (y0 + y1) / 2.0;
		double xc2 = (x1 + x2) / 2.0;
		double yc2 = (y1 + y2) / 2.0;
		double xc3 = (x2 + x3) / 2.0;
		double yc3 = (y2 + y3) / 2.0;

		double len1 = Math.sqrt((x1 - x0) * (x1 - x0) + (y1 - y0) * (y1 - y0));
		double len2 = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		double len3 = Math.sqrt((x3 - x2) * (x3 - x2) + (y3 - y2) * (y3 - y2));

		double k1 = len1 / (len1 + len2);
		double k2 = len2 / (len2 + len3);

		double xm1 = xc1 + (xc2 - xc1) * k1;
		double ym1 = yc1 + (yc2 - yc1) * k1;

		double xm2 = xc2 + (xc3 - xc2) * k2;
		double ym2 = yc2 + (yc3 - yc2) * k2;

		int ctrl1_x = (int) Math.floor(xm1 + (xc2 - xm1) * smoothFactor * 1.0
				+ x1 - xm1 + 0.5);
		int ctrl1_y = (int) Math.floor(ym1 + (yc2 - ym1) * smoothFactor * 1.0
				+ y1 - ym1 + 0.5);

		int ctrl2_x = (int) Math.floor(xm2 + (xc2 - xm2) * smoothFactor * 1.0
				+ x2 - xm2 + 0.5);
		int ctrl2_y = (int) Math.floor(ym2 + (yc2 - ym2) * smoothFactor * 1.0
				+ y2 - ym2 + 0.5);

		Point[] result = { new Point(ctrl1_x, ctrl1_y),
				new Point(ctrl2_x, ctrl2_y) };

		return result;
	}

	private void interpolate(Vector<Point> output, double x1, double y1,
			double x2, double y2, double x3, double y3, double x4, double y4) {

		int steps = NUM_STEPS;

		double subdiv_step = 1.0 / (NUM_STEPS + 1);
		double subdiv_step2 = subdiv_step * subdiv_step;
		double subdiv_step3 = subdiv_step * subdiv_step * subdiv_step;

		double pre1 = 3.0 * subdiv_step;
		double pre2 = 3.0 * subdiv_step2;
		double pre4 = 6.0 * subdiv_step2;
		double pre5 = 6.0 * subdiv_step3;

		double tmp1x = x1 - x2 * 2.0 + x3;
		double tmp1y = y1 - y2 * 2.0 + y3;

		double tmp2x = (x2 - x3) * 3.0 - x1 + x4;
		double tmp2y = (y2 - y3) * 3.0 - y1 + y4;

		double fx = x1;
		double fy = y1;

		double dfx = (x2 - x1) * pre1 + tmp1x * pre2 + tmp2x * subdiv_step3;
		double dfy = (y2 - y1) * pre1 + tmp1y * pre2 + tmp2y * subdiv_step3;

		double ddfx = tmp1x * pre4 + tmp2x * pre5;
		double ddfy = tmp1y * pre4 + tmp2y * pre5;

		double dddfx = tmp2x * pre5;
		double dddfy = tmp2y * pre5;

		while ((--steps) >= 0) {
			fx += dfx;
			fy += dfy;
			dfx += ddfx;
			dfy += ddfy;
			ddfx += dddfx;
			ddfy += dddfy;
			output.add(new Point((int) Math.floor(fx + 0.5), (int) Math
					.floor(fy + 0.5)));
		}
		output.add(new Point((int) x4, (int) y4));
	}
}
