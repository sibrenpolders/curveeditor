package CurveEditor.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.JPanel;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class DrawArea extends JPanel implements MouseListener {

	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 600;

	private Vector<Vector<Point>> data = new Vector<Vector<Point>>();
	private int[] selected = new int[20];

	public DrawArea() {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(new Color(255, 255, 255));
		addMouseListener(this);
		
		for (int i = 0; i < 20; ++i)
			selected[i] = -1;
	}

	public DrawArea(Vector<Curve> curves) {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(new Color(255, 255, 255));
		addMouseListener(this);

		for (int i = 0; i < 20; ++i)
			selected[i] = -1;

		for (int i = 0; i < curves.size(); ++i)
			data.add(curves.elementAt(i).getOutput());

		// repaint
	}

	public void drawCurve(Curve c) {
		data.add(c.getOutput());
		// repaint
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
