package CurveEditor.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Polygon;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

import CurveEditor.Curves.Curve;
import CurveEditor.Curves.Point;


public class DrawArea extends JPanel{

	public DrawArea(){
		setPreferredSize(new Dimension(600, 600));
		setBackground(new Color(255,255,255));
		
		//java.awt.Graphics2D c = new java.awt.Graphics2D();
		//c.drawLine(55, 55, 66, 300);
		
		//paint(c);

	}	

	public DrawArea(Vector<Curve> curves)
	{

	}		

	public void drawPoint(Point p){

	}

	public void drawCurve(Curve c)
	{

	}

	public void redrawAllCurves(Vector<Curve> curves){

	}

	public void clearPoint(Point p){

	}

	public void clearArea(){

	}

	public void showImage(String filename){

	}

	public Point retrievePoint(){
		return null;
	}

	public void drawSelectedCurve(Curve c){

	}	
}
