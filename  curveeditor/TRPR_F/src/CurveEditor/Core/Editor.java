package CurveEditor.Core;

import java.util.EmptyStackException;
import java.util.Vector;
import CurveEditor.Algorithms.*;
import CurveEditor.Curves.*;
import CurveEditor.Exceptions.*;

/*
 * Deze klasse is het centraal orgaan van heel de applicatie. Hier worden
 * de curves in specifieke lijsten bijgehouden (geselecteerde, ongeselecteerde
 * en gehooverde). Bewerkingen die men uiteindelijk op de (lijsten van) curves kan 
 * doen worden in deze klasse voorzien; deze zorgen er uiteraard ook voor dat de 
 * opslag van de curves consequent blijft. Ook wordt hier bijgehouden in welke MODE
 * de gebruiker zich nu bevindt; dit heeft namelijk zijn weerslag op acties die de
 * gebruiker dan doet.  
 * Kortom: deze klasse brengt alles samen.
 */
public class Editor {

	// De verschillende modes waarin de gebruiker zich kan bevinden.
	protected static enum MODE {
		NONE, ADD_INPUT, SELECT_CURVE, SELECT_CONTROL_POINT, DESELECT_CURVE, DESELECT_CONTROL_POINT, DESELECT_ALL, NEW_CURVE, MOVE_CURVES, MOVE_CONTROL_POINTS
	};

	// Default zoekbereik indien vanuit een gegeven punt
	// een input- of outputpunt van de curves gezocht wordt.
	protected static short SEARCH_RANGE = 3;

	protected MODE mode;
	// Lijst van alle geïmplementeerde algoritmen.
	protected Vector<Algorithm> algorithms;
	protected Vector<Curve> curves; // Lijst van ongeselecteerde curves.
	// Lijst van geselecteerde curves --> hier worden de bewerkingen op gedaan.
	protected Vector<Curve> selectedCurves;
	// Lijst van curves die onder de cursor staan; moeten wel in curves of
	// selectedCurves blijven staan.
	protected Vector<Curve> hooveredCurves;
	// Lijst van geselecteerde controlepunten --> kunnen verplaatst worden;
	// curves die dit punt als controlepunt gebruiken, moeten in selectedCurves
	// staan.
	protected Vector<Point> selectedPoints;
	// Lijst van punten die onder de cursor staan.
	protected Vector<Point> hooveredPoints;
	// Algorithm dat momenteel geselecteerd is; verandert men dit, dan wordt dit
	// autom. verandert voor elke selectedCurve.
	protected Algorithm currentAlgorithm;
	protected FileIO file; // Zorgt voor het uit- en inlezen van bestanden.
	// Datastructuur die kan gebruikt worden om na te gaan op welke curve
	// geklikt is, over welke curve of inputpunt gehoovered wordt, etc..
	protected CurveContainer selectionTool;

	// Default constructor.
	public Editor() {
		init();
	}

	// Constructor die m.b.v. een bestand reeds al wat curves inlaadt.
	public Editor(String file) {
		init();
		loadFile(file);
	}

	// Alle variabelen op hun initiële waarde zetten.
	protected void init() {
		mode = MODE.NONE;

		// Vectoren aanmaken.
		algorithms = new Vector<Algorithm>();
		curves = new Vector<Curve>();
		selectedCurves = new Vector<Curve>();
		hooveredCurves = new Vector<Curve>();
		selectedPoints = new Vector<Point>();
		hooveredPoints = new Vector<Point>();

		// Hier moeten alle geïmplementeerde algoritmes ingegeven worden.
		algorithms.add(new Linear('L', (short) 1)); // 'L'
		algorithms.add(new Bezier('B', (short) 3)); // 'B' --> C0
		algorithms.add(new BezierG1('G', (short) 3)); // 'G' --> G1
		algorithms.add(new BezierC1('C', (short) 3)); // 'C' --> C1
		algorithms.add(new Hermite('H', (short) 0)); // 'H'
		algorithms.add(new HermiteCardinal('A', (short) 0)); // 'A'
		algorithms.add(new HermiteCatmullRom('R', (short) 0)); // 'R'

		// Lineair is default algoritme.
		currentAlgorithm = getAlgorithm('L', (short) 1);

		file = new FileIO();

		try {
			// Er is nog geen tekencanvas of i.d.a. --> dimensieloos.
			selectionTool = new CurveContainer(1, 1);
		} catch (InvalidArgumentException e) {
			System.out
					.println("Error while creating selectionTool: Invalid Arguments.");
		}
	}

