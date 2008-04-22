package CurveEditor.Core;

import java.util.Vector;

import CurveEditor.Algorithms.*;
import CurveEditor.Curves.*;
import CurveEditor.Tools.*;

public class Editor {

	public static enum MODE {
		NONE, ADD_INPUT, SELECT_CURVE, NEW_CURVE, SELECT_CONTROL_POINT
	};

	protected MODE mode;
	protected Vector<Algorithm> algorithms;
	protected Vector<Tool> tools;
	protected Vector<Curve> curves;
	protected Vector<Curve> selectedCurves;
	protected Algorithm currentAlgorithm;
	protected Tool currentTool;
	protected FileIO file;
	protected CurveMap map;

	public Editor(String filename) {
		init();
		// bestand inladen a.h.v. filename
	}

	public Editor() {
		init();
	}

	private void init() {
		mode = MODE.NEW_CURVE;
		currentTool = null;

		algorithms = new Vector<Algorithm>();
		tools = new Vector<Tool>();
		curves = new Vector<Curve>();
		selectedCurves = new Vector<Curve>();

		algorithms.add(new Linear((short) 1));
		algorithms.add(new Bezier3((short) 3));
		algorithms.add(new BezierUnlimited((short) 0));
		algorithms.add(new Hermite('H', (short) 1));
		algorithms.add(new HermiteCardinal('C', (short) 1));
		algorithms.add(new HermiteCatmullRom('R', (short) 1));

		currentAlgorithm = getAlgorithm('L', (short) 1);

		file = new FileIO();
		map = new CurveMap();
	}

	protected void reset() {
		algorithms.clear();
		tools.clear();
		curves.clear();
		selectedCurves.clear();
	}

	public void setMode(Editor.MODE m) {
		this.mode = m;
	}

	protected Vector<Algorithm> getAlgorithms() {
		return algorithms;
	}

	protected Algorithm getAlgorithm(char type, short degree) {
		for (int i = 0; i < algorithms.size(); ++i)
			if (algorithms.get(i).getType() == type
					&& algorithms.get(i).getDegree() == degree)
				return algorithms.get(i);
		return null;
	}

	protected void setCurrentAlgorithm(char type, short degree) {
		currentAlgorithm = getAlgorithm(type, degree); // mag niet null zijn

		// de geselecteerde curves naar dat type veranderen
		for (int i = 0; i < selectedCurves.size(); ++i) {
			selectedCurves.get(i).setType(type);
			selectedCurves.get(i).setDegree(degree);
			selectedCurves.get(i).clearOutput();
			currentAlgorithm.calculateCurve(selectedCurves.get(i));
		}
	}

	protected Vector<Tool> getTools() {
		return tools;
	}

	protected Tool getTool(char type) {
		for (int i = 0; i < tools.size(); ++i)
			if (tools.get(i).getType() == type)
				return tools.get(i);
		return null;
	}

	protected void setCurrentTool(char type) {
		currentTool = getTool(type);
	}

	protected Curve searchCurve(Point p) {
		CurveMap.CurveAndPointContainer result = map.searchCurveByControlPoint(p);

		if (result != null)
			return result.c;
		else
			return map.searchCurveByCurvePoint(p).c;
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

	protected void searchAndSelectCurve(Point p) {
		Curve c = searchCurve(p);
		if (c != null) {
			selectCurve(c);
		}
	}

	protected void startNewCurve() {
		deselectAllCurves();
		selectedCurves.add(new Curve(currentAlgorithm.getType(),
				currentAlgorithm.getDegree()));
	}
}
