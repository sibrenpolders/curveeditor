package CurveEditor.GUI;

import java.awt.Dimension;
import java.awt.Toolkit;

public class DisplaySize {
	// standaard breedtes en hoogtes van de gui elementen
	public static int FRAMEWIDTH = 600; // standaard breedte voor DrawArea
	public static int FRAMEHEIGHT = 600; // standaard hoogte voor DrawArea		
	public static int CHOICEWIDTH = 275;
	public static int CHOICEHEIGHT = FRAMEHEIGHT;
	public static final int MENUWIDTH = FRAMEWIDTH + CHOICEWIDTH;	
	public static final int MENUHEIGHT = 20;
	public static final int TOOLBARWIDTH = MENUWIDTH;
	public static final int TOOLBARHEIGHT = MENUHEIGHT + 5;
	
	// Dimensies van de verschillende gui elementen
	private static Dimension drawArea;
	private static Dimension choiceArea;
	private static Dimension menu;
	private static Dimension toolbar;
	
	// huidige schermgrootte
	private Dimension frameSize;
	
	public DisplaySize( ) {
		drawArea = new Dimension( FRAMEWIDTH, FRAMEHEIGHT );
		choiceArea = new Dimension( CHOICEWIDTH, CHOICEHEIGHT );
		menu = new Dimension( MENUWIDTH, MENUHEIGHT );
		toolbar = new Dimension( TOOLBARWIDTH, TOOLBARHEIGHT );
	}
	
	public void recalculate( Dimension d ) {
		// de hoogte van zowel menu en toolbar zijn vast. verandering van hoogte van het displayscherm heeft dus geen invloed
		// op deze 2 elementen. Bij choiceArea is de breedte vast.
		System.out.println( d );
		drawArea.setSize( d.getWidth() - CHOICEWIDTH, d.getHeight() - MENUHEIGHT - TOOLBARHEIGHT );
		choiceArea.setSize( CHOICEWIDTH, d.getHeight() - MENUHEIGHT - TOOLBARHEIGHT );
		menu.setSize( d.getWidth(), MENUHEIGHT );
		toolbar.setSize( d.getWidth(), TOOLBARHEIGHT );
		System.out.println( drawArea );
	}
	
	public static Dimension drawAreaD( ) {
		return drawArea;
	}
	
	public static Dimension choiceAreaD( ) {
		return choiceArea;
	}
	
	public static Dimension menuD( ) {
		return menu;
	}
	
	public static Dimension toolbarD( ) {
		return toolbar;
	}
	
	public void setCurrentSize( Dimension d ) {
		frameSize = d;
	}
	
	public Dimension frameSize( ) {
		return frameSize;	
	}
	
	public boolean frameSizeChanged( Dimension d ) {
		if ( !frameSize.equals( d )) {
			setCurrentSize( d );
			recalculate( frameSize );
			return true;
		}
		
		return false;
	}
}
