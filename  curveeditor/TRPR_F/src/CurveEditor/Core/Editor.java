package CurveEditor.Core;

import java.util.Vector;

import CurveEditor.Algorithms.*;
import CurveEditor.Curves.*;
import CurveEditor.Tools.*;

public class Editor {

	protected static enum MODE {
		NONE, ADD_INPUT, SELECT_CURVE, SELECT_CONTROL_POINT, DESELECT_CURVE, DESELECT_CONTROL_POINT, DESELECT_ALL, NEW_CURVE, MOVE_CURVES, MOVE_CONTROL_POINTS
	};

	protected static short SEARCH_RANGE = 3;

	protected MODE mode;
	protected Vector<Algorithm> algorithms;
	protected Vector<Tool> tools; // voorlopig niet gebruiken
	protected Vector<Curve> curves; // ongeselecteerde curves
	// geselecteerde curves --> hier worden de bewerkingen op gedaan
	protected Vector<Curve> selectedCurves;
	// curves die onder de cursor staan; moeten ook in curves of selectedCurves
	// staan
	protected Vector<Curve> hooveredCurves;
	// geselecteerde controlepunten --> kunnen verplaatst worden; curves die dit
	// punt als controlepunt gebruiken, moeten in selectedCurves staan
	protected Vector<Point> selectedPoints;
	// punten die onder de cursor staan
	protected Vector<Point> hooveredPoints;
	// algorithm dat momenteel geselecteerd is; verandert men dit, dan wordt dit
	// autom. verandert voor elke selectedCurve
	protected Algorithm currentAlgorithm;
	protected Tool currentTool;
	protected FileIO file;
	// datastructuur die kan gebruikt worden om na te gaan op welke curve
	// geklikt is
	protected CurveContainer selectionTool;

	public Editor(String filename) {
		init();
		// bestand inladen a.h.v. filename
	}

	public Editor() {
		init();
	}

	protected void init() {
		mode = MODE.NONE;
		currentTool = null;

		algorithms = new Vector<Algorithm>();
		tools = new Vector<Tool>();
		curves = new Vector<Curve>();
		selectedCurves = new Vector<Curve>();
		hooveredCurves = new Vector<Curve>();
		selectedPoints = new Vector<Point>();
		hooveredPoints = new Vector<Point>();

		// Hier moeten alle geïmplementeerde algoritmes ingegeven worden.
		algorithms.add(new Linear((short) 1)); // 'L'
		algorithms.add(new Bezier((short) 3)); // 'B'
		algorithms.add(new Hermite('H', (short) 1));
		algorithms.add(new HermiteCardinal('C', (short) 1));
		algorithms.add(new HermiteCatmullRom('R', (short) 1));

		currentAlgorithm = getAlgorithm('B', (short) 3);

		file = new FileIO();
		// afmetingen van het canvas zijn nodig om een datastructuur aan te
		// maken --> aanmaken in een subklasse
		selectionTool = null;
	}

	protected void reset() {
		algorithms.clear();
		tools.clear();
		curves.clear();
		selectedCurves.clear();
		hooveredCurves.clear();
		selectedPoints.clear();
		hooveredPoints.clear();
	}

	// algoritme zoeken a.h.v. het type en de orde
	protected Algorithm getAlgorithm(char type, short degree) {
		for (int i = 0; i < algorithms.size(); ++i)
			if (algorithms.get(i).getType() == type
					&& algorithms.get(i).getDegree() == degree)
				return algorithms.get(i);
		return null;
	}

	protected void setCurrentAlgorithm(char type, short degree) {
		Algorithm temp = getAlgorithm(type, degree);

		if (temp != null) {
			currentAlgorithm = temp;
			// de op dat moment geselecteerde curves naar dat type veranderen
			for (int i = 0; i < selectedCurves.size(); ++i) {
				selectedCurves.get(i).setType(type);
				selectedCurves.get(i).setDegree(degree);
				selectedCurves.get(i).clearOutput();
				currentAlgorithm.calculate(selectedCurves.get(i));
			}
		}
	}

	protected Tool getTool(char type) {
		for (int i = 0; i < tools.size(); ++i)
			if (tools.get(i).getType() == type)
				return tools.get(i);
		return null;
	}

	protected void setCurrentTool(char type) {

		Tool temp = getTool(type);

		if (temp != null)
			currentTool = temp;
	}

