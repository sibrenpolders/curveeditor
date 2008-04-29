package CurveEditor.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class DrawArea extends JPanel {

	// Dimensies van het tekencanvas.
	// Voorlopig vast gekozen, nadien kijken of dit at runtime correct kan
	// veranderd worden
	private static int FRAMEWIDTH = 600;
	private static int FRAMEHEIGHT = 600;

	private int frameWidth = FRAMEWIDTH;
	private int frameHeight = FRAMEHEIGHT;
	private boolean coords;
	private boolean nrs;
	private boolean tangents;
	private Vector<Curve> curves;
	private Vector<Curve> selectedCurves;
	private Vector<Curve> hooveredCurves;
	private Vector<Point> selectedPoints;
	private Graphics g;

	public boolean coords() {
		return coords;
	}

	public boolean nrs() {
		return nrs;
	}

	public boolean tangents() {
		return tangents;
	}

	public DrawArea(Vector<Curve> curves, Vector<Curve> selectedCurves,
			Vector<Curve> hooveredCurves, Vector<Point> selectedPoints) {
		reset(curves, selectedCurves, hooveredCurves, selectedPoints, true,
				true, true);
	}

	public void reset(Vector<Curve> curves, Vector<Curve> selectedCurves,
			Vector<Curve> hooveredCurves, Vector<Point> selectedPoints,
			boolean coords, boolean nrs, boolean tangents) {
		setPreferredSize(new Dimension(FRAMEWIDTH, FRAMEHEIGHT));
		setBackground(new Color(255, 255, 255));

		this.curves = curves;
		this.selectedCurves = selectedCurves;
		this.hooveredCurves = hooveredCurves;
		this.selectedPoints = selectedPoints;

		this.coords = coords;
		this.nrs = nrs;
		this.tangents = tangents;

		this.repaint();
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

		this.g.setColor(Color.black);
		this.drawOutput(curves, false, false);
		this.g.setColor(Color.red);
		drawOutput(selectedCurves, coords, nrs);
		if (tangents) {
			this.g.setColor(Color.blue);
			drawTangents(selectedCurves);
			drawTangents(hooveredCurves);
		}
		this.g.setColor(Color.magenta);
		drawOutput(hooveredCurves, coords, nrs);
	}

	private void drawOutput(Vector<Curve> curves, boolean coords, boolean nrs) {
		for (int i = 0; i < curves.size(); ++i) {
			// de outputpunten uittekenen
			for (int j = 0; j < curves.get(i).getOutput().size(); ++j)
				g.drawLine(curves.get(i).getOutput().get(j).X(), curves.get(i)
						.getOutput().get(j).Y(), curves.get(i).getOutput().get(
						j).X(), curves.get(i).getOutput().get(j).Y());

			// de controlepunten uittekenen
			for (int j = 0; j < curves.get(i).getInput().size(); ++j) {
				g.fillRect(curves.get(i).getInput().get(j).X() - 2, curves.get(
						i).getInput().get(j).Y() - 2, 5, 5);

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

	public void emptyField() {
		g.clipRect(0, 0, frameWidth, frameHeight);
		g.setColor(Color.white);
		g.fillRect(0, 0, frameWidth, frameHeight);
		g.setColor(Color.black);
	}
}