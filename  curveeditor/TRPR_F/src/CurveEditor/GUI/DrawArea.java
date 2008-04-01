package CurveEditor.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.JPanel;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class DrawArea extends JPanel implements MouseListener {

	// Dimensies van het tekencanvas.
	// Voorlopig vast gekozen, nadien kijken of dit at runtime correct kan
	// veranderd worden
	private static int FRAME_WIDTH = 600;
	private static int FRAME_HEIGHT = 600;

	// De verzameling van vectoren met punten van de verschillende curves, een
	// gedelete curve geeft gewoon een lege vector.
	// Anders overal indexen zitten aanpassen --> performance loss.
	private Vector<Vector<Point>> data = new Vector<Vector<Point>>();

	// De indexen van de geselecteerde curves in de Vector <data>.
	private int[] selected = new int[20];

	private void deselectAll() {
		for (int i = 0; i < selected.length; ++i)
			selected[i] = -1;
	}

	private void selectAll() {
		selected = new int[data.size()];

		for (int i = 0; i < selected.length; ++i)
			if (data.get(i).size() > 0)
				selected[i] = i;
			else
				selected[i] = -1;
	}

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
		for (int i = 0; i < data.size(); ++i)
			if (data.get(i).size() > 0)
				for (int j = 0; j < data.get(i).size(); ++j)
					g.drawLine(data.get(i).get(j).X(), data.get(i).get(j).Y(),
							data.get(i).get(j).X(), data.get(i).get(j).Y());

		g.setColor(Color.red);
		for (int i = 0; i < selected.length; ++i)
			if (selected[i] != -1 && data.get(selected[i]).size() > 0)
				for (int j = 0; j < data.get(i).size(); ++j)
					g.drawLine(data.get(i).get(j).X(), data.get(i).get(j).Y(),
							data.get(i).get(j).X(), data.get(i).get(j).Y());
	}

	// Constructor.
	public DrawArea() {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(new Color(255, 255, 255));
		addMouseListener(this); // nodig voor clickevents te kunnen opvangen

		for (int i = 0; i < selected.length; ++i)
			selected[i] = -1;

		this.repaint();
	}

	// Constructor dat direct enkele ingeladen curves kan uittekenen.
	public DrawArea(Vector<Curve> curves) {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(new Color(255, 255, 255));
		addMouseListener(this);

		for (int i = 0; i < selected.length; ++i)
			selected[i] = -1;

		for (int i = 0; i < curves.size(); ++i)
			data.add(curves.elementAt(i).getOutput());

		this.repaint();
	}

	// Teken een meegegeven curve uit.
	public void drawCurve(Curve c) {
		data.add(c.getOutput());
		this.repaint();
	}

	public Point retrievePoint() {
		return null;
	}

	public void drawSelectedCurve(Curve c) {
		boolean finished = false;
		int i = 0;
		for (; i < data.size() && !finished; ++i)
			if (data.elementAt(i).equals(c.getOutput()))
				finished = true;
		--i;
		finished = false;

		for (int j = 0; i < 20 && !finished; ++j)
			if (selected[j] == -1) {
				selected[j] = i;
				finished = true;
			}

		// repaint
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
