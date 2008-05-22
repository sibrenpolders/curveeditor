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

import CurveEditor.Core.*;
import CurveEditor.Curves.*;
import CurveEditor.Exceptions.*;
import CurveEditor.Tools.PathSimulation;

/*
 * Deze van Editor afgeleide klasse verbindt de mogelijke fysieke acties van de gebruiker
 * (selecteren, hooveren, klikken, menuselecties) met de juiste methodes van Editor.
 * Kortom: deze klasse implementeert al de Editor-functionaliteit in een GUI.
 */
public class GUI extends Editor implements MouseListener, MouseMotionListener,
		ComponentListener {
	// De GUI-elementen.
	protected ChoiceArea choice;
	protected DrawArea draw;
	protected Menu menu;
	protected Toolbar toolbar;

	// Listeners.
	protected ToolBarListener tBListener;
	protected MenuListener mListener;
	protected ChoiceAreaListener cListener;

	protected DisplaySize displaySize;

	// De container bevat choiceArea en drawArea, en de container wordt
	// aan de container van het frame/window toegevoegd. Op deze manier hebben
	// we de GUI opgesplitst in twee delen: menu- en toolbar enerzijds
	// (horizontaal onder elkaar), choice- en drawarea anderzijds (verticaal
	// langs elkaar, en samen horizontaal onder de rest).
	protected JPanel container;
	protected JFrame frame;

	public GUI() throws InvalidArgumentException {
		super();
		loadComponents();
	}

	// Constructor die m.b.v. een bestand reeds al wat curves inlaadt.
	public GUI(String file) throws InvalidArgumentException {
		super(file);
		loadComponents();
	}

	// Initialiseren van alle GUI-elementen.
	private void loadComponents() throws InvalidArgumentException {
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

		// frame is het window, in feite.
		frame = new JFrame("Curve Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setTitle("Curve Editor");
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(null);

		displaySize = new DisplaySize();

		mListener = new MenuListener();
		menu = new Menu(mListener);
		contentPane.add(menu); // toevoegen

		tBListener = new ToolBarListener();
		toolbar = new Toolbar(tBListener);
		contentPane.add(toolbar); // toevoegen

		// De container die de verticaal langs elkaar geplaatste elementen
		// bevat.
		container = new JPanel();
		container.setLayout(null);
		container.setBounds(0, DisplaySize.MENUHEIGHT
				+ DisplaySize.TOOLBARHEIGHT, DisplaySize.SCREENWIDTH,
				DisplaySize.CHOICEHEIGHT);
		container.add(Box.createRigidArea(new Dimension(10, 0)));

		cListener = new ChoiceAreaListener();
		choice = new ChoiceArea(cListener, cListener);
		container.add(choice); // toevoegen

		draw = new DrawArea(this.curves, this.selectedCurves,
				this.hooveredCurves, this.selectedPoints, this.hooveredPoints);
		draw.addMouseListener(this);
		draw.addMouseMotionListener(this);
		container.add(draw); // toevoegen
		container.add(Box.createRigidArea(new Dimension(10, 0)));
		container.add(Box.createHorizontalGlue());

		contentPane.add(container);

		frame.pack();
		frame.setVisible(true);

		frame
				.setBounds(0, 0, DisplaySize.SCREENWIDTH,
						DisplaySize.SCREENHEIGHT);
		displaySize.setCurrentSize(frame.getSize());
		frame.addComponentListener(this);

		// selectionTool resizen naar de nieuwe dimensies.
		selectionTool.resize(frame.getWidth(), frame.getWidth());

	}

	// _______________________MOUSE EVENTS_______________________

	// Methode die reageert op een click-event in DrawArea.
	// Afhankelijk van de MODE die in Editor geset is, wordt een
	// actie uitgevoerd.
	public void mouseClicked(MouseEvent e) {
		hooveredCurves.clear();
		hooveredPoints.clear();
		if (mode == Editor.MODE.ADD_INPUT) {
			Point a = new Point(e.getX(), e.getY());
			try {
				addPoint(a);
				draw.repaint();
			} catch (InvalidArgumentException e1) {
				e1.printStackTrace();
			}
		} else if (mode == Editor.MODE.DESELECT_CURVE) {
			// Curve zoeken; pickCurve switcht automatisch de selectiestatus
			// van de gevonden curve.
			if (pickCurve(new Point(e.getX(), e.getY())) != null) {
				draw.repaint();
			}
		} else if (mode == Editor.MODE.NEW_CURVE) {
			startNewCurve();
			draw.repaint();
		} else if (mode == Editor.MODE.DESELECT_CONTROL_POINT) {
			// Punt zoeken; pickControlPoints switcht automatisch de
			// selectiestatus van de gevonden curves.
			if (pickControlPoint(new Point(e.getX(), e.getY())) != null) {
				draw.repaint();
			}
		}
	}

	// Indien de cursor drawArea binnenkomt --> van cursorweergave veranderen.
	public void mouseEntered(MouseEvent arg0) {
		draw.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	// Indien de cursor drawArea verlaat --> van cursorweergave veranderen
	// en alles dehooveren.
	public void mouseExited(MouseEvent arg0) {
		draw.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		if (hooveredCurves.size() > 0 || hooveredPoints.size() > 0) {
			hooveredCurves.clear();
			hooveredPoints.clear();
			draw.repaint();
		}
	}

	// Methode die reageert op een press-event in DrawArea.
	// Afhankelijk van de MODE die in Editor geset is, wordt een
	// actie uitgevoerd.
	public void mousePressed(MouseEvent e) {
		// We zitten in een selectiemode --> we beginnen de
		// dragging-functionaliteit van DrawArea.
		if (mode == Editor.MODE.SELECT_CURVE
				|| mode == Editor.MODE.DESELECT_CURVE
				|| mode == Editor.MODE.SELECT_CONTROL_POINT
				|| mode == Editor.MODE.DESELECT_CONTROL_POINT) {
			draw.beginSelectionRectangle(e.getX(), e.getY());
		}
		// We zitten in een movemode --> we beginnen de
		// dragging-functionaliteit van DrawArea maar willen een pijlte i.p.v.
		// een rechthoekje uittekenen.
		else if (mode == MODE.MOVE_CONTROL_POINTS || mode == MODE.MOVE_CURVES) {
			push();
			draw.beginMovingArrow(e.getX(), e.getY());
		}
	}

	// Methode die reageert op een release-event in DrawArea.
	// Afhankelijk van de MODE die in Editor geset is, wordt een
	// actie uitgevoerd.
	public void mouseReleased(MouseEvent e) {
		// We willen curves selecteren --> alle curves in het
		// draggingrechthoekje worden geselecteerd.
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
				} else
					mode = MODE.DESELECT_CURVE;
			}
		// We willen inputpunten selecteren --> alle inputpunten in het
		// draggingrechthoekje worden geselecteerd, net zoals de daarbijhorende
		// Curves.
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

				} else
					mode = MODE.DESELECT_CONTROL_POINT;
			}
			for (int i = 0; i < hooveredPoints.size(); ++i) {
				if (!isSelectedControlPoint(hooveredPoints.elementAt(i))) {
					selectedPoints.add(hooveredPoints.elementAt(i));
				}
			}
		}
		// We willen geselecteerde curves of inputpunten selecteren
		// --> translatieafstand berekenen en al de nodige dingen verschuiven.
		else if (mode == MODE.MOVE_CURVES || mode == MODE.MOVE_CONTROL_POINTS) {
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
			pushNew();
		}

		draw.resetDragging();
		hooveredCurves.clear();
		hooveredPoints.clear();
		draw.repaint();
	}

	// Methode die reageert op een drag-event in DrawArea.
	// Afhankelijk van de MODE die in Editor geset is, wordt een
	// actie uitgevoerd.
	public void mouseDragged(MouseEvent e) {

		// We zitten in een selectiemode --> draggingrectangle van DrawArea
		// updaten en nieuwe gehooverde elementen zoeken.
		if (e.getSource().equals(draw)
				&& (mode == Editor.MODE.SELECT_CURVE
						|| mode == Editor.MODE.DESELECT_CURVE
						|| mode == Editor.MODE.SELECT_CONTROL_POINT || mode == Editor.MODE.DESELECT_CONTROL_POINT)
				&& draw.draggingStarted()) {
			int xBegin = draw.getXBegin();
			int yBegin = draw.getYBegin();
			int xEnd = (e.getX() < 0) ? 0 : e.getX();
			xEnd = (xEnd > selectionTool.maxX()) ? selectionTool.maxX() - 1
					: xEnd;
			int yEnd = (e.getY() < 0) ? 0 : e.getY();
			yEnd = (yEnd > selectionTool.maxY()) ? selectionTool.maxY() - 1
					: yEnd;

			draw.updateDragging(xEnd, yEnd);

			// We zitten in een curveselectiemode --> curves in het
			// draggingrechthoekje zoeken en hooveren.
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
			}

			// We zitten in een puntselectiemode --> inputpunten in het
			// draggingrechthoekje zoeken en hooveren, en ook de daaraan
			// verbonden curves hooveren.
			else if (mode == MODE.DESELECT_CONTROL_POINT
					|| mode == MODE.SELECT_CONTROL_POINT) {
				hooveredCurves.clear();
				hooveredPoints.clear();

				for (int x = Math.min(xEnd, xBegin); x <= Math
						.max(xEnd, xBegin); ++x)
					for (int y = Math.min(yEnd, yBegin); y <= Math.max(yEnd,
							yBegin); ++y) {
						Point temp = new Point(x, y);
						if ((temp = selectionTool.isControlPoint(temp)) != null) {
							hooveredPoints.add(temp);
							Curve temp2 = selectionTool
									.searchCurvesByControlPoint(temp, (short) 0);
							if (temp2 != null) {
								boolean found = false;
								for (int k = 0; k < hooveredCurves.size(); ++k) {
									if (hooveredCurves.elementAt(k).equals(
											temp2))
										found = true;
								}

								if (!found)
									hooveredCurves.add(temp2);

							}
						}
					}
			}
		}

		// We willen geselecteerde curves of inputpunten moven
		// --> translatieafstand berekenen en al de nodige dingen verschuiven.
		else if (e.getSource().equals(draw)
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

	// Methode die reageert op een move-event in DrawArea.
	// Afhankelijk van de MODE die in Editor geset is, wordt een
	// actie uitgevoerd. In curveselectiemodie worden curves al dan niet
	// gehooverd, in puntselectiemode punten.
	public void mouseMoved(MouseEvent e) {
		if (mode == MODE.DESELECT_CURVE || mode == MODE.SELECT_CURVE) {
			hooveredCurves.clear();

			// Indien zich een curve op de locatie bevindt --> deze aan
			// hooveredCurves toevoegen.
			if (e.getSource().equals(draw) && e.getX() >= 0 && e.getY() >= 0
					&& e.getX() < selectionTool.maxX()
					&& e.getY() < selectionTool.maxY())
				hooverCurve(new Point(e.getX(), e.getY()));
		} else if (mode == MODE.DESELECT_CONTROL_POINT
				|| mode == MODE.SELECT_CONTROL_POINT) {
			hooveredCurves.clear();
			hooveredPoints.clear();
			// Indien zich een inputpunt op de locatie bevindt --> deze aan
			// hooveredPoints toevoegen.
			if (e.getSource().equals(draw) && e.getX() >= 0 && e.getY() >= 0
					&& e.getX() < selectionTool.maxX()
					&& e.getY() < selectionTool.maxY())
				hooverPoint(new Point(e.getX(), e.getY()));
		}

		draw.repaint();
	}

	// _______________________FILE I/O_______________________

	// Een nieuw venster openen dat een mappenstructuur voorstelt.
	// Als op "OK" geklikt wordt, wordt a.h.v. de gekozen bestandsnaam
	// en -locatie het desbetreffende bestand ingeladen.
	private void open() {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CurveEditor files (*.xml)", "xml");

		jfc.setFileFilter(filter);
		if (JFileChooser.APPROVE_OPTION == jfc.showOpenDialog(draw))
			loadFile(jfc.getSelectedFile().getAbsolutePath()); // file inladen

		draw.repaint();
	}

	// Methode die uitgevoerd wordt indien de gebruiker op "Save" klikt.
	private void save() {
		// Checken of er reeds een bestandsnaam gekend is waarnaar opgeslagen
		// kan worden. Indien dat niet het geval is --> SaveAs(); anders naar
		// dat bestand opslaan.
		String fileName = file.getCurrentFilename();

		if (null == fileName)
			saveAs();
		else {
			Vector<Curve> tmp = curves;
			tmp.addAll(selectedCurves);
			saveFile(fileName, tmp);
		}
	}

	// Een nieuw venster openen dat een mappenstructuur voorstelt.
	// Als op "OK" geklikt wordt, wordt a.h.v. de gekozen bestandsnaam
	// en -locatie alles naar het desbetreffende bestand opgeslaan.
	private void saveAs() {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CurveEditor files (*.xml)", "xml");
		jfc.setFileFilter(filter);
		if (JFileChooser.APPROVE_OPTION == jfc.showSaveDialog(draw)) {
			Vector<Curve> tmp = curves;
			tmp.addAll(selectedCurves);
			saveFile(jfc.getSelectedFile().getAbsolutePath(), tmp); // opslaan
		}
	}

	// Een volledig nieuwe file starten: alle Vectoren clearen.
	private void newFile() {
		reset();
	}

	// _______________________Listeners_______________________

	/*
	 * deze functies zullen bepaalde gemeenschappelijk events opgevangen, door
	 * de 3 klassen die volgen: ToolbarListener, MenuListener en
	 * ChoiceAreaListener, herkennen en afhandelen Om zodoende minder grote
	 * if-else if - .. - else structuren te krijgen in de 3 genoemde klassen De
	 * functie zal true teruggeven als het event afgehandeld is, false anders.
	 */
	// Alles wat te maken heeft met FILE, wordt gebruikt door Menu EN Toolbar
	private boolean fileEvent(String actionCommand) {
		System.out.println(actionCommand);
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
				|| actionCommand.equals("Start New Curve")) {
			changeMode(MODE.NEW_CURVE);
			choice.toggleEditPanel(true);
			draw.repaint();
			return true;
		} else if (actionCommand.equals("Clr") || actionCommand.equals("Clear")) {
			changeMode(MODE.NONE);
			reset();
			choice.deselect();
			draw.repaint();
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
				|| actionCommand.equals("Move Curves")) {
			changeMode(MODE.MOVE_CURVES);
			eventHandled = true;
		}
		// ander naam, zelfde beestje
		else if (actionCommand.equals("Del C")
				|| actionCommand.equals("Delete Curve")
				|| actionCommand.equals("Delete Curves")) {
			deleteSelectedCurves();
			recalculateSelectedCurves();
			changeMode(MODE.SELECT_CURVE);
			eventHandled = true;
		} else if (actionCommand.equals("Deselect All Curves")) {
			deselectAll();
			changeMode(MODE.SELECT_CURVE);
			eventHandled = true;
		} else if (actionCommand.equals("Select All Curves")) {			
			selectAllCurves();
			eventHandled = true;
		}
		if (eventHandled) {
			choice.toggleEditPanel(true);
			draw.repaint();
		}
		
		return eventHandled;
	}

	private boolean pointEvent(String actionCommand) {
		boolean eventHandled = false, toggle = true;

		// ander naam, zelfde beestje
		if (actionCommand.equals("Sel P")
				|| actionCommand.equals("Select Point")) {
			changeMode(MODE.SELECT_CONTROL_POINT);
			eventHandled = true;
		}
		// ander naam, zelfde beestje
		else if (actionCommand.equals("Mov P")
				|| actionCommand.equals("Move Point")
				|| actionCommand.equals("Move Control Points")) {
			changeMode(MODE.MOVE_CONTROL_POINTS);
			eventHandled = true;
		}
		// ander naam, zelfde beestje
		else if (actionCommand.equals("Del P")
				|| actionCommand.equals("Delete Control Points")) {
			deleteSelectedControlPoints();
			recalculateSelectedCurves();
			eventHandled = true;
			changeMode(MODE.SELECT_CONTROL_POINT);
		} else if (actionCommand.equals("Deselect All Control Points")) {
			deselectAll();
			changeMode(MODE.SELECT_CURVE);
			eventHandled = true;			
		}
		// ander naam, zelfde beestje
		else if (actionCommand.equals("Add Control Point")
				|| actionCommand.equals("Add Point")) {
			changeMode(MODE.ADD_INPUT);
			eventHandled = true;
			toggle = false;
		}

		if (eventHandled && toggle) {
			choice.toggleEditPanel(false);
			draw.repaint();
		}
		
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
			else if (actionCommand.equals("Path Simulation")) {
				(new Thread(new PathSimulation(draw, selectedCurves))).start();
			} else if (actionCommand.equals("Undo")) {
				undo();
				draw.repaint();
			} else if (actionCommand.equals("Redo")) {
				redo();
				draw.repaint();
			} else if (actionCommand.equals("Preferences"))
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
					|| curveEvent(actionCommand) || fileEvent(actionCommand))
				return;
			else if (actionCommand.equals("ADD")) {
				Point a = choice.getInputPoint();
				if (a != null) {
					changeMode(MODE.ADD_INPUT);
					try {
						addPoint(a);
					} catch (InvalidArgumentException e1) {
						e1.printStackTrace();
					}
					draw.repaint();
				} else
					JOptionPane.showMessageDialog(draw, "No point is given",
							"Curve Editor: ERROR", JOptionPane.ERROR_MESSAGE);

			} else if (actionCommand.equals("Select All Curves")) {
				selectAllCurves();
				draw.repaint();
			} else if (actionCommand.equals("Deselect All Curves")) {
				deselectAllCurves();
				changeMode(MODE.SELECT_CURVE);
				draw.repaint();
			} else if (actionCommand.equals("Move Curves")) {
				changeMode(MODE.MOVE_CURVES);
				draw.repaint();
			} else if (actionCommand.equals("Deselect All Control Points")) {
				deselectAll();
				changeMode(MODE.SELECT_CONTROL_POINT);
				draw.repaint();
			} else if (actionCommand.equals("Move Control Points")) {
				changeMode(MODE.MOVE_CONTROL_POINTS);
				draw.repaint();
			} else if (actionCommand.equals("Delete Control Points")) {
				deleteSelectedControlPoints();
				changeMode(MODE.SELECT_CONTROL_POINT);
				draw.repaint();
			} else if (actionCommand.equals("Connect Curves (No Extra Points)")) {
				connectNoExtraPoint();
				mode = MODE.SELECT_CURVE;
				draw.repaint();
			} else if (actionCommand.equals("Connect Curves (C0)")) {
				connectCurvesC0();
				mode = MODE.SELECT_CURVE;
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

	// Methode die uitgevoerd wordt indien het frame geresized wordt.
	// De dimensies van alle GUI-elementen worden dan herberekend en geset.
	public void componentResized(ComponentEvent e) {
		if (frame != null && displaySize != null
				&& displaySize.changeFrameSize(frame.getSize())) {

			// Dimensies zetten.
			container.setBounds(0, DisplaySize.MENUHEIGHT
					+ DisplaySize.TOOLBARHEIGHT, DisplaySize.SCREENWIDTH,
					DisplaySize.CHOICEHEIGHT);
			choice.setSize();
			draw.setSize();
			menu.setSize();
			toolbar.setSize();
			try {
				selectionTool.resize(DisplaySize.DRAWWIDTH,
						DisplaySize.DRAWHEIGHT);
			} catch (InvalidArgumentException e1) {
				e1.printStackTrace();
			}
		}
		frame.repaint();
	}

	public void componentShown(ComponentEvent e) {

	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}
}
