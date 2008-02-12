package CurveEditor;


public class Situation {
	private Curve currentCurve = null;
	private Point currentPoint = null;
	private String currentType = null;
	private short currentDegree = 0;
	private Algorithm currentAlgorithm = null;
	private Tool currentTool = null;

	public Curve currentCurve(){
		return currentCurve;
	}
	public Point currentPoint(){
		return currentPoint;
	}
	public String currentType(){
		return currentType;
	}
	public short currentDegree(){
		return currentDegree;
	}
	public Algorithm currentAlgorithm(){
		return currentAlgorithm;
	}
	public Tool currentTool(){
		return currentTool;
	}

	public void setCurrentCurve(Curve c){
		currentCurve = c;
	}
	public void setCurrentPoint(Point c){
		currentPoint = c;
	}
	public void setCurrentType(String c){
		currentType = c;
	}
	public void setCurrentDegree(short c){
		currentDegree = c;
	}
	public void setCurrentAlgorithm(Algorithm c){
		currentAlgorithm = c;
	}
	public void setCurrentTool(Tool c){
		currentTool = c;
	}
}
