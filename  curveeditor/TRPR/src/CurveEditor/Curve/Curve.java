package CurveEditor.Curve;

import java.util.Vector;

//interpolatiepunten worden berekend vanuit Editor
public class Curve {
	//de controlepunten, volgorde is belangrijk !!!
	protected Vector<Point> input;

	//de berekende tussenpunten
	protected Vector<Point> output;	

	//identifier
	protected String type;

	//de orde
	protected short degree;

	public Curve(char Type, short degree){

	}

	public Vector<Point> getInput(){
		return input;
	}

	public void setInput(Vector<Point> input ){
		this.input = input;
	}

	public Vector<Point> getOutput(){
		return output;
	}

	public void setOutput(Vector<Point> Output ){
		this.output = Output;
	}

	//verwijder de interpolatiepunten
	public void clearOutput(){
		this.output.clear();
	}


	public void addOutput(Point o){
		this.output.add(o);
	}

	public void removeInput(Point o){
		this.input.remove(o);
	}

	public void addInput(Point o){
		this.input.add(o);
	}

	public String getType(){
		return type;
	}

	public short getDegree(){
		return degree;
	}

	public void setType(String t){
		this.type = t;
	}

	public void setDegree(short d){
		this.degree = d;
	}

	public String toString(){
		return null;
	}
}
