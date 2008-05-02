package CurveEditor.GUI;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import CurveEditor.Algorithms.Algorithm;
import CurveEditor.Core.CurveContainer;
import CurveEditor.Core.Editor;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class GUI extends Editor implements MenuListener, MouseListener,
		MouseMotionListener {
	protected ChoiceArea choice;
	protected DrawArea draw;
	private Menu menu;
	private Toolbar toolbar;
	private Listener listener;

	public GUI() {
		super();
		loadComponents();
	}

	public GUI(String filename) {
		super(filename);
		loadComponents();
	}

	private void loadComponents() {
		JFrame frame = new JFrame("Curve Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Curve Editor");
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		listener = new Listener();
		menu = new Menu(listener);
		contentPane.add(menu);

		toolbar = new Toolbar(listener);
		contentPane.add(toolbar);

		JPanel screen = new JPanel();
		screen.setLayout(new BoxLayout(screen, BoxLayout.X_AXIS));

		screen.add(Box.createRigidArea(new Dimension(10, 0)));

		choice = new ChoiceArea(listener);
		screen.add(choice);

		draw = new DrawArea(this.curves, this.selectedCurves,
				this.hooveredCurves, this.selectedPoints, this.hooveredPoints);
		draw.addMouseListener(this);
		draw.addMouseMotionListener(this);
		screen.add(draw);
		screen.add(Box.createRigidArea(new Dimension(10, 0)));
		screen.add(Box.createHorizontalGlue());

		contentPane.add(screen);

		frame.pack();
		frame.setVisible(true);

		selectionTool = new CurveContainer(600, 600);
	}

	public void menuCanceled(MenuEvent arg0) {

	}

	public void menuDeselected(MenuEvent arg0) {

	}

	public void menuSelected(MenuEvent arg0) {

	}

	public void mouseClicked(MouseEvent e) {
		hooveredCurves.clear();
		hooveredPoints.clear();

		if (mode == Editor.MODE.ADD_INPUT) {
			Algorithm prev = currentAlgorithm;
			for (int i = 0; i < selectedCurves.size(); ++i) {
				Curve c = selectedCurves.elementAt(i);
				c.addInput(new Point(e.getX(), e.getY()));
				// Bij Hermite ( type == 'H' ) is het 2de ingegeven punt
				// telkens de tangens. Dus er moet niet getekend worden voordat
				// deze is ingegeven
				if (c.getType() != 'H' || c.getInput().size() % 2 == 0) {
					this.getAlgorithm(selectedCurves.get(i).getType(),
							selectedCurves.get(i).getDegree()).calculate(
							selectedCurves.get(i));

					selectionTool.deleteCurve(c);
					selectionTool.addCurve(c);
				}

			}
			currentAlgorithm = prev;
			draw.repaint();
		} else if (mode == Editor.MODE.SELECT_CURVE
				|| mode == Editor.MODE.DESELECT_CURVE) {
			Curve c = pickCurve(new Point(e.getX(), e.getY()));
			if (c != null)
				draw.repaint();
		} else if (mode == Editor.MODE.NEW_CURVE) {
			startNewCurve();
			draw.repaint();
		} else if (mode == Editor.MODE.SELECT_CONTROL_POINT
				|| mode == Editor.MODE.DESELECT_CONTROL_POINT) {
			pickControlPoint(new Point(e.getX(), e.getY()));
			draw.repaint();
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		draw.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public void mouseExited(MouseEvent arg0) {
		draw.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		if (hooveredCurves.size() > 0 || hooveredPoints.size() > 0) {
			hooveredCurves.clear();
			hooveredPoints.clear();
			draw.repaint();
		}
	}

	public void mousePressed(MouseEvent e) {
		if (mode == Editor.MODE.SELECT_CURVE
				|| mode == Editor.MODE.DESELECT_CURVE
				|| mode == Editor.MODE.SELECT_CONTROL_POINT
				|| mode == Editor.MODE.DESELECT_CONTROL_POINT)
			draw.beginSelectionRectangle(e.getX(), e.getY());
		else if (mode == MODE.MOVE_CONTROL_POINTS || mode == MODE.MOVE_CURVES)
			draw.beginMovingArrow(e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) {
		if (mode == MODE.SELECT_CURVE)
			for (int i = 0; i < hooveredCurves.size(); ++i) {
				boolean found = false;
				for (int j = 0; j < selectedCurves.size(); ++j)
					if (selectedCurves.elementAt(j).equals(
							hooveredCurves.elementAt(i)))
						found = true;
				if (!found) {
					selectedCurves.add(hooveredCurves.elementAt(i));

					for (int j = 0; j < curves.size(); ++j)
						if (curves.elementAt(j).equals(
								hooveredCurves.elementAt(i)))
							curves.remove(j--);
				}
			}
		else if (mode == MODE.SELECT_CONTROL_POINT) {
			for (int i = 0; i < hooveredCurves.size(); ++i) {
				boolean found = false;
				for (int j = 0; j < selectedCurves.size(); ++j)
					if (selectedCurves.elementAt(j).equals(
							hooveredCurves.elementAt(i)))
						found = true;
				if (!found) {
					selectedCurves.add(hooveredCurves.elementAt(i));

					for (int j = 0; j < curves.size(); ++j)
						if (curves.elementAt(j).equals(
								hooveredCurves.elementAt(i)))
							curves.remove(j--);
				}
			}
			for (int i = 0; i < hooveredPoints.size(); ++i) {
				if (!isSelectedControlPoint(hooveredPoints.elementAt(i))) {
					selectedPoints.add(hooveredPoints.elementAt(i));
				}
			}
		} else if (mode == MODE.MOVE_CURVES || mode == MODE.MOVE_CONTROL_POINTS) {
			int xB = draw.getXEnd();
			int yB = draw.getYEnd();
			int xE = e.getX();
			int yE = e.getY();
			int diffX = Math.abs(xB - xE);
			int diffY = Math.abs(yB - yE);
			if (xB > xE)
				diffX = -diffX;
			if (yB > yE)
				diffY = -diffY;

			if (mode == MODE.MOVE_CURVES)
				translateSelectedCurves(diffX, diffY);
			else
				translateSelectedControlPoints(diffX, diffY);
		}

		draw.resetDragging();
		hooveredCurves.clear();
		hooveredPoints.clear();
		draw.repaint();
	}

	public void mouseDragged(MouseEvent e) {
		if (e.getSource().equals(draw)
				&& (mode == Editor.MODE.SELECT_CURVE
						|| mode == Editor.MODE.DESELECT_CURVE
						|| mode == Editor.MODE.SELECT_CONTROL_POINT || mode == Editor.MODE.DESELECT_CONTROL_POINT)
				&& draw.draggingStarted()) {

			int xBegin = draw.getXBegin();
			int yBegin = draw.getYBegin();
			int xEnd = (e.getX() < 0) ? 0 : e.getX();
			xEnd = (xEnd > selectionTool.maxX) ? selectionTool.maxX - 1 : xEnd;
			int yEnd = (e.getY() < 0) ? 0 : e.getY();
			yEnd = (yEnd > selectionTool.maxY) ? selectionTool.maxY - 1 : yEnd;

			draw.updateDragging(xEnd, yEnd);

			if (mode == MODE.DESELECT_CURVE || mode == MODE.SELECT_CURVE) {
				if (hooveredCurves.size() > 0)
					hooveredCurves.clear();

				for (int x = Math.min(xEnd, xBegin); x < Math.max(xEnd, xBegin); ++x)
					for (int y = Math.min(yEnd, yBegin); y < Math.max(yEnd,
							yBegin); ++y) {
						Curve c = this.selectionTool
								.searchCurve(new Point(x, y));
						if (c != null) {
							boolean found = false;
							for (int i = 0; i < hooveredCurves.size(); ++i)
								if (hooveredCurves.elementAt(i).equals(c))
									found = true;

							if (!found)
								hooveredCurves.add(c);
						}
					}
			} else if (mode == MODE.DESELECT_CONTROL_POINT
					|| mode == MODE.SELECT_CONTROL_POINT) {
				hooveredCurves.clear();
				hooveredPoints.clear();

				for (int x = Math.min(xEnd, xBegin); x <= Math
						.max(xEnd, xBegin); ++x)
					for (int y = Math.min(yEnd, yBegin); y <= Math.max(yEnd,
							yBegin); ++y) {
						Point temp = new Point(x, y);
						if (selectionTool.isControlPoint(temp)) {
							hooveredPoints.add(temp);
							Vector<Curve> temp2 = selectionTool
									.searchCurvesByControlPoint(temp, (short) 0);
							for (int i = 0; i < temp2.size(); ++i) {
								boolean found = false;
								for (int k = 0; k < hooveredCurves.size(); ++k) {
									if (hooveredCurves.elementAt(k).equals(
											temp2.elementAt(i)))
										found = true;
								}

								if (!found)
									hooveredCurves.add(temp2.elementAt(i));

							}
						}
					}
			}
		} else if (e.getSource().equals(draw)
				&& (mode == Editor.MODE.MOVE_CONTROL_POINTS || mode == Editor.MODE.MOVE_CURVES)
				&& draw.draggingStarted()) {

			int xB = draw.getXEnd();
			int yB = draw.getYEnd();
			int xE = e.getX();
			int yE = e.getY();
			int diffX = Math.abs(xB - xE);
			int diffY = Math.abs(yB - yE);
			if (xB > xE)
				diffX = -diffX;
			if (yB > yE)
				diffY = -diffY;

			if (mode == MODE.MOVE_CURVES)
				translateSelectedCurves(diffX, diffY);
			else
				translateSelectedControlPoints(diffX, diffY);

			draw.updateDragging(e.getX(), e.getY());
		}

		draw.repaint();
	}

	public void mouseMoved(MouseEvent e) {
		if (mode == MODE.DESELECT_CURVE || mode == MODE.SELECT_CURVE) {
			boolean repaint = false;
			if (hooveredCurves.size() > 0) {
				hooveredCurves.clear();
				repaint = true;
			}

			if (e.getSource().equals(draw) && e.getX() >= 0 && e.getY() >= 0
					&& e.getX() < selectionTool.maxX
					&& e.getY() < selectionTool.maxY) {
				Curve c = this.selectionTool.searchCurve(new Point(e.getX(), e
						.getY()));
				if (c != null) {
					repaint = true;
					hooveredCurves.add(c);
				}
			}

			if (repaint)
				draw.repaint();
		} else if (mode == MODE.DESELECT_CONTROL_POINT
				|| mode == MODE.SELECT_CONTROL_POINT) {
			boolean repaint = false;
			if (hooveredCurves.size() > 0 || hooveredPoints.size() > 0) {
				hooveredCurves.clear();
				hooveredPoints.clear();
				repaint = true;
			}

			if (e.getSource().equals(draw) && e.getX() >= 0 && e.getY() >= 0
					&& e.getX() < selectionTool.maxX
					&& e.getY() < selectionTool.maxY) {
				Point p = hooverPoint(new Point(e.getX(), e.getY()));
				if (p != null) {
					repaint = true;
				}
			}

			if (repaint)
				draw.repaint();
		}
	}

	public void propertyChange(PropertyChangeEvent evt) {

	}

	private void open() {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CurveEditor files (*.xml)", "xml");

		jfc.setFileFilter(filter);
		if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(draw))
			file.load(jfc.getSelectedFile().getAbsolutePath(), curves);
	}

	private void save() {
		// checken of er reeds een bestandsnaam gekent is waarnaar opgeslagen
		// moet worden.
		String fileName = file.getCurrentFilename();

		if (null == fileName)
			saveAs();
		else
			file.save(fileName, curves);
	}

	private void saveAs() {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CurveEditor files (*.xml)", "xml");
		jfc.setFileFilter(filter);
		if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(draw))
			file.save(jfc.getSelectedFile().getAbsolutePath(), curves);
	}

	private void newFile() {
		reset();
		draw.init(curves, selectedCurves, hooveredCurves, selectedPoints,
				hooveredPoints, true, true, true);
	}

	private boolean toggleCoords() {
		draw.toggleCoords();
		return draw.coords();
	}

	private boolean toggleNrs() {
		draw.toggleNrs();
		return draw.nrs();
	}

	private boolean toggleTangents() {
		draw.toggleTangents();
		return draw.tangents();
	}

	private class Listener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();

			System.out.println(e);
			if (actionCommand.equals("Bezier"))
				currentAlgorithm = getAlgorithm('B', (short) 3);
			else if (actionCommand.equals("Hermite"))
				currentAlgorithm = getAlgorithm('H', (short) 1);
			else if (actionCommand.equals("comboBoxChanged")) {
				String item = (String) ((JComboBox) e.getSource())
						.getSelectedItem();

				if (item.equals("Bezier normal"))
					currentAlgorithm = getAlgorithm('B', (short) 3);
				else if (item.equals("Unlimited 3"))
					currentAlgorithm = getAlgorithm('B', (short) 0);
				else if (item.equals("Hermite normal"))
					currentAlgorithm = getAlgorithm('H', (short) 1);
				else if (item.equals("Cardinal"))
					currentAlgorithm = getAlgorithm('C', (short) 1);
				else if (item.equals("Catmull Rom"))
					currentAlgorithm = getAlgorithm('R', (short) 1);
			} else if (actionCommand.equals("Open"))
				open();
			else if (actionCommand.equals("Save"))
				save();
			else if (actionCommand.equals("Save As ..."))
				saveAs();
			else if (actionCommand.equals("New"))
				newFile();
			else if (actionCommand.equals("New C"))
				changeMode(MODE.NEW_CURVE);
			else if (actionCommand.equals("Select P"))
				changeMode(MODE.SELECT_CONTROL_POINT);
			else if (actionCommand.equals("Select C"))
				changeMode(MODE.SELECT_CURVE);
			else if (actionCommand.equals("Move P"))
				changeMode(MODE.MOVE_CONTROL_POINTS);
			else if (actionCommand.equals("Move C"))
				changeMode(MODE.MOVE_CURVES);
			else if (actionCommand.equals("Delete P")) {
				deleteSelectedControlPoints();
				recalculateCurves();
			} else if (actionCommand.equals("Delete C")) {
				deleteSelectedCurves();
				recalculateSelectedCurves();
			}

			draw.repaint();
		}
	}
}