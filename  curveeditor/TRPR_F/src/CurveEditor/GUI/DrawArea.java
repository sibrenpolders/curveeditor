package CurveEditor.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;
import javax.swing.JPanel;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class DrawArea extends JPanel {

	private static final long serialVersionUID = 1L;

	// Dimensies van het tekencanvas.
	// Voorlopig vast gekozen, nadien kijken of dit at runtime correct kan
	// veranderd worden
	private static int FRAMEWIDTH = 600;
	private static int FRAMEHEIGHT = 600;
	private static int CONTROLPOINTWIDTH = 2;
	private static int HOOVEREDCURVEWIDTH = 1;
	private static int DEFAULTCURVEWIDTH = 0;

	private int frameWidth = FRAMEWIDTH;
	private int frameHeight = FRAMEHEIGHT;
	private int curveWidth = DEFAULTCURVEWIDTH;
	private boolean coords;
	private boolean nrs;
	private boolean tangents;
	private Vector<Curve> curves;
	private Vector<Curve> selectedCurves;
	private Vector<Curve> hooveredCurves;
	private Vector<Point> selectedPoints;
	private Vector<Point> hooveredPoints;
	private Graphics g;

	// variabelen voor selectierechthoekje
	private int xBegin = -1, yBegin = -1, xEnd = -1, yEnd = -1;

	public DrawArea(Vector<Curve> curves, Vector<Curve> selectedCurves,
			Vector<Curve> hooveredCurves, Vector<Point> selectedPoints,
			Vector<Point> hooveredPoints) {
		init(curves, selectedCurves, hooveredCurves, selectedPoints,
				hooveredPoints, true, true, true);
	}

	public void init(Vector<Curve> curves, Vector<Curve> selectedCurves,
			Vector<Curve> hooveredCurves, Vector<Point> selectedPoints,
			Vector<Point> hooveredPoints, boolean coords, boolean nrs,
			boolean tangents) {
		setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
		setBackground(new Color(255, 255, 255));

		this.curves = curves;
		this.selectedCurves = selectedCurves;
		this.hooveredCurves = hooveredCurves;
		this.selectedPoints = selectedPoints;
		this.hooveredPoints = hooveredPoints;

		this.coords = coords;
		this.nrs = nrs;
		this.tangents = tangents;

		this.repaint();
	}

	public boolean coords() {
		return coords;
	}

	public boolean nrs() {
		return nrs;
	}

	public boolean tangents() {
		return tangents;
	}

	public void toggleCoords() {
		coords = !coords;
	}

	public void toggleNrs() {
		nrs = !nrs;
	}

	public void toggleTangents() {
		tangents = !tangents;
	}

	public void update() {
		paint(g);
	}

	// Deze methode wordt impliciet aangeroepen als je ergens this.repaint()
	// uitvoert. Dit hertekent het vollédige tekencanvas.
	public void paintComponent(Graphics g) {
		this.g = g;
		super.paintComponent(g);

		emptyField();

		this.g.setColor(Color.LIGHT_GRAY);
		this.drawSelectionRectangle();

		this.g.setColor(Color.BLACK);
		this.drawOutput(curves, false, false);
		this.g.setColor(Color.RED);
		drawOutput(selectedCurves, coords, nrs);
		if (tangents) {
			this.g.setColor(Color.BLUE);
			drawTangents(selectedCurves);
		}

		this.g.setColor(Color.GREEN);
		drawSelectedPoints(selectedPoints);
		this.g.setColor(Color.BLACK);

		this.g.setColor(Color.magenta);
		this.curveWidth = HOOVEREDCURVEWIDTH;
		drawOutput(hooveredCurves, false, false);
		this.curveWidth = DEFAULTCURVEWIDTH;

		this.g.setColor(Color.YELLOW);
		drawSelectedPoints(hooveredPoints);
		this.g.setColor(Color.BLACK);
	}

	public void emptyField() {
		g.clipRect(0, 0, frameWidth, frameHeight);
		g.setColor(Color.white);
		g.fillRect(0, 0, frameWidth, frameHeight);
		g.setColor(Color.black);
	}

	private void drawSelectionRectangle() {
		if (xBegin != -1 && yBegin != -1 && xEnd != -1 && yEnd != -1) {
			int rectWidth = Math.abs(xBegin - xEnd);
			int rectHeigth = Math.abs(yBegin - yEnd);
			int yStart = (yBegin > yEnd) ? yEnd : yBegin;
			int xStart = (xBegin > xEnd) ? xEnd : xBegin;

			g.fillRect(xStart, yStart, rectWidth, rectHeigth);
		}
	}

	public void resetSelectionRectangle() {
		xBegin = -1;
		yBegin = -1;
		xEnd = -1;
		yEnd = -1;
	}

	public void beginSelectionRectangle(int x, int y) {
		xBegin = x;
		yBegin = y;
		xEnd = x;
		yEnd = y;
	}

	public void updateSelectionRectangle(int x, int y) {
		xEnd = x;
		yEnd = y;
	}

	public boolean selectionRectangleStarted() {
		return xBegin != -1 && yBegin != -1;
	}

	public int getXBegin() {
		return xBegin;
	}

	public int getYBegin() {
		return yBegin;
	}

	private void drawOutput(Vector<Curve> curves, boolean coords, boolean nrs) {
		for (int i = 0; i < curves.size(); ++i) {
			// de outputpunten uittekenen
			for (int j = 0; j < curves.get(i).getOutput().size() - 1; ++j) {
				g.drawLine(curves.get(i).getOutput().get(j).X(), curves.get(i)
						.getOutput().get(j).Y(), curves.get(i).getOutput().get(
						j + 1).X(), curves.get(i).getOutput().get(j + 1).Y());

				if (curveWidth != 0) {
					for (int k = 1; k <= curveWidth; ++k) {
						g.drawLine(curves.get(i).getOutput().get(j).X(), curves
								.get(i).getOutput().get(j).Y()
								+ k, curves.get(i).getOutput().get(j).X(),
								curves.get(i).getOutput().get(j).Y() + k);
						g.drawLine(curves.get(i).getOutput().get(j).X(), curves
								.get(i).getOutput().get(j).Y()
								- k, curves.get(i).getOutput().get(j).X(),
								curves.get(i).getOutput().get(j).Y() - k);
					}
				}
			}

			// de controlepunten uittekenen
			for (int j = 0; j < curves.get(i).getInput().size(); ++j) {
				g.fillRect(curves.get(i).getInput().get(j).X()
						- CONTROLPOINTWIDTH, curves.get(i).getInput().get(j)
						.Y()
						- CONTROLPOINTWIDTH, 2 * CONTROLPOINTWIDTH + 1,
						2 * CONTROLPOINTWIDTH + 1);

				if (coords)
					g.drawString(curves.get(i).getInput().get(j).X() + ", "
							+ curves.get(i).getInput().get(j).Y(), curves
							.get(i).getInput().get(j).X() + 4, curves.get(i)
							.getInput().get(j).Y() + 10);

				if (nrs)
					g.drawString("C" + i + ", P" + j, curves.get(i).getInput()
							.get(j).X() + 4, curves.get(i).getInput().get(j)
							.Y() - 0);
			}
		}
	}

	private void drawTangents(Vector<Curve> curves) {
		for (int i = 0; i < curves.size(); ++i) {
			Vector<Point> vip = curves.get(i).getInput();
			if (curves.get(i).getType() == 'H') {
				for (int j = 0; j + 1 < vip.size(); j = j + 2) {
					g.drawLine(vip.get(j).X(), vip.get(j).Y(), vip.get(j + 1)
							.X(), vip.get(j + 1).Y());
				}
			} else if (curves.get(i).getType() == 'B'
					&& curves.get(i).getDegree() == 3) {
				for (int j = 0; j < vip.size(); j = j + 3) {
					if (j + 1 < vip.size())
						g.drawLine(vip.get(j).X(), vip.get(j).Y(), vip.get(
								j + 1).X(), vip.get(j + 1).Y());
					if (j - 1 >= 0)
						g.drawLine(vip.get(j - 1).X(), vip.get(j - 1).Y(), vip
								.get(j).X(), vip.get(j).Y());
				}
			}
		}
	}

	private void drawSelectedPoints(Vector<Point> v) {
		for (int j = 0; j < v.size(); ++j) {
			g.fillRect(v.get(j).X() - (CONTROLPOINTWIDTH + 1), v.get(j).Y()
					- (CONTROLPOINTWIDTH + 1), 2 * (CONTROLPOINTWIDTH + 1) + 1,
					2 * (CONTROLPOINTWIDTH + 1) + 1);
		}
	}

	public void setSize(int x, int y) {
		super.setSize(x, y);
		frameWidth = x;
		frameHeight = y;
	}

	public int width() {
		return this.getWidth();
	}

	public int height() {
		return this.getHeight();
	}
}