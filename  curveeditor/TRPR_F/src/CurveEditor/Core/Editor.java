package CurveEditor.Core;

import java.util.HashMap;
import java.util.List;

import CurveEditor.Algorithms.Algorithm;
import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;
import CurveEditor.Tools.Tool;

public class Editor {

	protected MonitorPool mp = new MonitorPool();
	protected Situation currentSituation;
	protected HashMap<String, Algorithm> algorithms;
	protected HashMap<String, Tool> tools;
	protected List<Curve> curves;

	public Editor(String filename)
	{

	}

	public Editor(){

	}

	protected HashMap<String, Algorithm> getAlgorithms()
	{
		return algorithms;
	}

	public Algorithm getAlgorithm(char c)
	{
		return (Algorithm) algorithms.get(c);
	}

	public void addAlgorithm(Algorithm c){
		algorithms.put(c.getType(), c);
	}

	public List<Curve> getCurves()	{
		return curves;
	}

	public void setCurves(List<Curve> curves ){
		this.curves = curves;
	}

	protected HashMap<String, Tool> getTools()
	{
		return tools;
	}

	public Tool getTool(String c)
	{
		return (Tool) tools.get(c);
	}

	public void addTool(Tool c){
		tools.put(c.getType(), c);
	}

	public Curve searchCurve(Point p){
		return null;
	}

	public void selectCurve(Point p)
	{
		Curve c = searchCurve(p);
		c.setDegree(currentSituation.currentDegree());
		c.setType(currentSituation.currentType());		
		currentSituation.setCurrentCurve(c);
	}

	public void changeCurve(Point p)
	{
		Curve c = searchCurve(p);
		c.setDegree(currentSituation.currentDegree());
		c.setType(currentSituation.currentType());		
		currentSituation.setCurrentCurve(c);
	}
}
