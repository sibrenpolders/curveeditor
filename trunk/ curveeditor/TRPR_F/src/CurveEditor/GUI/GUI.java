package CurveEditor.GUI;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import CurveEditor.Algorithms.Algorithm;
import CurveEditor.Core.Curve2DArray;
import CurveEditor.Core.CurveHashMap;
import CurveEditor.Core.Editor;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class GUI extends Editor implements MenuListener, MouseListener {
	protected ChoiceArea choice;
	protected DrawArea draw;
	private Menu menu;
	private Toolbar toolbar;
	private Listener listener;

	public GUI() {
		super();
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
				this.hooveredCurves, this.selectedPoints);
		draw.addMouseListener(this);
		screen.add(draw);
		screen.add(Box.createRigidArea(new Dimension(10, 0)));
		screen.add(Box.createHorizontalGlue());

		contentPane.add(screen);

		frame.pack();
		frame.setVisible(true);
		// testMethod();

		selectionTool = new CurveHashMap(600, 600);
		selectionTool2 = new Curve2DArray(600, 600);

	}

	public GUI(String filename) {
		super(filename);

		JFrame frame = new JFrame("Curve Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setTitle("Curve Editor");
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		listener = new Listener();
		menu = new Menu(listener);
		contentPane.add(menu);

		draw = new DrawArea(this.curves, this.selectedCurves,
				this.hooveredCurves, this.selectedPoints);
		draw.addMouseListener(this);
		contentPane.add(draw);

		frame.pack();
		frame.setVisible(true);

		selectionTool = new CurveHashMap(draw.getWidth(), draw.getHeight());
		selectionTool2 = new Curve2DArray(600, 600);
	}

	public void testMethod() {
		setCurrentAlgorithm('H', (short) 1);
		selectedCurves.add(new Curve(currentAlgorithm.getType(),
				currentAlgorithm.getDegree()));
		selectedCurves.get(0).addInput(new Point(0, 0));

		getAlgorithm(selectedCurves.get(0).getType(),
				selectedCurves.get(0).getDegree()).calculate(
				selectedCurves.get(0));

		draw.repaint();

		changeMode(MODE.ADD_INPUT);
	}

	public void menuCanceled(MenuEvent arg0) {

	}

	public void menuDeselected(MenuEvent arg0) {

	}

	public void menuSelected(MenuEvent arg0) {

	}

	public void mouseClicked(MouseEvent e) {
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

					selectionTool2.deleteCurve(c);
					selectionTool2.addCurve(c);
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
	}

	public void mousePressed(MouseEvent e) {
		// kunnen we gebruiken voor curves te "slepen", bijvoorbeeld
	}

	public void mouseReleased(MouseEvent e) {

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
		draw.reset(curves, selectedCurves, hooveredCurves, selectedPoints,
				true, true, true);
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
			else if (actionCommand.equals("New Curve"))
				changeMode(MODE.NEW_CURVE);
			else if (actionCommand.equals("Select Point"))
				changeMode(MODE.SELECT_CONTROL_POINT);
			else if (actionCommand.equals("Select Curve"))
				changeMode(MODE.SELECT_CURVE);

			// for (int i = 0; i < selectedCurves.size(); ++i) {
			// Curve c = selectedCurves.get(i);
			// c.setType(currentAlgorithm.getType());
			// // Bij Hermiet ( type == 'H' ) is het 2de ingegeven punt
			// // telkens de tangens. Dus er moet niet getekend worden voordat
			// // deze is ingegeven
			// c.clearOutput();
			// currentAlgorithm.calculateComplete(selectedCurves.get(i));
			// }
			draw.repaint();
		}
	}
}