	public void changeMode(Editor.MODE m) {
		if (m == MODE.NONE) {
			deselectAll();
			this.mode = m;
		} else if (m == MODE.NEW_CURVE)
			startNewCurve();
		else if (m == MODE.DESELECT_ALL) {
			deselectAll();
			if (mode != MODE.SELECT_CURVE && mode != MODE.SELECT_CONTROL_POINT
					&& mode != MODE.DESELECT_CONTROL_POINT
					&& mode != MODE.DESELECT_CURVE)
				this.mode = MODE.NONE;
		} else if (m == MODE.ADD_INPUT) {
			selectedPoints.clear();
			this.mode = m;
		} else if (m == MODE.SELECT_CURVE || m == MODE.DESELECT_CURVE
				|| m == MODE.SELECT_CONTROL_POINT || m == MODE.SELECT_CURVE) {
			deselectAll();
			this.mode = m;
		} else {
			this.mode = m;
		}
	}

	protected void startNewCurve() {
		deselectAll();
		selectedCurves.add(new Curve(currentAlgorithm.getType(),
				currentAlgorithm.getDegree()));
		this.mode = MODE.ADD_INPUT;
	}

	protected void deleteSelectedControlPoints() {
		for (int i = 0; i < selectedPoints.size(); ++i) {
			for (int j = 0; j < selectedCurves.size(); ++j) {
				int temp;
				while ((temp = selectedCurves.elementAt(j).containsInputPointi(
						selectedPoints.elementAt(i))) != -1) {
					selectedCurves.elementAt(j).getInput().remove(temp);
				}
			}
		}

		for (int j = 0; j < selectedCurves.size(); ++j) {
			if (selectedCurves.elementAt(j).getNbInputPoints() == 0) {
				selectionTool.deleteCurve(selectedCurves.elementAt(j));
				selectedCurves.remove(j--);
			}
		}

		curves.addAll(selectedCurves);
		selectedCurves.clear();
		selectedPoints.clear();
	}

	protected void deleteSelectedCurves() {
		for (int j = 0; j < selectedCurves.size(); ++j)
			selectionTool.deleteCurve(selectedCurves.elementAt(j));

		selectedCurves.clear();
		selectedPoints.clear();
	}

	protected void deselectAll() {
		for (int i = 0; i < selectedCurves.size(); ++i)
			curves.add(selectedCurves.get(i));

		selectedCurves.clear();
		selectedPoints.clear();
	}

	// haalt curve uit de ene vector en plaatst 'm in de andere
	// het zoeken zelf gebeurt hier dus _niet_
	protected void selectCurve(Curve c) {
		int index = findIndexCurve(c);

		if (index != -1) {
			selectedCurves.add(curves.get(index));
			curves.remove(index);
		}
	}

	protected void deselectCurve(Curve c) {
		int index = findIndexSelectedCurve(c);

		if (index != -1) {
			curves.add(selectedCurves.get(index));
			selectedCurves.remove(index);
		}
	}

	protected void hooverCurve(Curve c) {
		int index = findIndexCurve(c);

		if (index != -1)
			hooveredCurves.add(curves.get(index));
	}

	protected void dehooverCurve(Curve c) {
		for (int i = 0; i < hooveredCurves.size(); ++i)
			if (hooveredCurves.elementAt(i).equals(c))
				hooveredCurves.remove(i--);
	}

	protected Point hooverPoint(Point p) {
		Vector<Point> temp = selectionTool.searchControlPoint(p);
		if (temp != null && temp.size() > 0) {
			for (int i = 0; i < temp.size(); ++i) {
				hooveredPoints.add(temp.elementAt(i));
				Vector<Curve> temp2 = selectionTool
						.searchCurvesByControlPoint(temp.elementAt(i));
				for (int j = 0; j < temp2.size(); ++j) {
					boolean found = false;
					for (int k = 0; k < hooveredCurves.size(); ++k) {
						if (hooveredCurves.elementAt(k).equals(
								temp2.elementAt(j)))
							found = true;
					}

					if (!found)
						hooveredCurves.add(temp2.elementAt(i));

				}
			}
			return temp.elementAt(0);
		} else
			return null;
	}

	protected void deselectControlPoint(Point p) {
		for (int j = 0; j < selectedPoints.size(); ++j)
			if (selectedPoints.elementAt(j).X() == p.X()
					&& selectedPoints.elementAt(j).Y() == p.Y())
				selectedPoints.remove(j--);
	}

	protected boolean isSelectedControlPoint(Point p) {
		for (int j = 0; j < selectedPoints.size(); ++j)
			if (selectedPoints.elementAt(j).X() == p.X()
					&& selectedPoints.elementAt(j).Y() == p.Y())
				return true;
		return false;
	}

	protected int nbSelectedControlPoints(Curve c) {
		int result = 0;
		for (int j = 0; j < selectedPoints.size(); ++j)
			if (c.containsInputPoint(selectedPoints.elementAt(j)) != null)
				++result;

		return result;
	}

