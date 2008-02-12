package CurveEditor.Core;

import java.util.HashMap;
import java.util.List;

public class Editor {

	protected MonitorPool mp = new MonitorPool();
	protected Situation currentSituation;
	protected HashMap algorithms;
	protected HashMap tools;
	protected List<Curve> curves;

	public Editor(String filename)
	{

	}

	public Editor(){

	}

	protected HashMap getAlgorithms()
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

	protected HashMap getTools()
	{
		return tools;
	}

	public Tool getTool(String c)
	{
		return (Tool) algorithms.get(c);
	}

	public void addTool(Tool c){
		algorithms.put(c.getType(), c);
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
