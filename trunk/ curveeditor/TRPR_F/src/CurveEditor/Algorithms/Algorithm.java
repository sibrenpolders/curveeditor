package CurveEditor.Algorithms;

import CurveEditor.Curves.Curve;


public abstract class Algorithm {
	protected Curve c;
	protected String type;

	public Algorithm(Curve c)
	{
		this.c = c;
	}

	public String getType()
	{
		return type;
	}

	protected void setType(String type ){
		this.type = type;
	}

	public String toString(){
		return null;
	}

	public abstract void calculateCurve(Curve c);
}