	protected Point pickControlPoint(Point p) {
		Point result = null;

		Vector<Point> temp = selectionTool.searchControlPoint(p);
		for (int i = 0; temp != null && i < temp.size(); ++i) {
			result = temp.elementAt(0);

			if (isSelectedControlPoint(temp.elementAt(i)))
				deselectControlPoint(temp.elementAt(i));
			else {
				selectedPoints.add(temp.elementAt(i));
				for (int j = 0; j < curves.size(); ++j)
					if (curves.elementAt(j).containsInputPoint(
							temp.elementAt(i)) != null) {
						selectedCurves.add(curves.elementAt(j));
						curves.remove(j--);
					}
			}
		}

		for (int i = 0; i < selectedCurves.size(); ++i)
			if (nbSelectedControlPoints(selectedCurves.elementAt(i)) == 0) {
				curves.add(selectedCurves.elementAt(i));
				selectedCurves.remove(i--);
			}

		return result;
	}

	protected Curve pickCurve(Point p) {
		Curve c = this.selectionTool.searchCurve(p);

		if (c == null)
			return null;
		else {
			if (isSelectedCurve(c))
				deselectCurve(c);
			else
				selectCurve(c);

			return c;
		}
	}

	protected boolean isSelectedCurve(Curve c) {
		for (int j = 0; j < selectedCurves.size(); ++j)
			if (selectedCurves.elementAt(j).equals(c))
				return true;
		return false;
	}

	protected void deselectAllCurves() {
		for (int i = 0; i < selectedCurves.size(); ++i)
			curves.add(selectedCurves.get(i));

		selectedCurves.clear();
	}

	protected void selectAllCurves() {
		for (int i = 0; i < curves.size(); ++i)
			selectedCurves.add(curves.get(i));

		curves.clear();
	}

	protected int findIndexCurve(Curve c) {
		int index = 0;
		boolean stop = false;

		for (; index < curves.size() && !stop; ++index)
			if (c.equals(curves.get(index)))
				stop = true;

		if (stop)
			return --index;
		else
			return -1;
	}

	protected int findIndexSelectedCurve(Curve c) {
		int index = 0;
		boolean stop = false;

		for (; index < selectedCurves.size() && !stop; ++index)
			if (c.equals(selectedCurves.get(index)))
				stop = true;

		if (stop)
			return --index;
		else
			return -1;
	}

	protected void recalculateSelectedCurves() {
		Algorithm prev = currentAlgorithm;
		for (int i = 0; i < selectedCurves.size(); ++i) {
			Curve c = selectedCurves.elementAt(i);
			selectionTool.deleteCurve(c);
			this.getAlgorithm(selectedCurves.get(i).getType(),
					selectedCurves.get(i).getDegree()).calculate(
					selectedCurves.get(i));

			selectionTool.addCurve(c);
		}

		currentAlgorithm = prev;
	}

	protected void recalculateCurves() {
		Algorithm prev = currentAlgorithm;
		for (int i = 0; i < curves.size(); ++i) {
			Curve c = curves.elementAt(i);
			selectionTool.deleteCurve(c);
			this.getAlgorithm(curves.get(i).getType(),
					curves.get(i).getDegree()).calculate(curves.get(i));

			selectionTool.addCurve(c);
		}

		currentAlgorithm = prev;
	}

	protected void translateSelectedControlPoints(int x, int y) {
		int index;
		for (int i = 0; i < selectedCurves.size(); ++i) {
			selectionTool.deleteCurve(selectedCurves.elementAt(i));
			selectedCurves.elementAt(i).clearOutput();

			for (int j = 0; j < selectedPoints.size(); ++j)
				if ((index = selectedCurves.elementAt(i).containsInputPointi(
						selectedPoints.elementAt(j))) != -1) {
					selectedCurves.elementAt(i).getInput().elementAt(index)
							.increaseX(x);
					selectedCurves.elementAt(i).getInput().elementAt(index)
							.increaseY(y);
				}
		}

		for (int i = 0; i < selectedPoints.size(); ++i) {
			selectedPoints.elementAt(i).increaseX(x);
			selectedPoints.elementAt(i).increaseY(y);
		}

		recalculateSelectedCurves();
	}

	protected void translateSelectedCurves(int x, int y) {
		for (int i = 0; i < selectedCurves.size(); ++i) {
			selectionTool.deleteCurve(selectedCurves.elementAt(i));
			selectedCurves.elementAt(i).clearOutput();
			selectedCurves.elementAt(i).translate(x, y);
		}

		for (int i = 0; i < selectedPoints.size(); ++i) {
			selectedPoints.elementAt(i).increaseX(x);
			selectedPoints.elementAt(i).increaseY(y);
		}

		recalculateSelectedCurves();
	}
}
