package CurveEditor.GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;
import CurveEditor.Exceptions.*;

/*
 * Deze klasse stelt het tekencanvas voor, alwaar inputpunten en 
 * curves kunnen uitgetekend worden. Deze houdt referenties bij 
 * naar de verschillende Vectoren van Points en Curves. Afhankelijk
 * van het type van die Vector (hoovered, selected, niet-selected) 
 * worden de elementen anders uitgetekend.
 */
public final class DrawArea extends JPanel {

	private static final long serialVersionUID = 1L;

	// De dikte van een inputpunt.
	private static int CONTROLPOINTWIDTH = 2;
	// De dikte van een hoovered Curve.
	private static int HOOVEREDCURVEWIDTH = 1;
	// De standaard dikte van een Curve, enkel de outpunten
	// zelf worden dus uitgetekend, geen punten daarrond.
	private static int DEFAULTCURVEWIDTH = 0;
	private int curveWidth = DEFAULTCURVEWIDTH;

	// Flags om bij te houden of iets al dan niet
	// dient uitgetekend te worden.
	private boolean coords;
	private boolean nrs;
	private boolean tangents;
	private boolean drawRectangle;

	// Referenties naar Vectoren uit GUI.java.
	// De inhoud zal uiteindelijk op het canvas uitgetekend worden.
	private Vector<Curve> curves;
	private Vector<Curve> selectedCurves;
	private Vector<Curve> hooveredCurves;
	private Vector<Point> selectedPoints;
	private Vector<Point> hooveredPoints;
	private Graphics g;

	// De huidige positie van het rechthoekje, horende
	// bij de pathsimulationtool.
	private Point runPoint;

	// Variabelen voor selectierechthoekje dat aangemaakt wordt
	// wanneer met m.b.v. dragging elementen wilt selecteren.
	private int xBegin = -1, yBegin = -1, xEnd = -1, yEnd = -1;

	public DrawArea(Vector<Curve> curves, Vector<Curve> selectedCurves,
			Vector<Curve> hooveredCurves, Vector<Point> selectedPoints,
			Vector<Point> hooveredPoints) throws InvalidArgumentException {
		init(curves, selectedCurves, hooveredCurves, selectedPoints,
				hooveredPoints, false, false, false);
	}

