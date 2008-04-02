package CurveEditor.Core;

import java.util.HashMap;
import java.util.Vector;

import CurveEditor.Algorithms.Algorithm;
import CurveEditor.Change.ChangeListener;
import CurveEditor.Change.WatchedObject;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;
import CurveEditor.Tools.Tool;

public class Editor implements WatchedObject {

	protected MonitorPool mp = new MonitorPool();
	protected Situation currentSituation;
	protected HashMap<String, Algorithm> algorithms;
	protected HashMap<String, Tool> tools;
	protected Vector<Curve> curves;
	protected Vector<Curve> selectedCurves;

	public Editor(String filename) {

	}

	public Editor() {
		listeners = new Vector<ChangeListener>();
		curves = new Vector<Curve>();
		selectedCurves = new Vector<Curve>();

	}

	protected HashMap<String, Algorithm> getAlgorithms() {
		return algorithms;
	}

	public Algorithm getAlgorithm(char c) {
		return (Algorithm) algorithms.get(c);
	}

	public void addAlgorithm(Algorithm c) {
		algorithms.put(c.getType(), c);
	}

	protected HashMap<String, Tool> getTools() {
		return tools;
	}

	public Tool getTool(String c) {
		return (Tool) tools.get(c);
	}

	public void addTool(Tool c) {
		tools.put(c.getType(), c);
	}

	public Curve searchCurve(Point p) {
		return null;
	}

	public void selectCurve(Point p) {
		Curve c = searchCurve(p);
		c.setType(currentSituation.currentType());
		currentSituation.setCurrentCurve(c);
	}

	public void changeCurve(Point p) {
		Curve c = searchCurve(p);
		c.setType(currentSituation.currentType());
		currentSituation.setCurrentCurve(c);
	}

	public void deselectAllCurves() {
		for (int i = 0; i < selectedCurves.size(); ++i)
			curves.add(selectedCurves.get(i));

		selectedCurves.clear();

		notifyListeners();
	}

	public void selectAllCurves() {
		for (int i = 0; i < curves.size(); ++i)
			selectedCurves.add(curves.get(i));

		curves.clear();

		notifyListeners();
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

	public void deselectCurve(Curve c) {
		int index = findIndexSelectedCurve(c);

		if (index != -1) {
			curves.add(selectedCurves.get(index));
			selectedCurves.remove(index);
		}

		notifyListeners();
	}

	public void selectCurve(Curve c) {
		int index = findIndexSelectedCurve(c);

		if (index != -1) {
			selectedCurves.add(curves.get(index));
			curves.remove(index);
		}

		notifyListeners();
	}

	protected Vector<ChangeListener> listeners;

	@Override
	public void addChangeListener(ChangeListener c) {
		listeners.add(c);
	}

	@Override
	public void notifyListeners() {
		for (int i = 0; i < listeners.size(); ++i)
			listeners.get(i).contentsChanged(this);
	}
}
