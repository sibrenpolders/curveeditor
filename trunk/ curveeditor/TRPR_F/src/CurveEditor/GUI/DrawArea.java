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
	
	//een regel omvat x, y + kleur (0 = zwart, 1 = wit, 2 = rood)
	private int[][] data = new int[3][];
	
	public DrawArea() {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(new Color(255, 255, 255));
		addMouseListener(this);
	}

	public DrawArea(Vector<Curve> curves) {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(new Color(255, 255, 255));
		addMouseListener(this);
		
	}

	public void drawCurve(Curve c) {

	}
	
	public Point retrievePoint() {
		return null;
	}

	public void drawSelectedCurve(Curve c) {

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
