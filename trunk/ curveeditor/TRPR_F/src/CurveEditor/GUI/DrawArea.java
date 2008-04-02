package CurveEditor.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

import CurveEditor.Change.ChangeListener;
import CurveEditor.Change.WatchedObject;
import CurveEditor.Curves.Curve;

public class DrawArea extends JPanel implements ChangeListener {

	// Dimensies van het tekencanvas.
	// Voorlopig vast gekozen, nadien kijken of dit at runtime correct kan
	// veranderd worden
	private static int FRAME_WIDTH = 600;
	private static int FRAME_HEIGHT = 600;

	private List<Curve> curves;
	private List<Curve> selectedCurves;

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
						selectedCurves.get(i).getOutput().get(j).X(),
						selectedCurves.get(i).getOutput().get(j).Y());

			for (int j = 0; j < selectedCurves.get(i).getInput().size(); ++j)
				g.fillRect(selectedCurves.get(i).getInput().get(j).X(),
						selectedCurves.get(i).getInput().get(j).Y(), 2, 2);
		}
	}

	public DrawArea(List<Curve> curves, List<Curve> selectedCurves) {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(new Color(255, 255, 255));

		this.curves = curves;
		this.selectedCurves = selectedCurves;

		this.repaint();
	}

	public void contentsChanged(WatchedObject object) {
		this.repaint();
	}
}