	public void init(Vector<Curve> curves, Vector<Curve> selectedCurves,
			Vector<Curve> hooveredCurves, Vector<Point> selectedPoints,
			Vector<Point> hooveredPoints, boolean coords, boolean nrs,
			boolean tangents) throws InvalidArgumentException {
		setSize(); // De grootte instellen.
		setBackground(new Color(255, 255, 255));

		if (curves == null || selectedCurves == null || hooveredCurves == null
				|| selectedCurves == null || selectedPoints == null
				|| hooveredPoints == null)
			throw new InvalidArgumentException(
					"DrawArea.java - init(): Invalid Argument.");

		this.curves = curves;
		this.selectedCurves = selectedCurves;
		this.hooveredCurves = hooveredCurves;
		this.selectedPoints = selectedPoints;
		this.hooveredPoints = hooveredPoints;

		this.coords = coords;
		this.nrs = nrs;
		this.tangents = tangents;
		this.drawRectangle = true;

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

	// Verander de positie van het rechthoekje horende bij
	// de pathsimulationtool en herteken.
	public void drawRunner(Point p) {
		runPoint = p;
		repaint();
	}

	// Deze methode wordt impliciet aangeroepen als je ergens DrawArea.repaint()
	// uitvoert. Dit hertekent het vollédige tekencanvas.
	public void paintComponent(Graphics g) {
		this.g = g;
		super.paintComponent(g);

		emptyField(); // Clear het canvas.

		this.g.setColor(Color.LIGHT_GRAY);
		if (drawRectangle)
			// Teken het selectierechthoekje indien nodig.
			this.drawSelectionRectangle();
		else
			// Anders, teken een dragging arrow in de plaats.
			this.drawArrow();

		this.g.setColor(Color.BLACK);
		// Ongeselecteerde curves worden altijd zonder extra
		// info uitgetekend.
		this.drawOutput(curves, false, false);
		this.g.setColor(Color.RED);
		// Geselecteerde curves worden a.h.v. de geselecteerde
		// opties uitgetekend.
		drawOutput(selectedCurves, coords, nrs);
		if (tangents) {
			this.g.setColor(Color.BLUE);
			drawTangents(selectedCurves);
		}

		this.g.setColor(Color.GREEN);
		drawSelectedPoints(selectedPoints);
		this.g.setColor(Color.BLACK);

		this.g.setColor(Color.magenta);
		// Verander de curvedikte en teken de gehooverde
		// curves uit.
		this.curveWidth = HOOVEREDCURVEWIDTH;
		drawOutput(hooveredCurves, false, false);
		this.curveWidth = DEFAULTCURVEWIDTH;

		this.g.setColor(Color.YELLOW);
		drawSelectedPoints(hooveredPoints);

		// Als de pathsimulationtool actief is --> teken het vierkantje
		// op zijn huidige positie uit.
		if (runPoint != null) {
			this.g.setColor(Color.CYAN);
			g.fillRect(runPoint.X() - 3 * CONTROLPOINTWIDTH, runPoint.Y() - 3
					* CONTROLPOINTWIDTH, 6 * CONTROLPOINTWIDTH + 1,
					6 * CONTROLPOINTWIDTH + 1);
			runPoint = null;
		}

		this.g.setColor(Color.BLACK);
	}

	private void emptyField() {
		g.clipRect(0, 0, DisplaySize.DRAWWIDTH, DisplaySize.DRAWHEIGHT);
		g.setColor(Color.white);
		g.fillRect(0, 0, DisplaySize.DRAWWIDTH, DisplaySize.DRAWHEIGHT);
		g.setColor(Color.black);
	}

	// Teken het draggingrechthoekje a.h.v. zijn begin- en
	// eindposities, indien deze geset zijn.
	private void drawSelectionRectangle() {
		if (xBegin != -1 && yBegin != -1 && xEnd != -1 && yEnd != -1) {
			int rectWidth = Math.abs(xBegin - xEnd);
			int rectHeigth = Math.abs(yBegin - yEnd);
			int yStart = (yBegin > yEnd) ? yEnd : yBegin;
			int xStart = (xBegin > xEnd) ? xEnd : xBegin;

			g.fillRect(xStart, yStart, rectWidth, rectHeigth);
		}
	}

	// Teken het draggingpijltje a.h.v. zijn begin- en
	// eindpositie, indien deze geset zijn.
	private void drawArrow() {
		if (xBegin != -1 && yBegin != -1 && xEnd != -1 && yEnd != -1) {
			g.drawLine(xBegin, yBegin, xEnd, yEnd);
			g.drawLine(xBegin, yBegin + 1, xEnd, yEnd + 1);
			g.drawLine(xBegin, yBegin - 1, xEnd, yEnd - 1);
		}
	}

	// Reset de dragging-functie --> ongesette begin- en
	// eindpositie --> er zal niks getekend worden.
	public void resetDragging() {
		xBegin = -1;
		yBegin = -1;
		xEnd = -1;
		yEnd = -1;
		drawRectangle = true;
	}

	// Start de dragging-functie --> beginpositie
	// wordt geset --> rechthoekje zal getekend worden.
	public void beginSelectionRectangle(int x, int y) {
		xBegin = x;
		yBegin = y;
		xEnd = x;
		yEnd = y;
		drawRectangle = true;
	}

	// Start de dragging-functie --> beginpositie
	// wordt geset --> pijltje zal getekend worden.
	public void beginMovingArrow(int x, int y) {
		xBegin = x;
		yBegin = y;
		xEnd = x;
		yEnd = y;
		drawRectangle = false;
	}

	// Update de eindpositie van het draggingelement.
	public void updateDragging(int x, int y) {
		xEnd = x;
		yEnd = y;
	}

	// Controleer of de dragging-functie gestart is.
	public boolean draggingStarted() {
		return xBegin != -1 && yBegin != -1;
	}

	// Vraag de coördinatie van de posities van het
	// draggingelement.
	public int getXBegin() {
		return xBegin;
	}

	public int getYBegin() {
		return yBegin;
	}

	public int getXEnd() {
		return xEnd;
	}

	public int getYEnd() {
		return yEnd;
	}

	// Teken een Vector van Curves uit a.h.v. meegegeven flags.
	private void drawOutput(Vector<Curve> curves, boolean coords, boolean nrs) {
		Vector<Point> out;
		Vector<Point>in;
		for (int i = 0; i < curves.size(); ++i) {
			// De outputpunten uittekenen.
			out = curves.get(i).getOutput();
			for (int j = 0; j < out.size() - 1; ++j) {
				g.drawLine(out.get(j).X(), out.get(j).Y(), out.get(j + 1).X(),
						out.get(j + 1).Y());
				// Indien de curve dikker moet zijn dan de default dikte,
				// kleur de nodige omliggende pixels ook in.
				if (curveWidth != 0) {
					for (int k = 1; k <= curveWidth; ++k) {
						g.drawLine(out.get(j).X(), out.get(j).Y() + k, out.get(
								j + 1).X(), out.get(j + 1).Y() + k);
						g.drawLine(out.get(j).X(), out.get(j).Y() - k, out.get(
								j + 1).X(), out.get(j + 1).Y() - k);
					}
				}
			}

			// De inputpunten uittekenen.
			in = curves.get(i).getInput();
			for (int j = 0; j < in.size(); ++j) {
				// Een rechthoekje op de plaats van het punt uittekenen.
				g.fillRect(in.get(j).X() - CONTROLPOINTWIDTH, in.get(j).Y()
						- CONTROLPOINTWIDTH, 2 * CONTROLPOINTWIDTH + 1,
						2 * CONTROLPOINTWIDTH + 1);

				// Informatie weergeven, indien gevraagd.
				if (coords)
					g.drawString(in.get(j).X() + ", " + in.get(j).Y(), in
							.get(j).X() + 4, in.get(j).Y() + 10);

				if (nrs)
					g.drawString("C" + i + ", P" + j, in.get(j).X() + 4, in
							.get(j).Y() - 0);
			}
		}
	}

	// Teken de raakvectoren uit van de Curves in een Vector van
	// Curves.
	private void drawTangents(Vector<Curve> curves) {
		for (int i = 0; i < curves.size(); ++i) {
			Vector<Point> vip = curves.get(i).getInput();

			if (curves.get(i).getType() == 'H') {
				for (int j = 0; j + 1 < vip.size(); j = j + 2) {
					g.drawLine(vip.get(j).X(), vip.get(j).Y(), vip.get(j + 1)
							.X(), vip.get(j + 1).Y());
				}
			}
			// Per vier inputpunten: raakvector 1 is van punt 1 naar punt 2,
			// raakvector 2 is van punt 4 naar punt 3.
			else if ((curves.get(i).getType() == 'B'
					|| curves.get(i).getType() == 'C' || curves.get(i)
					.getType() == 'G')
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

	// Teken de geselecteerde inputpunten uit --> iets groter rechthoekje
	// dan niet-geselecteerd.
	private void drawSelectedPoints(Vector<Point> v) {
		for (int j = 0; j < v.size(); ++j) {
			g.fillRect(v.get(j).X() - (CONTROLPOINTWIDTH + 1), v.get(j).Y()
					- (CONTROLPOINTWIDTH + 1), 2 * (CONTROLPOINTWIDTH + 1) + 1,
					2 * (CONTROLPOINTWIDTH + 1) + 1);
		}
	}

	// Verander de grootte van DrawArea en herteken.
	public void setSize() {
		setBounds(DisplaySize.CHOICEWIDTH, 0, DisplaySize.DRAWWIDTH,
				DisplaySize.DRAWHEIGHT);
		repaint();
	}

	// Sla het canvas op naar het gegeven bestand in het gegeven formaat, indien
	// dat bestaat.
	public void saveToFile(String fileName, String extension) {
		BufferedImage bi = new BufferedImage(DisplaySize.DRAWWIDTH,
				DisplaySize.DRAWHEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D ig2 = bi.createGraphics();
		Graphics temp = g;
		paintComponent(ig2);
		g = temp;

		try {
			if (extension.compareToIgnoreCase("png") == 0
					|| extension.compareToIgnoreCase("gif") == 0)
				ImageIO.write(bi, extension.toUpperCase(), new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}