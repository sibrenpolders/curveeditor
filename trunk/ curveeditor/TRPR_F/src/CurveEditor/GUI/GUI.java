package CurveEditor.GUI;

import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import CurveEditor.Core.CurveContainer;
import CurveEditor.Core.Editor;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;

public class GUI extends Editor implements MouseListener, MouseMotionListener,
		ComponentListener {
	protected ChoiceArea choice;
	protected DrawArea draw;
	private Menu menu;
	private Toolbar toolbar;
	private ToolBarListener tBListener;
	private MenuListener mListener;
	private ChoiceAreaListener cListener;
	private DisplaySize displaySize;
	private JPanel container;
	private JFrame frame; // container frame

	public GUI() {
		super();
		loadComponents();
	}

	public GUI(String filename) {
		super(filename);
		loadComponents();
	}

	private void loadComponents() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		frame = new JFrame("Curve Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setTitle("Curve Editor");
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(null);// new BoxLayout(contentPane,
									// BoxLayout.Y_AXIS));

		displaySize = new DisplaySize();

		mListener = new MenuListener();
		menu = new Menu(mListener);
		contentPane.add(menu);

		tBListener = new ToolBarListener();
		toolbar = new Toolbar(tBListener);
		contentPane.add(toolbar);

		container = new JPanel();
		container.setLayout(null); // new BoxLayout(screen, BoxLayout.X_AXIS));
		container.setBounds(0, DisplaySize.MENUHEIGHT
				+ DisplaySize.TOOLBARHEIGHT, DisplaySize.SCREENWIDTH,
				DisplaySize.CHOICEHEIGHT);
		container.add(Box.createRigidArea(new Dimension(10, 0)));

		cListener = new ChoiceAreaListener();
		choice = new ChoiceArea(cListener, cListener);
		container.add(choice);

		draw = new DrawArea(this.curves, this.selectedCurves,
				this.hooveredCurves, this.selectedPoints, this.hooveredPoints);
		draw.addMouseListener(this);
		draw.addMouseMotionListener(this);
		container.add(draw);
		container.add(Box.createRigidArea(new Dimension(10, 0)));
		container.add(Box.createHorizontalGlue());

		contentPane.add(container);

		frame.pack();
		frame.setVisible(true);

		selectionTool = new CurveContainer(600, 600);

		frame
				.setBounds(0, 0, DisplaySize.SCREENWIDTH,
						DisplaySize.SCREENHEIGHT);
		displaySize.setCurrentSize(frame.getSize());
		frame.addComponentListener(this);
	}

	public void mouseClicked(MouseEvent e) {
		hooveredCurves.clear();
		hooveredPoints.clear();

		if (mode == Editor.MODE.ADD_INPUT) {
			Point a = new Point(e.getX(), e.getY());
			addPoint(a);
			draw.repaint();
		} else if (mode == Editor.MODE.DESELECT_CURVE) {
			Curve c = pickCurve(new Point(e.getX(), e.getY()));
			if (c != null) {
				draw.repaint();
				mode = MODE.DESELECT_CURVE;
			}
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

				if (found)
					mode = MODE.DESELECT_CURVE;
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

		recalculateCurves();
		draw.repaint();
	}

	private void save() {
		// checken of er reeds een bestandsnaam gekent is waarnaar opgeslagen
		// moet worden.
		String fileName = file.getCurrentFilename();

		if (null == fileName)
			saveAs();
		else {
			Vector<Curve> tmp = curves;
			tmp.addAll(selectedCurves);
			file.save(fileName, tmp);
		}
	}

	private void saveAs() {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CurveEditor files (*.xml)", "xml");
		jfc.setFileFilter(filter);
		if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(draw)) {
			Vector<Curve> tmp = curves;
			tmp.addAll(selectedCurves);
			file.save(jfc.getSelectedFile().getAbsolutePath(), tmp);
		}
	}

	private void newFile() {
		reset();
		draw.init(curves, selectedCurves, hooveredCurves, selectedPoints,
				hooveredPoints, draw.coords(), draw.tangents(), draw.nrs());
	}

	// Deze functie is (tijdelijk?) ter vervanging van uw recalculateSelected
	// curves...
	// Ik weet niet echt hoe die functie werkt maar ik heb problemen met van
	// bezier lineair naar hermite te gaan
	// Eigenlijk altijd problemen als ik m.b.v. deze functie naar hermite ga
	private void recalS() {
		for (int i = 0; i < selectedCurves.size(); ++i) {
			selectedCurves.get(i).clearOutput();
			currentAlgorithm.calculateComplete(selectedCurves.get(i));
		}

		draw.repaint();
	}

	/*
	 * deze functies zullen bepaalde gemeenschappelijk events opgevangen, door
	 * de 3 klassen die volgen: ToolbarListener, MenuListener en
	 * ChoiceAreaListener, herkennen en afhandelen Om zodoende minder grote
	 * if-else if - .. - else structuren te krijgen in de 3 genoemde klassen De
	 * functie zal true teruggeven als het event afgehandeld is, false anders.
	 */
	// Alles wat te maken heeft met FILE, wordt gebruikt door Menu EN Toolbar
	private boolean fileEvent(String actionCommand) {
		if (actionCommand.equals("Open")) {
			open();
			return true;
		} else if (actionCommand.equals("none")) {
			changeMode(MODE.NONE);
			choice.deselect();
			return true;
		} else if (actionCommand.equals("Save")) {
			save();
			return true;
		} else if (actionCommand.equals("New")) {
			newFile();
			return true;
		}
		// 2 verschillende namen, maar hetzelfde beestje
		else if (actionCommand.equals("New C")
				|| actionCommand.equals("New Curve")
				|| actionCommand.equals("Start a new curve")) {
			changeMode(MODE.NEW_CURVE);
			choice.toggleEditPanel(true);
			return true;
		} else if (actionCommand.equals("Clr") || actionCommand.equals("Clear")) {
			changeMode(MODE.NONE);
			reset();
			choice.deselect();
		}
		return false;
	}

	private boolean algorithmEvent(String actionCommand) {
		boolean eventHandled = false;

		if (actionCommand.equals("Bezier")) {
			setCurrentAlgorithm('L');
			eventHandled = true;
		} else if (actionCommand.equals("Hermite")) {
			setCurrentAlgorithm('H');
			eventHandled = true;
		} else if (actionCommand.equals("Bezier C0")) {
			setCurrentAlgorithm('B');
			eventHandled = true;
		} else if (actionCommand.equals("Bezier G1")) {
			setCurrentAlgorithm('G');
			eventHandled = true;
		} else if (actionCommand.equals("Linear")) {
			setCurrentAlgorithm('L');
			eventHandled = true;
		} else if (actionCommand.equals("Bezier C1")) {
			setCurrentAlgorithm('C');
			eventHandled = true;
		} else if (actionCommand.equals("Hermite Normal")) {
			setCurrentAlgorithm('H');
			eventHandled = true;
		} else if (actionCommand.equals("Cardinal")) {
			setCurrentAlgorithm('A');
			eventHandled = true;
		} else if (actionCommand.equals("Catmull-Rom")) {
			setCurrentAlgorithm('R');
			eventHandled = true;
		}

		if (eventHandled) {
			// de geslecteerde curves zijn onder invloed van deze keuze
			recalculateSelectedCurves();
			// recalS();
			draw.repaint();
		}

		return eventHandled;
	}

	private boolean curveEvent(String actionCommand) {
		boolean eventHandled = false;

		// ander naam, zelfde beestje
		if (actionCommand.equals("Sel C")
				|| actionCommand.equals("Select Curve")) {
			changeMode(MODE.SELECT_CURVE);
			eventHandled = true;
		}
		// ander naam, zelfde beestje
		else if (actionCommand.equals("Mov C")
				|| actionCommand.equals("Move Curve")
				|| actionCommand.equals("Move Selected Curves")) {
			changeMode(MODE.MOVE_CURVES);
			eventHandled = true;
		}
		// ander naam, zelfde beestje
		else if (actionCommand.equals("Del C")
				|| actionCommand.equals("Delete Curve")
				|| actionCommand.equals("Delete Selected Curves")) {
			deleteSelectedCurves();
			recalS();
			eventHandled = true;
		} else if (actionCommand.equals("Deselect All")) // telt ook mee voor
			// pointEvent int
			// choiceArea & de
			// Deselect all van
			// de menuBar
			deselectAll();

		if (eventHandled)
			choice.toggleEditPanel(true);
		return eventHandled;
	}

	private boolean pointEvent(String actionCommand) {
		boolean eventHandled = false;

		// ander naam, zelfde beestje
		if (actionCommand.equals("Sel P")
				|| actionCommand.equals("Select Point")) {
			changeMode(MODE.SELECT_CONTROL_POINT);
			eventHandled = true;
		}
		// ander naam, zelfde beestje
		else if (actionCommand.equals("Mov P")
				|| actionCommand.equals("Move Point")
				|| actionCommand.equals("Move Selected Control Points")) {
			changeMode(MODE.MOVE_CONTROL_POINTS);
			eventHandled = true;
		}
		// ander naam, zelfde beestje
		else if (actionCommand.equals("Del P")
				|| actionCommand.equals("Delete Point")) {
			deleteSelectedControlPoints();
			recalS();
			eventHandled = true;
		} else if (actionCommand.equals("Deselect Point")) {
			eventHandled = true;
			deleteSelectedControlPoints();
		}
		// ander naam, zelfde beestje
		else if (actionCommand.equals("Add P")
				|| actionCommand.equals("Add Point")) {
			changeMode(MODE.ADD_INPUT);
			eventHandled = true;
		}

		if (eventHandled)
			choice.toggleEditPanel(false);
		return eventHandled;
	}

	private class ToolBarListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();

			if (fileEvent(actionCommand) || pointEvent(actionCommand)
					|| curveEvent(actionCommand))
				return;
		}
	}

	private class MenuListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();

			if (fileEvent(actionCommand) || algorithmEvent(actionCommand)
					|| pointEvent(actionCommand) || curveEvent(actionCommand))
				return;

			else if (actionCommand.equals("undo"))
				System.out.println("BE GONE!");
			else if (actionCommand.equals("redo"))
				System.out.println("COME BACK!");
			else if (actionCommand.equals("Preferrences"))
				System.out.println("PREF");
			else if (actionCommand.equals("Save As ..."))
				saveAs();
		}
	}

	private class ChoiceAreaListener implements ItemListener, ActionListener {
		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();

			if (actionCommand.equals("comboBoxChanged"))
				actionCommand = (String) ((JComboBox) e.getSource())
						.getSelectedItem();

			if (algorithmEvent(actionCommand) || pointEvent(actionCommand)
					|| curveEvent(actionCommand))
				return;
			else if (actionCommand.equals("ADD")) {
				Point a = choice.getInputPoint();
				if (a != null) {
					changeMode(MODE.ADD_INPUT);
					addPoint(a);
				} else
					JOptionPane.showMessageDialog(draw, "No point is given",
							"Curve Editor: ERROR", JOptionPane.ERROR_MESSAGE);

			} else if (actionCommand.equals("Select All Curves")) {
				selectAllCurves();
				draw.repaint();
			} else if (actionCommand.equals("Deselect All Curves")) {
				deselectAllCurves();
				draw.repaint();
			} else if (actionCommand.equals("Move Selected Curves")) {
				changeMode(MODE.MOVE_CURVES);
				draw.repaint();
			} else if (actionCommand.equals("Deselect All Control Points")) {
				deselectAll();
				draw.repaint();
			} else if (actionCommand.equals("Move Selected Control Points")) {
				changeMode(MODE.MOVE_CONTROL_POINTS);
				draw.repaint();
			} else if (actionCommand.equals("Delete Selected Control Points")) {
				deleteSelectedControlPoints();
				draw.repaint();
			} else if (actionCommand
					.equals("Connect Selected Curves (No Extra Points)")) {
				connectNoExtraPoint();
				draw.repaint();
			} else if (actionCommand.equals("Connect Selected Curves (C0)")) {
				connectCurvesC0();
				draw.repaint();
			}
		}

		public void itemStateChanged(ItemEvent e) {
			String eventName = e.getItemSelectable().toString();

			if (eventName.contains("Coordinates"))
				draw.toggleCoords();
			else if (eventName.contains("Tangents"))
				draw.toggleTangents();
			else if (eventName.contains("Numbers"))
				draw.toggleNrs();
			draw.repaint();
		}

		// cleared de output van alle geslecteerde punten en herberekend ze in
		// functie van het
		// nieuwe geselecteerde algorithme. Weet niet juist wat uw functie doet
		// dus heb deze meer even
		// hier gezet.

	}

	@Override
	public void componentHidden(ComponentEvent e) {

	}

	@Override
	public void componentMoved(ComponentEvent e) {

	}

	@Override
	public void componentResized(ComponentEvent e) {
		if (displaySize.frameSizeChanged(frame.getSize())) { // de
																// schermgrootte
																// is aangepast
																// door de
																// gebruiker
			container.setBounds(0, DisplaySize.MENUHEIGHT
					+ DisplaySize.TOOLBARHEIGHT, DisplaySize.SCREENWIDTH,
					DisplaySize.CHOICEHEIGHT);
			choice.setSize();
			draw.setSize();
			menu.setSize();
			toolbar.setSize();
			selectionTool.resize(DisplaySize.DRAWWIDTH, DisplaySize.DRAWHEIGHT);
		}

		frame.repaint();
	}

	@Override
	public void componentShown(ComponentEvent e) {

	}
}
