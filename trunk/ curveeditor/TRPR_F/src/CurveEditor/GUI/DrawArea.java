package CurveEditor.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

import CurveEditor.Curves.Curve;

public class DrawArea extends JPanel {

	// Dimensies van het tekencanvas.
	// Voorlopig vast gekozen, nadien kijken of dit at runtime correct kan
	// veranderd worden
	public static int FRAME_WIDTH = 600;
	public static int FRAME_HEIGHT = 600;

	private Vector<Curve> curves;
	private Vector<Curve> selectedCurves;

	public void update(Graphics g) {
		paint(g);
	}

	// Deze methode wordt impliciet aangeroepen als je ergens this.repaint()
	// uitvoert.
	// Dit hertekent het vollédige tekencanvas.
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.clipRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		g.setColor(Color.white);
		g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		g.setColor(Color.black);
		for (int i = 0; i < curves.size(); ++i) {
			for (int j = 0; j < curves.get(i).getOutput().size(); ++j)
				g.drawLine(curves.get(i).getOutput().get(j).X(), curves.get(i)
						.getOutput().get(j).Y(), curves.get(i).getOutput().get(
						j).X(), curves.get(i).getOutput().get(j).Y());

			for (int j = 0; j < curves.get(i).getInput().size(); ++j)
				g.fillRect(curves.get(i).getInput().get(j).X(), curves.get(i)
						.getInput().get(j).Y(), 2, 2);
		}

		g.setColor(Color.red);

		for (int i = 0; i < selectedCurves.size(); ++i) {
			for (int j = 0; j < selectedCurves.get(i).getOutput().size(); ++j)
				g.drawLine(selectedCurves.get(i).getOutput().get(j).X(),
						selectedCurves.get(i).getOutput().get(j).Y(),
						selectedCurves.get(i).getOutput().get(j).X(), //--> regel 53, niks abnormaals in vgl. met vorige of volgende regel
																	/* Zover was ik ook al:D. Ik krijg die fout af en toe eens.
																	 * Direct na de start. Maar als gij hem nog niet gekregen hebt
																	 * dan zal het wel een rariteit zijn zoals die gtk error van u.
																	 * Verder geen zorgen over maken.
																	 */
						selectedCurves.get(i).getOutput().get(j).Y());

			for (int j = 0; j < selectedCurves.get(i).getInput().size(); ++j) {
				g.fillRect(selectedCurves.get(i).getInput().get(j).X() - 2,
						selectedCurves.get(i).getInput().get(j).Y() - 2, 5, 5);
				g.drawString(selectedCurves.get(i).getInput().get(j).X() + ", "
						+ selectedCurves.get(i).getInput().get(j).Y(),
						selectedCurves.get(i).getInput().get(j).X(),
						selectedCurves.get(i).getInput().get(j).Y());
			}
		}
	}

	public DrawArea(Vector<Curve> curves, Vector<Curve> selectedCurves) {
		reset(curves, selectedCurves);
	}

	public void reset(Vector<Curve> curves, Vector<Curve> selectedCurves) {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(new Color(255, 255, 255));

		this.curves = curves;
		this.selectedCurves = selectedCurves;

		this.repaint();
	}

	public void setSize(int x, int y) {
		super.setSize(x, y);
		FRAME_WIDTH = x;
		FRAME_HEIGHT = y;
	}

	public int width() {
		return this.getWidth();
	}

	public int height() {
		return this.getHeight();
	}
}