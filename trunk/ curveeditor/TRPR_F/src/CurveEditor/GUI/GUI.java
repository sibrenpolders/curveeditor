package CurveEditor.GUI;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import CurveEditor.Core.Editor;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class GUI extends Editor implements MenuListener, MouseListener {
	protected ChoiceArea choice;
	protected DrawArea draw;
	protected Menu menu;

	public GUI() {
		JFrame frame = new JFrame("Curve Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Curve Editor");
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		menu = new Menu();
		contentPane.add(menu);

		draw = new DrawArea(this.curves, this.selectedCurves);
		draw.addMouseListener(this);
		this.addChangeListener(draw);
		contentPane.add(draw);

		frame.pack();
		frame.setVisible(true);
	}

	public GUI(String filename) {

	}

	@Override
	public void menuCanceled(MenuEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void menuDeselected(MenuEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void menuSelected(MenuEvent arg0) {

	}

	public void selectCurve() {
		// currentSituation.setCurrentPoint(draw.retrievePoint());
		currentSituation.setCurrentCurve(searchCurve(currentSituation
				.currentPoint()));
		currentSituation.setCurrentDegree(currentSituation.currentCurve()
				.getDegree());
		// currentSituation.setCurrentType(currentSituation.currentCurve().getType());

		// draw.drawSelectedCurve(currentSituation.currentCurve());
		choice.refresh();

		// message
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (Editor.mode == Editor.MODE.ADD_INPUT) {
			for (int i = 0; i < selectedCurves.size(); ++i) {
				selectedCurves.get(i).addInput(new Point(e.getX(), e.getY()));
				this.getAlgorithm(selectedCurves.get(i).getType())
						.calculateCurve(selectedCurves.get(i));
			}

			draw.repaint();
		} else if (Editor.mode == Editor.MODE.SELECT_CURVE) {
			// zoek curve en voeg toe, of zo
		} else if (Editor.mode == Editor.MODE.NEW_CURVE) {
			
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		draw.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		draw.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		// kunnen we gebruiken voor curves te "slepen", bijvoorbeeld

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}
