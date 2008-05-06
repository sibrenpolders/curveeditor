package CurveEditor.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DisplaySize {
	// standaard breedtes en hoogtes van de gui elementen
	public static int DRAWWIDTH = 600; // standaard breedte voor DrawArea
	public static int DRAWHEIGHT = 600; // standaard hoogte voor DrawArea		
	public static final int CHOICEWIDTH = 275;
	public static int CHOICEHEIGHT = DRAWHEIGHT;
	public static int SCREENWIDTH = DRAWWIDTH + CHOICEWIDTH;	
	public static final int MENUHEIGHT = 20;
//	public static int TOOLBARWIDTH = MENUWIDTH;
	public static final int TOOLBARHEIGHT = MENUHEIGHT + 5;
	
//	public static int SCREENWIDTH = MENUWIDTH;
	public static int SCREENHEIGHT = CHOICEHEIGHT + MENUHEIGHT + TOOLBARHEIGHT;
	
	// Dimensies van de verschillende gui elementen
//	private static Dimension drawArea;
//	private static Dimension choiceArea;
//	private static Dimension menu;
//	private static Dimension toolbar;
	
	// huidige schermgrootte
	private Dimension frameSize;
	
	public DisplaySize( ) {
//		drawArea = new Dimension( FRAMEWIDTH, FRAMEHEIGHT );
//		choiceArea = new Dimension( CHOICEWIDTH, CHOICEHEIGHT );
//		menu = new Dimension( MENUWIDTH, MENUHEIGHT );
//		toolbar = new Dimension( TOOLBARWIDTH, TOOLBARHEIGHT );
	}
	
	public void recalculate( Dimension d ) {
		// de hoogte van zowel menu en toolbar zijn vast. verandering van hoogte van het displayscherm heeft dus geen invloed
		// op deze 2 elementen. Bij choiceArea is de breedte vast.		
		SCREENWIDTH = (int) d.getWidth();
		SCREENHEIGHT = (int) d.getHeight();
		DRAWWIDTH = SCREENWIDTH - CHOICEWIDTH;
		DRAWHEIGHT = SCREENHEIGHT - MENUHEIGHT - TOOLBARHEIGHT;
		CHOICEHEIGHT =  SCREENHEIGHT - MENUHEIGHT - TOOLBARHEIGHT;		
//		MENUWIDTH = (int) d.getWidth();
//		TOOLBARWIDTH = (int) d.getWidth();
	}
	
//	public static Dimension drawAreaD( ) {
//		return drawArea;
//	}
//	
//	public static Dimension choiceAreaD( ) {
//		return choiceArea;
//	}
//	
//	public static Dimension menuD( ) {
//		return menu;
//	}
//	
//	public static Dimension toolbarD( ) {
//		return toolbar;
//	}
//	
	public void setCurrentSize( Dimension d ) {
		frameSize = d;
	}
	
	public Dimension frameSize( ) {
		return frameSize;	
	}
	
	public boolean frameSizeChanged( Dimension d ) {		
		if ( frameSize == null || !frameSize.equals( d )) {
			setCurrentSize( d );
			recalculate( frameSize );
			return true;
		}
		
		return false;
	}
}
