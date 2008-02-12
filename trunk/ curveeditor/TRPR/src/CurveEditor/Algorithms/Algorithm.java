package CurveEditor.Algorithms;


public abstract class Algorithm {
	protected Curve c;
	protected String type;
	protected short degree;

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

	public short getDegree(){
		return degree;
	}

	protected void setDegree(short degree ){
		this.degree = degree;
	}

	public String toString(){
		return null;
	}

	public abstract void calculateCurve(Curve c);
}
