package CurveEditor.Core;

import java.util.Vector;

import CurveEditor.Algorithms.*;
import CurveEditor.Curves.*;
import CurveEditor.Tools.*;

public class Editor {

	protected static enum MODE {
		NONE, ADD_INPUT, SELECT_CURVE, SELECT_CONTROL_POINT, DESELECT_CURVE, DESELECT_CONTROL_POINT, DESELECT_ALL, NEW_CURVE
	};

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
	// algorithm dat momenteel geselecteerd is; verandert men dit, dan wordt dit
	// autom. verandert voor elke selectedCurve
	protected Algorithm currentAlgorithm;
	protected Tool currentTool;
	protected FileIO file;
	// datastructuur die kan gebruikt worden om na te gaan op welke curve
	// geklikt is
	protected CurveMap2 selectionTool;

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

		// Hier moeten alle geïmplementeerde algoritmes ingegeven worden.
		algorithms.add(new Linear((short) 1)); // 'L'
		algorithms.add(new Bezier2((short) 2)); // 'B'
		algorithms.add(new Bezier3((short) 3)); // 'B'
		algorithms.add(new BezierUnlimited((short) 0));
		algorithms.add(new Hermite('H', (short) 1));
		algorithms.add(new HermiteCardinal('C', (short) 1));
		algorithms.add(new HermiteCatmullRom('R', (short) 1));

		currentAlgorithm = getAlgorithm('L', (short) 1);

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
		if (m == MODE.NONE)
			deselectAll();
		else if (m == MODE.NEW_CURVE)
			startNewCurve();
		else if(m == MODE.DESELECT_ALL)
			deselectAll();
		else if(m == MODE.ADD_INPUT)
			selectedPoints.clear();

		this.mode = m;
	}

	protected void deselectAll() {
		for (int i = 0; i < selectedCurves.size(); ++i)
			curves.add(selectedCurves.get(i));

		selectedCurves.clear();
		selectedPoints.clear();
	}

	protected void startNewCurve() {
		deselectAll();
		selectedCurves.add(new Curve(currentAlgorithm.getType(),
				currentAlgorithm.getDegree()));
	}

//	protected Curve searchCurve(Point p) {
//		CurveMap.CurveAndPointContainer result = map
//				.searchCurveByControlPoint(p);
//
//		if (result != null)
//			return result.c;
//		else
//			return map.searchCurveByCurvePoint(p).c;
//	}

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

	protected void deselectCurve(Curve c) {
		int index = findIndexSelectedCurve(c);

		if (index != -1) {
			curves.add(selectedCurves.get(index));
			selectedCurves.remove(index);
		}
	}

	protected void selectCurve(Curve c) {
		int index = findIndexCurve(c);

		if (index != -1) {
			selectedCurves.add(curves.get(index));
			curves.remove(index);
		}
	}
}
