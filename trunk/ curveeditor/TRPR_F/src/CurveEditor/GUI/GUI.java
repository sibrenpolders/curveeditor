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
import CurveEditor.Core.Editor;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class GUI extends Editor implements MenuListener, MouseListener {
	protected ChoiceArea choice;
	protected DrawArea draw;
	private Menu menu;
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
		menu.setVisible(true);
		contentPane.add(menu);

		JPanel screen = new JPanel();
		screen.setLayout(new BoxLayout(screen, BoxLayout.X_AXIS));

		screen.add(Box.createRigidArea(new Dimension(10, 0)));

		choice = new ChoiceArea(listener);
		screen.add(choice);

		draw = new DrawArea(this.curves, this.selectedCurves);
		draw.addMouseListener(this);
		screen.add(draw);

		screen.add(Box.createRigidArea(new Dimension(10, 0)));
		screen.add(Box.createHorizontalGlue());

		contentPane.add(screen);

		frame.pack();
		frame.setVisible(true);
		testMethod();
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

		draw = new DrawArea(this.curves, this.selectedCurves);
		draw.addMouseListener(this);
		contentPane.add(draw);

		frame.pack();
		frame.setVisible(true);
	}

	public void testMethod() {
		setCurrentAlgorithm('B', (short) 3);
		selectedCurves.add(new Curve(currentAlgorithm.getType(),
				currentAlgorithm.getDegree()));
		selectedCurves.get(0).addInput(new Point(0, 0));
		selectedCurves.get(0).addInput(new Point(50, 50));
		selectedCurves.get(0).addInput(new Point(50, 120));
		selectedCurves.get(0).addInput(new Point(100, 50));

		getAlgorithm(selectedCurves.get(0).getType(),
				selectedCurves.get(0).getDegree()).calculate(
				selectedCurves.get(0));

		draw.repaint();

		setMode(MODE.ADD_INPUT);
	}

	public void menuCanceled(MenuEvent arg0) {

	}

	public void menuDeselected(MenuEvent arg0) {

	}

	public void menuSelected(MenuEvent arg0) {

	}

	public void mouseClicked(MouseEvent e) {
		if (mode == Editor.MODE.ADD_INPUT) {
			for (int i = 0; i < selectedCurves.size(); ++i) {
				Curve c = selectedCurves.get(i);
				c.addInput(new Point(e.getX(), e.getY()));
				// Bij Hermiet ( type == 'H' ) is het 2de ingegeven punt
				// telkens de tangens. Dus er moet niet getekend worden voordat
				// deze is ingegeven
				if (c.getType() != 'H' || c.getInput().size() % 2 == 0)

					currentAlgorithm.calculate(selectedCurves.get(i));
				// this.getAlgorithm(selectedCurves.get(i).getType(),
				// selectedCurves.get(i).getDegree()).calculateCurve(
				// selectedCurves.get(i));

			}
			draw.repaint();
		} else if (mode == Editor.MODE.SELECT_CURVE) {
			// zoek curve en voeg toe, of zo
		} else if (mode == Editor.MODE.NEW_CURVE) {
			startNewCurve();
			selectedCurves.get(0).addInput(new Point(e.getX(), e.getY()));
			setMode(MODE.ADD_INPUT);
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
		draw.reset(curves, selectedCurves);
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

			for (int i = 0; i < selectedCurves.size(); ++i) {
				Curve c = selectedCurves.get(i);
				c.setType(currentAlgorithm.getType());
				// Bij Hermiet ( type == 'H' ) is het 2de ingegeven punt
				// telkens de tangens. Dus er moet niet getekend worden voordat deze is ingegeven
				if ( c.getType() != 'H' || c.getInput().size() % 2 == 0 )
				{
					c.clearOutput();
					currentAlgorithm.calculateCurveComplete (
									selectedCurves.get(i));
				}
			}
			draw.emptyField();
			draw.repaint();
		}
	}
}