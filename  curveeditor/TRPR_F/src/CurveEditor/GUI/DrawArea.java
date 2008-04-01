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

	public void test(){
		for(int i = 0; i < 7; ++i)
		{
			Vector<Point> t = new Vector<Point>();
			for(int j = 0; j < 20; ++j)
				t.add(new CurveEditor.Curves.Point((int)((double)Math.random()*(double)FRAME_WIDTH), (int)((double)Math.random()*(double)FRAME_HEIGHT)));
			data.add(t);
		}

	}
	
	public void update(Graphics g){
		paint(g);
	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);

		g.clipRect(0, 0, FRAME_WIDTH, FRAME_WIDTH);
		g.setColor(Color.white);
		g.fillRect(0, 0, FRAME_WIDTH, FRAME_WIDTH);
		g.setColor(Color.red);
		g.drawLine(50, 50, 100, 100); 	
		g.drawLine(0, 0, 440, 100); 
		for(int i = 0; i < data.size(); ++i)
		{
			for(int j = 0; j < data.get(i).size(); ++j)
				g.drawRect(data.get(i).get(j).X(), data.get(i).get(j).Y(), 5, 5); 				
		}
	}


	private static int FRAME_WIDTH = 600;
	private static int FRAME_HEIGHT = 600;

	//de vectoren met punten van de verschillende curves, een gedelete curve geeft gewoon een lege
	//vector; anders overal indexen zitten aanpassen --> performance loss
	private Vector<Vector<Point>> data = new Vector<Vector<Point>>();

	//de indexen van de geselecteerde curves in de Vector data
	private int[] selected = new int[20];

	private void deselectAll(){
		for(int i = 0; i < selected.length; ++i)
			selected[i] = -1;
	}

	private void selectAll(){
		selected = new int[data.size()];

		for(int i = 0; i < selected.length; ++i)
			selected[i] = i;
	}

	public DrawArea() {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(new Color(255, 255, 255));
		addMouseListener(this);

		for (int i = 0; i < selected.length; ++i)
			selected[i] = -1;
		test();
		this.repaint();
	}

	public DrawArea(Vector<Curve> curves) {
		setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
		setBackground(new Color(255, 255, 255));
		addMouseListener(this);

		for (int i = 0; i < selected.length; ++i)
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