	// Alles van punten en curves resetten.
	protected void reset() {
		push();

		// Clearen, en niet opnieuw aanmaken, om referenties elders
		// in het programma niet corrupt te maken !
		curves.clear();
		selectedCurves.clear();
		hooveredCurves.clear();
		selectedPoints.clear();
		hooveredPoints.clear();
		selectionTool.clear();
	}

	// _______________________ALGORITHM BEHEER_______________________

	// Algoritme zoeken a.h.v. een gegeven type en orde.
	// null wordt gereturned indien niks gevonden werd.
	protected Algorithm getAlgorithm(char type, short degree) {
		for (int i = 0; i < algorithms.size(); ++i)
			if (algorithms.get(i).getType() == type
					&& algorithms.get(i).getDegree() == degree)
				return algorithms.get(i);
		return null;
	}

	// Algoritme zoeken a.h.v. een gegeven type.
	// null wordt gereturned indien niks gevonden werd.
	protected Algorithm getAlgorithm(char type) {
		for (int i = 0; i < algorithms.size(); ++i)
			if (algorithms.get(i).getType() == type)
				return algorithms.get(i);
		return null;
	}

	// currentAlgorithm veranderen naar het algoritme met het
	// gegeven type en de gegeven orde. Indien die niet bestaat,
	// verandert er niks.
	protected void setCurrentAlgorithm(char type, short degree) {
		Algorithm temp = getAlgorithm(type, degree); // zoeken

		if (temp != null) {
			currentAlgorithm = temp;
			// De op dat moment geselecteerde curves naar dat type veranderen.
			for (int i = 0; i < selectedCurves.size(); ++i) {
				// De oorspronkeleijke gegevens verwijderen.
				selectionTool.deleteCurve(selectedCurves.elementAt(i));
				selectedCurves.get(i).clearOutput();
				try {
					// De nieuwe gegevens berekenen en opslaan.
					selectedCurves.get(i).setType(type);
					selectedCurves.get(i).setDegree(degree);
					currentAlgorithm.calculateComplete(selectedCurves.get(i));
					selectionTool.addCurve(selectedCurves.elementAt(i));
				} catch (InvalidArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// currentAlgorithm veranderen naar het algoritme met het
	// gegeven type. Indien die niet bestaat,
	// verandert er niks.
	protected void setCurrentAlgorithm(char type) {
		Algorithm temp = getAlgorithm(type);

		if (temp != null) {
			currentAlgorithm = temp;
			// De op dat moment geselecteerde curves naar dat type veranderen.
			for (int i = 0; i < selectedCurves.size(); ++i) {
				// De oorspronkeleijke gegevens verwijderen.
				selectionTool.deleteCurve(selectedCurves.elementAt(i));
				selectedCurves.get(i).clearOutput();
				try {
					// De nieuwe gegevens berekenen en opslaan.
					selectedCurves.get(i).setType(type);
					selectedCurves.get(i).setDegree(
							currentAlgorithm.getDegree());
					currentAlgorithm.calculateComplete(selectedCurves.get(i));
					selectionTool.addCurve(selectedCurves.elementAt(i));
				} catch (InvalidArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// _______________________MODE BEHEER_______________________

	// De huidige MODE veranderen. Deze functie dient aangeroepen te worden
	// indien de gebruiker met de applicatie geïnterageerd heeft en het nodig
	// is om van MODE te veranderen.
	protected void changeMode(Editor.MODE m) {
		if (m == MODE.NONE) {
			deselectAll(); // Alles deselecteren.
			this.mode = m;
		} else if (m == MODE.NEW_CURVE) {
			// Nieuwe curve aanmaken --> autom. komt men dan in
			// ADD_INPUT-mode terecht zodat men de nieuw aangemaakte
			// curve kan beginnen vullen.
			startNewCurve();
			this.mode = MODE.ADD_INPUT;
		} else if (m == MODE.DESELECT_ALL) {
			deselectAll(); // Alles deselecteren.
			// Indien men niet in een select- of deselect-mode zat
			// --> MODE op NONE zetten.
			if (mode != MODE.SELECT_CURVE && mode != MODE.SELECT_CONTROL_POINT
					&& mode != MODE.DESELECT_CONTROL_POINT
					&& mode != MODE.DESELECT_CURVE)
				this.mode = MODE.NONE;
		} else if (m == MODE.ADD_INPUT) {
			// De gebruiker wenst nieuwe punten in te geven --> enkel curves
			// hoeven nog geselecteerd te worden.
			selectedPoints.clear();
			this.mode = m;
		} else if (m == MODE.SELECT_CURVE || m == MODE.DESELECT_CURVE
				|| m == MODE.SELECT_CONTROL_POINT || m == MODE.SELECT_CURVE) {
			deselectAll(); // Alles deselecteren.
			this.mode = m;
		} else {
			this.mode = m;
		}
	}

	// Alles wordt gedeselecteerd.
	protected void deselectAll() {
		// Geselecteerde curves bij de ongeselecteerde voegen.
		curves.addAll(selectedCurves);
		selectedCurves.clear();
		selectedPoints.clear();
	}

	// _______________________CURVE BEHEER_______________________

	// De huidige situatie aanpassen opdat men één nieuwe lege
	// curve zou kunnen gaan aanpassen.
	protected void startNewCurve() {
		deselectAll();
		// selectedCurves bevat slechts één curve, een voorlopig lege.
		selectedCurves.add(new Curve(currentAlgorithm.getType(),
				currentAlgorithm.getDegree()));

		this.mode = MODE.ADD_INPUT;
	}

	// M.b.v. een gegeven Point, wordt een curve gezocht.
	// Deze curve moet een punt in het zoekbereik van het
	// gegeven als outputpunt hebben. null wordt terug-
	// gegeven indien zo geen Curve gevonden wordt.
	protected Curve pickCurve(Point p) {
		if (p == null)
			return null;

		// De Curve zoeken.
		Curve c = this.selectionTool.searchCurve(p);

		if (c == null)
			return null;
		else {
			// De gevonden Curve moeten van selectiestatus switchen.
			if (isSelectedCurve(c))
				deselectCurve(c);
			else
				selectCurve(c);

			return c;
		}
	}

	// De index van de curve in <curves> wordt gezocht.
	// -1 wordt teruggegeven indien deze niet gevonden wordt.
	protected int findIndexCurve(Curve c) {
		int index = 0;
		boolean stop = false;

		if (c != null)
			for (; index < curves.size() && !stop; ++index)
				if (c.equals(curves.get(index)))
					stop = true;

		if (stop)
			return --index;
		else
			return -1;
	}

	// De index van de curve in <selectedCurves> wordt gezocht.
	// -1 wordt teruggegeven indien deze niet gevonden wordt.
	protected int findIndexSelectedCurve(Curve c) {
		int index = 0;
		boolean stop = false;

		if (c != null)
			for (; index < selectedCurves.size() && !stop; ++index)
				if (c.equals(selectedCurves.get(index)))
					stop = true;

		if (stop)
			return --index;
		else
			return -1;
	}

	// Nagaan of de meegegeven Curve geselecteerd is of niet.
	protected boolean isSelectedCurve(Curve c) {
		for (int j = 0; j < selectedCurves.size(); ++j)
			if (selectedCurves.elementAt(j).equals(c))
				return true;
		return false;
	}

	// De meegegeven curve verandert van ongeselecteerde naar
	// geselecteerde status. Het zoeken naar de Curve zelf gebeurt dus _niet_
	// hier.
	protected void selectCurve(Curve c) {
		// Zoeken waar de curve zich in <curves> bevindt.
		int index = findIndexCurve(c);

		if (index != -1) {
			selectedCurves.add(curves.get(index));
			curves.remove(index);
		}
	}

	// De meegegeven curve verandert van geselecteerde naar
	// ongeselecteerde status. Het zoeken naar de Curve zelf gebeurt dus _niet_
	// hier.
	protected void deselectCurve(Curve c) {
		// Zoeken waar de curve zich in <selectedCurves> bevindt.
		int index = findIndexSelectedCurve(c);

		if (index != -1) {
			curves.add(selectedCurves.get(index));
			selectedCurves.remove(index);
		}
	}

	// Alle curves krijgen de status ongeselecteerd.
	protected void deselectAllCurves() {
		for (int i = 0; i < selectedCurves.size(); ++i) {
			curves.add(selectedCurves.get(i));
		}

		selectedCurves.clear();
	}

	// Alle curves krijgen de status geselecteerd.
	protected void selectAllCurves() {
		for (int i = 0; i < curves.size(); ++i) {
			selectedCurves.add(curves.get(i));
		}

		curves.clear();
	}

	// M.b.v. een gegeven punt een in het zoekbereik bestaande curve
	// zoeken. Deze curve krijgt de gehooverde status en deze wordt
	// teruggegeven.
	// Dit dient vooral ter controle of er iets gevonden werd. null wordt
	// teruggegeven indien er niks gevonden werd.
	protected Curve hooverCurve(Point p) {
		if (p == null)
			return null;

		Curve c = this.selectionTool.searchCurve(p);
		if (c != null)
			hooveredCurves.add(c);

		return c;
	}

	// De meegegeven curve verandert van niet-gehooverde naar
	// gehooverde status. Deze blijft de status van geselecteerd of
	// ongeselecteerd dus behouden.
	// Het zoeken naar de Curve zelf gebeurt dus _niet_ hier.
	protected void hooverCurve(Curve c) {
		// Zoeken waar de curve zich in <curves> bevindt.
		int index = findIndexCurve(c);

		if (index != -1)
			hooveredCurves.add(curves.get(index));
	}

	// De meegegeven curve verandert van gehooverde naar
	// niet-gehooverde status. Deze blijft de status van geselecteerd of
	// ongeselecteerd dus behouden.
	// Het zoeken naar de Curve zelf gebeurt dus _niet_ hier.
	protected void dehooverCurve(Curve c) {
		for (int i = 0; i < hooveredCurves.size(); ++i)
			if (hooveredCurves.elementAt(i).equals(c))
				hooveredCurves.remove(i--);
	}

	// Alle geselecteerde curves worden verwijderd.
	protected void deleteSelectedCurves() {
		for (int j = 0; j < selectedCurves.size(); ++j) {
			if (j == 0)
				push();

			selectionTool.deleteCurve(selectedCurves.elementAt(j));
		}

		selectedCurves.clear();
		selectedPoints.clear();
	}

	// Een nieuw inputpunt aan alle huidige geselecteerde curves toevoegen.
	// Deze curves, alsook de container, wordt herberekend.
	protected void addPoint(Point a) throws InvalidArgumentException {
		if (a == null)
			throw new InvalidArgumentException(
					"Editor.java - addPoint(Point): Invalid Argument.");
		else {
			if (selectedCurves.size() == 0)
				// Er is nog geen geselecteerde curve -> ééntje aanmaken.
				startNewCurve();

			for (int i = 0; i < selectedCurves.size(); ++i) {
				if (i == 0)
					push();

				Curve c = selectedCurves.elementAt(i);

				c.addInput(a); // Punt toevoegen.
				Algorithm temp = getAlgorithm(c.getType());

				// Bij Hermite ( type == 'H' ) is het 2de ingegeven punt
				// telkens de tangens. Dus er moet niet getekend worden voordat
				// deze is ingegeven.
				if (temp != null && c.getType() != 'H'
						|| c.getInput().size() % 2 == 0) {
					try {
						selectionTool.deleteCurve(c);
						temp.calculateComplete(c);
						selectionTool.addCurve(c);
					} catch (InvalidArgumentException e) {
						e.printStackTrace();
					}
				}
				if (i == selectedCurves.size() - 1)
					pushNew();
			}
		}
	}

	// De geselecteerde curves worden herberekend a.h.v. hun type en orde.
	protected void recalculateSelectedCurves() {
		for (int i = 0; i < selectedCurves.size(); ++i) {
			Curve c = selectedCurves.elementAt(i);
			Algorithm temp = getAlgorithm(c.getType());
			if (temp != null) {
				// De oude gegevens verwijderen.
				selectionTool.deleteCurve(c);
				c.clearOutput();
				try {
					// Nieuwe gegevens berekenen en opslaan.
					temp.calculateComplete(c);
					selectionTool.addCurve(c);
				} catch (InvalidArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// De ongeselecteerde curves worden herberekend a.h.v. hun type en orde.
	protected void recalculateCurves() {
		for (int i = 0; i < curves.size(); ++i) {
			Curve c = curves.elementAt(i);
			Algorithm temp = getAlgorithm(c.getType());
			if (temp != null) {
				// De oude gegevens verwijderen.
				selectionTool.deleteCurve(c);
				c.clearOutput();
				try {
					// Nieuwe gegevens berekenen en opslaan.
					temp.calculateComplete(c);
					selectionTool.addCurve(c);
				} catch (InvalidArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// De geselecteerde curves worden over een afstand x,y verschoven
	// en herberekend.
	protected void translateSelectedCurves(int x, int y) {
		// Curvegegevens verwijderen en de curves verschuiven.
		for (int i = 0; i < selectedCurves.size(); ++i) {
			selectionTool.deleteCurve(selectedCurves.elementAt(i));
			selectedCurves.elementAt(i).clearOutput();
			selectedCurves.elementAt(i).translate(x, y);
		}

		// Geselecteerde inputpunten moeten mee verschoven worden,
		// indien die er zijn.
		for (int i = 0; i < selectedPoints.size(); ++i) {
			selectedPoints.elementAt(i).increaseX(x);
			selectedPoints.elementAt(i).increaseY(y);
		}

		// Curvegegevens herberekenen.
		recalculateSelectedCurves();
	}

	// Alle geselecteerde curves tot één curve verbinden die de
	// eigenschappen overneemt van de eerst geselecteerde curve.
	// De curves worden onderling verbonden met C0-continuïteit
	// --> een curve wordt zodanig verschoven dat het eerste inputpunt
	// samenvalt met het laatste van de vorige curve.
	protected void connectCurvesC0() {
		if (selectedCurves.size() > 0) {
			push();
			Curve result = selectedCurves.get(0);
			selectionTool.deleteCurve(result);

			for (int i = 1; i < selectedCurves.size(); ++i) {
				selectionTool.deleteCurve(selectedCurves.get(i));
				// De geselecteerde curve "plakken" aan de rest.
				try {
					result = Curve.connectC0(result, selectedCurves.get(i));
				} catch (InvalidArgumentException e) {
					e.printStackTrace();
					recalculateSelectedCurves();
				}
			}

			// Alle curves deselecteren en verwijderen en de nieuwe
			// curve selecteren en aan de container toevoegen.
			selectedCurves.clear();
			selectedCurves.add(result);
			selectionTool.addCurve(result);
			recalculateSelectedCurves();
			pushNew();
		}
	}

	// Alle geselecteerde curves tot één curve verbinden die de
	// eigenschappen overneemt van de eerst geselecteerde curve.
	// De curves worden onderling verbonden zonder enige verschuiving
	// --> het eerste inputpunt van een curve volgt in de nieuwe curve
	// gewoon op het laatste inputpunt van de vorige curve.
	protected void connectNoExtraPoint() {
		if (selectedCurves.size() > 0) {
			push();
			Curve result = selectedCurves.get(0);
			selectionTool.deleteCurve(result);
			for (int i = 1; i < selectedCurves.size(); ++i) {
				selectionTool.deleteCurve(selectedCurves.get(i));
				// De geselecteerde curve "plakken" aan de rest.
				try {
					result = Curve.connectNoExtraPoint(result, selectedCurves
							.get(i));
				} catch (InvalidArgumentException e) {
					e.printStackTrace();
					recalculateSelectedCurves();
				}
			}

			// Alle curves deselecteren en verwijderen en de nieuwe
			// curve selecteren en aan de container toevoegen.
			selectedCurves.clear();
			selectedCurves.add(result);
			selectionTool.addCurve(result);
			recalculateSelectedCurves();
			pushNew();
		}

	}

	// _______________________POINT BEHEER_______________________

	// M.b.v. een gegeven Point, wordt een bestaan inputpoint gezocht.
	// Dit punt moet in het zoekbereik van het gegeven punt liggen. null wordt
	// teruggegeven indien zo geen Point gevonden wordt.
	protected Point pickControlPoint(Point p) {
		if (p == null)
			return null;

		Point result = null;
		Vector<Point> temp = selectionTool.searchControlPoint(p); // Zoeken.
		for (int i = 0; temp != null && i < temp.size(); ++i) {
			result = temp.elementAt(0);

			// Elk gevonden inputpunt moet van selectiestatus switchen.
			if (isSelectedControlPoint(temp.elementAt(i)))
				deselectControlPoint(temp.elementAt(i));
			else {
				selectedPoints.add(temp.elementAt(i));

				// Voor elke niet-geselecteerde curve nagaan of het nieuw
				// geselecteerde inputpunt een inputpunt van die Curve is.
				// Indien ja, moet deze Curve ook geselecteerd worden.
				for (int j = 0; j < curves.size(); ++j)
					if (curves.elementAt(j).containsInputPoint(
							temp.elementAt(i)) != null) {
						selectedCurves.add(curves.elementAt(j));
						curves.remove(j--);
					}
			}
		}

		// Voor elke geselecteerde Curve nagaan hoeveel geselecteerde
		// inputpunten deze nog bevat. Indien dat nul is, moet de curve
		// gedeselecteerd worden.
		for (int i = 0; i < selectedCurves.size(); ++i)
			if (nbSelectedControlPoints(selectedCurves.elementAt(i)) == 0) {
				curves.add(selectedCurves.elementAt(i));
				selectedCurves.remove(i--);
			}

		return result;
	}

	// Deselecteer een gegeven punt.
	// Er wordt geen gebruik gemaakt van een zoekbereik.
	// Enkel selectedPoints wordt bijgewerkt.
	protected void deselectControlPoint(Point p) {
		if (p != null)
			for (int j = 0; j < selectedPoints.size(); ++j)
				if (selectedPoints.elementAt(j).X() == p.X()
						&& selectedPoints.elementAt(j).Y() == p.Y())
					selectedPoints.remove(j--);
	}

	// Nagaan of een gegeven punt een geselecteerd inputpunt is.
	// Er wordt geen gebruik gemaakt van een zoekbereik.
	protected boolean isSelectedControlPoint(Point p) {
		if (p != null)
			for (int j = 0; j < selectedPoints.size(); ++j)
				if (selectedPoints.elementAt(j).X() == p.X()
						&& selectedPoints.elementAt(j).Y() == p.Y())
					return true;
		return false;
	}

	// Tel voor een gegeven Curve heeft inputpunten daarvan geselecteerd zijn.
	protected int nbSelectedControlPoints(Curve c) {
		int result = 0;
		if (c != null)
			for (int j = 0; j < selectedPoints.size(); ++j)
				if (c.containsInputPoint(selectedPoints.elementAt(j)) != null)
					++result;

		return result;
	}

	// M.b.v. een gegeven punt alle in het zoekbereik bestaande inputpunten
	// zoeken. Deze punten krijgen de gehooverde status, alsook de daaraan
	// verbonden curves. Een inputpunt wordt teruggegeven, dit dient vooral ter
	// controle of er iets gevonden werd. null wordt teruggegeven indien er niks
	// gevonden werd.
	protected Point hooverPoint(Point p) {
		if (p == null)
			return null;

		// De geldige punten zoeken.
		Vector<Point> temp = selectionTool.searchControlPoint(p);
		if (temp != null && temp.size() > 0) {
			for (int i = 0; i < temp.size(); ++i) {
				hooveredPoints.add(temp.elementAt(i));

				// Per gevonden inputpunt de daaraan verbonden curves zoeken en
				// hooveren indien dat al niet het geval was.
				Curve temp2 = selectionTool.searchCurvesByControlPoint(temp
						.elementAt(i));
				if (temp2 != null) {
					boolean found = false;
					for (int k = 0; k < hooveredCurves.size(); ++k) {
						if (hooveredCurves.elementAt(k).equals(temp2))
							found = true;
					}

					if (!found)
						hooveredCurves.add(temp2);

				}
			}
			// Het eerste gevonden punt teruggeven, dat kan dan bvb. gebruikt
			// worden om uit te tekenen.
			return temp.elementAt(0);
		} else
			return null;
	}

	// De geselecteerde controlepunten worden verwijderd als
	// inputpunt van de curves waartoe zij allemaal behoren.
	// De curves worden uiteraard opnieuw berekend.
	protected void deleteSelectedControlPoints() {
		for (int i = 0; i < selectedPoints.size(); ++i) {
			if (i == 0)
				push();

			for (int j = 0; j < selectedCurves.size(); ++j) {
				int temp;
				// Als het controlepunt tot de curve behoort --> het punt uit
				// die curve verwijderen.
				while ((temp = selectedCurves.elementAt(j).containsInputPointi(
						selectedPoints.elementAt(i))) != -1) {
					selectedCurves.elementAt(j).getInput().remove(temp);
				}
			}
			// Het punt ook uit de container halen.
			selectionTool.deleteControlPoint(selectedPoints.elementAt(i).X(),
					selectedPoints.elementAt(i).Y());
		}

		// Indien curves nu leeg blijken te zijn --> die curves verwijderen.
		for (int j = 0; j < selectedCurves.size(); ++j) {
			if (selectedCurves.elementAt(j).getNbInputPoints() == 0) {
				selectionTool.deleteCurve(selectedCurves.elementAt(j));
				selectedCurves.remove(j--);
			}
		}

		recalculateSelectedCurves(); // De curves herberekenen.
		curves.addAll(selectedCurves); // De curves deselecteren.
		selectedCurves.clear();
		selectedPoints.clear();
		pushNew();
	}

	// De geselecteerde inputpunten worden over een afstand x,y verschoven.
	// De aan de inputpunten verbonden curves worden met die nieuwe
	// posities herberekend.
	protected void translateSelectedControlPoints(int x, int y) {
		// Curvegegevens verwijderen.
		for (int i = 0; i < selectedCurves.size(); ++i) {
			selectionTool.deleteCurve(selectedCurves.elementAt(i));
			selectedCurves.elementAt(i).clearOutput();
		}

		// De inputpunten verschuiven.
		for (int i = 0; i < selectedPoints.size(); ++i) {
			selectedPoints.elementAt(i).increaseX(x);
			selectedPoints.elementAt(i).increaseY(y);
		}

		// Curvegegevens herberekenen.
		recalculateSelectedCurves();
	}

	// _______________________FILE I/O_______________________

	// Een bestand inladen.
	protected void loadFile(String s) {
		push();
		file.load(s, curves);
		recalculateCurves();
		pushNew();
	}

	// In een bestand opslaan.
	protected void saveFile(String s, Vector<Curve> v) {
		file.save(s, v);
	}

	protected void undo() {
		try {
			file.undo(curves, selectedCurves);
			selectionTool.clear();
			recalculateCurves();
			recalculateSelectedCurves();
		} catch (EmptyStackException e) {
			System.out.println("Nothing to undo.");
		} catch (InvalidArgumentException e) {
			System.out.println(e);
		}
	}

	protected void push() {
		try {
			file.push(curves, selectedCurves);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
	}

	protected void pushNew() {
		try {
			file.pushNew(curves, selectedCurves);
		} catch (InvalidArgumentException e) {
			e.printStackTrace();
		}
	}

	protected void redo() {
		try {
			file.redo(curves, selectedCurves);
			selectionTool.clear();
			recalculateCurves();
			recalculateSelectedCurves();
		} catch (EmptyStackException e) {
			System.out.println("Nothing to redo.");
		} catch (InvalidArgumentException e) {
			System.out.println(e);
		}
	}
}
